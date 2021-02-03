package sample;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class Today extends CalenderLectures{
    public Today(){
        keywords.add("Today");
        wordPlacements.add(1);
    }
    @Override
    String action(String[] input) throws ParseException {
        ArrayList<Lecture> l = Data.searchDates(Data.date);
        String toPrint = "";
        for (Lecture e : l) {
            toPrint += e.toString() + '\n';
        }
        return toPrint;
    }
}
