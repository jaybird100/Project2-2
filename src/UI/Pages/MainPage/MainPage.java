package UI.Pages.MainPage;

import UI.Main;
import UI.PageController;
import UI.Pages.Page;
import UI.Pages.PopUpWindows.Calculator;
import UI.Pages.PopUpWindows.HelpWindow;
import UI.Pages.PopUpWindows.SkillEditor;
import Utils.Data;
import Utils.Parser;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainPage extends Page {
    @FXML
    Label usernameLabel;
    @FXML
    VBox messagingBoardHolder;
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
    @FXML
    TextArea reminderArea;
    MessagingBoard board;

    String previousUser="";
    int i=0;
    public void show(){
        reminderArea.setText(Data.checkNotifications(Data.checkNotif));
        if(messagingBoardHolder.getChildren().get(0) instanceof MessagingBoard) {
            if(!Main.username().equals(previousUser)) {
                ((MessagingBoard) messagingBoardHolder.getChildren().get(0)).getVBox().getChildren().clear();
            }
        }else{
            board = new MessagingBoard();
            board.setPrefViewportWidth(1000);
            board.setPrefViewportHeight(1000);
            board.keep = 20;
            messagingBoardHolder.getChildren().add(0, board);
        }
        if(Main.username().equals("")){
            Main.changeUser("User");
        }
        previousUser = Main.username();
        usernameLabel.setText(Main.username()+"'s Personal Assistant");
    }

    @FXML
    protected void sendButton(Event event){
        if (event.getSource() == sendButton ||
                (event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.ENTER)){
            i=0;
            if(askTextField.getText().equals("")){
                return;
            }
            Message m = new Message(askTextField.getText(), Main.username(), LocalDateTime.now());
            ((MessagingBoard)messagingBoardHolder.getChildren().get(0)).addMessage(m, true);
            askTextField.setText("");
            PageController.getInstance().log.add(m);
            String response = Parser.parse(m.message);
            m = new Message(response, Main.botName);
            ((MessagingBoard) messagingBoardHolder.getChildren().get(0)).addMessage(m, false);
            PageController.getInstance().log.add(m);
        }
        if(event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.UP){
            i = Math.min(i+1, board.messageList.size());
            String p = board.getPrevious(i, Main.username());
            askTextField.setText(p);
            askTextField.positionCaret(p.length());
        }
        if(event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.DOWN){
            i = Math.max(i-1, 0);
            String p = board.getPrevious(i, Main.username());
            askTextField.setText(p);
            askTextField.positionCaret(p.length());
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
