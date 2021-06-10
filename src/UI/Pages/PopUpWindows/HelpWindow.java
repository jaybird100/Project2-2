package UI.Pages.PopUpWindows;

import CFG.CFGSystem;
import Utils.Data;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HelpWindow {
    public static void open() {
        Pane root = new Pane();
        Stage hint = new Stage();
        hint.setTitle("Command Help");
        TextArea explain = new TextArea();
        StringBuilder ex = new StringBuilder();
        for (int i = 0; i < Data.commands.size(); i++) {
            ex.append(Data.commands.get(i)).append("\n");
        }
        for (String command : CFGSystem.commands) {
            ex.append(command).append("\n");
        }
        explain.setText(ex.toString());
        explain.setTranslateX(20);
        explain.setTranslateY(20);
        explain.setPrefWidth(360);
        explain.setPrefHeight(340);
        explain.setEditable(false);
        root.getChildren().add(explain);

        hint.setScene(new Scene(root, 400, 400));
        hint.setX(400);
        hint.setY(250);
        hint.getScene().getStylesheets().add("UI/CSS/theme.css");
        hint.show();
    }
}
