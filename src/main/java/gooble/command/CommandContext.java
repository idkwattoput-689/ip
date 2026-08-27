package gooble.command;

import gooble.command.Parser;
import gooble.task.TaskList;
import gooble.ui.Ui;

/**
 * Shared application services available to command handlers.
 */
public class CommandContext {
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /** Creates a context containing the services shared by command handlers. */
    public CommandContext(TaskList tasks, Ui ui, Parser parser) {
        this.tasks = tasks;
        this.ui = ui;
        this.parser = parser;
    }

    /** Returns the task list used by commands. */
    public TaskList tasks() { return tasks; }

    /** Returns the user-interface component used by commands. */
    public Ui ui() { return ui; }

    /** Returns the parser used by commands. */
    public Parser parser() { return parser; }
}
