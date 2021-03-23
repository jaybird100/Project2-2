package UI;

import UI.Pages.Calendar.Calendar;
import UI.Pages.MainPage.Message;
import UI.Pages.Page;
import UI.Pages.Temp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class PageController {

    public String username="";
    public List<Message> log = new ArrayList<>();

    private static PageController instance;

    public final Stage stage;
    final int windowWidth;
    final int windowHeight;
    HashMap<String, Page> controllers = new HashMap<>();

    public static PageController createInstance(Stage primaryStage, int windowWidth, int windowHeight) throws IOException {
        instance = new PageController(primaryStage, windowWidth, windowHeight);
        init();
        return instance;
    }
    private static void init() throws IOException {
        File f = new File("src/UserInfo.txt");
        if(!f.isFile()) {
            getInstance().setScene("frontpage");
        }else{
            try {
                Scanner scanner = new Scanner(f);
                getInstance().username= scanner.nextLine();
                getInstance().setScene("mainpage");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static PageController getInstance(){
        return instance;
    }

    public static void scene(String scene) throws IOException {
        instance.setScene(scene);
    }

    private PageController(Stage primaryStage, int windowWidth, int windowHeight) throws IOException {
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
        stage = primaryStage;
    }

    private void setScene(String scene) throws IOException {
        scene = scene.toLowerCase();
        if(!controllers.containsKey(scene)){
            if(Helper.in(scene, new String[]{"frontpage", "mainpage", "skillcreation"})){
                getSceneByFXML(scene);
            }
            else{
                switch (scene){
                    case "temp":
                        Temp t = new Temp();
                        init(scene, t.root, t);
                        break;
                    case "calendar":
                        Calendar c = new Calendar();
                        init(scene, c.root, c);
                        break;
                }
            }
        }
        Page controller = controllers.get(scene);
        assert controller!=null;
        controller.show();
        stage.setScene(controller.scene);
        stage.show();
    }

    private void getSceneByFXML(String scene) throws IOException {
        String path ="";
        switch(scene){
            case "frontpage":
                path = "/UI/FXML/FrontPage.fxml";
                break;
            case "mainpage":
                path = "/UI/FXML/MainPage.fxml";
                break;
            case "skillcreation":
                path = "/UI/FXML/skillCreationPage.fxml";
                break;
        }
        assert !path.equals("");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        init(scene, loader.load(), loader.getController());
    }

    private void init(String scene, Parent root, Page controller){
        controllers.put(scene, controller);
        controllers.get(scene).scene = new Scene(root, windowWidth, windowHeight);
        controller.init();
    }
}
