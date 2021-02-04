package A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Day extends Attribute<Date> {
    static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yy E");
    static String[] days = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    static Day today = new Day(Variables.currentTime());
    public Day(Date data) {
        super(data);
    }
    public Day(String time, String date, String day) throws ParseException {
        super(formatter.parse(time+" "+date+" "+day));
    }
}
