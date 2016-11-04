//@@author A0138455Y
package guitests;

import org.junit.Test;

import seedu.flexitrack.commons.core.Messages;
import seedu.flexitrack.logic.commands.MarkCommand;
import seedu.flexitrack.logic.commands.UnmarkCommand;
import seedu.flexitrack.testutil.TestTask;
import seedu.flexitrack.testutil.TestUtil;

public class MarkCommandTest extends FlexiTrackGuiTest {
    TestTask[] currentList = td.getTypicalSortedTasks();
    
    @Test
    public void mark() {
        // mark a task
        //TestTask[] currentList = td.getTypicalSortedTasks();
        assertMarkSuccess(4, currentList);
        currentList = TestUtil.markTasksToList(currentList, 4);
    }   
    
    @Test
    public void markFloatingTask() {
        // mark a floating task
        //TestTask[] currentList = td.getTypicalSortedTasks();
        assertMarkSuccess(1, currentList);
        currentList = TestUtil.markTasksToList(currentList, 1);
    }   
    
    @Test
    public void markInvailidIndex() {
        // mark a task with invalid number
        commandBox.runCommand(TestTask.getMarkCommand(100));
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        //assertTrue(taskListPanel.isListMatching(currentList));
    }   
    
    @Test
    public void markAnMarkedTask() {
        // mark an already marked task
        commandBox.runCommand(TestTask.getMarkCommand(8));
        assertMarkFail(8, currentList);
        //currentList = TestUtil.markTasksToList(currentList, 4);
    }   
    
    @Test
    public void unmark() {
        // un-mark a marked task
        commandBox.runCommand(TestTask.getMarkCommand(8));
        assertUnMarkSuccess(8, currentList);
        currentList = TestUtil.unMarkTasksToList(currentList, 8);
        //assertTrue(taskListPanel.isListMatching(currentList));
    }   
    
    @Test
    public void unmarkUndoneTask() {
        // un-mark an unmarked task
        assertUnMarkFail(3, currentList);
        currentList = TestUtil.unMarkTasksToList(currentList, 3);
        //assertTrue(taskListPanel.isListMatching(currentList));
    }   
    
    @Test
    public void unmarkInvalidIndex() {
        // unmark a task with invalid number
        commandBox.runCommand(TestTask.getUnMarkCommand(100));
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        //assertTrue(taskListPanel.isListMatching(currentList));
    }

 private void assertMarkFail(int taskToMark, TestTask... currentList) {
     commandBox.runCommand(TestTask.getMarkCommand(taskToMark));
     assertResultMessage("Task already marked!");
     
     // confirm the list now contains all previous tasks plus the new task
     //TestTask[] expectedList = TestUtil.markTasksToList(currentList, taskToMark);
     //assertTrue(taskListPanel.isListMatching(expectedList));
        
    }

//   private void assertUnMarkSuccess(int taskToUnMark, TestTask... currentList) {
    private void assertUnMarkSuccess(int taskToUnMark, TestTask... currentList) {
        commandBox.runCommand(TestTask.getUnMarkCommand(taskToUnMark));
        assertResultMessage(String.format(UnmarkCommand.MESSAGE_UNMARK_TASK_SUCCESS, taskToUnMark));
        
        // confirm the list now contains all previous tasks plus the new task
        //TestTask[] expectedList = TestUtil.unMarkTasksToList(currentList, taskToUnMark);
        //assertTrue(taskListPanel.isListMatching(expectedList));
    }
    
    private void assertUnMarkFail(int taskToUnMark , TestTask... currentList) {
        commandBox.runCommand(TestTask.getUnMarkCommand(taskToUnMark));
        assertResultMessage("Task already unmarked!");
        
        // confirm the list now contains all previous tasks plus the new task
        //TestTask[] expectedList = TestUtil.unMarkTasksToList(currentList, taskToUnMark);
        //assertTrue(taskListPanel.isListMatching(expectedList));
    }

    
    private void assertMarkSuccess(int taskToMark, TestTask... currentList) {
        commandBox.runCommand(TestTask.getMarkCommand(taskToMark));
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, taskToMark));
        // confirm the list now contains all previous tasks plus the new task
        //TestTask[] expectedList = TestUtil.markTasksToList(currentList, taskToMark);
        //assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
