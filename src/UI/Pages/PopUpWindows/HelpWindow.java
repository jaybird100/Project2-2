package UI.Pages.PopUpWindows;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelpWindow {
    public static void open(){
        VBox root = new VBox();
        Stage hint = new Stage();
        hint.setTitle("Help");
        Label explain = new Label("Displays the entire calendar");
        Label t1 = new Label("Enter \"ff y\" or \"ff n\" to turn on or off autocorrect and logic parsing features");
        root.getChildren().add(explain);
        root.getChildren().add(t1);

        Scene scene = new Scene(root, 500, 200);
        scene.getStylesheets().add("UI/CSS/theme.css");
        hint.setScene(scene);
        hint.setX(700);
        hint.setY(250);
        hint.show();
    }
}
