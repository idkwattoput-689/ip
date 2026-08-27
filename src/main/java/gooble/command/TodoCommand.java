package gooble.command;

import gooble.GoobleException;
import gooble.task.Todo;

/** Handles adding a to-do task. */
public class TodoCommand extends Command {
    /** Creates a to-do command from complete user input. */
    public TodoCommand(String input) {
        super(input);
    }

    /** Adds a to-do task described by the command input. */
    public void execute(CommandContext context) throws GoobleException {
        String description = input.substring("todo".length()).trim();
        context.parser().validateDescription(description);
        context.tasks().add(new Todo(description));
        context.ui().showAdded(context.tasks().get(context.tasks().size() - 1));
        context.ui().showTaskCount(context.tasks().size());
    }
}
