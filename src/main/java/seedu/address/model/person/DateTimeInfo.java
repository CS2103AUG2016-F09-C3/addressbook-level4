package seedu.address.model.person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import seedu.address.commons.exceptions.IllegalValueException;


/**
 * Represents a DateTimeInfo class in FlexiTrack
 */
public class DateTimeInfo {
    public static final String MESSAGE_DATETIMEINFO_CONSTRAINTS = "Time format should follow DD/MM/YYYY-hhmm(in 24 hour format)";
//	public static final String DATETIMEINFO_VALIDATION_REGEX = "?[0-3][0-9]/?[0-1][0-9]/([0-9]{4})(-[0-2]?[0-9]?)?(-[0-2]?[0-9][0-9][0-9])?";
	public static final String DATETIMEINFO_VALIDATION_REGEX = "\\d+";
    
//	private Calendar dateAndTime= new GregorianCalendar();
	public String setTime;
	
	public DateTimeInfo( String givenTime)throws IllegalValueException {
	    assert givenTime != null;
	    givenTime = givenTime.trim();
        if (!isValidDateTimeInfo(givenTime)) {
            throw new IllegalValueException(MESSAGE_DATETIMEINFO_CONSTRAINTS);
        }
        this.setTime = givenTime;
//        setDateTimeInfo(givenTime);
	}
	
	/**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidDateTimeInfo(String test) {
        return test.matches(DATETIMEINFO_VALIDATION_REGEX) ;
    }
    
    public void setDateTimeInfo(String givenTime) {
        this.setTime = givenTime;
/*        SimpleDateFormat sdf = new SimpleDateFormat("DDMMYYYY");
        Date dateparse = new  Date();
        try {
            dateparse = sdf.parse(givenTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dateAndTime.setTime(dateparse);*/
    }

    @Override
    public String toString() {
        return setTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DateTimeInfo // instanceof handles nulls
                && this.setTime.equals(((DateTimeInfo) other).setTime)); // state check
    }

    @Override
    public int hashCode() {
        return setTime.hashCode();
    }
}
