package Utils;

import javafx.scene.control.Label;

import java.io.*;

public class Help {
    private static String displayText;


    public static void displayCommands(Label label) throws IOException {
        displayText="";
        BufferedReader in = new BufferedReader(new FileReader(Variables.DEFAULT_SKILL_PARSER_FILE_PATH+"help.txt"));
        String line;
        while ((line = in.readLine()) != null) {
            displayText=displayText+" \n"+ line;
            label.setText(displayText);

        }

    }

}
