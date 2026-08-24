import java.util.Scanner;

/**
 * Starts Gooble, responds to entered commands, and exits when requested.
 */
public class Gooble {
    public static void main(String[] args) {
        String divider = "_".repeat(60);
        String banner = "  ____            _     _      \n"
                + " / ___| ___   ___ | |__ | | ___ \n"
                + "| |  _ / _ \\ / _ \\| '_ \\| |/ _ \\\n"
                + "| |_| | (_) | (_) | |_) | |  __/\n"
                + " \\____|\\___/ \\___/|_.__/|_|\\___|\n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Hello! I'm Gooble.");
        System.out.println("What can I do for you?");
        System.out.println(divider);

        Scanner scanner = new Scanner(System.in);
        TaskList tasks = new TaskList();

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            String commandWord = command.split(" ", 2)[0];
            CommandType type = CommandType.fromString(commandWord);

            if (type == CommandType.BYE) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            try {
                if (type == CommandType.LIST) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                } else if (type == CommandType.MARK) {
                    String taskNumber = command.substring("mark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (!tasks.isValidIndex(taskIndex)) {
                            System.out.println("That task number does not exist.");
                        } else {
                            tasks.markAsDone(taskIndex);
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  " + tasks.get(taskIndex));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (type == CommandType.UNMARK) {
                    String taskNumber = command.substring("unmark ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (!tasks.isValidIndex(taskIndex)) {
                            System.out.println("That task number does not exist.");
                        } else {
                            tasks.markAsNotDone(taskIndex);
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  " + tasks.get(taskIndex));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (type == CommandType.DELETE) {
                    String taskNumber = command.substring("delete ".length()).trim();
                    try {
                        int taskIndex = Integer.parseInt(taskNumber) - 1;
                        if (!tasks.isValidIndex(taskIndex)) {
                            System.out.println("That task number does not exist.");
                        } else {
                            Task removedTask = tasks.remove(taskIndex);
                            System.out.println("Noted. I've removed this task:");
                            System.out.println("  " + removedTask);
                            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide a valid task number.");
                    }
                } else if (type == CommandType.TODO) {
                    String description = command.substring("todo".length()).trim();
                    validateDescription(description);
                    tasks.add(new Todo(description));
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1));
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
                        tasks.add(new Deadline(description, deadline));
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + tasks.get(tasks.size() - 1));
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + tasks.get(tasks.size() - 1));
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
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
            System.out.println(divider);
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
}
