/**
 * Handles the basic list command.
 */
public class ListCommand extends Command {
    public ListCommand(String input) { super(input); }

    @Override
    public void execute(CommandContext context) throws GoobleException {
        if (!input.equals("list")) {
            throw new GoobleException("Please use: list from yyyy-MM-dd HHmm to yyyy-MM-dd HHmm");
        }
        context.ui().showTasks(context.tasks());
    }
}
