package UI;

import CFG.CFGSystem;
import Utils.Data;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {
    public final int windowWidth= 700;
    public final int windowHeight = 500;
    public static String botName = "Karen";
    public static Label title;
    private static String username;

    PageController controller;
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Virtual Assistant");
        controller = PageController.instance(primaryStage, windowWidth, windowHeight);
        PageController.init();
    }

    public static void main(String[] args) throws IOException {
        //premptive preparation of data for query answering
        setUsername();
        Data.fillData();
        CFGSystem.load(new File("src/SkillParserFiles/CFGSkill.txt"));
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
        try {
            FileWriter writer = new FileWriter("src/UserInfo.txt");
            writer.write(username);
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static String username(){
        return username;
    }
}
