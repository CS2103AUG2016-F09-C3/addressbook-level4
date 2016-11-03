package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.flexitrack.testutil.TestTask;
import seedu.flexitrack.testutil.TestUtil;

//@@author A0127686R
public class ListCommandTest extends FlexiTrackGuiTest {

    @Test
    public void testListBasic() {
        TestTask[] currentList = td.getTypicalSortedTasks();

       // list all future tasks
        String listCommand = "list future";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);

        // list all past tasks
        listCommand = "list past";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);

        // list all tasks
        listCommand = "list";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);

        currentList = TestUtil.markTasksToList(currentList, 6);
        currentList = TestUtil.markTasksToList(currentList, 4);
        currentList = TestUtil.markTasksToList(currentList, 3);
        currentList = TestUtil.markTasksToList(currentList, 1);
        
        commandBox.runCommand("mark 6");
        commandBox.runCommand("mark 4");
        commandBox.runCommand("mark 3");
        commandBox.runCommand("mark 1");
        
        // list all marked tasks
        listCommand = "list mark";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
        
        // list all unmarked tasks
        listCommand = "list unmark";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
        
        // list future tasks that are marked
        listCommand = "list future mark";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
                
    }
    
    public void testListRelative() {
        commandBox.runCommand("add lecture 1 from/ Nov 08 2016 09:00 to/Nov 08 2016 11:00");
        commandBox.runCommand("add exam 1 from/Nov 20 2016 09:00 to/Nov 20 2016 10:30 ");
        commandBox.runCommand("add past 1 from/Nov 01 2016 09:00 to/ Nov 01 2016 11:00");
        commandBox.runCommand("add past 2 from/Oct 20 2016 15:00 to/Oct 20 2016 16:00");
        TestTask[] currentList = td.getTypicalSortedTasks();

        // list last week task 
        String listCommand = "list last week";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
        
        // list last month task 
        listCommand = "list last month";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
        
        // list next week 
        listCommand = "list next week";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);

        //list next month
        listCommand = "list next month";
        assertFindSuccess(listCommand, currentList);
        currentList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
        
    }

    private void assertFindSuccess(String listCommand, TestTask... currentList) {
        commandBox.runCommand(listCommand);

        // confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.listTasksAccordingToCommand(currentList, listCommand);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
