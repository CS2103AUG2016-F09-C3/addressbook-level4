package seedu.flexitrack.logic.commands;
import java.util.Set;

import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.model.task.DateTimeInfo;
import seedu.flexitrack.model.task.Name;
import seedu.flexitrack.model.task.Task;
import seedu.flexitrack.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.flexitrack.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0138455Y
/**
 * Adds a block  event period to the FlexiTrack.
 */
public class BlockCommand extends Command {
public static final String COMMAND_WORD = "block";
    
    public static final String COMMAND_SHORTCUT = "b";
    

    public static final String MESSAGE_USAGE = COMMAND_WORD  + ", Shortcut [" + COMMAND_SHORTCUT + "]" + ": Block a period of time in the FlexiTrack.\n"
            + "Parameters to block a period of time: [Description] from/ [starting time] to/ [ending time]\n"
            + "\tExample: " + COMMAND_WORD + " reserve for study from/ 1st June 5pm to/ 1st July 7pm\n";

    public static final String MESSAGE_SUCCESS = "Block the date for %1$s";
    public static final String MESSAGE_DUPLICATE_TIME = "This period of time has already taken by other event, Please choose another time.";
    public static final String MESSAGE_OVERLAPPING_EVENT_WARNING = "\nWarning: this event is overlaping a existing event!";
    private static final String MESSAGE_UNDO_SUCCESS = "Undid block: %1$s";
    
    private Task toBlock;
    private boolean isOverlapping = false;
    
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException
     *             if any of the raw values are invalid
     */
    public BlockCommand(String name, String dueDate, String startTime, String endTime)
            throws IllegalValueException {
        this.toBlock = new Task(new Name(name), new DateTimeInfo(dueDate), new DateTimeInfo(startTime),
                new DateTimeInfo(endTime));
    }
    
    public BlockCommand() {
        this.toBlock = null; 
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            if(model.checkBlock(toBlock)) {
                return new CommandResult(BlockCommand.MESSAGE_DUPLICATE_TIME);
            }
            this.isOverlapping = model.checkOverlapEvent(toBlock);
            
            model.addTask(toBlock);
            toBlock = toBlock.copy();
            recordCommand(this);
            if (toBlock.getIsEvent()) {
                return new CommandResult((String.format(MESSAGE_SUCCESS, toBlock)) + "\n" + DateTimeInfo
                        .durationOfTheEvent(toBlock.getStartTime().toString(), toBlock.getEndTime().toString())
                        + (isOverlapping? MESSAGE_OVERLAPPING_EVENT_WARNING : ""));
            } else {
                return new CommandResult(String.format(MESSAGE_SUCCESS, toBlock));
            }
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TIME);
        }

    }
    @Override
    public void executeUndo() {
        Task toDelete = toBlock;
        try {
            model.deleteTask(toDelete);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }      
    }
    
  //@@author A0127855W
    @Override
    public String getUndoMessage(){
        return String.format(MESSAGE_UNDO_SUCCESS, toBlock);
    }

}
