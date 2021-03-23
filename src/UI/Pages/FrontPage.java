package UI.Pages;

import UI.PageController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;


public class FrontPage extends Page {
    @FXML
    Button mainPageButton;
    @FXML
    TextField usernameTextField;

    @FXML
    public void onButtonPress(Event event) throws IOException {
        if (event.getSource() == mainPageButton ||
                (event instanceof KeyEvent && ((KeyEvent) event).getCode() == KeyCode.ENTER)){
            PageController.getInstance().username = usernameTextField.getText();
            PageController.scene("mainpage");
        }
    }

    public void show() {
        mainPageButton.requestFocus();
    }
}