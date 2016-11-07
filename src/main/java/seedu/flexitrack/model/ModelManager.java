package seedu.flexitrack.model;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.flexitrack.MainApp;
import seedu.flexitrack.commons.core.ComponentManager;
import seedu.flexitrack.commons.core.LogsCenter;
import seedu.flexitrack.commons.core.UnmodifiableObservableList;
import seedu.flexitrack.commons.events.model.FlexiTrackChangedEvent;
import seedu.flexitrack.commons.events.ui.StoragePathChangeEvent;
import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.commons.events.ui.JumpToListRequestEvent;
import seedu.flexitrack.commons.util.StringUtil;
import seedu.flexitrack.logic.commands.ListCommand;
import seedu.flexitrack.model.task.DateTimeInfo;
import seedu.flexitrack.model.task.ReadOnlyTask;
import seedu.flexitrack.model.task.Task;
import seedu.flexitrack.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.flexitrack.model.task.UniqueTaskList.IllegalEditException;
import seedu.flexitrack.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.flexitrack.ui.UiManager;

/**
 * Represents the in-memory model of the tasktracker data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final FlexiTrack flexiTracker;
    private final FilteredList<Task> filteredTasks;
    
    /**
     * Initializes a ModelManager with the given FlexiTracker FlexiTracker and
     * its variables should not be null
     */
    public ModelManager(FlexiTrack src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with tasktracker: " + src + " and user prefs " + userPrefs);

        flexiTracker = new FlexiTrack(src);
        filteredTasks = new FilteredList<>(flexiTracker.getTasks());
    }

    public ModelManager() {
        this(new FlexiTrack(), new UserPrefs());
    }

    public ModelManager(ReadOnlyFlexiTrack initialData, UserPrefs userPrefs) {
        flexiTracker = new FlexiTrack(initialData);
        filteredTasks = new FilteredList<>(flexiTracker.getTasks());
        indicateFlexiTrackerChanged();
    }

    @Override
    public void resetData(ReadOnlyFlexiTrack newData) {
        flexiTracker.resetData(newData);
        updateFilteredListToShowAll();
        indicateFlexiTrackerChanged();
    }

    @Override
    public ReadOnlyFlexiTrack getFlexiTrack() {
        return flexiTracker;
    }

    /** Raises an event to indicate the model has changed */
    public void indicateFlexiTrackerChanged() {
    	flexiTracker.sort();
        raise(new FlexiTrackChangedEvent(flexiTracker));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        flexiTracker.removeTask(target);
        indicateFlexiTrackerChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws DuplicateTaskException {
        flexiTracker.addTask(task);
        updateFilteredListToShowAll();
        indicateFlexiTrackerChanged();
        jumpToATask(task);
    }

    //@@ author A0127686R
    /**
     * Move the Panel list to the task that was just changed 
     * 
     * @param task  To be Shown in the Panel List 
     */
    private void jumpToATask(Task task) {
        assert task != null;
        int index = flexiTracker.findIndexOfTask(task);
        if (index == -1) {
            logger.warning("----------------[ModelManager][" + " TASK CANT BE FOUND " + "]");
        }
        JumpToListRequestEvent jump = new JumpToListRequestEvent(index);
        raise(jump);
    }

  //@@author A0138455Y
    @Override
    public Task markTask(ReadOnlyTask targetIndex) throws IllegalValueException {
        Task markedTask = flexiTracker.markTask(targetIndex);
        indicateFlexiTrackerChanged();
        jumpToATask(markedTask);
        return markedTask;
    }

    @Override
    public Task unmarkTask(ReadOnlyTask targetIndex) throws IllegalValueException {
        Task unMarkedTask = flexiTracker.unmarkTask(targetIndex);
        indicateFlexiTrackerChanged();
        jumpToATask(unMarkedTask);
        return unMarkedTask;
    }
    
    @Override
    /*
     * raise the storage path change event
     * 
     */
    public void changeStorage(String storagePath) {
        raise(new StoragePathChangeEvent(storagePath));
    }
    
    //@@author
    
  //@@author A0127855W
    @Override
    /**
     * Edits a Task in the tasks tracker.
     * 
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     * @throws TaskNotFoundException if specified task is not found.
     */
    public Task editTask(ReadOnlyTask taskToEdit, String[] args)
            throws IllegalEditException, IllegalValueException {
        Task editedTask = flexiTracker.editTask(taskToEdit, args);
        indicateFlexiTrackerChanged();
        jumpToATask(editedTask);
        return editedTask;
    }

    
  //@@author A0138455Y
    @Override
    public boolean checkBlock(Task toCheck) throws DuplicateTaskException {
        return flexiTracker.checkBlock(toCheck);
    }
    
    @Override
    public boolean checkOverlapEvent(Task toCheck) {
        return flexiTracker.checkOverlapEvent(toCheck);
    }   
    //@@author

    @Override
    public List<DateTimeInfo> findSpecifiedGapTiming(int keyword, int length, int numberOfSlot) {
        return flexiTracker.findNextAvailableSlots(keyword, length, numberOfSlot);
    }
    
    // =========== Filtered Tasks List Accessors
    // ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    //@@author A0127686R
    @Override
    public void updateFilteredListToFitUserInput(String args){
        updateFilteredTaskList(new PredicateExpression(new DateQualifier(args)));
    }
    
    //@@author 
    @Override
    public void updateFilteredTaskList(Set<String> keywords) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    // ========== Inner classes/interfaces used for filtering
    // ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);

        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);

        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            if (nameKeyWords.toString().contains("f/")) {
                return nameKeyWords.stream()
                        .filter(keyword -> StringUtil.equalsIgnoreCase(task.getName().getNameOnly(), keyword)).findAny()
                        .isPresent();
            }

            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().getNameOnly(), keyword)).findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }
    
    // @@author A0127686R
    /**
     * Helper class to process if a particular task should be shown on the list
     */
    private class DateQualifier implements Qualifier {
        private String keyWords;
        private String dateInfo;

        DateQualifier(String keyWord) {
            this.keyWords = keyWord;
            this.dateInfo = trimKeyWords(keyWord);
        }

        /**
         * Delete all the list command that is not date from the keywords
         * 
         * @param keyrordArgs   A string containing the keywords and other input
         * @return              The trimmed keyword in a string
         */
        private String trimKeyWords(String keyrordArgs) {
            return keyrordArgs.replace(ListCommand.LIST_FUTURE_COMMAND, "").replace(ListCommand.LIST_PAST_COMMAND, "")
                    .replace(ListCommand.LIST_UNMARK_COMMAND, "").replace(ListCommand.LIST_MARK_COMMAND, "")
                    .replace(ListCommand.LIST_LAST_WEEK_COMMAND, "").replace(ListCommand.LIST_LAST_MONTH_COMMAND, "")
                    .replace(ListCommand.LIST_NEXT_WEEK_COMMAND, "").replace(ListCommand.LIST_NEXT_MONTH_COMMAND, "")
                    .replace(ListCommand.LIST_BLOCK_COMMAND, "").trim();
        }

        @Override
        public boolean run(ReadOnlyTask task) {

            if (!isTaskGoingToBeShown(task)) {
                return false;
            }

            if (keyWords.contains(ListCommand.LIST_UNMARK_COMMAND)) {
                return !task.getIsDone();
            } else if (keyWords.contains(ListCommand.LIST_MARK_COMMAND)) {
                return task.getIsDone();
            } else if (keyWords.contains(ListCommand.LIST_BLOCK_COMMAND)) {
                return task.getIsBlock();
            }


            return isTaskGoingToBeShown(task);

        }

        /**
         * Check of the task will be shown to the user in the panel list
         * 
         * @param task  The current task of interest
         * @return      True if the task met all the requirement and will be shown
         */
        private boolean isTaskGoingToBeShown(ReadOnlyTask task) {
            if (keyWords.contains(ListCommand.LIST_FUTURE_COMMAND)) {
                return isTaskInTheFuture(task);
            } else if (keyWords.contains(ListCommand.LIST_PAST_COMMAND)) {
                return isTaskInThePast(task);
            } else if (keyWords.contains(ListCommand.LIST_LAST_COMMAND)
                    || keyWords.contains(ListCommand.LIST_NEXT_COMMAND)) {
                return isTaskWithinTheSpecifiedTiming(task);
            } else if (!dateInfo.equals("")) {
                return doesTaskCrossTheParticularStatedDate(task);
            } else {
                return true;
            }
        }

        /**
         * Process If the task happens on a particular date
         * 
         * @param task  The current task of interest
         * @return      True if task contain or cross the date
         */
        private boolean doesTaskCrossTheParticularStatedDate(ReadOnlyTask task) {
            DateTimeInfo dateTimeInfo=null;
            try {
                dateTimeInfo = new DateTimeInfo (dateInfo);
            } catch (IllegalValueException e) {
                logger.warning("----------------[ModelManager][" + " THIS ERROR SHOULD NOT OCCUR. METHOD USING A VALID INPUT " + "]");
            }
            return dateTimeInfo.isOnTheDate(task);
        }

        /**
         * Process if the task happens between now and the time stated
         * 
         * @param task
         *            The current task of interest
         * @return True if task is within the stated time
         */
        private boolean isTaskWithinTheSpecifiedTiming(ReadOnlyTask task) {
            return DateTimeInfo.withInTheDuration(keyWords, task, DateTimeInfo.getCurrentTime().toString());
        }

        /**
         * Process if a particular task has passed
         * 
         * @param task  The current task of interest
         * @return      True if it has passed
         */
        private boolean isTaskInThePast(ReadOnlyTask task) {
            return DateTimeInfo.getCurrentTime().isInThePast(task.getEndingTimeOrDueDate());
        }

        /**
         * Process if a particular task has not passed yet
         * 
         * @param task  The current task of interest
         * @return      True if it has not passed yet
         */
        private boolean isTaskInTheFuture(ReadOnlyTask task) {
            if (task.getIsNotFloatingTask()) {
                return DateTimeInfo.getCurrentTime().isInTheFuture(task.getEndingTimeOrDueDate());
            } else {
                return !task.getIsDone();
            }
        }
        
    }


}
