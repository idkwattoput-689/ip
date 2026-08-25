import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Base64;

/**
 * Manage the list of task in Gooble's task list
 */
public class TaskList {

    /** Location where the current task list is saved between application runs. */
    private static final Path STORAGE_PATH = Path.of("data", "Gooble.txt");

    private final ArrayList<Task> tasks;
    private final Storage storage;

    /*
    Create an empty TaskList
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.storage = new Storage(STORAGE_PATH);
        load();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("A task cannot be null.");
        }
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
            savedTasks.add(serialize(task));
        }

        storage.save(savedTasks);
    }

    /**
     * Encodes task fields so separators in user input cannot corrupt the file.
     *
     * @param task task to encode
     * @return one storage record
     */
    private String serialize(Task task) {
        String type = "G";
        List<String> fields = new ArrayList<>();
        fields.add(task.getDescription());
        if (task instanceof Todo) {
            type = "T";
        } else if (task instanceof Deadline deadline) {
            type = "D";
            fields.add(deadline.getStoredDeadline());
        } else if (task instanceof Event event) {
            type = "E";
            fields.add(event.getStartDate());
            fields.add(event.getEndDate());
        }

        StringBuilder record = new StringBuilder(type)
                .append('|').append("X".equals(task.getStatusIcon()) ? '1' : '0');
        for (String field : fields) {
            record.append('|').append(encode(field));
        }
        return record.toString();
    }

    /** Encodes one user-provided field for storage. */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /** Decodes one persisted field, returning null when it is invalid. */
    private String decode(String value) {
        try {
            return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Restores tasks from the storage file when it is available.
     */
    private void load() {
        for (String savedTask : storage.load()) {
            Task task = parseSavedTask(savedTask);
            if (task != null) {
                tasks.add(task);
            }
        }
    }

    /**
     * Creates a task from the display format used by the storage file.
     *
     * @param savedTask one line from the storage file
     * @return the restored task, or {@code null} when the line is not recognised
     */
    private Task parseSavedTask(String savedTask) {
        Task persistedTask = parsePersistedTask(savedTask);
        if (persistedTask != null) {
            return persistedTask;
        }

        return parseLegacyTask(savedTask);
    }

    /** Parses the current type|status|encoded-fields format. */
    private Task parsePersistedTask(String savedTask) {
        String[] fields = savedTask.split("\\|", -1);
        if (fields.length < 3 || fields[0].length() != 1
                || (fields[1].length() != 1 || (fields[1].charAt(0) != '0' && fields[1].charAt(0) != '1'))) {
            return null;
        }

        List<String> decodedFields = new ArrayList<>();
        for (int i = 2; i < fields.length; i++) {
            String decoded = decode(fields[i]);
            if (decoded == null) {
                return null;
            }
            decodedFields.add(decoded);
        }
        if (decodedFields.isEmpty() || decodedFields.stream().anyMatch(String::isBlank)) {
            return null;
        }

        Task task;
        try {
            switch (fields[0]) {
            case "G":
                task = decodedFields.size() == 1 ? new Task(decodedFields.get(0)) : null;
                break;
            case "T":
                task = decodedFields.size() == 1 ? new Todo(decodedFields.get(0)) : null;
                break;
            case "D":
                task = decodedFields.size() == 2
                        ? new Deadline(decodedFields.get(0), DeadlineDateParser.parse(decodedFields.get(1))) : null;
                break;
            case "E":
                task = decodedFields.size() == 3
                        ? new Event(decodedFields.get(0), decodedFields.get(1), decodedFields.get(2)) : null;
                break;
            default:
                task = null;
            }
        } catch (GoobleException | IllegalArgumentException e) {
            return null;
        }
        return task == null ? null : restoreStatus(task, fields[1].charAt(0) == '1' ? 'X' : ' ');
    }

    /** Parses the display format written by the first persistence version. */
    private Task parseLegacyTask(String savedTask) {
        if (savedTask.startsWith("[T][") && savedTask.length() >= 7) {
            String description = savedTask.substring(7);
            return validStatus(savedTask.charAt(4)) && !description.isBlank()
                    ? restoreStatus(new Todo(description), savedTask.charAt(4)) : null;
        }
        if (savedTask.startsWith("[D][") && savedTask.length() >= 7) {
            int deadlineMarker = savedTask.lastIndexOf(" (by: ");
            if (validStatus(savedTask.charAt(4)) && deadlineMarker > 7 && savedTask.endsWith(")")) {
                String description = savedTask.substring(7, deadlineMarker);
                String deadline = savedTask.substring(deadlineMarker + 6, savedTask.length() - 1);
                try {
                    return !description.isBlank() && !deadline.isBlank()
                            ? restoreStatus(new Deadline(description, DeadlineDateParser.parse(deadline)), savedTask.charAt(4))
                            : null;
                } catch (GoobleException e) {
                    return null;
                }
            }
        }
        if (savedTask.startsWith("[E][") && savedTask.length() >= 7) {
            int startMarker = savedTask.lastIndexOf(" (from: ");
            int endMarker = savedTask.lastIndexOf(" to: ");
            if (validStatus(savedTask.charAt(4)) && startMarker > 7 && endMarker > startMarker
                    && savedTask.endsWith(")")) {
                String description = savedTask.substring(7, startMarker);
                String startDate = savedTask.substring(startMarker + 8, endMarker);
                String endDate = savedTask.substring(endMarker + 5, savedTask.length() - 1);
                return !description.isBlank() && !startDate.isBlank() && !endDate.isBlank()
                        ? restoreStatus(new Event(description, startDate, endDate), savedTask.charAt(4)) : null;
            }
        }
        if (savedTask.startsWith("[") && savedTask.length() >= 5
                && (savedTask.charAt(1) == ' ' || savedTask.charAt(1) == 'X')
                && savedTask.charAt(2) == ']' && savedTask.charAt(3) == ' ') {
            String description = savedTask.substring(4);
            return !description.isBlank() ? restoreStatus(new Task(description), savedTask.charAt(1)) : null;
        }
        return null;
    }

    /** Returns whether a legacy record contains a supported status marker. */
    private boolean validStatus(char status) {
        return status == ' ' || status == 'X';
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
