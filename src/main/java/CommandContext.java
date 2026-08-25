/**
 * Shared application services available to command handlers.
 */
public class CommandContext {
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    public CommandContext(TaskList tasks, Ui ui, Parser parser) {
        this.tasks = tasks;
        this.ui = ui;
        this.parser = parser;
    }

    public TaskList tasks() { return tasks; }
    public Ui ui() { return ui; }
    public Parser parser() { return parser; }
}
