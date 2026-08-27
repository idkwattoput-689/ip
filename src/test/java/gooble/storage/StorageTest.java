package gooble.storage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests task-record persistence behavior. */
class StorageTest {
    @Test
    void load_missingFile_returnsEmptyList(@TempDir Path tempDir) {
        Storage storage = new Storage(tempDir.resolve("tasks.txt"));

        assertEquals(List.of(), storage.load());
    }

    @Test
    void saveThenLoad_recordsArePreserved(@TempDir Path tempDir) {
        Path storagePath = tempDir.resolve("nested").resolve("tasks.txt");
        Storage storage = new Storage(storagePath);
        List<String> records = List.of("T|0|read book", "D|1|return book|2026-02-01 0900");

        storage.save(records);

        assertEquals(records, storage.load());
        assertFalse(Files.exists(storagePath.resolveSibling("tasks.txt.tmp")));
    }
}
