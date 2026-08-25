/** Handles adding an event task. */
public class EventCommand implements CommandHandler {
    public void execute(String input, CommandContext context) throws GoobleException {
        String[] parts = context.parser().parseEvent(input);
        context.tasks().add(new Event(parts[0], parts[1], parts[2]));
        context.ui().showAdded(context.tasks().get(context.tasks().size() - 1));
        context.ui().showTaskCount(context.tasks().size());
    }
}
