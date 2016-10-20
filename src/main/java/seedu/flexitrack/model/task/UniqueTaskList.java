package seedu.flexitrack.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.flexitrack.commons.util.CollectionUtil;
import seedu.flexitrack.commons.exceptions.DuplicateDataException;
import seedu.flexitrack.commons.exceptions.IllegalValueException;

import java.util.*;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate persons");
        }
    }

    /**
     * Signals that an operation targeting a specified person in the list would fail because
     * there is no such matching person in the list.
     */
    public static class TaskNotFoundException extends Exception {}
    
    public static class IllegalEditException extends Exception {}

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty PersonList.
     */
    public UniqueTaskList() {}

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(toAdd);
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public ObservableList<Task> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    public void mark(int targetIndex, boolean isDone) {
        assert targetIndex >= 0; 
        Task markTask = internalList.get(targetIndex);
        markTask.markTask(isDone);
        internalList.set(targetIndex, markTask);

    }
    
    public String edit(int targetIndex, String[] args) throws IllegalEditException, TaskNotFoundException, IllegalValueException {
        assert targetIndex >= 0; 
        Task editTask;
        
        try{
        	editTask = internalList.get(targetIndex);
        }catch(IndexOutOfBoundsException ioobe){
        	throw new TaskNotFoundException();
        }
        
        checkForIllegalFloatingTaskEdit(args, editTask);
        editTaskParameters(editTask, args);
        internalList.set(targetIndex, editTask);
        
        if(editTask.getIsEvent()){
        	return DateTimeInfo.durationOfTheEvent(editTask.getStartTime().toString(), editTask.getEndTime().toString());
        }else{
        	return "";
        }

    }

	private void checkForIllegalFloatingTaskEdit(String[] args, Task editTask) throws IllegalEditException {
		if(!editTask.getIsTask() && !editTask.getIsEvent()){
        	if((args[1] != null) && (args[2] != null || args[3] != null)){
        		throw new IllegalEditException();
        	}
        	if((args[2] != null && args[3] == null) || (args[3] != null && args[2] == null)){
        		throw new IllegalEditException();
        	}
        }
	}

	private void editTaskParameters(Task editTask, String[] args) throws IllegalValueException, IllegalEditException {
		for(int i = 0; i<args.length; i++){
        	if(!(args[i]==null)){
        		switch(i){
        		case 0: editTask.setName(args[i]);break;
        		case 1: 
        			if(!editTask.getIsEvent()){
        				editTask.setDueDate(args[i]);
        				editTask.setIsTask(true);
        			}else{
        				throw new IllegalEditException();
        			}break;
        		case 2:
        			if(!editTask.getIsTask()){
        				editTask.setStartTime(args[i]);
        				editTask.setIsEvent(true);
        			}else{
        				throw new IllegalEditException();
        			}break;
        		case 3:
        			if(!editTask.getIsTask()){
        				editTask.setEndTime(args[i]);
        				editTask.setIsEvent(true);
        			}else{
        				throw new IllegalEditException();
        			}break;
        		}
        	}
        }
	}
    
}
