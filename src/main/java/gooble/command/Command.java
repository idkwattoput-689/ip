package gooble.command;

import gooble.GoobleException;

/**
 * Base class for commands that can be executed by Gooble.
 */
public abstract class Command {
    protected final String input;

    protected Command(String input) {
        this.input = input;
    }
    /**
     * Executes this command with the application's shared services.
     *
     * @param input complete user input
     * @param context shared application services
     * @throws GoobleException when the command is invalid
     */
    public abstract void execute(CommandContext context) throws GoobleException;

    /** Returns whether this command ends the application. */
    public boolean isExit() {
        return false;
    }
}
