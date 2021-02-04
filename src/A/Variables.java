package A;

import java.awt.*;
import java.util.Date;

public class Variables {


    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double SCREEN_WIDTH= screenSize.width;
    public static final double SCREEN_HEIGHT = screenSize.height;


    public static final String FILE_SEPARATOR =System.getProperty("file.separator");
    public static final String DEFAULT_TEXT_FILE_PATH = "2"+FILE_SEPARATOR+"diffApp"+FILE_SEPARATOR+"txtFiles"+FILE_SEPARATOR;
    public static final String DEFAULT_CLASS_FILE_PATH = "2"+FILE_SEPARATOR+"diffApp"+FILE_SEPARATOR+"classFiles"+FILE_SEPARATOR;
    public static final String DEFAULT_CSV_FILE_PATH = "2"+FILE_SEPARATOR+"diffApp"+FILE_SEPARATOR+"CSVFiles"+FILE_SEPARATOR;
    public static Date currentTime (){
        return new Date(System.currentTimeMillis());
    }
    //0 day of week(abb)
    //1 month
    //2 day of month
    //3 time hrs:min:sec
    //4 time zone
    //5 year
    public static String[] getDateInfo(){
        return currentTime().toString().split(" ");
    }


    public static final String [] SKILL_KEYWORDS= new String []{"lecture"};
    public static final String [] FUNCTION_KEYWORDS= new String []{"on"};


}

