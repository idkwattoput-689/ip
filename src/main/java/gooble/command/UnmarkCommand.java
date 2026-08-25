package gooble.command;

/** Handles marking a task incomplete. */
public class UnmarkCommand extends Command {
    public UnmarkCommand(String input) { super(input); }
    public void execute(CommandContext context) {
        MarkCommand.execute(input, context, false);
    }
}
