package sample;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class LecturesInDays extends CalenderLectures{
    public LecturesInDays(){
        keywords.add("in");
        wordPlacements.add(1);
        keywords.add("days");
        wordPlacements.add(3);
    }
    @Override
    String action(String[] input) throws ParseException {
        int days = Integer.parseInt(input[2]);
        String dt = Data.date;  // Start date
        Calendar cal = Calendar.getInstance();
        cal.setTime(Data.formatter.parse(dt));
        cal.add(Calendar.DATE, days);  // number of days to add
        dt = Data.formatter.format(cal.getTime());
        ArrayList<Lecture> l = Data.searchDates(dt);
        String toPrint = "";
        for (Lecture e : l) {
            toPrint += e.toString() + '\n';
        }
        return toPrint;
    }
}
