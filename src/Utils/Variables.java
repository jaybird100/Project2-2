package Utils;

import java.awt.*;

public class Variables {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double SCREEN_WIDTH= screenSize.width;
    public static final double SCREEN_HEIGHT = screenSize.height;


    public static final String FILE_SEPARATOR =System.getProperty("file.separator");
    public static final String DEFAULT_CSV_FILE_PATH = "src"+FILE_SEPARATOR+"CSVFiles"+FILE_SEPARATOR;


}
