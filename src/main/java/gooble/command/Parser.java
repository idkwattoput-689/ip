package gooble.command;

import java.time.LocalDateTime;

import gooble.GoobleException;
import gooble.task.DeadlineDateParser;
import gooble.task.Task;

/**
 * Interprets the command word and arguments entered by the user.
 */
public class Parser {
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String LIST_FROM_PREFIX = "list from ";
    private static final String DEADLINE_MARKER = " /by ";
    private static final String EVENT_START_MARKER = " /from ";
    private static final String EVENT_END_MARKER = " /to ";
    private static final String DATE_RANGE_SEPARATOR = " to ";

    /** Creates the command object corresponding to complete user input. */
    public Command parse(String command) {
        CommandType type = parseType(command);
        if (type == CommandType.LIST && command.startsWith(LIST_FROM_PREFIX)) {
            return new ListFromCommand(command);
        }
        Command parsed = type.handler(command);
        return parsed == null ? new UnknownCommand(command) : parsed;
    }

    /**
     * Determines the command type from a complete input line.
     *
     * @param command complete user input
     * @return matching command type, or {@link CommandType#UNKNOWN}
     */
    public CommandType parseType(String command) {
        String commandWord = command.split("\\s+", 2)[0];
        return CommandType.fromString(commandWord);
    }

    /**
     * Extracts the trimmed text after a command word.
     *
     * @param command complete user input
     * @param commandWord command whose argument should be extracted
     * @return command argument, or an empty string when omitted
     */
    public String argumentAfter(String command, String commandWord) {
        if (command.length() <= commandWord.length()) {
            return "";
        }
        return command.substring(commandWord.length()).trim();
    }

    /**
     * Ensures that a command contains a non-empty description.
     *
     * @param description description extracted from a command
     * @throws GoobleException when the description is empty
     */
    public void validateDescription(String description) throws GoobleException {
        if (description.isEmpty()) {
            throw new GoobleException("Missing a description. Gooble needs something to put on the task list.");
        }
    }

    /** Parses a tag command into a one-based task number and one tag. */
    public String[] parseTag(String command) throws GoobleException {
        String details = argumentAfter(command, "tag");
        String[] parts = details.split("\\s+");
        if (parts.length < 2) {
            throw new GoobleException("Please provide a tag in the format #tag. Gooble cannot tag vibes alone.");
        }
        if (parts.length > 2) {
            throw new GoobleException("Please provide exactly one tag. Gooble's tag drawer is already wobbly.");
        }
        validateTag(parts[1]);
        return new String[] { parts[0], parts[1].toLowerCase(java.util.Locale.ROOT) };
    }

    /** Validates a user-provided tag. */
    public void validateTag(String tag) throws GoobleException {
        if (!tag.startsWith("#")) {
            throw new GoobleException("Please provide a tag in the format #tag. Gooble cannot tag vibes alone.");
        }
        if (!Task.isValidTag(tag)) {
            throw new GoobleException("Tags may contain only letters, numbers, hyphens, and underscores. "
                    + "Gooble rejects tag confetti.");
        }
    }

    /** Parses a deadline command into description and deadline text. */
    public String[] parseDeadline(String command) throws GoobleException {
        String details = command.substring(DEADLINE_COMMAND.length()).trim();
        validateDescription(details);
        int markerIndex = details.indexOf(DEADLINE_MARKER);
        if (markerIndex == -1) {
            throw new GoobleException("Please specify a deadline using /by. Gooble needs a date to do calendar magic.");
        }
        String description = details.substring(0, markerIndex).trim();
        String deadline = details.substring(markerIndex + DEADLINE_MARKER.length()).trim();
        validateDescription(description);
        if (deadline.isEmpty()) {
            throw new GoobleException("Please specify a deadline using /by. Gooble needs a date to do calendar magic.");
        }
        return new String[] { description, deadline };
    }

    /** Parses an event command into description, start, and end text. */
    public String[] parseEvent(String command) throws GoobleException {
        String details = command.substring(EVENT_COMMAND.length()).trim();
        validateDescription(details);
        int startIndex = details.indexOf(EVENT_START_MARKER);
        int endIndex = details.indexOf(EVENT_END_MARKER);
        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
            throw new GoobleException("Please specify an event time using /from and /to. "
                    + "Gooble needs both ends of the event.");
        }
        String description = details.substring(0, startIndex).trim();
        String start = details.substring(startIndex + EVENT_START_MARKER.length(), endIndex).trim();
        String end = details.substring(endIndex + EVENT_END_MARKER.length()).trim();
        validateDescription(description);
        if (start.isEmpty() || end.isEmpty()) {
            throw new GoobleException("Please specify an event time using /from and /to. "
                    + "Gooble needs both ends of the event.");
        }
        return new String[] { description, start, end };
    }

    /** Parses and validates a list command's inclusive date-time range. */
    public DeadlineDateParser.DeadlineDate[] parseDateRange(String command) throws GoobleException {
        String range = command.substring(LIST_FROM_PREFIX.length()).trim();
        int separator = range.indexOf(DATE_RANGE_SEPARATOR);
        if (separator <= 0 || separator + DATE_RANGE_SEPARATOR.length() >= range.length()) {
            throw new GoobleException("Please use: list from yyyy-MM-dd HHmm to yyyy-MM-dd HHmm");
        }
        DeadlineDateParser.DeadlineDate from = DeadlineDateParser.parse(range.substring(0, separator).trim());
        DeadlineDateParser.DeadlineDate to = DeadlineDateParser.parse(
                range.substring(separator + DATE_RANGE_SEPARATOR.length()).trim());
        if (from.time() == null || to.time() == null) {
            throw new GoobleException("Please include both dates and times, e.g. 2026-02-01 0900");
        }
        LocalDateTime fromDateTime = LocalDateTime.of(from.date(), from.time());
        LocalDateTime toDateTime = LocalDateTime.of(to.date(), to.time());
        if (toDateTime.isBefore(fromDateTime)) {
            throw new GoobleException("Please ensure the 'to' date and time is not before the 'from' date and time.");
        }

        // A successful range parse always returns two fully specified, ordered times.
        assert from.time() != null && to.time() != null;
        assert !java.time.LocalDateTime.of(to.date(), to.time())
                .isBefore(java.time.LocalDateTime.of(from.date(), from.time()));
        return new DeadlineDateParser.DeadlineDate[] { from, to };
    }
}
