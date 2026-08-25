package gooble.command;

import gooble.GoobleException;
import gooble.task.DeadlineDateParser;
import gooble.task.Event;

import java.time.LocalDateTime;

/** Handles filtering events by an inclusive date-time range. */
public class ListFromCommand extends Command {
    public ListFromCommand(String input) { super(input); }
    public void execute(CommandContext context) throws GoobleException {
        DeadlineDateParser.DeadlineDate[] range = context.parser().parseDateRange(input);
        LocalDateTime from = LocalDateTime.of(range[0].date(), range[0].time());
        LocalDateTime to = LocalDateTime.of(range[1].date(), range[1].time());

        System.out.println("Here are the events in your list for that period:");
        int matchingNumber = 1;
        for (int i = 0; i < context.tasks().size(); i++) {
            if (!(context.tasks().get(i) instanceof Event event)) {
                continue;
            }
            try {
                DeadlineDateParser.DeadlineDate eventFrom =
                        DeadlineDateParser.parse(event.getStartDate());
                DeadlineDateParser.DeadlineDate eventTo =
                        DeadlineDateParser.parse(event.getEndDate());
                if (eventFrom.time() == null || eventTo.time() == null) {
                    continue;
                }
                LocalDateTime start = LocalDateTime.of(eventFrom.date(), eventFrom.time());
                LocalDateTime end = LocalDateTime.of(eventTo.date(), eventTo.time());
                if (!start.isBefore(from) && !end.isAfter(to)) {
                    System.out.println(matchingNumber + "." + event);
                    matchingNumber++;
                }
            } catch (GoobleException e) {
                // Free-form event dates cannot be range-filtered.
            }
        }
    }
}
