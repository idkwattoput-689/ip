package gooble.command;

import gooble.GoobleException;
import gooble.task.Event;

/** Handles adding an event task. */
public class EventCommand extends Command {
    /** Creates an event command for the complete user input. */
    public EventCommand(String input) { super(input); }

    /** Parses and adds a task scheduled between two event times. */
    public void execute(CommandContext context) throws GoobleException {
        String[] parts = context.parser().parseEvent(input);
        context.tasks().add(new Event(parts[0], parts[1], parts[2]));
        context.ui().showAdded(context.tasks().get(context.tasks().size() - 1));
        context.ui().showTaskCount(context.tasks().size());
    }
}
