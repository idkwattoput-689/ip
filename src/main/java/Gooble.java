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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
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
