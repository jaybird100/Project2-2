package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {
    Data d = new Data();
    final int windowWidth= 800;
    final int windowHeight = 400;

    public Main() throws IOException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Project 2-2");
        primaryStage.initStyle(StageStyle.DECORATED);
        TextField textField = new TextField();
        textField.setPrefWidth(windowWidth-100);
        Button button = new Button("Enter");
        Label label = new Label("");
        label.setWrapText(true);
        button.setOnAction(action -> {
            d.setPrint(textField.getText());
            label.setText(Data.toPrint);
        });
        GridPane root = new GridPane();
        root.add(textField,0,0,1,5);
        root.add(button,6,0,1,3);
        root.add(label,0,3,9,5);
        primaryStage.setScene(new Scene(root,windowWidth,windowHeight));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}