/**
 * Represents a task that must be completed by a specified deadline.
 */
public class Deadline extends Task {
    /** The deadline associated with this task. */
    private final String deadline;

    /**
     * Creates an incomplete deadline task with a description and deadline.
     *
     * @param description the text describing the task
     * @param deadline the date or time by which the task should be completed
     */
    public Deadline(String description, String deadline) {
        super(description);
        if (deadline == null || deadline.isBlank()) {
            throw new IllegalArgumentException("Deadline cannot be empty.");
        }
        this.deadline = deadline;
    }

    /**
     * Returns the deadline text.
     *
     * @return the deadline
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * Returns the formatted representation used for a deadline task.
     *
     * @return the task type, completion status, description, and deadline
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + deadline + ")";
    }
}
