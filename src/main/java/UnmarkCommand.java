/** Handles marking a task incomplete. */
public class UnmarkCommand implements CommandHandler {
    public void execute(String input, CommandContext context) {
        MarkCommand.execute(input, context, false);
    }
}
