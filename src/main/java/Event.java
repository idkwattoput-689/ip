/**
 * Represents a task scheduled between a start and end date or time.
 */
public class Event extends Task {
    /** The start date or time of this event. */
    private final String startDate;

    /** The end date or time of this event. */
    private final String endDate;

    /**
     * Creates an incomplete event with a description, start date, and end date.
     *
     * @param description the text describing the event
     * @param startDate the event's start date or time
     * @param endDate the event's end date or time
     */
    public Event(String description, String startDate, String endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the formatted representation used for an event.
     *
     * @return the task type, completion status, description, start date, and end date
     */
    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + startDate + " to: " + endDate + ")";
    }
}
