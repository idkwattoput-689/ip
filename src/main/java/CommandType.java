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
    public CommandHandler handler() {
        return switch (this) {
        case LIST -> new ListCommand();
        case MARK -> new MarkCommand();
        case UNMARK -> new UnmarkCommand();
        case DELETE -> new DeleteCommand();
        case TODO -> new TodoCommand();
        case ADD -> new AddCommand();
        case BYE -> new ByeCommand();
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
