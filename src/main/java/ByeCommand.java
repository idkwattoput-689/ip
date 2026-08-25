/** Handles exiting the application. */
public class ByeCommand extends Command {
    public void execute(String input, CommandContext context) {
        context.ui().showBye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
