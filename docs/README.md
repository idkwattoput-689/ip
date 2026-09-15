# Gooble User Guide

Gooble is a friendly task assistant for managing to-dos, deadlines, events, and
tags. It is available as both a JavaFX graphical interface and a command-line
interface (CLI).

![Gooble graphical user interface](Ui.png)

## Quick start

### Prerequisites

- JDK 25
- IntelliJ IDEA (recommended for development)

### Run the graphical interface

From the project directory, run:

```powershell
.\gradlew.bat run
```

Type a command in the input box and click **Send** or press **Enter**. Enter
`bye` to display the goodbye message and close the GUI window.

### Run the command-line interface

Run the `gooble.Gooble` main class from IntelliJ, or use:

```powershell
.\gradlew.bat classes
java -cp build\classes\java\main gooble.Gooble
```

The CLI displays a prompt after each command. Enter `bye` to end the session.

## Commands at a glance

| Command | Purpose | Example |
| --- | --- | --- |
| `add <description>` | Add a simple task | `add read chapter 3` |
| `todo <description>` | Add a to-do task | `todo prepare presentation` |
| `deadline <description> /by <date>` | Add a task with a deadline | `deadline submit report /by 2026-09-20 1800` |
| `event <description> /from <start> /to <end>` | Add an event | `event team meeting /from 2026-09-18 1400 /to 2026-09-18 1530` |
| `list` | Show all tasks | `list` |
| `list from <start> to <end>` | Show events in a period | `list from 2026-09-18 0000 to 2026-09-19 2359` |
| `find <keyword>` | Find tasks by description or tag | `find report` |
| `mark <number>` | Mark a task as complete | `mark 2` |
| `unmark <number>` | Mark a task as incomplete | `unmark 2` |
| `delete <number>` | Delete a task | `delete 2` |
| `tag <number> #<tag>` | Add a tag | `tag 1 #school` |
| `untag <number>` | Remove all tags | `untag 1` |
| `help [command]` | Show general or command-specific help | `help deadline` |
| `bye` | Exit Gooble | `bye` |

Task numbers refer to the positions shown by `list` and `find`.

## Features

### Adding tasks

Use `add` for a simple task or `todo` for an explicitly marked to-do task:

```text
todo Prepare presentation slides
Got it. I've added this task:
  [T][ ] Prepare presentation slides
Now you have 1 tasks in the list.
```

### Deadlines

Create a deadline with `deadline <description> /by <date>`. Gooble accepts
`yyyy-MM-dd`, optionally followed by a 24-hour time such as `1800`, or
`d/M/yyyy HHmm`.

```text
deadline Submit project report /by 2026-09-20 1800
Got it. I've added this task:
  [D][ ] Submit project report (by: Sep 20 2026, 6:00 PM)
```

Gooble also displays seasonal greetings for supported dates such as Valentine's
Day and recognized Chinese New Year dates.

### Events

Create an event with a start and end time:

```text
event Team planning meeting /from 2026-09-18 1400 /to 2026-09-18 1530
Got it. I've added this task:
  [E][ ] Team planning meeting (from: 2026-09-18 1400 to: 2026-09-18 1530)
```

Use `list from <start> to <end>` to display only events fully contained within
the requested period. The end of the range must not be earlier than its start.

### Viewing and finding tasks

Use `list` to display every task:

```text
list
Here are the tasks in your list:
1.[T] [ ] Prepare presentation slides
2.[D] [ ] Submit project report (by: Sep 20 2026, 6:00 PM)
```

Use `find <keyword>` to search task descriptions. Searches are case-insensitive
and preserve the order of matching tasks. Searches also include tags, so
`find #school` finds tasks tagged `#school`.

### Completing and deleting tasks

Use `mark <number>` and `unmark <number>` to change a task's completion status:

```text
mark 1
Nice! I've marked this task as done:
  [X] Prepare presentation slides

unmark 1
OK, I've marked this task as not done yet:
  [ ] Prepare presentation slides
```

Use `delete <number>` to remove a task:

```text
delete 1
Noted. I've removed this task:
  [T][ ] Prepare presentation slides
```

### Tags

Add a tag with `tag <number> #<tag>`:

```text
tag 1 #School
I've tagged this task:
  [T][ ] Prepare presentation slides [#school]
```

Tags are normalized to lowercase and may contain letters, numbers, hyphens,
and underscores. Each task can have at most three tags; adding a fourth tag
removes the oldest tag. Use `untag <number>` to remove all tags from a task.

### Help

Use `help` to display the complete command guide. Use `help <command>` for a
focused explanation and example, such as `help deadline`.

## Saving your tasks

Gooble automatically saves changes to `data/Gooble.txt`. Tasks are restored
from this file when Gooble starts, so they remain available between sessions.
The storage folder and file are created automatically when the first task is
saved.

## Error handling

Gooble rejects invalid input with a helpful message instead of adding an
incorrect task or crashing. Common examples include:

- Missing descriptions, such as `todo` or `add` without text.
- Unknown commands, such as `dance`.
- Missing or invalid task numbers for `mark`, `unmark`, `delete`, `tag`, and
  `untag`.
- Invalid deadline or event date-time formats.
- Event ranges where the end is earlier than the start.
- Invalid tags such as `#school!`, or tag commands containing multiple tags.
- Duplicate tasks with the same details.

For invalid commands, Gooble responds:

```text
Invalid command. Gooble is confused, but not offended. Type help to see what I understand.
```

## Building and testing

To build the distributable fat JAR:

```powershell
.\gradlew.bat clean shadowJar
```

The output is `build\libs\Gooble.jar`. Run it from a folder containing the
desired `data` directory with:

```powershell
java -jar Gooble.jar
```

Run the automated tests and Checkstyle checks with:

```powershell
.\gradlew.bat check
```

The detailed test report is generated at
`build\reports\tests\test\index.html`. The full command-line test scenarios
are documented in `test\ui-test-plan.md`.
