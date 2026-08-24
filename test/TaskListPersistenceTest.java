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
    public static void main(String[] args) {
        TaskList savedTasks = new TaskList();
        savedTasks.add(new Todo("read book"));
        savedTasks.add(new Deadline("return book", "Sunday"));
        savedTasks.add(new Event("project meeting", "Mon 2pm", "4pm"));
        savedTasks.markAsDone(0);

        TaskList restoredTasks = new TaskList();

        assertTask(restoredTasks.size() == 3, "all saved tasks should be restored");
        assertTask(restoredTasks.get(0).toString().equals("[T][X] read book"),
                "to-do status should be restored");
        assertTask(restoredTasks.get(1).toString().equals("[D][ ] return book (by: Sunday)"),
                "deadline details should be restored");
        assertTask(restoredTasks.get(2).toString().equals("[E][ ] project meeting (from: Mon 2pm to: 4pm)"),
                "event details should be restored");
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
