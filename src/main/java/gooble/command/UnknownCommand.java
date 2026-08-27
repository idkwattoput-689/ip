package gooble.command;

import gooble.GoobleException;

/** Handles unrecognized commands. */
public class UnknownCommand extends Command {
    /** Creates a command for unrecognized user input. */
    public UnknownCommand(String input) { super(input); }

    /** Reports that the entered command is not recognized. */
    @Override
    public void execute(CommandContext context) throws GoobleException {
        throw new GoobleException("Invalid command smhmh");
    }
}
