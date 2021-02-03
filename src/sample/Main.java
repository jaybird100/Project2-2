package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.ParseException;


public class Main extends Application {
    final int windowWidth= 700;
    final int windowHeight = 500;

    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project 2-2");
        primaryStage.initStyle(StageStyle.DECORATED);
        TextField textField = new TextField();
        textField.setPrefWidth(windowWidth-100);
        Button button = new Button("Enter");
        Label label = new Label("");
        label.setWrapText(true);
        button.setOnAction(action -> {
            try {
                label.setText(Data.p.parse(textField.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });
        textField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                try {
                    label.setText(Data.p.parse(textField.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        ScrollPane sp = new ScrollPane();
        sp.setContent(label);
        GridPane root = new GridPane();
        root.add(textField,0,0,1,5);
        root.add(button,6,0,1,3);
        root.add(sp,0,3,9,5);
        primaryStage.setScene(new Scene(root,windowWidth,windowHeight));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        Data.fillData();
        launch(args);
    }


}
