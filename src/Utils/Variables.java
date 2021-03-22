package Utils;

import java.awt.*;
import java.util.Calendar;

public class Variables {
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double SCREEN_WIDTH= screenSize.width;
    public static final double SCREEN_HEIGHT = screenSize.height;
    public static final String USER_HOME_PATH=System.getProperty("user.home");


    public static final String FILE_SEPARATOR =System.getProperty("file.separator");
    public static final String DEFAULT_CSV_FILE_PATH = "src"+FILE_SEPARATOR+"CSVFiles"+FILE_SEPARATOR;
    public static final String DEFAULT_SKILL_PARSER_FILE_PATH = "src"+FILE_SEPARATOR+"SkillParserFiles"+FILE_SEPARATOR;





}
