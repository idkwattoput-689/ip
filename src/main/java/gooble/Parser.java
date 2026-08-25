package gooble;

/**
 * Interprets the command word and arguments entered by the user.
 */
public class Parser {
    /** Creates the command object corresponding to complete user input. */
    public Command parse(String command) {
        CommandType type = parseType(command);
        if (type == CommandType.LIST && command.startsWith("list from ")) {
            return new ListFromCommand(command);
        }
        Command parsed = type.handler(command);
        return parsed == null ? new UnknownCommand(command) : parsed;
    }

    /**
     * Determines the command type from a complete input line.
     *
     * @param command complete user input
     * @return matching command type, or {@link CommandType#INVALID}
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
            throw new GoobleException("You need to add in some description for that lmao");
        }
    }

    /** Parses a deadline command into description and deadline text. */
    public String[] parseDeadline(String command) throws GoobleException {
        String details = command.substring("deadline".length()).trim();
        validateDescription(details);
        String marker = " /by ";
        int markerIndex = details.indexOf(marker);
        if (markerIndex == -1) {
            throw new GoobleException("Please specify a deadline using /by.");
        }
        String description = details.substring(0, markerIndex).trim();
        String deadline = details.substring(markerIndex + marker.length()).trim();
        validateDescription(description);
        if (deadline.isEmpty()) {
            throw new GoobleException("Please specify a deadline using /by.");
        }
        return new String[] { description, deadline };
    }

    /** Parses an event command into description, start, and end text. */
    public String[] parseEvent(String command) throws GoobleException {
        String details = command.substring("event".length()).trim();
        validateDescription(details);
        String startMarker = " /from ";
        String endMarker = " /to ";
        int startIndex = details.indexOf(startMarker);
        int endIndex = details.indexOf(endMarker);
        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
            throw new GoobleException("Please specify an event time using /from and /to.");
        }
        String description = details.substring(0, startIndex).trim();
        String start = details.substring(startIndex + startMarker.length(), endIndex).trim();
        String end = details.substring(endIndex + endMarker.length()).trim();
        validateDescription(description);
        if (start.isEmpty() || end.isEmpty()) {
            throw new GoobleException("Please specify an event time using /from and /to.");
        }
        return new String[] { description, start, end };
    }

    /** Parses and validates a list command's inclusive date-time range. */
    public DeadlineDateParser.DeadlineDate[] parseDateRange(String command) throws GoobleException {
        String range = command.substring("list from ".length()).trim();
        int separator = range.indexOf(" to ");
        if (separator <= 0 || separator + 4 >= range.length()) {
            throw new GoobleException("Please use: list from yyyy-MM-dd HHmm to yyyy-MM-dd HHmm");
        }
        DeadlineDateParser.DeadlineDate from = DeadlineDateParser.parse(range.substring(0, separator).trim());
        DeadlineDateParser.DeadlineDate to = DeadlineDateParser.parse(range.substring(separator + 4).trim());
        if (from.time() == null || to.time() == null) {
            throw new GoobleException("Please include both dates and times, e.g. 2026-02-01 0900");
        }
        if (java.time.LocalDateTime.of(to.date(), to.time())
                .isBefore(java.time.LocalDateTime.of(from.date(), from.time()))) {
            throw new GoobleException("Please ensure the 'to' date and time is not before the 'from' date and time.");
        }
        return new DeadlineDateParser.DeadlineDate[] { from, to };
    }
}
