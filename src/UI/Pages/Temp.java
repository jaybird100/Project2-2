package UI.Pages;

import UI.Pages.MainPage.Message;
import UI.Pages.MainPage.MessagingBoard;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class Temp extends Page {
    public final Parent root;

    public final MessagingBoard pane;
    public Temp(){
        pane = new MessagingBoard();
        pane.keep = 10;
        root = new VBox(new Label("A JavaFX Label"));
        ((VBox) root).getChildren().add(pane);
    }

    @Override
    public void init(){
        scene.getStylesheets().addAll("UI/CSS/theme.css", "UI/CSS/mainPage.css");
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.A){
                    String t= "WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@W\"WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@W\"WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@W";
                    pane.addMessage(new Message(t, "me"), true);
                }
                if(event.getCode() == KeyCode.S){
                    String t= "WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@W\"WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@W\"WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@WEWEWE@W";
                    pane.addMessage(new Message(t, "not me"), false);
                }
            }
        });
    }
}
