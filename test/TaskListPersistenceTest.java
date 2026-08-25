import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Checks that tasks saved by a task list are restored by a later task list.
 */
public class TaskListPersistenceTest {
    /**
     * Saves one task of each supported type, then verifies their details and
     * completion state after the task list is recreated.
     *
     * @param args ignored command-line arguments
     */
    public static void main(String[] args) throws GoobleException {
        verifyFirstRunWithoutStorage();
        writeMalformedAndLegacyRecords();
        clearStorage();

        TaskList savedTasks = new TaskList();
        savedTasks.add(new Todo("read | book ) [special]"));
        savedTasks.add(new Deadline("return book", DeadlineDateParser.parse("2/12/2019 1800")));
        savedTasks.add(new Event("project meeting", "Mon (2pm)", "4pm | room 1"));
        savedTasks.markAsDone(0);

        TaskList restoredTasks = new TaskList();

        assertTask(restoredTasks.size() == 3, "all saved tasks should be restored");
        assertTask(restoredTasks.get(0).toString().equals("[T][X] read | book ) [special]"),
                "to-do status should be restored");
        assertTask(restoredTasks.get(1).toString().equals("[D][ ] return book (by: Dec 02 2019, 6:00 PM)"),
                "deadline details should be restored");
        assertTask(restoredTasks.get(2).toString().equals("[E][ ] project meeting (from: Mon (2pm) to: 4pm | room 1)"),
                "event details should be restored");
    }

    /** Verifies that a missing data folder/file is safe on first startup. */
    private static void verifyFirstRunWithoutStorage() {
        try {
            Files.deleteIfExists(Path.of("data", "Gooble.txt"));
            Files.deleteIfExists(Path.of("data"));
        } catch (Exception e) {
            throw new AssertionError("Could not prepare first-run fixture", e);
        }

        TaskList firstRun = new TaskList();
        assertTask(firstRun.size() == 0, "a missing storage file should start empty");
        firstRun.add(new Task("first task"));
        assertTask(Files.isRegularFile(Path.of("data", "Gooble.txt")),
                "saving should create the missing data folder and file");
    }

    /** Ensures malformed records are ignored and legacy records remain readable. */
    private static void writeMalformedAndLegacyRecords() {
        try {
            Files.createDirectories(Path.of("data"));
            Files.write(Path.of("data", "Gooble.txt"), List.of(
                    "not a task",
                    "T|2|cmVhZA==",
                    "[Q] malformed",
                    "[X] legacy task"), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AssertionError("Could not prepare persistence edge cases", e);
        }

        TaskList restored = new TaskList();
        assertTask(restored.size() == 1, "malformed records should be ignored");
        assertTask(restored.get(0).toString().equals("[X] legacy task"),
                "legacy records should still be readable");
    }

    /** Removes the fixture before testing newly saved records. */
    private static void clearStorage() {
        try {
            Files.write(Path.of("data", "Gooble.txt"), List.of(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AssertionError("Could not clear persistence fixture", e);
        }
    }

    /**
     * Throws an error when a persistence expectation is not met.
     *
     * @param condition whether the expectation passed
     * @param message explanation of the failed expectation
     */
    private static void assertTask(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
