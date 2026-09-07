package gooble.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Tests task state, validation, and display behavior. */
class TaskTest {
    @Test
    void constructor_validDescription_createsIncompleteTask() {
        Task task = new Task("read book");

        assertEquals("read book", task.getDescription());
        assertEquals(" ", task.getStatusIcon());
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    void constructor_nullOrBlankDescription_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null));
        assertThrows(IllegalArgumentException.class, () -> new Task("   "));
    }

    @Test
    void markAsDone_taskBecomesComplete() {
        Task task = new Task("read book");

        task.markAsDone();

        assertEquals("X", task.getStatusIcon());
        assertEquals("[X] read book", task.toString());
    }

    @Test
    void markAsNotDone_completedTaskBecomesIncomplete() {
        Task task = new Task("read book");
        task.markAsDone();

        task.markAsNotDone();

        assertEquals(" ", task.getStatusIcon());
        assertEquals("[ ] read book", task.toString());
    }

    @Test
    void addTag_validTagsAreNormalizedAndDisplayed() {
        Task task = new Task("read book");

        task.addTag("#Fun");

        assertEquals(java.util.List.of("#fun"), task.getTags());
        assertEquals("[ ] read book [#fun]", task.toString());
    }

    @Test
    void addTag_fourthTagRemovesOldestTag() {
        Task task = new Task("read book");
        task.addTag("#one");
        task.addTag("#two");
        task.addTag("#three");

        task.addTag("#four");

        assertEquals(java.util.List.of("#two", "#three", "#four"), task.getTags());
    }

    @Test
    void addTag_duplicateOrInvalidTag_throwsException() {
        Task task = new Task("read book");
        task.addTag("#fun");

        assertThrows(IllegalArgumentException.class, () -> task.addTag("#FUN"));
        assertThrows(IllegalArgumentException.class, () -> task.addTag("fun!"));
    }
}
