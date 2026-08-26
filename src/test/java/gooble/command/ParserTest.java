package gooble.command;

import gooble.GoobleException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests command parsing and validation rules. */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseType_commandWithArguments_returnsCommandType() {
        assertEquals(CommandType.TODO, parser.parseType("todo read book"));
        assertEquals(CommandType.UNKNOWN, parser.parseType("launch app"));
    }

    @Test
    void argumentAfter_commandWithArgument_returnsTrimmedArgument() {
        assertEquals("read book", parser.argumentAfter("todo   read book", "todo"));
        assertEquals("", parser.argumentAfter("todo", "todo"));
    }

    @Test
    void parse_supportedAndUnknownCommands_returnsExpectedCommand() {
        assertEquals(TodoCommand.class, parser.parse("todo read book").getClass());
        assertEquals(UnknownCommand.class, parser.parse("launch app").getClass());
    }

    @Test
    void parseDeadline_validCommand_returnsDescriptionAndDeadline() throws GoobleException {
        assertArrayEquals(new String[] {"return book", "2026-02-01 0900"},
                parser.parseDeadline("deadline return book /by 2026-02-01 0900"));
    }

    @Test
    void parseDeadline_missingMarkerOrDescription_throwsException() {
        assertThrows(GoobleException.class, () -> parser.parseDeadline("deadline return book"));
        assertThrows(GoobleException.class, () -> parser.parseDeadline("deadline  /by 2026-02-01"));
    }

    @Test
    void parseEvent_validCommand_returnsDescriptionAndTimes() throws GoobleException {
        assertArrayEquals(new String[] {"project meeting", "Mon 2pm", "4pm"},
                parser.parseEvent("event project meeting /from Mon 2pm /to 4pm"));
    }

    @Test
    void parseEvent_missingTimeMarker_throwsException() {
        assertThrows(GoobleException.class, () -> parser.parseEvent("event project meeting"));
        assertThrows(GoobleException.class,
                () -> parser.parseEvent("event project meeting /from Mon 2pm /to"));
    }

    @Test
    void parseDateRange_validOrderedRange_returnsBothDeadlines() throws GoobleException {
        var range = parser.parseDateRange("list from 2026-02-01 0900 to 2026-02-01 1700");

        assertEquals(LocalDate.of(2026, 2, 1), range[0].date());
        assertEquals(LocalTime.of(9, 0), range[0].time());
        assertEquals(LocalTime.of(17, 0), range[1].time());
    }

    @Test
    void parseDateRange_reversedOrMissingTimes_throwsException() {
        assertThrows(GoobleException.class,
                () -> parser.parseDateRange("list from 2026-02-02 0900 to 2026-02-01 1700"));
        assertThrows(GoobleException.class,
                () -> parser.parseDateRange("list from 2026-02-01 to 2026-02-02"));
    }
}
