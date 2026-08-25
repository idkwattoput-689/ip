# Gooble project template

This is a project template for a greenfield Java project. The application is named Gooble. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Gooble.java` file, right-click it, and choose `Run Gooble.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:

```
____________________________________________________________
  ____            _     _      
/ ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
\____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
```
**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Features

### Greeting the user

When Gooble starts, it welcomes the user and lets them know that it is ready to receive commands or task descriptions.

### Adding tasks to a list

Users can enter a task description, and Gooble stores it in the task list for later reference.

Example:

```
Textbook
____________________________________________________________
added: Textbook
____________________________________________________________
```

### Viewing the task list

Users can enter `list` to view all tasks currently stored in Gooble.

Example:

```
list
____________________________________________________________
Here are the tasks in your list:
1.[ ] Textbook
____________________________________________________________
```

### Automatic task persistence

Gooble automatically saves the task list to `data/Gooble.txt` whenever it changes.
This includes adding, deleting, marking, and unmarking tasks. When Gooble starts,
it loads the saved tasks from the same file, so tasks remain available between
sessions.

If the storage file does not exist, Gooble starts with an empty task list and
creates the storage folder when the first task is saved.

### Exiting the chatbot

Users can enter 'bye' and Gooble will say bye to the users and end.

Example:

```
bye
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

### Filtering events by date and time

Users can show only events fully contained within a date-time range using
`list from [start] to [end]`. Both boundaries must use `yyyy-MM-dd HHmm`,
and the end must not be earlier than the start.

Example:

```
list from 2026-03-09 0000 to 2026-03-11 2359
____________________________________________________________
Here are the events in your list for that period:
1.[E][ ] in range (from: 2026-03-10 0900 to: 2026-03-10 1000)
____________________________________________________________
```

### Marking objects in the list

Users can enter 'mark [i]' and Gooble will mark the ith task in the list.

Example:

```
mark 2
____________________________________________________________
Nice! I've marked this task as done:
  [X] tea
____________________________________________________________
list
____________________________________________________________
Here are the tasks in your list:
1.[ ] books
2.[X] tea
____________________________________________________________
```

### Unmark object in the list

Users can enter 'unmark [i]' and Gooble will unmark the ith task in the list.

Example:

```
unmark 2
____________________________________________________________
OK, I've marked this task as not done yet:
  [ ] tea
____________________________________________________________
list
____________________________________________________________
Here are the tasks in your list:
1.[ ] books
2.[ ] tea
____________________________________________________________
```
### Adding to-do tasks

Users can add a to-do task without a date or deadline using `todo [description]`.

Example:

```
todo borrow book
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
```

### Adding deadline tasks

Users can add a task with a deadline using `deadline [description] /by [deadline]`. Deadline dates
must use `yyyy-MM-dd` (optionally followed by a 24-hour time such as `1800`) or `d/M/yyyy HHmm`.

Example:

```
deadline return book /by 2/12/2019 1800
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Dec 02 2019, 6:00 PM)
Now you have 1 tasks in the list.
____________________________________________________________
```

### Special deadline dates

When a deadline falls on Valentine's Day (February 14), Gooble adds:

```
Love is in the air~
```

When a deadline falls on a recognized Chinese New Year date, Gooble adds:

```
恭喜发财！！
```

These comments are shown when the deadline is added and are not included in
the task text when using `list`.

### Adding events

Users can add an event with a start and end date or time using
`event [description] /from [start] /to [end]`.

Example:

```
event project meeting /from Mon 2pm /to 4pm
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
____________________________________________________________
```

### Deleting tasks

Users can delete a task from the list using 'delete i', and Gooble will remove the ith task in the list

Example:

```
delete 2
____________________________________________________________
Noted. I've removed this task:
  [ ] chuyue
Now you have 1 tasks in the list.
____________________________________________________________
```

## Error Handling

Gooble currently handles the following error:

### Empty description

When there is no description after keying in a command, Gooble will deem it as invalid and reject it.

Example:

```
add
____________________________________________________________
You need to add in some description for that lmao
____________________________________________________________
```

### Invalid command

When the user keys in any invalid command(Any words that are not add, todo, deadline or event), Gooble will deem it as invalid and reject it

Example:

```
dfs
____________________________________________________________
Invalid command smhmh
____________________________________________________________

```

### Invalid date and time input

When a deadline uses an unsupported date format, or a filtered event list uses
an invalid date-time range, Gooble rejects the command and provides guidance.
For event filtering, the `to` date and time must not be earlier than the `from`
date and time.

Example:

```
event burger 
____________________________________________________________
Please specify an event time using /from and /to.
____________________________________________________________
deadline burger
____________________________________________________________
Please specify a deadline using /by.
____________________________________________________________
list from 2026-03-12 1000 to 2026-03-10 0900
____________________________________________________________
Please ensure the 'to' date and time is not before the 'from' date and time.
____________________________________________________________
```

## AI declaration

Level: AI-5.

AI codes, you review: Get AI to do the task. Review it yourself fully, including the code, tests, behavior etc.
