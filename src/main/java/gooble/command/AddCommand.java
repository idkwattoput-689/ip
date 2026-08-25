package gooble.command;

import gooble.GoobleException;
import gooble.task.Task;
import gooble.task.TaskList;
import gooble.task.Todo;

/** Handles adding a general task. */
public class AddCommand extends Command {
    public AddCommand(String input) { super(input); }
    public void execute(CommandContext context) throws GoobleException {
        String description = input.substring("add".length()).trim();
        context.parser().validateDescription(description);
        context.tasks().add(new Task(description));
        context.ui().showAddedGeneral(description);
    }
}
