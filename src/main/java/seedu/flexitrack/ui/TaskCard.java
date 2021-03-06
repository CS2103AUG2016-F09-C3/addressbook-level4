package seedu.flexitrack.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.flexitrack.model.task.ReadOnlyTask;

public class TaskCard extends UiPart {

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label title;
    @FXML
    private Label id;
    @FXML
    private Label dateTime;

    private ReadOnlyTask task;
    private int displayedIndex;

    public TaskCard() {

    }

    public static TaskCard load(ReadOnlyTask task, int displayedIndex) {
        TaskCard card = new TaskCard();
        card.task = task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }
    
    //@@author A0147092E
    @FXML
    public void initialize() {
        String dateInfo;
        if (task.getIsTask()) {
            dateInfo = " by " + task.getDueDate();
            if (task.getIsDone()){
                cardPane.setStyle("-fx-background-color: #78AB46");
            } else {
                cardPane.setStyle("-fx-background-color: #ffa54f");
            }
        } else if (task.getIsEvent()) {
            dateInfo = " from " + task.getStartTime() + " to " + task.getEndTime();
            if (task.getIsDone()) { 
                cardPane.setStyle("-fx-background-color: #78AB46");
            } else {
                cardPane.setStyle("-fx-background-color: #cd8500");
            }
        } else {
            dateInfo = "";
            if (task.getIsDone()) {
                cardPane.setStyle("-fx-background-color: #78AB46");
            } else {
                cardPane.setStyle("-fx-background-color: #FFFFFF");
            }
        }
        dateTime.setText(dateInfo);
        title.setText(task.getName().toString());
        id.setText(displayedIndex + ". ");
    }
    //@@author

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
