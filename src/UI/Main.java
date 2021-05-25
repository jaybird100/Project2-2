package UI;

import Utils.Data;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    public final int windowWidth= 700;
    public final int windowHeight = 500;
    public static Label title;
    private static String username;

    PageController controller;
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Virtual Assistant");
        controller = PageController.createInstance(primaryStage, windowWidth, windowHeight);
    }

    public static void main(String[] args) throws IOException {
        //premptive preparation of data for query answering
        setUsername();
        Data.fillData();
        launch(args);
    }

    private static void setUsername(){
        File f = new File("src/UserInfo.txt");
        username="";
        try {
            Scanner scanner = new Scanner(f);
            username = scanner.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void changeUser(String username){
        Main.username = username;
    }

    public static String username(){
        return username;
    }
}
