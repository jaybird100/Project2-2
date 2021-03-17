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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class Main extends Application {

    public final int windowWidth= 700;
    public final int windowHeight = 500;
    public String username;
    private Group root;
    //Mat matrix = null;
    SkillEditor skillEditor;
    Label title;

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

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = new Group();
        primaryStage.setTitle("Virtual Assistant");

        findName(primaryStage);
        title = new Label(username + "'s Personal Assistant"); //it doesn't work without this for some reason
        root.getChildren().add(title);
        //title.setPrefSize(primaryStage.getWidth(), 50);
        //title.setTextAlignment(TextAlignment.CENTER);
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

   public void findName(Stage primaryStage){
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
           title.setText(name.getText() + "'s Personal Assistant");
           getName.close();
           primaryStage.show();

       });
       root.getChildren().add(enter);

       Scene scene = new Scene(root, windowWidth/2, windowHeight/2, Color.LAVENDER);
       getName.setScene(scene);
       getName.setX(windowWidth/2);
       getName.setY(windowHeight/2);

       getName.show();
   }

}
