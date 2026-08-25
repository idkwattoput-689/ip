package gooble.command;

/**
 * Represents the type of command a user can enter in Gooble.
 */
public enum CommandType {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    ADD,
    BYE,
    UNKNOWN;

    /** Returns the handler for commands migrated to the handler architecture. */
    public Command handler(String input) {
        return switch (this) {
        case LIST -> new ListCommand(input);
        case MARK -> new MarkCommand(input);
        case UNMARK -> new UnmarkCommand(input);
        case DELETE -> new DeleteCommand(input);
        case TODO -> new TodoCommand(input);
        case ADD -> new AddCommand(input);
        case DEADLINE -> new DeadlineCommand(input);
        case EVENT -> new EventCommand(input);
        case BYE -> new ByeCommand(input);
        default -> null;
        };
    }

    /**
     * Returns the CommandType matching the given command word.
     *
     * @param commandWord the first word of user input
     * @return the matching CommandType, or UNKNOWN if unrecognized
     */
    public static CommandType fromString(String commandWord) {
        try {
            return CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
