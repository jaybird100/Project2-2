package UI;

import CFG.FileParser;
import UI.MainPage.Message;
import UI.Temp.Temp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return instance;
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
        stage.setTitle("Project 2-2");
        setScene("frontpage");
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
                path = "/UI/FrontPage/FrontPage.fxml";
                break;
            case "mainpage":
                path = "/UI/MainPage/MainPage.fxml";
                break;
            case "skillcreation":
                path = "/UI/SkillCreationPage/skillCreationPage.fxml";
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
