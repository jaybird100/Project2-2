package sample;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class Yesterday extends CalenderLectures {
    public Yesterday(){
        keywords.add("Yesterday");
        wordPlacements.add(1);
    }
    @Override
    String action(String[] input) throws ParseException {
        String dt = Data.date;  // Start date
        Calendar cal = Calendar.getInstance();
        cal.setTime(Data.formatter.parse(dt));
        cal.add(Calendar.DATE, -1);  // number of days to add
        dt = Data.formatter.format(cal.getTime());
        ArrayList<Lecture> l = Data.searchDates(dt);
        String toPrint = "";
        for (Lecture e : l) {
            toPrint += e.toString() + '\n';
        }
        return toPrint;
    }
}
