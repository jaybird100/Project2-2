package UI.MainPage;

import CFG.Action;
import CFG.InputParser;
import CFG.Match;
import UI.Page;
import UI.PageController;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MainPage extends Page {
    @FXML
    Label usernameLabel;
    @FXML
    VBox logTextField;
    @FXML
    TextField askTextField;
    @FXML
    Button sendButton;
    @FXML
    Button createSkillButton;
    @FXML
    Button backButton;

    MessagingBoard board;

    public void show(){
        if(logTextField.getChildren().get(0) instanceof MessagingBoard) {
            ((MessagingBoard) logTextField.getChildren().get(0)).getVBox().getChildren().clear();
        }else{
            board = new MessagingBoard();
            board.setPrefViewportWidth(1000);
            board.setPrefViewportHeight(1000);
            board.keep = 20;
            logTextField.getChildren().add(0, board);
        }
        if(PageController.getInstance().username.equals("")){
            PageController.getInstance().username ="User";
        }
        usernameLabel.setText(PageController.getInstance().username);
    }

    @FXML
    protected void sendButton(Event event){
        if (event.getSource() == sendButton ||
                (event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.ENTER)){
            if(askTextField.getText().equals("")){
                return;
            }
            Message m = new Message(askTextField.getText(), PageController.getInstance().username, LocalDateTime.now());
            ((MessagingBoard)logTextField.getChildren().get(0)).addMessage(m, true);
            askTextField.setText("");
            PageController.getInstance().log.add(m);
            List<Match> responses = InputParser.parse(m.message);
            if(responses.size()!=0 && responses.get(0).isValid(2)) {
                m = new Message(responses.get(0).getResponse(), "Karen");
                ((MessagingBoard) logTextField.getChildren().get(0)).addMessage(m, false);
                PageController.getInstance().log.add(m);
            }
        }
    }

    @FXML
    protected void skillCreationButton(Event event) throws IOException {
        PageController.scene("skillcreation");
    }
    @FXML
    protected void backButton(Event event) throws IOException{
        PageController.scene("frontpage");
    }
}
