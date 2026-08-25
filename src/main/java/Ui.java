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
}
