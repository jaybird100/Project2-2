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

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import FacialRecognition.*;

public class Main extends Application {
    public final int windowWidth= 700;
    public final int windowHeight = 500;
    public static String botName = "Karen";
    public static Label title;
    private static String username;

    PageController controller;
    private Group root;
    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        checkForFace(primaryStage);
    }

    public void startCFGAgent(Stage primaryStage) {
        primaryStage.setTitle("Virtual Assistant");
        try {
            controller = PageController.instance(primaryStage, windowWidth, windowHeight);
            PageController.init();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void checkForFace(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, windowWidth * 3 / 4, windowHeight / 2, Color.LAVENDER);

        Stage findFace = new Stage();
        findFace.setTitle("First, a Human Check");
        Label question1 = new Label("First, we need to check if there's a person using the Assistant.");
        Label question2 = new Label("Click on Enter when you're ready to switch on the camera.");
        question1.setLayoutX(20);
        question1.setLayoutY(30);
        root.getChildren().add(question1);
        question2.setLayoutX(20);
        question2.setLayoutY(50);
        root.getChildren().add(question2);

        // COMMENT here to switch between face detection methods
        //FaceDetector faceDetector = new ClassifierFaceDetector();
        FaceDetector faceDetector = new YOLOFaceDetector();

        scene.setOnKeyPressed((KeyEvent ENTER) -> {
            if (ENTER.getCode().equals(KeyCode.ENTER)) {
                //System.out.println("kek");
                //startCFGAgent(primaryStage);
                faceDetector.init(primaryStage);
                faceDetector.findFace();
                if(faceDetector.isFaceFound()) {
                    //Group root1 = new Group();
                    //primaryStage.setTitle("Virtual Assistant");
                    //findName(primaryStage);
                    //title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
                    //root1.getChildren().add(title);
                    //title.setLayoutX(180);
                    //title.setLayoutY(20);
                    //title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));
                    startCFGAgent(new Stage());
                    //Scene scene1 = new Scene(root1, windowWidth, windowHeight, Color.LAVENDER);
                    //skillEditor = new SkillEditor(primaryStage, root1);
                    //primaryStage.setScene(scene1);
                }
            }
        });

        Button enter = new Button("YOLOv4");
        root.getChildren().add(enter);
        //enter.setGraphic(new ImageView(new Image("Skins/Next.jpg", 25, 25, true, true)));
        enter.setLayoutX(135);
        enter.setLayoutY(75);
        enter.setOnAction(e -> {
            //System.out.println("lol");
            //startCFGAgent(primaryStage);
            faceDetector.init(primaryStage);
            faceDetector.findFace();
            if(faceDetector.isFaceFound()) {
                //Group root1 = new Group();
                //primaryStage.setTitle("Virtual Assistant");
                //findName(primaryStage);
                //title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
                //root1.getChildren().add(title);
                //title.setLayoutX(180);
                //title.setLayoutY(20);
                //title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));
                startCFGAgent(new Stage());
                //Scene scene1 = new Scene(root1, windowWidth, windowHeight, Color.LAVENDER);
                //skillEditor = new SkillEditor(primaryStage, root1);
                //primaryStage.setScene(scene1);
            }
        });
        Button enter1 = new Button("HoG into SVM");
        FaceDetector faceDetector1 = new HOGFaceDetector();
        root.getChildren().add(enter1);
        //enter.setGraphic(new ImageView(new Image("Skins/Next.jpg", 25, 25, true, true)));
        enter1.setLayoutX(135);
        enter1.setLayoutY(105);
        enter1.setOnAction(e -> {
            //System.out.println("lol");
            //startCFGAgent(primaryStage);
            faceDetector1.init(primaryStage);
            faceDetector1.findFace();
            if(faceDetector1.isFaceFound()) {
                //Group root1 = new Group();
                //primaryStage.setTitle("Virtual Assistant");
                //findName(primaryStage);
                //title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
                //root1.getChildren().add(title);
                //title.setLayoutX(180);
                //title.setLayoutY(20);
                //title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));
                startCFGAgent(new Stage());
                //Scene scene1 = new Scene(root1, windowWidth, windowHeight, Color.LAVENDER);
                //skillEditor = new SkillEditor(primaryStage, root1);
                //primaryStage.setScene(scene1);
            }
        });
        Button enter2 = new Button("PCA into MLP");
        root.getChildren().add(enter2);
        //enter.setGraphic(new ImageView(new Image("Skins/Next.jpg", 25, 25, true, true)));
        enter2.setLayoutX(135);
        enter2.setLayoutY(145);
        enter2.setOnAction(e -> {
            //System.out.println("lol");
            //startCFGAgent(primaryStage);
            faceDetector1.init(primaryStage);
            faceDetector1.findFace();
            if(faceDetector1.isFaceFound()) {
                //Group root1 = new Group();
                //primaryStage.setTitle("Virtual Assistant");
                //findName(primaryStage);
                //title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
                //root1.getChildren().add(title);
                //title.setLayoutX(180);
                //title.setLayoutY(20);
                //title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));
                startCFGAgent(new Stage());
                //Scene scene1 = new Scene(root1, windowWidth, windowHeight, Color.LAVENDER);
                //skillEditor = new SkillEditor(primaryStage, root1);
                //primaryStage.setScene(scene1);
            }
        });

        findFace.setScene(scene);
        findFace.setX(windowWidth / 2);
        findFace.setY(windowHeight / 2);
        findFace.show();
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
