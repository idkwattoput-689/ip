/** Handles adding a general task. */
public class AddCommand implements CommandHandler {
    public void execute(String input, CommandContext context) throws GoobleException {
        String description = input.substring("add".length()).trim();
        context.parser().validateDescription(description);
        context.tasks().add(new Task(description));
        context.ui().showAddedGeneral(description);
    }
}
