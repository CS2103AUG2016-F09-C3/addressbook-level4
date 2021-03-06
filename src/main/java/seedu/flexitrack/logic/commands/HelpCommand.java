//@@author A0138455Y
package seedu.flexitrack.logic.commands;

import seedu.flexitrack.commons.core.EventsCenter;
import seedu.flexitrack.commons.events.ui.ShowHelpRequestEvent;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";
    public static final String COMMAND_SHORTCUT = "h";

    public static final String MESSAGE_USAGE = COMMAND_WORD  + ", Shortcut [" + COMMAND_SHORTCUT + "]" + ": Shows program usage instructions.\n" + "Example: "
            + COMMAND_WORD;

    public static final String HELP_MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "List of Commands: " + AddCommand.COMMAND_WORD + ", " + ClearCommand.COMMAND_WORD + ", "
            + DeleteCommand.COMMAND_WORD + ", " + EditCommand.COMMAND_WORD + ", " + ExitCommand.COMMAND_WORD + ", "
            + FindCommand.COMMAND_WORD + ", " + ListCommand.COMMAND_WORD + ", " + MarkCommand.COMMAND_WORD + ", "
            + UnmarkCommand.COMMAND_WORD + ", " + BlockCommand.COMMAND_WORD + ", " 
            + ChangeStoragePathCommand.COMMAND_WORD + "(Change Storage Path), " + UndoCommand.COMMAND_WORD 
            + ", " + RedoCommand.COMMAND_WORD + ", " + GapCommand.COMMAND_WORD + "\n" + "Example: " + COMMAND_WORD + " "
            + ClearCommand.COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";
    private String userInput;

    public HelpCommand(String args) {
        this.userInput = args;
    }

    @Override
    public CommandResult execute() {        
        switch (userInput) {

        case AddCommand.COMMAND_WORD:
            return new CommandResult(AddCommand.MESSAGE_USAGE);

        case EditCommand.COMMAND_WORD:
            return new CommandResult(EditCommand.MESSAGE_USAGE);

        case DeleteCommand.COMMAND_WORD:
            return new CommandResult(DeleteCommand.MESSAGE_USAGE);

        case ClearCommand.COMMAND_WORD:
            return new CommandResult(ClearCommand.MESSAGE_USAGE);

        case FindCommand.COMMAND_WORD:
            return new CommandResult(FindCommand.MESSAGE_USAGE);

        case MarkCommand.COMMAND_WORD:
            return new CommandResult(MarkCommand.MESSAGE_USAGE);

        case UnmarkCommand.COMMAND_WORD:
            return new CommandResult(UnmarkCommand.MESSAGE_USAGE);

        case ListCommand.COMMAND_WORD:
            return new CommandResult(ListCommand.MESSAGE_USAGE);

        case ExitCommand.COMMAND_WORD:
            return new CommandResult(ExitCommand.MESSAGE_USAGE);

        case BlockCommand.COMMAND_WORD:
            return new CommandResult(BlockCommand.MESSAGE_USAGE);
            
        case UndoCommand.COMMAND_WORD:
            return new CommandResult(UndoCommand.MESSAGE_USAGE);
            
        case RedoCommand.COMMAND_WORD:
            return new CommandResult(RedoCommand.MESSAGE_USAGE);
            
        case GapCommand.COMMAND_WORD:
            return new CommandResult(GapCommand.MESSAGE_USAGE);
            
        case ChangeStoragePathCommand.COMMAND_WORD:
            return new CommandResult(ChangeStoragePathCommand.MESSAGE_USAGE);
        
        default:
            return new CommandResult(HELP_MESSAGE_USAGE);
        }
        /*
         * EventsCenter.getInstance().post(new ShowHelpRequestEvent()); return
         * new CommandResult(SHOWING_HELP_MESSAGE);
         */
    }

}
