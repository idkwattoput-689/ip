/**
 * Starts Gooble, displays its greeting, and exits.
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
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);
    }
}
