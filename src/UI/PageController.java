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
        String skill = "RULE    <S>:    <ACTION>\n" +
                "RULE   <ACTION>:   <LOCATION> | <SCHEDULE>\n" +
                "RULE   <SCHEDULE>: WHICH LECTURES ARE THERE <TIMEEXPRESSION> | <TIMEEXPRESSION> WHICH LECTURES ARE THERE\n"+
                "RULE   <TIMEEXPRESSION>:   ON <DAY> AT <TIME> | AT <TIME> ON <DAY>\n" +
                "RULE   <TIME>: 12 | 9 \n" +
                "RULE   <LOCATION>: WHERE IS <ROOM> | HOW DO <PRO> GET TO <ROOM> | WHERE IS <ROOM> LOCATED\n" +
                "RULE   <PRO>: I | YOU | HE | SHE\n" +
                "RULE   <ROOM>: DEEPSPACE | SPACEBOX\n" +
                "RULE   <DAY>: MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY | SUNDAY\n" +
                "Action <SCHEDULE> *,  <DAY>  Saturday|Sunday :  There are no lectures on Saturday\n" +
                "Action <SCHEDULE> *,  <DAY>  Monday, <TIME> 9 : We start the week with math\n" +
                "Action <SCHEDULE> *,  <DAY>  Monday, <TIME> 12: On Monday noon we have Theoratical Computer Science\n" +
                "Action <LOCATION> *,  <ROOM> DeepSpace : DeepSpace is the first room after the entrance\n" +
                "Action <LOCATION> * : <ROOM> is in the first floor\n" +
                "Action : I have no idea\n" +
                "Action <GREETING> *: Hello :)";

        FileParser.addSkillRegex(skill);
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
