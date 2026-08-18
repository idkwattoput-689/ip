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
        String[] tasks = new String[MAX_TASKS];
        /** Tracks whether each corresponding task has been marked as done. */
        boolean[] completed = new boolean[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    String status = completed[i] ? "[X]" : "[ ]";
                    System.out.println((i + 1) + "." + status + " " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumber = command.substring("mark ".length()).trim();
                try {
                    int taskIndex = Integer.parseInt(taskNumber) - 1;
                    if (taskIndex < 0 || taskIndex >= taskCount) {
                        System.out.println("That task number does not exist.");
                    } else {
                        completed[taskIndex] = true;
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  [X] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please provide a valid task number.");
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            }
            System.out.println(divider);
        }
    }
}
