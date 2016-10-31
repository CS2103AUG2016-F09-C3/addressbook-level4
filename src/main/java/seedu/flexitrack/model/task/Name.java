package seedu.flexitrack.model.task;

import seedu.flexitrack.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's name in the address book. Guarantees: immutable; is
 * valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Task should be spaces or alphanumeric characters";
    public static final String NAME_VALIDATION_REGEX = ".+";
    public static final String DONE_PREFIX = "(Done) ";
    public static final String BLOCK_PREFIX = "(Blocked) ";
        
    private String isDonePrefix;
    private String fullName;
    private String isBlockPrefix;
    
    
    /**
     * Validates given name.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public Name(String name) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidName(name)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        if (name.contains(DONE_PREFIX)){
            this.isDonePrefix = name.substring(0, 7); 
            this.fullName = name.substring(7);
        } else {
            this.fullName = name;
            this.isDonePrefix = ""; 
        }
        if (name.contains(BLOCK_PREFIX)){
            this.isBlockPrefix = name.substring(0, 10); 
            this.fullName = name.substring(10);
        } else {
            this.fullName = name;
            this.isBlockPrefix = ""; 
        }
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    public void setName(String name) {
        this.fullName = name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                        && this.fullName.equals(((Name) other).fullName)); // state
                                                                           // check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
    
    public boolean getIsDone(){ 
        return isDonePrefix.equals(DONE_PREFIX);
    }
    
    public boolean getIsBlock() {
        return isBlockPrefix.equals(BLOCK_PREFIX);
    }
    
    public String setBlock(){ 
        return isBlockPrefix = BLOCK_PREFIX; 
    }
    
  //@@author A0127686R
    @Override
    public String toString(){ 
        return isDonePrefix + isBlockPrefix + fullName; 
    }

    public String getNameOnly(){ 
        return fullName; 
    }

    public String setAsMark(){ 
        return isDonePrefix = DONE_PREFIX; 
    }

    public String setAsUnmark(){ 
        return isDonePrefix = ""; 
    }
}
