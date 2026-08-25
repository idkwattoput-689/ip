/** Handles deleting a task. */
public class DeleteCommand extends Command {
    public DeleteCommand(String input) { super(input); }
    public void execute(CommandContext context) {
        String number = context.parser().argumentAfter(input, "delete");
        try {
            int index = Integer.parseInt(number) - 1;
            if (!context.tasks().isValidIndex(index)) {
                System.out.println("That task number does not exist.");
            } else {
                Task removed = context.tasks().remove(index);
                context.ui().showDeleted(removed, context.tasks().size());
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid task number.");
        }
    }
}
