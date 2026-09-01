package gooble.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gooble.storage.Storage;

/** Tests task-list indexing and task state operations. */
class TaskListTest {
    @Test
    void isValidIndex_emptyAndPopulatedList_returnsExpectedResult(@TempDir Path tempDir) {
        TaskList tasks = new TaskList(new Storage(tempDir.resolve("tasks.txt")));
        assertFalse(tasks.isValidIndex(0));

        tasks.add(new Todo("read book"));

        assertTrue(tasks.isValidIndex(0));
        assertFalse(tasks.isValidIndex(-1));
        assertFalse(tasks.isValidIndex(1));
    }

    @Test
    void addGetAndRemove_taskListMaintainsTasks(@TempDir Path tempDir) {
        TaskList tasks = new TaskList(new Storage(tempDir.resolve("tasks.txt")));
        Task task = new Todo("read book");

        tasks.add(task);

        assertEquals(task, tasks.get(0));
        assertEquals(task, tasks.remove(0));
        assertEquals(0, tasks.size());
    }

    @Test
    void markAsDoneAndNotDone_updatesTaskState(@TempDir Path tempDir) {
        TaskList tasks = new TaskList(new Storage(tempDir.resolve("tasks.txt")));
        tasks.add(new Todo("read book"));

        tasks.markAsDone(0);
        assertEquals("X", tasks.get(0).getStatusIcon());

        tasks.markAsNotDone(0);
        assertEquals(" ", tasks.get(0).getStatusIcon());
    }

    @Test
    void findByDescription_matchesKeywordsIgnoringCase(@TempDir Path tempDir) {
        TaskList tasks = new TaskList(new Storage(tempDir.resolve("tasks.txt")));
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy tea"));
        tasks.add(new Todo("return BOOK"));

        assertEquals(2, tasks.findByDescription("book").size());
        assertEquals("read book", tasks.findByDescription("BOOK").get(0).getDescription());
    }
}
