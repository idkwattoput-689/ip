import java.time.LocalDateTime;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Starts Gooble, responds to entered commands, and exits when requested.
 */
public class Gooble {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;
    private final CommandContext commandContext;

    /**
     * Creates a Gooble application backed by the supplied task file.
     *
     * @param filePath path to the task storage file
     */
    public Gooble(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(Path.of(filePath));
        tasks = new TaskList(storage);
        commandContext = new CommandContext(tasks, ui, parser);
    }

    /** Runs the interactive command loop. */
    public void run() {
        String banner = "  ____            _     _      \n"
                + " / ___| ___   ___ | |__ | | ___ \n"
                + "| |  _ / _ \\ / _ \\| '_ \\| |/ _ \\\n"
                + "| |_| | (_) | (_) | |_) | |  __/\n"
                + " \\____|\\___/ \\___/|_.__/|_|\\___|\n";

        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            ui.showDivider();

            CommandType type = parser.parseType(command);

            if (type == CommandType.BYE) {
                try {
                    type.handler().execute(command, commandContext);
                } catch (GoobleException e) {
                    System.out.println(e.getMessage());
                }
                ui.showDivider();
                break;
            }

            try {
                Command handler = type.handler();
                if (type == CommandType.LIST && command.startsWith("list from ")) {
                    new ListFromCommand().execute(command, commandContext);
                } else if (handler != null) {
                    handler.execute(command, commandContext);
                } else if (type == CommandType.LIST) {
                    if (type.handler() != null && command.equals("list")) {
                        type.handler().execute(command, commandContext);
                    } else if (command.startsWith("list from ")) {
                        printEventsInRange(command, tasks, parser);
                    } else {
                        throw new GoobleException("Please use: list from yyyy-MM-dd HHmm to yyyy-MM-dd HHmm");
                    }
                } else if (type == CommandType.MARK) {
                    String taskNumber = parser.argumentAfter(command, "mark");
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (!tasks.isValidIndex(taskIndex)) {
                            System.out.println("That task number does not exist.");
                        } else {
                            tasks.markAsDone(taskIndex);
                            ui.showMarkedDone(tasks.get(taskIndex));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (type == CommandType.UNMARK) {
                    String taskNumber = parser.argumentAfter(command, "unmark");
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (!tasks.isValidIndex(taskIndex)) {
                            System.out.println("That task number does not exist.");
                        } else {
                            tasks.markAsNotDone(taskIndex);
                            ui.showMarkedNotDone(tasks.get(taskIndex));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (type == CommandType.DELETE) {
                    String taskNumber = parser.argumentAfter(command, "delete");
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (!tasks.isValidIndex(taskIndex)) {
                            System.out.println("That task number does not exist.");
                        } else {
                            Task removedTask = tasks.remove(taskIndex);
                            ui.showDeleted(removedTask, tasks.size());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (type == CommandType.TODO) {
                    String description = command.substring("todo".length()).trim();
                    parser.validateDescription(description);
                    tasks.add(new Todo(description));
                    ui.showAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                } else if (type == CommandType.DEADLINE) {
                    String[] deadlineParts = parser.parseDeadline(command);
                    {
                        String description = deadlineParts[0];
                        String deadline = deadlineParts[1];
                        DeadlineDateParser.DeadlineDate parsedDeadline = DeadlineDateParser.parse(deadline);
                        tasks.add(new Deadline(description, parsedDeadline));
                        ui.showAdded(tasks.get(tasks.size() - 1));
                        if (DeadlineDateParser.isValentinesDay(parsedDeadline)) {
                            System.out.println("Love is in the air~");
                        }
                        if (DeadlineDateParser.isChineseNewYear(parsedDeadline)) {
                            System.out.println("\u606d\u559c\u53d1\u8d22\uff01\uff01");
                        }
                        ui.showTaskCount(tasks.size());
                    }
                } else if (type == CommandType.EVENT) {
                    String[] eventParts = parser.parseEvent(command);
                    {
                        String description = eventParts[0];
                        String startDate = eventParts[1];
                        String endDate = eventParts[2];
                        tasks.add(new Event(description, startDate, endDate));
                        ui.showAdded(tasks.get(tasks.size() - 1));
                        ui.showTaskCount(tasks.size());
                    }
                } else if (type == CommandType.ADD) {
                    String content = command.substring("add".length()).trim();
                    parser.validateDescription(content);
                    tasks.add(new Task(content));
                    ui.showAddedGeneral(content);
                } else {
                    throw new GoobleException("Invalid command smhmh");
                }
            } catch (GoobleException e) {
                System.out.println(e.getMessage());
            }
            ui.showDivider();
        }
    }

    /** Starts Gooble with its default storage file. */
    public static void main(String[] args) {
        new Gooble("data/Gooble.txt").run();
    }

    /** Prints events fully contained within a validated date-time range. */
    private static void printEventsInRange(String command, TaskList tasks, Parser parser) throws GoobleException {
        DeadlineDateParser.DeadlineDate[] range = parser.parseDateRange(command);
        DeadlineDateParser.DeadlineDate from = range[0];
        DeadlineDateParser.DeadlineDate to = range[1];

        LocalDateTime fromDateTime = LocalDateTime.of(from.date(), from.time());
        LocalDateTime toDateTime = LocalDateTime.of(to.date(), to.time());

        System.out.println("Here are the events in your list for that period:");
        int matchingEventNumber = 1;
        for (int i = 0; i < tasks.size(); i++) {
            if (!(tasks.get(i) instanceof Event event)) {
                continue;
            }
            try {
                DeadlineDateParser.DeadlineDate eventFrom = DeadlineDateParser.parse(event.getStartDate());
                DeadlineDateParser.DeadlineDate eventTo = DeadlineDateParser.parse(event.getEndDate());
                if (eventFrom.time() == null || eventTo.time() == null) {
                    continue;
                }
                LocalDateTime eventStart = LocalDateTime.of(eventFrom.date(), eventFrom.time());
                LocalDateTime eventEnd = LocalDateTime.of(eventTo.date(), eventTo.time());
                if (!eventStart.isBefore(fromDateTime) && !eventEnd.isAfter(toDateTime)) {
                    System.out.println(matchingEventNumber + "." + event);
                    matchingEventNumber++;
                }
            } catch (GoobleException e) {
                // Existing events may use free-form text and cannot be date-filtered.
            }
        }
    }
}
