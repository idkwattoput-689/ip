/** Handles adding a to-do task. */
public class TodoCommand extends Command {
    public void execute(String input, CommandContext context) throws GoobleException {
        String description = input.substring("todo".length()).trim();
        context.parser().validateDescription(description);
        context.tasks().add(new Todo(description));
        context.ui().showAdded(context.tasks().get(context.tasks().size() - 1));
        context.ui().showTaskCount(context.tasks().size());
    }
}
