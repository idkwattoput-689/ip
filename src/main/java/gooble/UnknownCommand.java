package gooble;

/** Handles unrecognized commands. */
public class UnknownCommand extends Command {
    public UnknownCommand(String input) { super(input); }

    @Override
    public void execute(CommandContext context) throws GoobleException {
        throw new GoobleException("Invalid command smhmh");
    }
}
