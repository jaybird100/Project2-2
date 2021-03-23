package UI.Pages.PopUpWindows;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HelpWindow {
    public static void open(){
        Group root = new Group();
        Stage hint = new Stage();
        hint.setTitle("???");
        Label explain = new Label("Displays the entire calendar");
        explain.setTranslateX(20);
        explain.setTranslateY(20);
        root.getChildren().add(explain);

        Scene scene = new Scene(root, 200, 100);
        scene.getStylesheets().add("UI/CSS/theme.css");
        hint.setScene(scene);
        hint.setX(700);
        hint.setY(250);
        hint.show();
    }
}
