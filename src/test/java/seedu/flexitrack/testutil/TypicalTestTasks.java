package seedu.flexitrack.testutil;

import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.model.FlexiTrack;
import seedu.flexitrack.model.task.*;

/**
 *
 */
public class TypicalTestTasks {

    public static TestTask homework1, homework2, homework3, soccer, dinner, exam, midterm, basketball, lecture;
// TODO: change the test case  soccer, homework, exam, dinner, homework2, homework3, lecture, basketball, midterm
    public TypicalTestTasks() {
        try {
            homework1 =  new TaskBuilder().withName("Homework cs 2103").withDueDate("21 June 5pm")
                    .withEndTime("29 Feb 23.23").withStartTime("29 Feb 23.23").build();
            homework2 = new TaskBuilder().withName("Homework cs 2101").withDueDate("23 Jun")
                    .withEndTime("29 Feb 23.23").withStartTime("29 Feb 23.23").build();
            homework3 = new TaskBuilder().withName("Homework ma 1505").withStartTime("29 Feb 23.23").withEndTime("29 Feb 23.23").withDueDate("29 Feb 23.23").build();
            soccer = new TaskBuilder().withName("Soccer training").withStartTime("20 Jun 5.30").withEndTime("20 jun 8.30").withDueDate("29 Feb 23.23").build();
            dinner = new TaskBuilder().withName("Dinner with parents").withStartTime("5th jan 6pm").withEndTime("5th jan 7.30").withDueDate("29 Feb 23.23").build();
            exam = new TaskBuilder().withName("MA 1505 Exams").withStartTime("15 Apr 10am").withEndTime("15 apr 11.45").withDueDate("29 Feb 23.23").build();
            midterm = new TaskBuilder().withName("Midter cs 2101").withStartTime("29 Feb 23.23").withEndTime("29 Feb 23.23").withDueDate("29 november").build();

            //Manually added
            basketball = new TaskBuilder().withName("Basketball training").withStartTime("25 december 18").withEndTime("25 dec 21").withDueDate("29 Feb 23.23").build();
            lecture = new TaskBuilder().withName("Lecture CS2103").withStartTime("10oct 9am").withEndTime("10 oct 11am").withDueDate("29 Feb 23.23").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadFlexiTrackWithSampleData(FlexiTrack ab) {
//TODO: change the add Task cases
        try {
            ab.addTask(new Task(homework1));
            ab.addTask(new Task(homework2));
            ab.addTask(new Task(homework3));
            ab.addTask(new Task(soccer));
            ab.addTask(new Task(dinner));
            ab.addTask(new Task(exam));
            ab.addTask(new Task(midterm));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{homework1, homework2, homework3, soccer, dinner, exam, midterm};
    }

    public FlexiTrack getTypicalFlexiTrack(){
        FlexiTrack ab = new FlexiTrack();
        loadFlexiTrackWithSampleData(ab);
        return ab;
    }
}
