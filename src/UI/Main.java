package UI;

import CFG.InputParser;
import Utils.Data;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class Main extends Application {

    final int windowWidth= 700;
    final int windowHeight = 500;

    PageController controller;
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Virtual Assistant");
        controller = PageController.createInstance(primaryStage, windowWidth, windowHeight);
    }
    public static void main(String[] args) throws IOException {
        //premptive preparation of data for query answering
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
