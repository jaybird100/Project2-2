package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Data {
    static Parser p = new Parser();
    static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

    static String date;

    static ArrayList<Lecture> lectures = new ArrayList<>();

    static ArrayList<Skill> everySkill = new ArrayList<>();

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
        everySkill.add(new Yesterday());
        everySkill.add(new Today());
        everySkill.add(new Tomorrow());
        everySkill.add(new LecturesInDays());
        everySkill.add(new LecturesOn());
       // System.out.println(everySkill.size());
    }




    static ArrayList<Lecture> searchDates(String in){
        ArrayList<Lecture> toReturn = new ArrayList<>();
        for(Lecture lec:lectures){
            if(lec.day.equalsIgnoreCase(in)||lec.date.equalsIgnoreCase(in)){
                toReturn.add(lec);
            }
        }
        return toReturn;
    }
}
