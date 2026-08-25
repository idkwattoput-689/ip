/**
 * Interprets the command word and arguments entered by the user.
 */
public class Parser {
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
}
