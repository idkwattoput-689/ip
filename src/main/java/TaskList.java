import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Manage the list of task in Gooble's task list
 */
public class TaskList {

    /** Location where the current task list is saved between application runs. */
    private static final Path STORAGE_PATH = Path.of("data", "Gooble.txt");

    private final ArrayList<Task> tasks;

    /*
    Create an empty TaskList
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        load();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
        save();
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index zero-based index of the task to remove
     * @return the removed task
     */
    public Task remove(int index) {
        Task removedTask = tasks.remove(index);
        save();
        return removedTask;
    }


    /**
     * Returns the task at the given index.
     *
     * @param index zero-based index of the task
     * @return the task at that index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the given index refers to a valid task position.
     *
     * @param index zero-based index to check
     * @return {@code true} if the index is within range
     */
    public boolean isValidIndex(int index) {
        return index >= 0 && index < tasks.size();
    }

    /**
     * Marks a task as completed and saves the updated list.
     *
     * @param index zero-based index of the task to mark
     */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
        save();
    }

    /**
     * Marks a task as incomplete and saves the updated list.
     *
     * @param index zero-based index of the task to unmark
     */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
        save();
    }

    /**
     * Rewrites the storage file with a simple representation of every task.
     */
    private void save() {
        List<String> savedTasks = new ArrayList<>();
        for (Task task : tasks) {
            savedTasks.add(task.toString());
        }

        try {
            Files.createDirectories(STORAGE_PATH.getParent());
            Files.write(STORAGE_PATH, savedTasks, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save tasks to disk.", e);
        }
    }

    /**
     * Restores tasks from the storage file when it is available.
     */
    private void load() {
        if (!Files.exists(STORAGE_PATH)) {
            return;
        }

        try {
            for (String savedTask : Files.readAllLines(STORAGE_PATH, StandardCharsets.UTF_8)) {
                Task task = parseSavedTask(savedTask);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load tasks from disk.", e);
        }
    }

    /**
     * Creates a task from the display format used by the storage file.
     *
     * @param savedTask one line from the storage file
     * @return the restored task, or {@code null} when the line is not recognised
     */
    private Task parseSavedTask(String savedTask) {
        if (savedTask.startsWith("[T][") && savedTask.length() >= 7) {
            return restoreStatus(new Todo(savedTask.substring(7)), savedTask.charAt(4));
        }
        if (savedTask.startsWith("[D][") && savedTask.length() >= 7) {
            int deadlineMarker = savedTask.lastIndexOf(" (by: ");
            if (deadlineMarker != -1 && savedTask.endsWith(")")) {
                String description = savedTask.substring(7, deadlineMarker);
                String deadline = savedTask.substring(deadlineMarker + 6, savedTask.length() - 1);
                return restoreStatus(new Deadline(description, deadline), savedTask.charAt(4));
            }
        }
        if (savedTask.startsWith("[E][") && savedTask.length() >= 7) {
            int startMarker = savedTask.lastIndexOf(" (from: ");
            int endMarker = savedTask.lastIndexOf(" to: ");
            if (startMarker != -1 && endMarker > startMarker && savedTask.endsWith(")")) {
                String description = savedTask.substring(7, startMarker);
                String startDate = savedTask.substring(startMarker + 8, endMarker);
                String endDate = savedTask.substring(endMarker + 5, savedTask.length() - 1);
                return restoreStatus(new Event(description, startDate, endDate), savedTask.charAt(4));
            }
        }
        if (savedTask.startsWith("[") && savedTask.length() >= 4) {
            return restoreStatus(new Task(savedTask.substring(4)), savedTask.charAt(1));
        }
        return null;
    }

    /**
     * Applies the saved completion status to a newly created task.
     *
     * @param task task created from saved details
     * @param status saved status character
     * @return the task with its saved completion status restored
     */
    private Task restoreStatus(Task task, char status) {
        if (status == 'X') {
            task.markAsDone();
        }
        return task;
    }

}
