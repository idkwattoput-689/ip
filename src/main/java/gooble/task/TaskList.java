package gooble.task;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import gooble.GoobleException;
import gooble.storage.Storage;

/**
 * Manages the tasks in Gooble's task list.
 */
public class TaskList {

    /** Location where the current task list is saved between application runs. */
    private static final Path STORAGE_PATH = Path.of("data", "Gooble.txt");
    private static final String GENERIC_TYPE = "G";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final char INCOMPLETE_STATUS = '0';
    private static final char COMPLETE_STATUS = '1';

    private final ArrayList<Task> tasks;
    private final Storage storage;

    /** Creates a task list backed by the default Gooble storage file. */
    public TaskList() {
        this(new Storage(STORAGE_PATH));
    }

    /**
     * Creates a task list backed by the supplied storage component.
     *
     * @param storage component used to load and save task records
     */
    public TaskList(Storage storage) {
        if (storage == null) {
            throw new IllegalArgumentException("Storage cannot be null.");
        }
        this.tasks = new ArrayList<>();
        this.storage = storage;
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

    /** Returns tasks whose descriptions contain the keyword, ignoring case. */
    public List<Task> findByDescription(String keyword) {
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
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
        String type = GENERIC_TYPE;
        List<String> fields = new ArrayList<>();
        fields.add(task.getDescription());
        if (task instanceof Todo) {
            type = TODO_TYPE;
        } else if (task instanceof Deadline deadline) {
            type = DEADLINE_TYPE;
            fields.add(deadline.getStoredDeadline());
        } else if (task instanceof Event event) {
            type = EVENT_TYPE;
            fields.add(event.getStartDate());
            fields.add(event.getEndDate());
        }

        StringBuilder record = new StringBuilder(type)
                .append('|').append("X".equals(task.getStatusIcon())
                        ? COMPLETE_STATUS : INCOMPLETE_STATUS);
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
        boolean hasEnoughFields = fields.length >= 3;
        boolean hasValidType = fields[0].length() == 1;
        boolean hasValidStatus = fields[1].length() == 1
                && (fields[1].charAt(0) == INCOMPLETE_STATUS
                || fields[1].charAt(0) == COMPLETE_STATUS);
        if (!hasEnoughFields || !hasValidType || !hasValidStatus) {
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

        try {
            Task task = createPersistedTask(fields[0], decodedFields);
            return task == null ? null
                    : restoreStatus(task, fields[1].charAt(0) == COMPLETE_STATUS ? 'X' : ' ');
        } catch (GoobleException | IllegalArgumentException e) {
            return null;
        }
    }

    /** Creates a task from a validated persisted type and its decoded fields. */
    private Task createPersistedTask(String type, List<String> fields) throws GoobleException {
        switch (type) {
            case GENERIC_TYPE:
                return fields.size() == 1 ? new Task(fields.get(0)) : null;
            case TODO_TYPE:
                return fields.size() == 1 ? new Todo(fields.get(0)) : null;
            case DEADLINE_TYPE:
                return fields.size() == 2
                        ? new Deadline(fields.get(0), DeadlineDateParser.parse(fields.get(1))) : null;
            case EVENT_TYPE:
                return fields.size() == 3
                        ? new Event(fields.get(0), fields.get(1), fields.get(2)) : null;
            default:
                return null;
        }
    }

    /** Parses the display format written by the first persistence version. */
    private Task parseLegacyTask(String savedTask) {
        if (savedTask.startsWith("[T][")) {
            return parseLegacyTodo(savedTask);
        }
        if (savedTask.startsWith("[D][")) {
            return parseLegacyDeadline(savedTask);
        }
        if (savedTask.startsWith("[E][")) {
            return parseLegacyEvent(savedTask);
        }
        return parseLegacyGeneric(savedTask);
    }

    /** Parses a legacy todo record. */
    private Task parseLegacyTodo(String savedTask) {
        if (savedTask.length() < 7) {
            return null;
        }
        char status = savedTask.charAt(4);
        String description = savedTask.substring(7);
        return validStatus(status) && !description.isBlank()
                ? restoreStatus(new Todo(description), status) : null;
    }

    /** Parses a legacy deadline record. */
    private Task parseLegacyDeadline(String savedTask) {
        if (savedTask.length() < 7) {
            return null;
        }
        char status = savedTask.charAt(4);
        int deadlineMarker = savedTask.lastIndexOf(" (by: ");
        if (!validStatus(status) || deadlineMarker <= 7 || !savedTask.endsWith(")")) {
            return null;
        }
        String description = savedTask.substring(7, deadlineMarker);
        String deadline = savedTask.substring(deadlineMarker + 6, savedTask.length() - 1);
        if (description.isBlank() || deadline.isBlank()) {
            return null;
        }
        try {
            return restoreStatus(new Deadline(description, DeadlineDateParser.parse(deadline)), status);
        } catch (GoobleException e) {
            return null;
        }
    }

    /** Parses a legacy event record. */
    private Task parseLegacyEvent(String savedTask) {
        if (savedTask.length() < 7) {
            return null;
        }
        char status = savedTask.charAt(4);
        int startMarker = savedTask.lastIndexOf(" (from: ");
        int endMarker = savedTask.lastIndexOf(" to: ");
        if (!validStatus(status) || startMarker <= 7 || endMarker <= startMarker
                || !savedTask.endsWith(")")) {
            return null;
        }
        String description = savedTask.substring(7, startMarker);
        String startDate = savedTask.substring(startMarker + 8, endMarker);
        String endDate = savedTask.substring(endMarker + 5, savedTask.length() - 1);
        return description.isBlank() || startDate.isBlank() || endDate.isBlank()
                ? null : restoreStatus(new Event(description, startDate, endDate), status);
    }

    /** Parses a legacy generic task record. */
    private Task parseLegacyGeneric(String savedTask) {
        if (savedTask.length() < 5 || !savedTask.startsWith("[")
                || !validStatus(savedTask.charAt(1)) || savedTask.charAt(2) != ']'
                || savedTask.charAt(3) != ' ') {
            return null;
        }
        String description = savedTask.substring(4);
        return description.isBlank() ? null
                : restoreStatus(new Task(description), savedTask.charAt(1));
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
