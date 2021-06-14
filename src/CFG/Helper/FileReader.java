package CFG.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileReader {
    /**
     * Loads the file that has been passed through
     * @param file path
     * @return input of the file as a String
     */
    public static String loadFile(File file){
        return usingBufferedReader(file);
    }
    private static String usingBufferedReader(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file)))
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
