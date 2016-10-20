package seedu.flexitrack.model.task;

import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.commons.util.CollectionUtil;
import seedu.flexitrack.model.tag.UniqueTagList;

import java.util.Objects;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {

    private Name name;
    private DateTimeInfo dueDate;
    private DateTimeInfo startTime;
    private DateTimeInfo endTime;
    private boolean isEvent;
    private boolean isTask;
    private boolean isDone = false;

    private UniqueTagList tags;
    /**
     * Every field must be present and not null.
     */
    public Task(Name name, DateTimeInfo dueDate, DateTimeInfo startTime, DateTimeInfo endTime, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, tags);
        this.name = name;
        this.dueDate = dueDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
        this.isTask = dueDate.isDateNull()?false:true;
        this.isEvent = startTime.isDateNull()?false:true;
        this.endTime.isEndTimeInferred();
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDueDate(), source.getStartTime(), source.getEndTime(), source.getTags());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean getIsTask() {
        return isTask;
    }

    @Override
    public boolean getIsEvent() {
        return isEvent;
    }
    
    @Override
    public boolean getIsDone() {
        return isDone;
    }

    @Override
    public DateTimeInfo getDueDate() {
        return dueDate;
    }
    
    @Override
    public DateTimeInfo getStartTime() {
        return startTime;
    }
    
    @Override
    public DateTimeInfo getEndTime() {
        return endTime;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this person's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, dueDate, startTime, endTime, isTask, isEvent, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    private void setIsDone(boolean isDone) {
        this.isDone = isDone;
        if(isDone) {
            name.setName("(Done)"+name.toString());
        } else {
            name.setName(name.toString().replace("(Done)", ""));
        }
    }
    
    public void markTask(boolean isDone) {
       setIsDone(isDone);
    }
    
    public void setName(String name){
    	this.name.setName(name);
    }
    
    public void setDueDate(String dueDate) throws IllegalValueException{
    	this.dueDate = new DateTimeInfo(dueDate);
    }

    public void setStartTime(String startTime) throws IllegalValueException{
    	this.startTime = new DateTimeInfo(startTime);
    }
    
    public void setEndTime(String endTime) throws IllegalValueException{
    	this.endTime = new DateTimeInfo(endTime);
    }
}
