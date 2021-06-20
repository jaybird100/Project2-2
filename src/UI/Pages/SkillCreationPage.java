package UI.Pages;

import CFG.CNForm.CNFConverter;
import CFG.Helper.FileReader;
import CFG.CFGSystem;
import UI.PageController;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SkillCreationPage extends Page {

    @FXML
    GridPane gridPane;
    @FXML
    Button backButton;
    @FXML
    Button loadButton;
    @FXML
    Button saveButton;
    @FXML
    TextArea skillTextArea;

    @FXML
    protected void backButton(Event event) throws IOException {
        PageController.scene("mainpage");
    }

    @FXML
    protected void saveButton(Event event){
        CNFConverter.loadAsCNF(skillTextArea.getText(), CFGSystem.dataBase);
        skillTextArea.clear();

        Label l = new Label("Added");
        l.setStyle("-fx-text-fill: green");
        l.setFont(Font.font(20));
        long curTime = System.currentTimeMillis();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = (System.currentTimeMillis()-curTime)/(double)1000;
                if(t>=1.5) {
                    gridPane.getChildren().remove(l);
                    this.stop();
                }
            }
        };
        timer.start();
        gridPane.add(l, 2, 0);
    }

    @FXML
    protected void loadButton(Event event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        //fileChooser.getExtensionFilters().add(extFilter);
        File f = fileChooser.showOpenDialog(PageController.instance().stage);
        if(f!=null && f.canRead()){
            String fileString = FileReader.loadFile(f);
            skillTextArea.setText(fileString);
        }
    }
}
