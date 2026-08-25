/** Handles marking a task incomplete. */
public class UnmarkCommand extends Command {
    public void execute(String input, CommandContext context) {
        MarkCommand.execute(input, context, false);
    }
}
