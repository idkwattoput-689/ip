package gooble.task;

import gooble.GoobleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests the supported deadline date and time input formats. */
class DeadlineDateParserTest {
    @Test
    void parse_isoDateWithoutTime_returnsDateAndNoTime() throws GoobleException {
        DeadlineDateParser.DeadlineDate deadline = DeadlineDateParser.parse("2026-02-01");

        assertEquals(LocalDate.of(2026, 2, 1), deadline.date());
        assertNull(deadline.time());
    }

    @Test
    void parse_slashDateWithTime_returnsDateAndTime() throws GoobleException {
        DeadlineDateParser.DeadlineDate deadline = DeadlineDateParser.parse("2/12/2019 1800");

        assertEquals(LocalDate.of(2019, 12, 2), deadline.date());
        assertEquals(LocalTime.of(18, 0), deadline.time());
    }

    @Test
    void parse_inputWithSurroundingWhitespace_trimsInput() throws GoobleException {
        DeadlineDateParser.DeadlineDate deadline = DeadlineDateParser.parse("  2026-02-01  0905  ");

        assertEquals(LocalDate.of(2026, 2, 1), deadline.date());
        assertEquals(LocalTime.of(9, 5), deadline.time());
    }

    @Test
    void parse_invalidCalendarDate_exceptionThrown() {
        assertThrows(GoobleException.class, () -> DeadlineDateParser.parse("2025-02-29"));
    }

    @Test
    void parse_invalidTime_exceptionThrown() {
        assertThrows(GoobleException.class, () -> DeadlineDateParser.parse("2026-02-01 2460"));
    }

    @Test
    void parse_nullBlankOrTooManyParts_exceptionThrown() {
        assertThrows(GoobleException.class, () -> DeadlineDateParser.parse(null));
        assertThrows(GoobleException.class, () -> DeadlineDateParser.parse("   "));
        assertThrows(GoobleException.class, () -> DeadlineDateParser.parse("2026-02-01 0900 extra"));
    }
}
