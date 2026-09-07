# Gooble User Guide

## Tags

Add one tag to an existing task with:

```text
tag TASK_NUMBER #tag
```

For example:

```text
tag 1 #Fun
```

The tag is normalized to lowercase and displayed as:

```text
[ ] read book [#fun]
```

Tags may contain letters, numbers, hyphens, and underscores. A task may have
up to three tags. If a fourth tag is added, the oldest tag is removed.

Remove all tags from a task with:

```text
untag TASK_NUMBER
```

`find` searches both descriptions and tags:

```text
find #fun
```

Invalid examples include `tag 1 fun`, `tag 1 #fun!`, and `tag 1 #fun #school`.
These commands are rejected with a validation message.
