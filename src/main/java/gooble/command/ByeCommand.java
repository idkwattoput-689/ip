package gooble.command;

/** Handles exiting the application. */
public class ByeCommand extends Command {
    /** Creates an exit command for the complete user input. */
    public ByeCommand(String input) { super(input); }

    /** Displays the farewell message. */
    public void execute(CommandContext context) {
        context.ui().showBye();
    }

    /** Indicates that this command ends the application. */
    @Override
    public boolean isExit() {
        return true;
    }
}
