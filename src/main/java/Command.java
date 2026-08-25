/**
 * Base class for commands that can be executed by Gooble.
 */
public abstract class Command {
    /**
     * Executes this command with the application's shared services.
     *
     * @param input complete user input
     * @param context shared application services
     * @throws GoobleException when the command is invalid
     */
    public abstract void execute(String input, CommandContext context) throws GoobleException;

    /** Returns whether this command ends the application. */
    public boolean isExit() {
        return false;
    }
}
