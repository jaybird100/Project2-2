package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Data {
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");

    // <DATE> = Inputted ADate, <DAY> = Inputted day, Date<TODAY> = today, <DATE><TODAY+<NUM>> = in <NUM> days, NEXT<DAY> = next <DAY>, <NUM> = number
    // Date<YESTERDAY> = yesterday, Date<TOMORROW> = tomorrow
    // Date<Today+<Num>>==0, NEXT<DAY> == 1 in ADate
    static ArrayList<String> codes = new ArrayList<>();
    static ArrayList<Attribute> correspondingAtt = new ArrayList<>();


    static ADate today = new ADate(LocalDate.now());

    static ArrayList<Lecture> lectures = new ArrayList<Lecture>();

    static ArrayList<String> commands = new ArrayList<>();
    static ArrayList<Skill> toCall = new ArrayList<>();
    static ArrayList<Article> objectsFromTxt = new ArrayList<>();
    static ArrayList<ArrayList<Attribute>> limiters = new ArrayList<>();
    static ArrayList<ArrayList<Integer>> attributeIndexes = new ArrayList<>();

    static void fillData() throws IOException {
        codes.add("<DATE>");
        correspondingAtt.add(new ADate());
        codes.add("<DAY>");
        correspondingAtt.add(new Day());
        codes.add("Date<TODAY>");
        correspondingAtt.add(today);
        codes.add("<DATE><TODAY+<NUM>>");
        correspondingAtt.add(new ADate(0));
        codes.add("Next<DAY>");
        correspondingAtt.add(new ADate(1));
        codes.add("<NUM>");
        correspondingAtt.add(new Number());
        codes.add("Date<YESTERDAY>");
        correspondingAtt.add(new ADate(today.date.minusDays(1)));
        codes.add("Date<TOMORROW>");
        correspondingAtt.add(new ADate(today.date.plusDays(1)));
        BufferedReader reader = new BufferedReader(new FileReader("Lectures.csv"));
        String row = reader.readLine();
        while(row != null){
            String[] data = row.split(",");
            if(!data[0].equals("Course")){
                Lecture lecture;
                Course c = new Course(data[0].trim());
                Time t = new Time(data[1].trim());
                ADate d = new ADate(data[2].trim());
                if(data.length>4){
                    ExtraText ex = new ExtraText(data[4].trim());
                    lecture = new Lecture(c,t,d,ex);
                }else{
                    lecture = new Lecture(c,t,d);
                }
                lectures.add(lecture);
            }
            row = reader.readLine();
        }
        reader = new BufferedReader(new FileReader("skills.txt"));
        row=reader.readLine();
        while(row!=null){
            row=reader.readLine();
            if(row==null){
                break;
            }
            commands.add(row);
            row=reader.readLine();
            if(row.trim().equalsIgnoreCase("Fetch")){
                toCall.add(new Fetch());
            }else{
                toCall.add(null);
            }
            row=reader.readLine();
            if(row.trim().equalsIgnoreCase("Lecture")){
                objectsFromTxt.add(new Lecture());
            }else{
                objectsFromTxt.add(null);
            }
            row = reader.readLine();
            if(row.trim().equalsIgnoreCase("all")){
                limiters.add(null);
            }else{
                String[] eachLimiter = row.split("&&");
                ArrayList<Attribute> limits = new ArrayList<>();
                for(String r:eachLimiter) {
                    for(int a=0;a<codes.size();a++){
                        if(r.equalsIgnoreCase(codes.get(a))){
                            limits.add(correspondingAtt.get(a));
                        }
                    }
                }
                limiters.add(limits);
            }
            row=reader.readLine();
            if(row.trim().equalsIgnoreCase("all")){
                attributeIndexes.add(null);
            }else{
                ArrayList<Integer> toAdd = new ArrayList<>();
                String[] r = row.split(" ");
                for(String w:r){
                    toAdd.add(Integer.parseInt(w));
                }
            }
            row=reader.readLine();
        }

    }



}
