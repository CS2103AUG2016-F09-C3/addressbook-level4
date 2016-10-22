package seedu.flexitrack.logic.commands;

import seedu.flexitrack.commons.core.Messages;
import seedu.flexitrack.commons.core.UnmodifiableObservableList;
import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.model.task.ReadOnlyTask;

/**
 * Selects a task identified using it's last displayed index from the
 * FlexiTrack.
 */
public class UnmarkCommand extends Command {
    public final int targetIndex;

    public static final String COMMAND_WORD = "unmark";
    public static final String COMMAND_SHORTCUT = "u";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ", Shortcut [" + COMMAND_SHORTCUT + "]"
            + ": Unmarks the task identified by the index number used in the task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n" + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_TASK_SUCCESS = "Unmark Task: %1$s";

    public UnmarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        try {
            model.unmarkTask(lastShownList.get(targetIndex - 1));
            return new CommandResult(String.format(MESSAGE_UNMARK_TASK_SUCCESS, targetIndex));
        } catch (IllegalValueException e) {
            return new CommandResult(e.getMessage());
        }

    }
}
