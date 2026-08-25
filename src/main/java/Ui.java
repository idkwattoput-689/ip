/**
 * Handles Gooble's console presentation.
 */
public class Ui {
    private static final String DIVIDER = "_".repeat(60);
    private static final String BANNER = "  ____            _     _\n"
            + " / ___| ___   ___ | |__ | | ___\n"
            + "| |  _ / _ \\ / _ \\| '_ \\| |/ _ \\\n"
            + "| |_| | (_) | (_) | |_) | |  __/\n"
            + " \\____|\\___/ \\___/|_.__/|_|\\___|\n";

    /** Prints a horizontal divider used to separate console messages. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Prints Gooble's welcome banner and greeting. */
    public void showWelcome() {
        showDivider();
        System.out.print(BANNER);
        System.out.println("Hello! I'm Gooble.");
        System.out.println("What can I do for you?");
        showDivider();
    }

    /**
     * Prints every task in its current list order.
     *
     * @param tasks task list to display
     */
    public void showTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Reports that a task was marked complete. */
    public void showMarkedDone(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /** Reports that a task was marked incomplete. */
    public void showMarkedNotDone(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /** Reports a removed task and the remaining task count. */
    public void showDeleted(Task task, int remainingCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + remainingCount + " tasks in the list.");
    }
}
