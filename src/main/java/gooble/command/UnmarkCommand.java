package gooble.command;

/** Handles marking a task incomplete. */
public class UnmarkCommand extends Command {
    /** Creates an unmark command for the complete user input. */
    public UnmarkCommand(String input) { super(input); }

    /** Marks the task identified by the number after the {@code unmark} command as incomplete. */
    public void execute(CommandContext context) {
        MarkCommand.execute(input, context, false);
    }
}
