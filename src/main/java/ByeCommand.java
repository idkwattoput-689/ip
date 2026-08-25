/** Handles exiting the application. */
public class ByeCommand implements CommandHandler {
    public void execute(String input, CommandContext context) {
        context.ui().showBye();
    }
}
