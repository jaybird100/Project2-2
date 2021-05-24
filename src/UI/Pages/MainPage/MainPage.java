package UI.Pages.MainPage;

import CFG.InputParser;
import CFG.Match;
import UI.PageController;
import UI.Pages.Page;
import UI.Pages.PopUpWindows.Calculator;
import UI.Pages.PopUpWindows.HelpWindow;
import UI.Pages.PopUpWindows.SkillEditor;
import Utils.Parser;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;

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
    Button loadCFGButton;
    @FXML
    Button createSkillButton;
    @FXML
    Button calendarButton;
    @FXML
    Button helpButton;
    @FXML
    Button calculatorButton;
    @FXML
    Button backButton;
    MessagingBoard board;

    String previous ="";
    String previousUser="";
    public void show(){
        if(logTextField.getChildren().get(0) instanceof MessagingBoard && PageController.getInstance().username.equals(previousUser)) {
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
        previousUser = PageController.getInstance().username;
        usernameLabel.setText(PageController.getInstance().username+"'s Personal Assistant");
    }

    @FXML
    protected void sendButton(Event event){
        if (event.getSource() == sendButton ||
                (event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.ENTER)){
            previous = askTextField.getText();
            if(askTextField.getText().equals("")){
                return;
            }
            Message m = new Message(askTextField.getText(), PageController.getInstance().username, LocalDateTime.now());
            ((MessagingBoard)logTextField.getChildren().get(0)).addMessage(m, true);
            askTextField.setText("");
            PageController.getInstance().log.add(m);
            String response = Parser.parse(m.message);
            m = new Message(response, "Karen");
            ((MessagingBoard) logTextField.getChildren().get(0)).addMessage(m, false);
            PageController.getInstance().log.add(m);
        }
        if(event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.UP){
            askTextField.setText(previous);
            askTextField.positionCaret(previous.length());
        }
    }

    @FXML
    protected void loadCFGButton(Event event) throws IOException {
        PageController.scene("skillcreation");
    }
    @FXML
    protected void backButton(Event event) throws IOException{
        PageController.scene("frontpage");
    }


    @FXML
    protected void createSkillButton(Event event) throws IOException{
        SkillEditor.open();
    }
    @FXML
    protected void calendarButton(Event event) throws IOException{
        PageController.scene("calendar");
    }
    @FXML
    protected void helpButton(Event event){
        HelpWindow.open();
    }

    @FXML
    protected void calculatorButton(Event event){
        Calculator.open();
    }
}
