package seedu.flexitrack.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.flexitrack.model.task.ReadOnlyTask;

public class PersonCard extends UiPart{

    private static final String FXML = "PersonListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label title;
    @FXML
    private Label id;
    @FXML
    private Label dateTime;
    @FXML
    private Label tags;

    private ReadOnlyTask task;
    private int displayedIndex;

    public PersonCard(){

    }

    public static PersonCard load(ReadOnlyTask person, int displayedIndex){
        PersonCard card = new PersonCard();
        card.task = person;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
    	String dateInfo;
    	if(task.getIsTask()){
    	    dateInfo = " by " + task.getDueDate();
    	}else if(task.getIsEvent()){
    	    dateInfo =  " from " + task.getStartTime() +" to "+ task.getEndTime();
    	}else{
    	    dateInfo = "";
    	}
    	dateTime.setText(dateInfo);
        title.setText(task.getName().fullName);
        id.setText(displayedIndex + ". ");
        tags.setText(task.tagsString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
