package UI;

import Utils.Data;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    public final int windowWidth= 700;
    public final int windowHeight = 500;
    public String username;
    private Group root;
    //Mat matrix = null;
    SkillEditor skillEditor;
    public static Label title;

    public static void main(String[] args) throws IOException {
        //premptive preparation of data for query answering
        Data.fillData();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        checkForFace(primaryStage);
    }

    public void checkForFace(Stage primaryStage){
        Group root = new Group();
        Scene scene = new Scene(root, windowWidth / 2, windowHeight / 2, Color.LAVENDER);

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
        FaceDetector faceDetector = new HOGFaceDetector();

        scene.setOnKeyPressed((KeyEvent ENTER) -> {
            if (ENTER.getCode().equals(KeyCode.ENTER)) {
                faceDetector.init(primaryStage);
                faceDetector.findFace();
                if(faceDetector.isFaceFound()) {
                    findFace.close();
                    Group root1 = new Group();
                    primaryStage.setTitle("Virtual Assistant");
                    findName(primaryStage);
                    title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
                    root1.getChildren().add(title);
                    title.setLayoutX(180);
                    title.setLayoutY(20);
                    title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));

                    Scene scene1 = new Scene(root1, windowWidth, windowHeight, Color.LAVENDER);
                    skillEditor = new SkillEditor(primaryStage, root1);
                    primaryStage.setScene(scene1);
                }
            }
        });

        Button enter = new Button("ENTER");
        root.getChildren().add(enter);
        //enter.setGraphic(new ImageView(new Image("Skins/Next.jpg", 25, 25, true, true)));
        enter.setLayoutX(135);
        enter.setLayoutY(75);
        enter.setOnAction(e -> {
            faceDetector.init(primaryStage);
            faceDetector.findFace();
            if(faceDetector.isFaceFound()) {
                findFace.close();
                Group root1 = new Group();
                primaryStage.setTitle("Virtual Assistant");
                findName(primaryStage);
                title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
                root1.getChildren().add(title);
                title.setLayoutX(180);
                title.setLayoutY(20);
                title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));

                Scene scene1 = new Scene(root1, windowWidth, windowHeight, Color.LAVENDER);
                skillEditor = new SkillEditor(primaryStage, root1);
                primaryStage.setScene(scene1);
            }
        });

        findFace.setScene(scene);
        findFace.setX(windowWidth / 2);
        findFace.setY(windowHeight / 2);
        findFace.show();

    }


    public static boolean doesTextExist(File file){
        try {
            Scanner scanner = new Scanner(file);
            return scanner.hasNext();
        } catch(FileNotFoundException e) {
            System.out.println("No file exists");
            return false;
        }
    }

    public void findName(Stage primaryStage){
        if(!doesTextExist(new File("name.txt"))) {
            Group root = new Group();
            Stage getName = new Stage();
            getName.setTitle("An important question");

            Label question = new Label("What would you like to be called?");
            question.setLayoutX(30);
            question.setLayoutY(30);
            root.getChildren().add(question);

            TextField name = new TextField();
            name.setPrefWidth(100);
            name.setLayoutX(30);
            name.setLayoutY(50);
            root.getChildren().add(name);

            Button enter = new Button();
            enter.setGraphic(new ImageView(new Image("Skins/Next.jpg", 25, 25, true, true)));
            enter.setLayoutX(135);
            enter.setLayoutY(50);
            enter.setOnAction(e -> {
                try {
                    FileWriter writer = new FileWriter("name.txt");
                    writer.write(name.getText());
                    writer.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                title.setText(name.getText() + "'s Personal Assistant");
                getName.close();
                primaryStage.show();

            });
            root.getChildren().add(enter);

            Scene scene = new Scene(root, windowWidth / 2, windowHeight / 2, Color.LAVENDER);
            scene.setOnKeyPressed((KeyEvent ENTER) -> {
                if (ENTER.getCode().equals(KeyCode.ENTER)) {
                    try {
                        FileWriter writer = new FileWriter("name.txt");
                        writer.write(name.getText());
                        writer.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    title.setText(name.getText() + "'s Personal Assistant");
                    getName.close();
                    primaryStage.show();
                }

            });
            getName.setScene(scene);
            getName.setX(windowWidth / 2);
            getName.setY(windowHeight / 2);
            getName.show();
        }
        else{
            try {
                Scanner scanner = new Scanner(new File("name.txt"));
                username= scanner.nextLine();
                primaryStage.show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
