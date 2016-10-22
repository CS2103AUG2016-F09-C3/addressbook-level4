package seedu.flexitrack.model;

import seedu.flexitrack.commons.core.UnmodifiableObservableList;
import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.model.task.ReadOnlyTask;
import seedu.flexitrack.model.task.Task;
import seedu.flexitrack.model.task.UniqueTaskList;
import seedu.flexitrack.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.flexitrack.model.task.UniqueTaskList.TaskNotFoundException;

import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /**
     * Clears existing backing model and replaces with the provided new data.
     */
    void resetData(ReadOnlyFlexiTrack newData);

    /** Returns the FLexiTrack */
    ReadOnlyFlexiTrack getFlexiTrack();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws DuplicateTaskException;

    /**
     * Returns the filtered task list as an
     * {@code UnmodifiableObservableList<ReadOnlyTask>}
     */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to show future tasks */
    void updateFilteredListToShowFutureTasks();
    
    /** Updates the filter of the filtered task list to show past tasks */
    void updateFilteredListToShowPastTasks();
    
    /** Updates the filter of the filtered task list to show mark tasks */
    void updateFilteredListToShowMarkTasks();
    
    /** Updates the filter of the filtered task list to show unmark tasks */
    void updateFilteredListToShowUnmarkTasks();
    
    /**
     * Updates the filter of the filtered task list to filter by the given
     * keywords
     */
    void updateFilteredTaskList(Set<String> keywords);

    /**
     * Marks the given task as done
     * 
     * @throws TaskNotFoundException
     */
    void markTask(int taskToMark);

    /**
     * Unmarks the given task as done
     * 
     * @throws TaskNotFoundException
     */
    void unmarkTask(int taskToMark);

    /**
     * Edits the given task
     * 
     * @throws TaskNotFoundException
     */
    String editTask(int taskToEdit, String[] args)
            throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.IllegalEditException, IllegalValueException;

}
