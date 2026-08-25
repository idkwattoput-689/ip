/**
 * Executes one parsed command using shared application services.
 */
@FunctionalInterface
public interface CommandHandler {
    void execute(String input, CommandContext context) throws GoobleException;
}
