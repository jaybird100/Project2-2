package Utils;

import Actions.*;
import Articles.*;
import Attributes.*;
import Attributes.Number;
import Attributes.Attribute;
import Inputs.SEFetch;
import Inputs.SEOpen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Data {

    public static ArrayList<Article> allArticle= new ArrayList<>();
    public static ArrayList<Action> allSkills = new ArrayList<>();
    public static SEFetch seFetch = new SEFetch();
    public static SEOpen seOpen = new SEOpen();


    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy");


    // <DATE> = Inputted ADate, <DAY> = Inputted day, Date<TODAY> = today, <DATE><TODAY+<NUM>> = in <NUM> days, NEXT<DAY> = next <DAY>, <NUM> = number
    // Date<YESTERDAY> = yesterday, Date<TOMORROW> = tomorrow, <COURSE> = inputted course
    // Date<Today+<Num>>==0, NEXT<DAY> == 1 in ADate
    //Indices of the two below should match
    static ArrayList<String> codes = new ArrayList<>();//Variables we are looking for and in their <> forms
    static ArrayList<Attribute> correspondingAtt = new ArrayList<>();//Corresponding attribute to the above codes

    //Significant Variables
    static ADate today = new ADate(LocalDate.now());

    //Lists of stored Articles from files
    public static ArrayList<Lecture> lectures = new ArrayList<Lecture>();//lectures from a stored file
    public static ArrayList<Event> events= new ArrayList<>();
    public static ArrayList<Webpage> webpages= new ArrayList<>();

    public static ArrayList<Notification> notifications = new ArrayList<>();
    public static ArrayList<FolderLocation> folderLocations=new ArrayList<>();

    //indices of the below should match
    public static ArrayList<String> commands = new ArrayList<>();//Possible query entries(line 1 of the agreed upon skill.txt file)
    public static ArrayList<Action> toCall = new ArrayList<>();//Skill to call to match the query(line 2)
    public static ArrayList<Article> objectsFromTxt = new ArrayList<>();//Article being looked for by the query(line 3)
    public static ArrayList<ArrayList<Attribute>> limiters = new ArrayList<>();//Attribute limiters based on the ArrayList<String> codes(Line 4)
    public static ArrayList<ArrayList<Integer>> attributeIndexes = new ArrayList<>();//each Article has arrayList of Attributes, this selects which Attributes of the article are printed out(Line 5)

    public static void fillData() throws IOException {
        //Reset the data
        allArticle = new ArrayList<>();
        allSkills = new ArrayList<>();
        seFetch = new SEFetch();
        seOpen = new SEOpen();
        codes = new ArrayList<>();
        correspondingAtt = new ArrayList<>();
        lectures = new ArrayList<>();
        events = new ArrayList<>();
        webpages = new ArrayList<>();
        notifications = new ArrayList<>();
        commands = new ArrayList<>();
        toCall = new ArrayList<>();
        objectsFromTxt = new ArrayList<>();
        limiters = new ArrayList<>();
        attributeIndexes = new ArrayList<>();

        allArticle.add(new Event());
        allArticle.add(new Lecture());
        allArticle.add(new Timer());
        allArticle.add(new Webpage());
        allArticle.add(new Notification());
        allArticle.add(new FolderLocation());

        // Each skill gets added
        allSkills.add(new Fetch());
        allSkills.add(new Open());
        allSkills.add(new Create());
        allSkills.add(new Set());


        //fill in the codes we are looking for & corresponding attributes
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
        codes.add("<COURSE>");
        correspondingAtt.add(new Course());
        codes.add("<EXTRA>");
        correspondingAtt.add(new ExtraText());
        codes.add("<TIME>");
        correspondingAtt.add(new Time());
        codes.add("<WEBTAG>");
        correspondingAtt.add(new WebpageTag());
        codes.add("<URL>");
        correspondingAtt.add(new URLAtt());
        codes.add("<ARTICLE>");
        correspondingAtt.add(new GenericArticle());
        codes.add("<PATH>");
        correspondingAtt.add(new Path());
        codes.add("<FOLDERTAG>");
        correspondingAtt.add(new FolderTag());

        //read the lectures csv and turn all lectures to Lecture objects
        BufferedReader reader = new BufferedReader(new FileReader(Variables.DEFAULT_CSV_FILE_PATH + "Lectures.csv"));
        String row = reader.readLine();
        //read every line that is not empty
        while (row != null) {
            //split each line by ,
            String[] data = row.split(",");
            //skip the first line in this file since that is just a guide
            if (!data[0].equals("Course")) {
                //otherwise create a Lecture based on the Course, Time, Date, and possible Extra Text in the line
                Lecture lecture;
                Course c = new Course(data[0].trim());
                Time t = new Time(data[1].trim());
                ADate d = new ADate(data[2].trim());
                if (data.length > 4) {
                    ExtraText ex = new ExtraText(data[4].trim());
                    lecture = new Lecture(c, t, d, ex);
                } else {
                    lecture = new Lecture(c, t, d);
                }
                //add the new lecture to the lectures ArrayList
                lectures.add(lecture);
            }
            row = reader.readLine();
        }

        //parse other files when they exist and add their objects to the respective ArrayList
        reader = new BufferedReader(new FileReader(Variables.DEFAULT_CSV_FILE_PATH + "Events.csv"));
        row = reader.readLine();
        while (row != null) {
            String[] data = row.split(",");
            System.out.println(Arrays.toString(data));
            Event e;
            ExtraText title = new ExtraText(data[0]);
            Time time = new Time(data[1].trim());
            ADate date = new ADate(data[2].trim());
            if (data.length == 4) {
                ExtraText notes = new ExtraText(data[3]);
                e = new Event(title, time, date, notes);
            } else {
                e = new Event(title, time, date);
            }
            events.add(e);
            row = reader.readLine();
        }
        reader = new BufferedReader(new FileReader(Variables.DEFAULT_CSV_FILE_PATH + "Links.csv"));
        row = reader.readLine();
        while (row != null) {
            String[] data = row.split(",");
            webpages.add(new Webpage(data[0], data[1].trim()));
            row = reader.readLine();
        }

        reader = new BufferedReader(new FileReader(Variables.DEFAULT_CSV_FILE_PATH + "Notifications.csv"));
        row = reader.readLine();
        while (row != null) {
            String[] data = row.split(",");
            if (!data[0].equals("Course")) {
                Notification n;
                Course c = new Course(data[0].trim());
                Time t = new Time(data[1].trim());
                ADate d = new ADate(data[2].trim());

                ExtraText ex = new ExtraText(data[4].trim());
                n = new Notification(c, t, d, ex);

                notifications.add(n);
            }

            reader = new BufferedReader(new FileReader(Variables.DEFAULT_CSV_FILE_PATH + "Paths.csv"));
            row = reader.readLine();
            while (row != null) {
                String[] y = row.split(",");
                folderLocations.add(new FolderLocation(y[0], y[1].trim()));
                row = reader.readLine();
            }


            //read the possible query entries from the .txt file for Phrases pertaining to queries
            reader = new BufferedReader(new FileReader(Variables.DEFAULT_SKILL_PARSER_FILE_PATH + "skills.txt"));
            row = reader.readLine();
            //read every line that is not empty
            while (row != null) {
                row = reader.readLine();
                if (row == null) {
                    break;
                }
                //add the command
                commands.add(row);
                //read next line
                row = reader.readLine();
                //add the call
                //add the check for other skills to this ifelse
                Action ac = null;
                for (Action a : allSkills) {
                    if (row.trim().equalsIgnoreCase(a.toString())) {
                        toCall.add(a);
                        ac = a;
                    }
                }
                row = reader.readLine();
                if (ac instanceof Fetch) {
                    //add the Article being queried
                    //add checks to other Articles to this ifelse
                    ///  needs list of articles and subsequent code
                    for (Article a : allArticle) {
                        if (row.trim().equalsIgnoreCase(a.toString())) {
                            objectsFromTxt.add(a);
                        }
                    }
                    //read next line
                    row = reader.readLine();
                    //check for limiters based on codes
                    //if the codes were added to the ArrayList<String> before nothing extra is needed here
                    if (row.trim().equalsIgnoreCase("all")) {
                        limiters.add(null);
                    } else {
                        String[] eachLimiter = row.split("&&");
                        trimArray(eachLimiter);
                        ArrayList<Attribute> limits = new ArrayList<>();
                        for (String r : eachLimiter) {
                            for (int a = 0; a < codes.size(); a++) {
                                if (r.equalsIgnoreCase(codes.get(a))) {
                                    limits.add(correspondingAtt.get(a));
                                }
                            }
                        }
                        limiters.add(limits);
                    }
                    //read the next line
                    row = reader.readLine();
                    //check for which out Attributes are wanted from the Article
                    if (row.trim().equalsIgnoreCase("all")) {
                        attributeIndexes.add(null);
                    } else {
                        ArrayList<Integer> toAdd = new ArrayList<>();
                        String[] r = row.split(" ");
                        for (String w : r) {
                            toAdd.add(Integer.parseInt(w));
                        }
                        attributeIndexes.add(toAdd);
                    }
                    row = reader.readLine();
                }
                if (ac instanceof Open) {
                    //add the Article being queried
                    if (row.equalsIgnoreCase("Webpage")) {
                        objectsFromTxt.add(new Webpage());
                    } else if (row.equalsIgnoreCase("FolderLocation")) {
                        objectsFromTxt.add(new FolderLocation());
                    }
                    limiters.add(null);
                    attributeIndexes.add(null);
                    row= reader.readLine();
                    continue;
                }
                if (ac instanceof Create) {
                    objectsFromTxt.add(null);
                    limiters.add(null);
                    attributeIndexes.add(null);
                    continue;
                }
                if (ac instanceof Set) {
                    row = reader.readLine();
                    objectsFromTxt.add(new Timer());
                    limiters.add(null);
                    attributeIndexes.add(null);
                    continue;
                }
            }
            //read other files....

        }
    }

        public static void trimArray (String[]in){
            for (int s = 0; s < in.length; s++) {
                in[s] = in[s].trim();
            }
        }


}
