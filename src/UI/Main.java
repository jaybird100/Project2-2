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

import java.io.*;
import java.util.Arrays;
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
        primaryStage.setTitle("Virtual Assistant");

        findName(primaryStage);
        title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
        root.getChildren().add(title);
        title.setLayoutX(180);
        title.setLayoutY(20);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18.0));

        Scene scene = new Scene(root, windowWidth, windowHeight, Color.LAVENDER);
        skillEditor = new SkillEditor(primaryStage, root);

        /* TODO parts for face recognition
        WritableImage writableImage = captureFrame();
        saveImage();
        ImageView imageView = new ImageView(writableImage);
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
*/

        primaryStage.setScene(scene);

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
        }else{
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
