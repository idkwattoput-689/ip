package gooble.command;

/** Handles exiting the application. */
public class ByeCommand extends Command {
    public ByeCommand(String input) { super(input); }
    public void execute(CommandContext context) {
        context.ui().showBye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
