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
____________________________________________________________
  ____            _     _      
/ ___| ___   ___ | |__ | | ___
| |  _ / _ \ / _ \| '_ \| |/ _ \
| |_| | (_) | (_) | |_) | |  __/
\____|\___/ \___/|_.__/|_|\___|
Hello! I'm Gooble.
What can I do for you?
____________________________________________________________

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
### Exiting the chatbot

Users can enter 'bye' and Gobble will say bye to the users and end.

Example:

```
bye
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## AI declaration

Level: AI-5.

AI codes, you review: Get AI to do the task. Review it yourself fully, including the code, tests, behavior etc.