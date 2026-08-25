import java.time.LocalDateTime;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Starts Gooble, responds to entered commands, and exits when requested.
 */
public class Gooble {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        String banner = "  ____            _     _      \n"
                + " / ___| ___   ___ | |__ | | ___ \n"
                + "| |  _ / _ \\ / _ \\| '_ \\| |/ _ \\\n"
                + "| |_| | (_) | (_) | |_) | |  __/\n"
                + " \\____|\\___/ \\___/|_.__/|_|\\___|\n";

        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(Path.of("data", "Gooble.txt"));
        TaskList tasks = new TaskList(storage);

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().trim();
            ui.showDivider();

            CommandType type = parser.parseType(command);

            if (type == CommandType.BYE) {
                System.out.println("Bye. Hope to see you again soon!");
                ui.showDivider();
                break;
            }

            try {
                if (type == CommandType.LIST) {
                    if (command.equals("list")) {
                        ui.showTasks(tasks);
                    } else if (command.startsWith("list from ")) {
                        printEventsInRange(command, tasks);
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
                    validateDescription(description);
                    tasks.add(new Todo(description));
                    ui.showAdded(tasks.get(tasks.size() - 1));
                    ui.showTaskCount(tasks.size());
                } else if (type == CommandType.DEADLINE) {
                    String deadlineDetails = command.substring("deadline".length()).trim();
                    validateDescription(deadlineDetails);
                    String deadlineMarker = " /by ";
                    int deadlineMarkerIndex = deadlineDetails.indexOf(deadlineMarker);

                    if (deadlineMarkerIndex == -1) {
                        throw new GoobleException("Please specify a deadline using /by.");
                    } else {
                        String description = deadlineDetails.substring(0, deadlineMarkerIndex).trim();
                        String deadline = deadlineDetails
                                .substring(deadlineMarkerIndex + deadlineMarker.length()).trim();
                        validateDescription(description);
                        if (deadline.isEmpty()) {
                            throw new GoobleException("Please specify a deadline using /by.");
                        }
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
                    String eventDetails = command.substring("event".length()).trim();
                    validateDescription(eventDetails);
                    String startMarker = " /from ";
                    String endMarker = " /to ";
                    int startMarkerIndex = eventDetails.indexOf(startMarker);
                    int endMarkerIndex = eventDetails.indexOf(endMarker);

                    if (startMarkerIndex == -1 || endMarkerIndex == -1
                            || endMarkerIndex < startMarkerIndex) {
                        throw new GoobleException("Please specify an event time using /from and /to.");
                    } else {
                        String description = eventDetails.substring(0, startMarkerIndex).trim();
                        String startDate = eventDetails.substring(startMarkerIndex + startMarker.length(),
                                endMarkerIndex).trim();
                        String endDate = eventDetails.substring(endMarkerIndex + endMarker.length()).trim();
                        validateDescription(description);
                        if (startDate.isEmpty() || endDate.isEmpty()) {
                            throw new GoobleException("Please specify an event time using /from and /to.");
                        }
                        tasks.add(new Event(description, startDate, endDate));
                        ui.showAdded(tasks.get(tasks.size() - 1));
                        ui.showTaskCount(tasks.size());
                    }
                } else if (type == CommandType.ADD) {
                    String content = command.substring("add".length()).trim();
                    validateDescription(content);
                    tasks.add(new Task(content));
                    System.out.println("added: " + content);
                } else {
                    throw new GoobleException("Invalid command smhmh");
                }
            } catch (GoobleException e) {
                System.out.println(e.getMessage());
            }
            ui.showDivider();
        }
    }

    /**
     * Ensures that a task command includes a non-empty description.
     *
     * @param description description extracted from the command
     * @throws GoobleException if the description is empty
     */
    private static void validateDescription(String description) throws GoobleException {
        if (description.isEmpty()) {
            throw new GoobleException("You need to add in some description for that lmao");
        }
    }

    /** Prints events fully contained within a validated date-time range. */
    private static void printEventsInRange(String command, TaskList tasks) throws GoobleException {
        String range = command.substring("list from ".length()).trim();
        int separator = range.indexOf(" to ");
        if (separator <= 0 || separator + 4 >= range.length()) {
            throw new GoobleException("Please use: list from yyyy-MM-dd HHmm to yyyy-MM-dd HHmm");
        }

        DeadlineDateParser.DeadlineDate from = DeadlineDateParser.parse(range.substring(0, separator).trim());
        DeadlineDateParser.DeadlineDate to = DeadlineDateParser.parse(range.substring(separator + 4).trim());
        if (from.time() == null || to.time() == null) {
            throw new GoobleException("Please include both dates and times, e.g. 2026-02-01 0900");
        }

        LocalDateTime fromDateTime = LocalDateTime.of(from.date(), from.time());
        LocalDateTime toDateTime = LocalDateTime.of(to.date(), to.time());
        if (toDateTime.isBefore(fromDateTime)) {
            throw new GoobleException("Please ensure the 'to' date and time is not before the 'from' date and time.");
        }

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
