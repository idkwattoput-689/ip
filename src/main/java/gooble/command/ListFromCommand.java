package gooble.command;

import java.time.LocalDateTime;

import gooble.GoobleException;
import gooble.task.DeadlineDateParser;
import gooble.task.Event;

/** Handles filtering events by an inclusive date-time range. */
public class ListFromCommand extends Command {
    /** Creates a date-range list command from complete user input. */
    public ListFromCommand(String input) {
        super(input);
    }

    /** Displays events contained in the requested inclusive date-time range. */
    public void execute(CommandContext context) throws GoobleException {
        DeadlineDateParser.DeadlineDate[] range = context.parser().parseDateRange(input);
        LocalDateTime from = LocalDateTime.of(range[0].date(), range[0].time());
        LocalDateTime to = LocalDateTime.of(range[1].date(), range[1].time());

        context.ui().showMessage("Here are the events in your list for that period:");
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
                    context.ui().showMessage(matchingNumber + "." + event);
                    matchingNumber++;
                }
            } catch (GoobleException e) {
                // Free-form event dates cannot be range-filtered.
            }
        }
    }
}
