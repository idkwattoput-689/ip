# Gooble UI test plan

Run these cases with the project-local `test-ui` skill. Each case starts a new Gooble session; include `bye` so the session exits normally.

## Test case: Greet the user and exit

Aim: Verify that Gooble greets the user at startup and exits politely on the bye command.

### Inputs

```text
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add a general task

Aim: Verify that entering ordinary text adds a task to the list.

### Inputs

```text
read book
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: List tasks

Aim: Verify that list shows an added task with its completion status.

### Inputs

```text
read book
list
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: read book
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Mark a task

Aim: Verify that mark changes a task's completion status to done.

### Inputs

```text
read book
mark 1
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [X] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Unmark a task

Aim: Verify that unmark changes a completed task back to incomplete.

### Inputs

```text
read book
mark 1
unmark 1
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
added: read book
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
  [X] read book
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [ ] read book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add a to-do

Aim: Verify that a to-do task is created and displayed with the T type marker.

### Inputs

```text
todo borrow book
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add a deadline

Aim: Verify that a deadline task keeps its by-date in the task display.

### Inputs

```text
deadline return book /by Sunday
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Add an event

Aim: Verify that an event includes its start and end dates in the display.

### Inputs

```text
event project meeting /from Mon 2pm /to 4pm
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test case: Reject incomplete task commands

Aim: Verify that todo, deadline, and event commands without descriptions show errors and do not add tasks.

### Inputs

```text
todo
deadline
event
list
bye
```

### Expected output

```text
____________________________________________________________
  ____            _     _
 / ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
 \____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________
____________________________________________________________
You need add in some description for that lmao
____________________________________________________________
____________________________________________________________
You need add in some description for that lmao
____________________________________________________________
____________________________________________________________
You need add in some description for that lmao
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
