package sample;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LecturesOn extends CalenderLectures{
    public LecturesOn(){
        keywords.add("on");
        wordPlacements.add(1);
    }
    @Override
    String action(String[] input) throws ParseException {
        String toPrint = "";
        int today = new Date().getDay();
        int compare = -1;
        if (input.length > 2) {
            if (input[2].equals("Monday")) {
                compare = 1;
            }
            if (input[2].equals("Tuesday")) {
                compare = 2;
            }
            if (input[2].equals("Wednesday")) {
                compare = 3;
            }
            if (input[2].equals("Thursday")) {
                compare = 4;
            }
            if (input[2].equals("Friday")) {
                compare = 5;
            }
            if (input[2].equals("Saturday")) {
                compare = 6;
            }
            if (input[2].equals("Sunday")) {
                compare = 0;
            }
        }
        if (compare != -1) {
            if (compare > today) {
                String dt = Data.date;  // Start date
                Calendar cal = Calendar.getInstance();
                cal.setTime(Data.formatter.parse(dt));
                cal.add(Calendar.DATE, compare - today);  // number of days to add
                dt = Data.formatter.format(cal.getTime());
                ArrayList<Lecture> l = Data.searchDates(dt);
                for (Lecture e : l) {
                    toPrint += e.toString() + '\n';
                }
            } else {
                if (compare < today) {
                    String dt = Data.date;  // Start date
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(Data.formatter.parse(dt));
                    cal.add(Calendar.DATE, 7 + (compare - today));
                    dt = Data.formatter.format(cal.getTime());
                    ArrayList<Lecture> l = Data.searchDates(dt);
                    for (Lecture e : l) {
                        toPrint += e.toString() + '\n';
                    }
                } else {
                    ArrayList<Lecture> l = Data.searchDates(Data.date);
                    for (Lecture e : l) {
                        toPrint += e.toString() + '\n';
                    }
                }
            }
        }
        return toPrint;
    }
}
