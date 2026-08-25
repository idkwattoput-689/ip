package gooble.command;

/** Handles marking a task complete or incomplete. */
public class MarkCommand extends Command {
    public MarkCommand(String input) { super(input); }
    public void execute(CommandContext context) {
        execute(input, context, true);
    }

    static void execute(String input, CommandContext context, boolean done) {
        String number = context.parser().argumentAfter(input, done ? "mark" : "unmark");
        try {
            int index = Integer.parseInt(number) - 1;
            if (!context.tasks().isValidIndex(index)) {
                System.out.println("That task number does not exist.");
            } else if (done) {
                context.tasks().markAsDone(index);
                context.ui().showMarkedDone(context.tasks().get(index));
            } else {
                context.tasks().markAsNotDone(index);
                context.ui().showMarkedNotDone(context.tasks().get(index));
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid task number.");
        }
    }
}
