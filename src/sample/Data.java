package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Data {
    public static String toPrint="";
    static ArrayList<Lecture> lectures = new ArrayList<>();
    public Data() throws IOException {
        fillLectures();
    }
    void fillLectures() throws IOException {
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
        for(Lecture a:lectures){
            System.out.println(a.toString());
        }
    }
    void setPrint(String input){
        Scanner c = new Scanner(input);
        if(c.next().equals("Lectures")&&c.next().equals("on")){
            ArrayList<Lecture> l = searchDates(c.next());
            toPrint="";
            for(Lecture e:l){
                toPrint+=e.toString()+'\n';
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
