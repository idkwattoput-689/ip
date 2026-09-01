package gooble.ui;

import java.util.List;
import java.util.function.Consumer;

import gooble.task.Task;
import gooble.task.TaskList;

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
    private final Consumer<String> output;

    /** Creates a console user interface. */
    public Ui() {
        this(System.out::print);
    }

    /** Creates a user interface that writes to the supplied output. */
    public Ui(Consumer<String> output) {
        this.output = output;
    }

    /** Displays one line of text. */
    public void showMessage(String message) {
        output.accept(message + System.lineSeparator());
    }

    /** Prints a horizontal divider used to separate console messages. */
    public void showDivider() {
        showMessage(DIVIDER);
    }

    /** Prints Gooble's welcome banner and greeting. */
    public void showWelcome() {
        showDivider();
        output.accept(BANNER);
        showMessage("Hello! I'm Gooble.");
        showMessage("What can I do for you?");
        showDivider();
    }

    /**
     * Prints every task in its current list order.
     *
     * @param tasks task list to display
     */
    public void showTasks(TaskList tasks) {
        showMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }

    /** Prints tasks matching a search keyword in their original task-list order. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        showMessage("Here are the matching tasks in your list tehee:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            showMessage((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /** Reports that a task was marked complete. */
    public void showMarkedDone(Task task) {
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + task);
    }

    /** Reports that a task was marked incomplete. */
    public void showMarkedNotDone(Task task) {
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + task);
    }

    /** Reports a removed task and the remaining task count. */
    public void showDeleted(Task task, int remainingCount) {
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showMessage("Now you have " + remainingCount + " tasks in the list.");
    }

    /** Reports a newly added task using Gooble's standard confirmation. */
    public void showAdded(Task task) {
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
    }

    /** Reports the number of tasks currently in the list. */
    public void showTaskCount(int totalCount) {
        showMessage("Now you have " + totalCount + " tasks in the list.");
    }

    /** Prints the application's exit message. */
    public void showBye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /** Reports the legacy add command's confirmation. */
    public void showAddedGeneral(String description) {
        showMessage("added: " + description);
    }
}
