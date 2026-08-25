import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Parses, formats, and stores the date and optional time for deadline tasks.
 */
public final class DeadlineDateParser {
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter SLASH_DATE = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
    private static final String INVALID_DATE_MESSAGE = "Please use a proper date format. "
            + "Examples: 2019-12-02 or 2/12/2019 1800";

    private DeadlineDateParser() {
    }

    /**
     * Parses an ISO date or a day/month/year date, with an optional 24-hour time.
     *
     * @param input the deadline text supplied by the user
     * @return a deadline value with a date and, when supplied, a time
     * @throws GoobleException if the input does not use a supported format
     */
    public static DeadlineDate parse(String input) throws GoobleException {
        if (input == null) {
            throw new GoobleException(INVALID_DATE_MESSAGE);
        }
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 1 || parts.length > 2 || parts[0].isBlank()) {
            throw new GoobleException(INVALID_DATE_MESSAGE);
        }

        try {
            LocalDate date = parseDate(parts[0]);
            LocalTime time = parts.length == 2 ? LocalTime.parse(parts[1], TIME) : null;
            return new DeadlineDate(date, time);
        } catch (DateTimeException e) {
            throw new GoobleException(INVALID_DATE_MESSAGE);
        }
    }

    /** Formats a deadline value for the task list. */
    public static String format(DeadlineDate deadline) {
        String formattedDate = deadline.date().format(DISPLAY_DATE);
        return deadline.time() == null ? formattedDate
                : formattedDate + ", " + deadline.time().format(DISPLAY_TIME);
    }

    /** Returns whether the supplied deadline falls on Valentine's Day. */
    public static boolean isValentinesDay(DeadlineDate deadline) {
        return deadline.date().getMonth() == Month.FEBRUARY && deadline.date().getDayOfMonth() == 14;
    }

    private static LocalDate parseDate(String text) {
        return text.contains("/") ? LocalDate.parse(text, SLASH_DATE) : LocalDate.parse(text, ISO_DATE);
    }

    /** Holds the typed date and optional time that make up a deadline. */
    public record DeadlineDate(LocalDate date, LocalTime time) {
        /** Creates a non-null deadline date with an optional deadline time. */
        public DeadlineDate {
            if (date == null) {
                throw new IllegalArgumentException("Deadline date cannot be null.");
            }
        }
    }
}
