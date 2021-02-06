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
import java.util.Arrays;

public class Main extends Application {

    final int windowWidth= 700;
    final int windowHeight = 500;


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
            label.setText(Parser.parse(textField.getText()));

        });
        textField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                label.setText(Parser.parse(textField.getText()));
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
        System.out.println(Arrays.deepToString(Data.commands.toArray()));
        System.out.println(Arrays.deepToString(Data.toCall.toArray()));
        System.out.println(Arrays.deepToString(Data.objectsFromTxt.toArray()));
        System.out.println(Arrays.deepToString(Data.limiters.toArray()));
        System.out.println(Arrays.deepToString(Data.attributeIndexes.toArray()));
        //System.out.println(Arrays.deepToString(Data.lectures.toArray()));
        launch(args);
    }
}
