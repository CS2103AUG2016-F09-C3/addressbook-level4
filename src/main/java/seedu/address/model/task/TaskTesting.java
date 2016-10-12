package seedu.address.model.task;

import seedu.address.model.person.DateTimeInfo;

public class TaskTesting {
    private String taskName;
    private DateTimeInfo dateTimeInfo;
    
    
/*    public TaskTesting(String taskName) {
        this(taskName, new DateTimeInfo());
    }*/
    
    public TaskTesting(String taskName, DateTimeInfo dateTimeInfo) {
        this.taskName = taskName;
        this.dateTimeInfo = dateTimeInfo;

    }
    
    private String getTaskName() {
        return taskName;
    }
    
    private DateTimeInfo getDate() {
        return dateTimeInfo;
    }
}
