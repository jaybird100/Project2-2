package UI.Pages;

import CFG.FileParser;
import UI.PageController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;

public class SkillCreationPage extends Page {

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
        FileParser.addSkillRegex(skillTextArea.getText());
        skillTextArea.clear();
    }

    @FXML
    protected void loadButton(Event event) throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        //fileChooser.getExtensionFilters().add(extFilter);
        File f = fileChooser.showOpenDialog(PageController.getInstance().stage);
        if(f!=null && f.canRead()){
            String fileString = usingBufferedReader(f);
            skillTextArea.setText(fileString);
        }
    }

    // Utils
    public static String usingBufferedReader(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
