package seedu.flexitrack.logic.parser;

import static seedu.flexitrack.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.flexitrack.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.flexitrack.commons.core.Messages.MESSAGE_NUMBER_NEED_TO_BE_IN_DIGIT;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.flexitrack.commons.core.LogsCenter;
import seedu.flexitrack.commons.exceptions.IllegalValueException;
import seedu.flexitrack.commons.util.StringUtil;
import seedu.flexitrack.logic.commands.AddCommand;
import seedu.flexitrack.logic.commands.BlockCommand;
import seedu.flexitrack.logic.commands.ChangeStoragePathCommand;
import seedu.flexitrack.logic.commands.ClearCommand;
import seedu.flexitrack.logic.commands.Command;
import seedu.flexitrack.logic.commands.DeleteCommand;
import seedu.flexitrack.logic.commands.EditCommand;
import seedu.flexitrack.logic.commands.ExitCommand;
import seedu.flexitrack.logic.commands.FindCommand;
import seedu.flexitrack.logic.commands.GapCommand;
import seedu.flexitrack.logic.commands.HelpCommand;
import seedu.flexitrack.logic.commands.IncorrectCommand;
import seedu.flexitrack.logic.commands.ListCommand;
import seedu.flexitrack.logic.commands.MarkCommand;
import seedu.flexitrack.logic.commands.RedoCommand;
import seedu.flexitrack.logic.commands.UndoCommand;
import seedu.flexitrack.logic.commands.UnmarkCommand;
import seedu.flexitrack.model.Model;
import seedu.flexitrack.model.task.DateTimeInfo;
import seedu.flexitrack.model.task.DateTimeInfoParser;


/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); 
    //@@author A0127855W 
    private static final HashMap<String, String> SHORTCUT_MAP = new HashMap<String, String>();                                                                                                       // more
    static {
        SHORTCUT_MAP.put(AddCommand.COMMAND_SHORTCUT, AddCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(ClearCommand.COMMAND_SHORTCUT, ClearCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(DeleteCommand.COMMAND_SHORTCUT, DeleteCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(EditCommand.COMMAND_SHORTCUT, EditCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(ExitCommand.COMMAND_SHORTCUT, ExitCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(HelpCommand.COMMAND_SHORTCUT, HelpCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(FindCommand.COMMAND_SHORTCUT, FindCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(ListCommand.COMMAND_SHORTCUT, ListCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(MarkCommand.COMMAND_SHORTCUT, MarkCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(UnmarkCommand.COMMAND_SHORTCUT, UnmarkCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(BlockCommand.COMMAND_SHORTCUT, BlockCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(UndoCommand.COMMAND_SHORTCUT, UndoCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(RedoCommand.COMMAND_SHORTCUT, RedoCommand.COMMAND_WORD);
        SHORTCUT_MAP.put(GapCommand.COMMAND_SHORTCUT, GapCommand.COMMAND_WORD);
    }  

    //@@author A0127686R
    private final Logger logger = LogsCenter.getLogger(Parser.class);

    private static final HashMap<String, String> SHORTCUT_LIST_MAP = new HashMap<String, String>();                                                                                                       // more
    static {
        SHORTCUT_LIST_MAP.put("f", ListCommand.LIST_FUTURE_COMMAND);
        SHORTCUT_LIST_MAP.put("p", ListCommand.LIST_PAST_COMMAND);
        SHORTCUT_LIST_MAP.put("m", ListCommand.LIST_MARK_COMMAND);
        SHORTCUT_LIST_MAP.put("u", ListCommand.LIST_UNMARK_COMMAND);
        SHORTCUT_LIST_MAP.put("b", ListCommand.LIST_BLOCK_COMMAND);
        SHORTCUT_LIST_MAP.put("l", ListCommand.LIST_LAST_COMMAND);
        SHORTCUT_LIST_MAP.put("n", ListCommand.LIST_NEXT_COMMAND);
        SHORTCUT_LIST_MAP.put("lw", ListCommand.LIST_LAST_WEEK_COMMAND);
        SHORTCUT_LIST_MAP.put("lm", ListCommand.LIST_LAST_MONTH_COMMAND);
        SHORTCUT_LIST_MAP.put("nw", ListCommand.LIST_NEXT_WEEK_COMMAND);
        SHORTCUT_LIST_MAP.put("nm", ListCommand.LIST_NEXT_MONTH_COMMAND);
    }
    // '/' forward slashes are reserved for delimiter prefixes
    private static final Pattern TASK_FIND_GAP_WITH_NUMBER_ARGS_FORMAT = Pattern
            .compile("(?<info>[^/]+)" + "[Nn]/(?<numberOfGaps>[^/]+)");
    private static final Pattern TASK_FIND_GAP_ARGS_FORMAT = Pattern.compile("(?<info>[^/]+)");

    private static final Pattern TASK_EVENT_TYPE_DATA_ARGS_FORMAT = Pattern
            .compile("(?<name>.+)" + "[fF][rR][oO][mM]/(?<startTime>[^/]+)" + "[Tt][Oo]/(?<endTime>[^/]+)");
    private static final Pattern TASK_DEADLINE_TYPE_DATA_ARGS_FORMAT = Pattern
            .compile("(?<name>.+)" + "[Bb][Yy]/(?<dueDate>[^/]+)");
    private static final Pattern TASK_FLOATING_TYPE_DATA_ARGS_FORMAT = Pattern.compile("(?<name>.+)");

    // @@author A0147092E
    private static final Pattern TASK_RECURRING_EVENT_TYPE_DATA_ARGS_FORMAT = Pattern
            .compile("(?<name>.+)" + "[Ff][Rr]/\\s*(?<numOfOccurrence>\\d{1,2})\\s*"
                    + "[tT][Yy]/(?<occurrenceType>[^/\\d]{3,7})" + "from/(?<startTime>[^/]+)" + "to/(?<endTime>[^/]+)");
    private static final Pattern TASK_RECURRING_TASK_TYPE_DATA_ARGS_FORMAT = Pattern
            .compile("(?<name>.+)" + "[Ff][Rr]/\\s*(?<numOfOccurrence>\\d{1,2})\\s*"
                    + "[Tt][Yy]/\\s*(?<occurrenceType>[^/\\d]{3,7})" + "by/(?<dueDate>[^/]+)");
    private static final Pattern TASK_RECURRING_FLOATING_TASK_TYPE_DATA_ARGS_FORMAT = Pattern
            .compile("(?<name>.+)" + "[Ff][Rr]/\\s*(?<numOfOccurrence>\\d{1,2})");
    //@@author A0127855W
    
    private static final Pattern EDIT_COMMAND_FORMAT = Pattern.compile("(?<index>[0-9]+)(?<arguments>.*)");
    private static final Pattern EDIT_ARGS_NAME = Pattern.compile("[Nn]/\\s*(?<name>.+)");
    private static final Pattern EDIT_ARGS_DUEDATE = Pattern.compile("[Bb][Yy]/\\s*(?<dueDate>[^/]+)");
    private static final Pattern EDIT_ARGS_STARTTIME = Pattern.compile("[Ff][Rr][oO][mM]/\\s*(?<startTime>[^/]+)");
    private static final Pattern EDIT_ARGS_ENDTIME = Pattern.compile("[tT][Oo]/\\s*(?<endTime>[^/]+)");
     //@@author A0138455Y
    private static final Pattern STORAGE_PATH_FORMAT = Pattern.compile("(?<path>^[^\\?~`!@#$^&-+=%*:|\"<>\\|]+)");
    //@@author
    
    private Model model;

    public final static String EMPTY_TIME_INFO = "Feb 29 2000 00:00:00";


    public Parser() {
    }

    public Parser(Model model) {
        this.model = model;
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput
     *            full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String parsedCommandWord = parseCommandWord(commandWord.toLowerCase());

        final String arguments = matcher.group("arguments");
        switch (parsedCommandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case BlockCommand.COMMAND_WORD:
            return prepareBlock(arguments);

        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();
            
        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case MarkCommand.COMMAND_WORD:
            return prepareMark(arguments);

        case UnmarkCommand.COMMAND_WORD:
            return prepareUnmark(arguments);

        case ListCommand.COMMAND_WORD:
            return prepareList(arguments.toLowerCase());

        case GapCommand.COMMAND_WORD:
            return prepareGap(arguments.toLowerCase());

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();
            
        case ChangeStoragePathCommand.COMMAND_WORD:
            return prepareChangePathCommand(arguments);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand(arguments.trim());

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    // @@author A0127686R
    /**
     * Prepare the user input arguments to be passed GapCommand class
     * 
     * @param args
     * @return a new Gap Command class
     */
    private Command prepareGap(String args) {
        args.toLowerCase();

        Matcher matcher = TASK_FIND_GAP_WITH_NUMBER_ARGS_FORMAT.matcher(args.trim());
        int numberOfSlot = GapCommand.DEFAULT_NUMBER_OF_SLOT;

        if (matcher.matches()) {
            args = matcher.group("info").trim();
            try {
                numberOfSlot = Integer.parseInt(matcher.group("numberOfGaps").trim());
            } catch (NumberFormatException nfe) {
                logger.info("----------------[GAP COMMAND PARSER][" + " user input length without a digit " + "]");
                return new IncorrectCommand(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_NUMBER_NEED_TO_BE_IN_DIGIT));
            }
        } else {
            if (args.trim().equals("") || args.trim().contains("n/")) {
                logger.info("----------------[GAP COMMAND PARSER][" + " user used n/ without specifying the number " + "]");
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GapCommand.MESSAGE_USAGE));
            }
            matcher = TASK_FIND_GAP_ARGS_FORMAT.matcher(args.trim());
            matcher.matches();
            args = matcher.group("info").trim();
        }

        return issueGapCommandIfArgsIsValid(args, numberOfSlot);
    }

    /**
     * Test if the args is valid, if it is convert the length and key keywords into integer representing them 
     * and issue a new Gap Command, else log and return invalid command 
     * 
     * @param args          Arguments of the length and keyword
     * @param numberOfSlot  The number of time slot to be find 
     * @return              GapCommand or IncorrectCommand 
     */
    private Command issueGapCommandIfArgsIsValid(String args, int numberOfSlot) {
        if (isGapArgumentValid(args)) {
            try {
                int keyword = extractKeywordFromArgs(args);
                int length = extractLength(args);
                return new GapCommand(keyword, length, numberOfSlot);

            } catch (NumberFormatException nfe) {
                logger.info("----------------[GAP COMMAND PARSER][" + " Invalid user input " + "]");
                return new IncorrectCommand(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_NUMBER_NEED_TO_BE_IN_DIGIT));
            }
        } else {
            logger.info("----------------[GAP COMMAND PARSER][" + " Invalid user input " + "]");
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GapCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Extract the length of the duration from string in keywords to number
     * 
     * @param args
     * @return length of duration in integer
     * @throws NumberFormatException
     */
    private int extractLength(String args) throws NumberFormatException {
        String length = args.replace(GapCommand.WORD_DAY + "s", "").replace(GapCommand.WORD_HOUR + "s", "")
                .replace(GapCommand.WORD_MINUTE + "s", "").replace(GapCommand.WORD_DAY, "")
                .replace(GapCommand.WORD_HOUR, "").replace(GapCommand.WORD_MINUTE, "")
                .replace(GapCommand.INITIAL_DAY, "").replace(GapCommand.INITIAL_MINUTE, "")
                .replace(GapCommand.INITIAL_HOUR, "");
        if (length.trim().equals("")) {
            return 1;
        } else {
            return Integer.parseInt(length.trim());
        }
    }

    /**
     * Extract the keyword from the arguments and return it in integer reference
     * number
     * 
     * @param args
     * @return number representing each key word.
     */
    private int extractKeywordFromArgs(String args) {
        if (args.contains(GapCommand.WORD_DAY) || args.contains(GapCommand.WORD_DAY + "s")
                || args.contains(GapCommand.INITIAL_DAY)) {
            return GapCommand.REF_NO_DAY;
        }
        if (args.contains(GapCommand.WORD_HOUR) || args.contains(GapCommand.WORD_HOUR + "s")
                || args.contains(GapCommand.INITIAL_HOUR)) {
            return GapCommand.REF_NO_HOUR;
        } else {
            return GapCommand.REF_NO_MINUTE;
        }
    }

    /**
     * Find out if the argument is a valid argument for a GapCommand. The
     * argument could not have more than one timing key words ( minute, hour or
     * day )
     * 
     * @param args
     * @return true if the argument is a valid argument
     */
    private boolean isGapArgumentValid(String args) {
        int numberOfMatch = 0;

        if (args.contains(GapCommand.WORD_DAY) || args.contains(GapCommand.WORD_DAY + "s")
                || args.contains(GapCommand.INITIAL_DAY)) {
            numberOfMatch = numberOfMatch + 1;
        }
        if (args.contains(GapCommand.WORD_HOUR) || args.contains(GapCommand.WORD_HOUR + "s")
                || args.contains(GapCommand.INITIAL_HOUR)) {
            numberOfMatch = numberOfMatch + 1;
        }
        if (args.contains(GapCommand.WORD_MINUTE) || args.contains(GapCommand.WORD_MINUTE + "s")
                || args.contains(GapCommand.INITIAL_MINUTE)) {
            numberOfMatch = numberOfMatch + 1;
        }
        return numberOfMatch == 1;
    }

    //@@author A0127855W
    /**
     * parseCommandWord
     * -------------------------------------------
     * Parses the given command word string, converting shortcut commands into their full versions
     * @param commandWord
     * @return String: Full command word
     */
    private String parseCommandWord(String commandWord) {     
        assert commandWord != null;
        return SHORTCUT_MAP.getOrDefault(commandWord, commandWord);
    }

  //@@author A0127686R
    /**
     * Check if the arguments are valid for list Command
     * 
     * @param arguments The user inputed argument
     * @return          New ListCommand containing arguments
     */
    private Command prepareList(String arguments) {
        String parsedArguments = parseListWord(arguments.trim());
        parsedArguments = parsedArguments.trim();
        try {
            if (isValideListFormat(parsedArguments)) {
                return new ListCommand(parsedArguments);
            }
        } catch (IllegalValueException e) {
            logger.info("----------------[LIST COMMAND PARSER][" + " Invalid user input " + "]");
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
        return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
    
    /**
     * if the argument is a shortcut, return the parsed proper word
     * 
     * @param listWord  The list argument key word inputed by the user
     * @return          The pair proper word, if not found return default 
     */
    private String parseListWord(String listWord) {     
        assert listWord != null;
        return SHORTCUT_LIST_MAP.getOrDefault(listWord, listWord);
    }
    
    //@@author A0138455Y
    /**
     * 
     * @param args
     * @return
     */
    private Command prepareBlock(String args) {
        final Matcher matcherEvent = TASK_EVENT_TYPE_DATA_ARGS_FORMAT.matcher(args.trim());

        // Validate arg string format
        try {
            if (matcherEvent.matches()) {
                return new BlockCommand("(Blocked) " + matcherEvent.group("name"), EMPTY_TIME_INFO, matcherEvent.group("startTime"),
                        matcherEvent.group("endTime"));
            } else {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BlockCommand.MESSAGE_USAGE));
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    //@@author A0127686R
    /**
     * @param arguments
     * @return
     * @throws IllegalValueException 
     */
    private boolean isValideListFormat(String arguments) throws IllegalValueException {
        String dateInfo = (arguments.replace(ListCommand.LIST_FUTURE_COMMAND, "").replace(ListCommand.LIST_PAST_COMMAND, "").
                replace(ListCommand.LIST_UNMARK_COMMAND, "").replace(ListCommand.LIST_MARK_COMMAND, "").
                replace(ListCommand.LIST_LAST_MONTH_COMMAND, "").replace(ListCommand.LIST_LAST_WEEK_COMMAND, "").
                replace(ListCommand.LIST_NEXT_MONTH_COMMAND, "").replace(ListCommand.LIST_NEXT_WEEK_COMMAND, "").
                replace(ListCommand.LIST_BLOCK_COMMAND, "").trim());
        if ( !dateInfo.equals("") ){
            DateTimeInfoParser timeArgs = new DateTimeInfoParser(dateInfo);
            assert timeArgs != null; 
        }
        return (arguments.contains(ListCommand.LIST_FUTURE_COMMAND) || arguments.contains(ListCommand.LIST_UNMARK_COMMAND)
                || arguments.contains(ListCommand.LIST_PAST_COMMAND) || arguments.contains(ListCommand.LIST_MARK_COMMAND)
                || arguments.contains(ListCommand.LIST_UNSPECIFIED_COMMAND) || arguments.contains(ListCommand.LIST_LAST_WEEK_COMMAND) 
                || arguments.contains(ListCommand.LIST_LAST_MONTH_COMMAND) || arguments.contains(ListCommand.LIST_NEXT_MONTH_COMMAND) 
                || arguments.contains(ListCommand.LIST_NEXT_WEEK_COMMAND) || arguments.contains(ListCommand.LIST_BLOCK_COMMAND));
    }

    //@@author A0127855W
    /**
     * prepareEdit
     * ------------------------------------------
     * Parses the edit command arguments and outputs the correct EditCommand object for execution
     * @param arguments
     * @return Command: The correct EditCommand object
     */
    private Command prepareEdit(String arguments) {
        assert arguments != null;

        int index;
        String editParameters;
        String[] passing = new String[EditCommand.EDIT_PARAMETER_PASSING_MASK.size()];

        final Matcher matcherEdit = EDIT_COMMAND_FORMAT.matcher(arguments.trim());

        if (!matcherEdit.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        } else {
            index = Integer.parseInt(matcherEdit.group("index"));
            editParameters = matcherEdit.group("arguments").trim();
        }

        final Matcher matcherName = EDIT_ARGS_NAME.matcher(editParameters);
        final Matcher matcherDueDate = EDIT_ARGS_DUEDATE.matcher(editParameters);
        final Matcher matcherStartTime = EDIT_ARGS_STARTTIME.matcher(editParameters);
        final Matcher matcherEndTime = EDIT_ARGS_ENDTIME.matcher(editParameters);

        boolean isNamePresent = matcherName.find();
        boolean isDueDatePresent = matcherDueDate.find();
        boolean isStartTimePresent = matcherStartTime.find();
        boolean isEndTimePresent = matcherEndTime.find();

        //Check that at least one edit parameter exists
        if (!isNamePresent && !isDueDatePresent && !isStartTimePresent && !isEndTimePresent) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        prepareEditParameter(passing, matcherName, isNamePresent, "name");
        prepareEditParameter(passing, matcherDueDate, isDueDatePresent, "dueDate");
        prepareEditParameter(passing, matcherStartTime, isStartTimePresent, "startTime");
        prepareEditParameter(passing, matcherEndTime, isEndTimePresent, "endTime");

        return new EditCommand(index, passing);
    }

    /**
     * prepareEditParameter
     * ---------------------------------------------------------
     * prepares the passing array for each parameter to be passed into the EditCommand constructor
     * @param passing
     * @param matcherType
     * @param typePresent
     * @param typeGroupID
     */
    private void prepareEditParameter(String[] passing, final Matcher matcherType, boolean typePresent, String typeGroupID) {
        if (typePresent) {
            passing[EditCommand.EDIT_PARAMETER_PASSING_MASK.get(typeGroupID)] = matcherType.group(typeGroupID);
        } else {
            passing[EditCommand.EDIT_PARAMETER_PASSING_MASK.get(typeGroupID)] = null;
        }
    }

    //@@author A0138455Y
    /**
     * Parses arguments in the context of the Unmarkcommand.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareUnmark(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE));
        }

        return new UnmarkCommand(index.get());
    }

    /**
     * Parses arguments in the context of the Markcommand.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareMark(String args) {

        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        return new MarkCommand(index.get());
    }
    
    /**
     * Parses arguments in the context of the ChangeStoragecommand.
     * @param arguments
     * @return
     */
    private Command prepareChangePathCommand(String args) {
        args = args.trim();
        Matcher matcher = STORAGE_PATH_FORMAT.matcher(args);
        //Validate args string format
        if(!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeStoragePathCommand.MESSAGE_USAGE));
        } else {
            String newPath = matcher.group("path").trim() + ".xml";
            return new ChangeStoragePathCommand(newPath);
        }
    }
    //@@author

    //@@author A0127686R
    /**
     * Parses arguments in the context of the add task command.
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args) {
        //@@author A0127686R
        args = args.trim();
        final Matcher matcherEvent = TASK_EVENT_TYPE_DATA_ARGS_FORMAT.matcher(args);
        final Matcher matcherDeadline = TASK_DEADLINE_TYPE_DATA_ARGS_FORMAT.matcher(args);
        final Matcher matcherFloating = TASK_FLOATING_TYPE_DATA_ARGS_FORMAT.matcher(args);
        //@@author A0147092E
        final Matcher matcherRecurringFloatingTask = TASK_RECURRING_FLOATING_TASK_TYPE_DATA_ARGS_FORMAT.matcher(args);
        final Matcher matcherRecurringEvent = TASK_RECURRING_EVENT_TYPE_DATA_ARGS_FORMAT.matcher(args);
        final Matcher matcherRecurringTask = TASK_RECURRING_TASK_TYPE_DATA_ARGS_FORMAT.matcher(args);
        // Validate arg string format
        try {
            //@@author A0147092E
            if (args.trim().contains("fr/") || args.trim().contains("ty/")) {
                if (matcherRecurringFloatingTask.matches()) {
                    return addRecurring(matcherRecurringFloatingTask, "floating");
                } else if (matcherRecurringEvent.matches()) {
                    return addRecurring(matcherRecurringEvent, "event");
                } else if (matcherRecurringTask.matches()) {
                    return addRecurring(matcherRecurringTask, "task");
                } else {
                    return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
                }
            //@@author
            } else {
              //@@author A0127686R
                if (matcherEvent.matches()) {
                    return addEventTask(matcherEvent);
                } else if (matcherDeadline.matches()) {
                    return addDeadlineTask(matcherDeadline);
                } else if (matcherFloating.matches()) {
                    return addFloatingTask(matcherFloating);
                } else {
                    return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
                }
            }
            //@@author 
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

  //@@author A0127686R
    /**
     * Add a floating task, null date for dudate, starting time and ending time. 
     * 
     * @return  new AddCommand for Floating task 
     * @throws IllegalValueException
     */
    private AddCommand addFloatingTask(Matcher matcher) throws IllegalValueException {
        return new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, EMPTY_TIME_INFO, EMPTY_TIME_INFO);
    }

    /**
     * Add a deadline task, null date for starting time and ending time. 
     * 
     * @return  new AddCommand for deadline task 
     * @throws IllegalValueException
     */
    private AddCommand addDeadlineTask(Matcher matcher) throws IllegalValueException {
        return new AddCommand(matcher.group("name"), matcher.group("dueDate"), EMPTY_TIME_INFO, EMPTY_TIME_INFO);
    }

    /** 
     * Add an event task, null date for dudate. 
     * 
     * @return  new AddCommand for event task 
     * @return
     * @throws IllegalValueException
     */
    private AddCommand addEventTask(Matcher matcher) throws IllegalValueException {
        return new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, matcher.group("startTime"),
                matcher.group("endTime"));
    }

    //@@author A0147092E
    /* Add a recurring task / event for day(s)/week(s)/month(s)
     * 
     * @param matcher - commands that matches the regular expression
     * @param recurringType - day, week, month
     */
    private Command addRecurring(Matcher matcher, String recurringType) throws IllegalValueException {
        final int DAY_INCREMENT = 1;
        final int WEEK_INCREMENT = 2;
        final int MONTH_INCREMENT = 3;
        
        String formattedStartTime;
        String formattedEndTime;
        String formattedDueDate;
        
        int numOfOccurrence = Integer.parseInt(matcher.group("numOfOccurrence").trim());
        
        if (recurringType.equalsIgnoreCase("floating")) {
            for (int i = 1; i < numOfOccurrence; i++) {
                Command command = new AddCommand(matcher.group("name") + "(" + (i+1) + ")", EMPTY_TIME_INFO, EMPTY_TIME_INFO, EMPTY_TIME_INFO);
                command.setData(model);
                command.execute();    
            } 
            return new AddCommand(matcher.group("name") + "(1)", EMPTY_TIME_INFO, EMPTY_TIME_INFO, EMPTY_TIME_INFO, numOfOccurrence);
            
        } else if (recurringType.equalsIgnoreCase("task")) {
            String occurrenceType = matcher.group("occurrenceType").trim().toLowerCase();
            DateTimeInfo dueDateTimeInfo = new DateTimeInfo (matcher.group("dueDate"));
            dueDateTimeInfo.checkDueDateOrStartTime(); 
            Date initialDueDate = new DateTimeInfoParser(dueDateTimeInfo.toString()).getParsedDateTime();
            
            switch (occurrenceType.toLowerCase()) {
            case "week": case "weekly":
                for (int i=1; i < numOfOccurrence; i++) {
                    formattedDueDate = dateIncrement(initialDueDate, WEEK_INCREMENT, i);
                    
                    Command command = new AddCommand(matcher.group("name"), formattedDueDate, EMPTY_TIME_INFO, EMPTY_TIME_INFO);
                    command.setData(model);
                    command.execute();
                }
                return new AddCommand(matcher.group("name"), matcher.group("dueDate"), EMPTY_TIME_INFO, EMPTY_TIME_INFO, numOfOccurrence);
                
            case"mth": case"month": case"monthly":
                for (int i=1; i < numOfOccurrence; i++) {
                    formattedDueDate = dateIncrement(initialDueDate, MONTH_INCREMENT, i);
                    
                    Command command = new AddCommand(matcher.group("name"), formattedDueDate, EMPTY_TIME_INFO, EMPTY_TIME_INFO);
                    command.setData(model);
                    command.execute();
                }
                return new AddCommand(matcher.group("name"), matcher.group("dueDate"), EMPTY_TIME_INFO, EMPTY_TIME_INFO, numOfOccurrence);
                
            case "day": case "daily":
                for (int i=1; i < numOfOccurrence; i++) {
                    formattedDueDate = dateIncrement(initialDueDate, DAY_INCREMENT, i);
                    
                    Command command = new AddCommand(matcher.group("name"), formattedDueDate, EMPTY_TIME_INFO, EMPTY_TIME_INFO);
                    command.setData(model);
                    command.execute();
                }
                return new AddCommand(matcher.group("name"), matcher.group("dueDate"), EMPTY_TIME_INFO, EMPTY_TIME_INFO, numOfOccurrence);
                
            default: 
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }
            
        } else {  // Recurring Event
            String occurrenceType = matcher.group("occurrenceType").trim().toLowerCase();
            DateTimeInfo startDateTimeInfo = new DateTimeInfo (matcher.group("startTime"));
            startDateTimeInfo.checkDueDateOrStartTime(); 
            DateTimeInfo endDateTimeInfo = new DateTimeInfo (matcher.group("endTime"));
            endDateTimeInfo.checkEndTime(startDateTimeInfo); 

            Date initialStartTime = new DateTimeInfoParser(startDateTimeInfo.toString()).getParsedDateTime();
            Date initialEndTime = new DateTimeInfoParser(endDateTimeInfo.toString()).getParsedDateTime();
            
            switch (occurrenceType.toLowerCase()) {
            case "week": case "weekly":
                for (int i=1; i < numOfOccurrence; i++) {
                    
                    formattedStartTime = dateIncrement(initialStartTime, WEEK_INCREMENT, i);
                    formattedEndTime = dateIncrement(initialEndTime, WEEK_INCREMENT, i);
                    
                    Command command =  new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, formattedStartTime, formattedEndTime);
                    command.setData(model);
                    command.execute();
                }
                return new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, matcher.group("startTime"), matcher.group("endTime"), numOfOccurrence);
                
            case "mth": case "month": case "monthly":
                for (int i=1; i < numOfOccurrence; i++) {
                    formattedStartTime = dateIncrement(initialStartTime, MONTH_INCREMENT, i);
                    formattedEndTime = dateIncrement(initialEndTime, MONTH_INCREMENT, i);
                    
                    Command command =  new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, formattedStartTime, formattedEndTime);
                    command.setData(model);
                    command.execute();
                }
                return new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, matcher.group("startTime"), matcher.group("endTime"), numOfOccurrence);
            
            case "day": case "daily":
                for (int i=1; i < numOfOccurrence; i++){
                    
                    formattedStartTime = dateIncrement(initialStartTime, DAY_INCREMENT, i);
                    formattedEndTime = dateIncrement(initialEndTime, DAY_INCREMENT, i);
                    
                    Command command =  new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, formattedStartTime, formattedEndTime);
                    command.setData(model);
                    command.execute();
                }
                return new AddCommand(matcher.group("name"), EMPTY_TIME_INFO, matcher.group("startTime"), matcher.group("endTime"), numOfOccurrence);
                
            default:
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }
        }
    }
    //@@author
    
    //@@author A0147092E
     /**
     * Increment a given date by number of day(s)/month(s)/week(s)
     *
     * @param initialDate
     * @param incrementType
     * @param incrementAmt
     * @return formatted date in String Format
     */
    public String dateIncrement(Date initialDate, int incrementType, int incrementAmt) {
        final int DAYS_PER_WEEK = 7;
        Date newDate;
        
        Calendar calendar = Calendar.getInstance();
        Date defaultDate = calendar.getTime();
        calendar.setTime(initialDate);

        switch (incrementType) {
        case 1: // increment by day
            calendar.add(Calendar.DATE, incrementAmt);
            newDate = calendar.getTime();
            return new SimpleDateFormat("MM-dd-yyyy HHmmss").format(newDate);
            
        case 2: // increment by week
            calendar.add(Calendar.DATE, DAYS_PER_WEEK * incrementAmt);
            newDate = calendar.getTime();
            return new SimpleDateFormat("MM-dd-yyyy HHmmss").format(newDate);
            
        case 3: // increment by month
            calendar.add(Calendar.MONTH, incrementAmt);
            newDate = calendar.getTime();
            return new SimpleDateFormat("MM-dd-yyyy HHmmss").format(newDate);
        
        default:
            return new SimpleDateFormat("MM-dd-yyyy HHmmss").format(defaultDate);
        }
    }
    //@@author

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned
     * integer is given as the index. Returns an {@code Optional.empty()}
     * otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args (full command args string)
     * @return the prepared command
     */
    public static Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());

        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (matcher.group("keywords").contains("f/") || matcher.group("keywords").contains("F/")) {
            return new FindCommand(matcher.group("keywords").trim());
        }

        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));

        return new FindCommand(keywordSet);
    }

}