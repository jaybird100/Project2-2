package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Data {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

    public static String toPrint="";

    String date;

    static ArrayList<Lecture> lectures = new ArrayList<>();
    public Data() throws IOException {
        fillData();
    }
    void fillData() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("Lectures.csv"));
        String row = reader.readLine();
        while(row != null){
            String[] data = row.split(",");
            if(!data[0].equals("Course")){
                Lecture lecture;
                if(data.length>4){
                    lecture = new Lecture(data[0].trim(),data[1].trim(),data[2].trim(),data[3].trim(),data[4].trim());
                }else{
                    lecture = new Lecture(data[0].trim(),data[1].trim(),data[2].trim(),data[3].trim());
                }
                lectures.add(lecture);
            }
            row = reader.readLine();
        }
        /*
        for(Lecture a:lectures){
            System.out.println(a.toString());
        }

         */
        date = formatter.format(new Date());
       // System.out.println(date);
    }
    void setPrint(String input) throws ParseException {
        String[] parts = input.split(" ");
       // System.out.println(Arrays.deepToString(parts));
        if(parts[0].equals("Lectures")){
            if(parts[1].equals("today")){
                ArrayList<Lecture> l = searchDates(date);
                toPrint = "";
                for (Lecture e : l) {
                    toPrint += e.toString() + '\n';
                }
            }
            if(parts[1].equals("yesterday")){
                String dt = date;  // Start date
                Calendar cal = Calendar.getInstance();
                cal.setTime(formatter.parse(dt));
                cal.add(Calendar.DATE, -1);  // number of days to add
                dt = formatter.format(cal.getTime());
                ArrayList<Lecture> l = searchDates(dt);
                toPrint = "";
                for (Lecture e : l) {
                    toPrint += e.toString() + '\n';
                }
            }
            if(parts[1].equals("tomorrow")){
                String dt = date;  // Start date
                Calendar cal = Calendar.getInstance();
                cal.setTime(formatter.parse(dt));
                cal.add(Calendar.DATE, 1);  // number of days to add
                dt = formatter.format(cal.getTime());
                ArrayList<Lecture> l = searchDates(dt);
                toPrint = "";
                for (Lecture e : l) {
                    toPrint += e.toString() + '\n';
                }
            }
            if(parts[1].equals("in")&&parts[3].equals("days")){
                int days = Integer.parseInt(parts[2]);
                String dt = date;  // Start date
                Calendar cal = Calendar.getInstance();
                cal.setTime(formatter.parse(dt));
                cal.add(Calendar.DATE, days);  // number of days to add
                dt = formatter.format(cal.getTime());
                ArrayList<Lecture> l = searchDates(dt);
                toPrint = "";
                for (Lecture e : l) {
                    toPrint += e.toString() + '\n';
                }
            }
            if(parts[1].equals("on")) {
                if(parts[2].equals("this")){
                    int today = new Date().getDay();
                    int compare=-1;
                    if(parts.length>3) {
                        if (parts[3].equals("Monday")) {
                            compare = 1;
                        }
                        if (parts[3].equals("Tuesday")) {
                            compare = 2;
                        }
                        if (parts[3].equals("Wednesday")) {
                            compare = 3;
                        }
                        if (parts[3].equals("Thursday")) {
                            compare = 4;
                        }
                        if (parts[3].equals("Friday")) {
                            compare = 5;
                        }
                        if (parts[3].equals("Saturday")) {
                            compare = 6;
                        }
                        if (parts[3].equals("Sunday")) {
                            compare = 0;
                        }
                    }
                    if(compare!=-1){
                        if(compare>today){
                            String dt = date;  // Start date
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(formatter.parse(dt));
                            cal.add(Calendar.DATE, compare-today);  // number of days to add
                            dt = formatter.format(cal.getTime());
                            ArrayList<Lecture> l = searchDates(dt);
                            toPrint = "";
                            for (Lecture e : l) {
                                toPrint += e.toString() + '\n';
                            }
                        }else{
                            if(compare<today){
                                String dt = date;  // Start date
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(formatter.parse(dt));
                                cal.add(Calendar.DATE, 7+(compare-today));  // number of days to add
                                dt = formatter.format(cal.getTime());
                                ArrayList<Lecture> l = searchDates(dt);
                                toPrint = "";
                                for (Lecture e : l) {
                                    toPrint += e.toString() + '\n';
                                }
                            }else{
                                ArrayList<Lecture> l = searchDates(date);
                                toPrint = "";
                                for (Lecture e : l) {
                                    toPrint += e.toString() + '\n';
                                }
                            }
                        }
                    }
                }else {
                    ArrayList<Lecture> l = searchDates(parts[2]);
                    toPrint = "";
                    for (Lecture e : l) {
                        toPrint += e.toString() + '\n';
                    }
                }
            }
        }
    }
    ArrayList<Lecture> searchDates(String in){
        ArrayList<Lecture> toReturn = new ArrayList<>();
        for(Lecture lec:lectures){
            if(lec.day.equals(in)||lec.date.equals(in)){
                toReturn.add(lec);
            }
        }
        return toReturn;
    }
}
