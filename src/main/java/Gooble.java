import java.util.Scanner;

/**
 * Starts Gooble, responds to entered commands, and exits when requested.
 */
public class Gooble {
    /** Maximum number of tasks the application needs to hold. */
    private static final int MAX_TASKS = 100;

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
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            try {
                if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumber = command.substring("mark ".length()).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumber) - 1;
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (command.startsWith("unmark ")) {
                String taskNumber = command.substring("unmark ".length()).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumber) - 1;
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("That task number does not exist.");
                    } else {
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else if (command.equals("todo") || command.startsWith("todo ")) {
                String description = command.substring("todo".length()).trim();
                validateDescription(description);
                tasks[taskCount] = new Todo(description);
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + tasks[taskCount - 1]);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
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
                    tasks[taskCount] = new Deadline(description, deadline);
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks[taskCount - 1]);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                }
            } else if (command.equals("event") || command.startsWith("event ")) {
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
                    tasks[taskCount] = new Event(description, startDate, endDate);
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks[taskCount - 1]);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                }
            } else if (command.equals("add") || command.startsWith("add ")){
                    String content = command.substring("add".length()).trim();
                    tasks[taskCount] = new Task(command);
                    taskCount++;
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
