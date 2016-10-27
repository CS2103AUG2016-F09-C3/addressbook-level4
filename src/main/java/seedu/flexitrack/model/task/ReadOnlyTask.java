package seedu.flexitrack.model.task;

import seedu.flexitrack.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in FlexiTrack. Implementations
 * should guarantee: details are present and not null, field values are
 * validated.
 */
public interface ReadOnlyTask extends Comparable<ReadOnlyTask>{

    Name getName();

    DateTimeInfo getDueDate();
    DateTimeInfo getStartTime();
    DateTimeInfo getEndTime();
    DateTimeInfo getStartingTimeOrDueDate();
    DateTimeInfo getEndingTimeOrDueDate();

    boolean getIsTask();
    boolean getIsEvent();
    boolean getIsDone();
    boolean getIsNotFloatingTask();
    
    /**
     * The returned TagList is a deep copy of the internal TagList, changes on
     * the returned list will not affect the person's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override
     * .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                        && other.getName().equals(this.getName()) // state
                                                                  // checks here
                                                                  // onwards
                        && other.getDueDate().equals(this.getDueDate())
                        && other.getStartTime().equals(this.getStartTime())
                        && other.getEndTime().equals(this.getEndTime()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        String text = (getIsTask() || getIsEvent())
                ? getIsTask() ? " by/" + getDueDate() : " from/" + getStartTime() + " to/" + getEndTime() : "";
        builder.append(getIsDone() ? "(Done)" : "" + getName()).append(text).append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns a string representation of this Person's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }
    
//@@author A0127855W
    /**
     * Comparator for ReadOnlyTask and its children classes
     * Sorts by whether the task is a floating task, then by whether the task is done, then by start time/due date, then by name
     */
    default int compareTo(ReadOnlyTask task) {
        if(!this.getIsEvent() && !this.getIsTask()){ //floating tasks come first
            if (!task.getIsEvent() && !task.getIsTask()){
                return compareByMarkThenByType(task, "Float");
            }else{
                return -1;
            }
        }else{
            return compareByMarkThenByType(task, "TaskEvent");
        }
    }

    /**
     * Compares whether the task is done, then proceeds to compare by start date/due date (if both are nor floating tasks) then by name
     * @param task
     * @param type
     * @return compare result
     */
    default int compareByMarkThenByType(ReadOnlyTask task, String type) {
      
        if(this.getIsDone() && !task.getIsDone()){
            return 1;
        }else if(!this.getIsDone() && task.getIsDone()){
            return -1;
        }else{
            if(type.equals("Float")){
                return this.getName().toString().compareTo(task.getName().toString());    
            }else if(type.equals("TaskEvent")){
                DateTimeInfo time1 = (this.getIsEvent()) ? this.getStartTime() : this.getDueDate();
                DateTimeInfo time2 = (task.getIsEvent()) ? task.getStartTime() : task.getDueDate();
                int c = time1.compareTo(time2);
                if (c == 0){
                    return this.getName().toString().compareTo(task.getName().toString());
                }else{
                    return c;
                }
            }else{
                return 0;
            }
        }
    }



}
