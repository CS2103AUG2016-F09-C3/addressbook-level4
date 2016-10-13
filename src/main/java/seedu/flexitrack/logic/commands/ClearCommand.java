package seedu.flexitrack.logic.commands;

import seedu.flexitrack.model.FlexiTrack;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(FlexiTrack.getEmptyFlexiTrack());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
