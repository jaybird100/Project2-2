package Utils;

import Articles.Webpage;
import Attributes.ADate;
import Attributes.Course;
import Actions.Fetch;
import Articles.Article;
import Attributes.Attribute;
import Actions.Open;
import Attributes.WebpageTag;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    public static String parse(String input){
        //split entry by " "
        String[] words = input.split(" ");
        int commandID=-1;
        // these are the indexes of the placements of to be inputted attributes within the command, like <DAY> or <DATE>
        ArrayList<Integer> datePlacements= new ArrayList<>();
        ArrayList<Integer> coursePlacement= new ArrayList<>();
        ArrayList<Integer> timePlacement= new ArrayList<>();
        ArrayList<Integer> numPlacement= new ArrayList<>();
        ArrayList<Integer> dayPlacement = new ArrayList<>();
        ArrayList<Integer> extraTextPlacement = new ArrayList<>();
        ArrayList<Integer> webPageTagPlacement = new ArrayList<>();
        //look through all the possible queries in skills.txt
        for(int i = 0; i< Data.commands.size(); i++){
            ArrayList<Integer> tempDatePlacements= new ArrayList<>();
            ArrayList<Integer> tempCoursePlacements= new ArrayList<>();
            ArrayList<Integer> tempTimePlacements= new ArrayList<>();
            ArrayList<Integer> tempNumPlacements= new ArrayList<>();
            ArrayList<Integer> tempDayPlacements = new ArrayList<>();
            ArrayList<Integer> tempExtraTextPlacement = new ArrayList<>();
            ArrayList<Integer> tempWebPageTagPlacement = new ArrayList<>();
            ArrayList<Integer> codeIDs = new ArrayList<>();
            //split the current query
            String[] command = Data.commands.get(i).split(" ");
            if(words.length==command.length) {//if the number of words in the entry(words)== number of words in the current query
                for (int q = 0; q < command.length; q++) {//for every word in the current query
                    for (int a = 0; a < Data.codes.size(); a++) {//check every code in Data.codes
                        if (command[q].equalsIgnoreCase(Data.codes.get(a))) {//if the current word in the command is a code
                           //check if it is a date, course,number,time, or day and add the index of the code to to the respective arraylist
                            // index of <> in command == index of the value of <> in query
                            if(command[q].equalsIgnoreCase("<DATE>")){
                                tempDatePlacements.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<COURSE>")){
                                tempCoursePlacements.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<NUM>")){
                                tempNumPlacements.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<TIME>")){
                                tempTimePlacements.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<DAY>")){
                                tempDayPlacements.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<WEBTAG>")){
                                tempWebPageTagPlacement.add(q);
                            }


                            codeIDs.add(q);
                        }
                    }
                }
                boolean everyWordMatch = true;
                for (int v = 0; v < command.length; v++) {
                    if (!codeIDs.contains(v)) {
                        //if the index v in command is not a code, it should match the index v in the query(words),
                        // otherwise, every word does not match
                        if (!words[v].equalsIgnoreCase(command[v])) {
                            everyWordMatch = false;
                        }
                    }
                }
                if (everyWordMatch) {//if every word matches then we have found the right command and we can exit the loop
                    System.out.println("EWM: " + Data.commands.get(i) + " == " + input);
                    //these store the indices of code types
                    datePlacements=tempDatePlacements;
                    coursePlacement=tempCoursePlacements;
                    numPlacement=tempNumPlacements;
                    timePlacement=tempTimePlacements;
                    dayPlacement=tempDayPlacements;
                    webPageTagPlacement=tempWebPageTagPlacement;
                    commandID = i;
                    break;
                }
            }
        }
        // counters that iterate through each arraylist.
        int dateCounter=0;
        int courseCounter=0;
        int numCounter = 0;
        int timeCounter =0;
        int dayCounter =0;

        //if the command is linked to a fetch action
        if(Data.toCall.get(commandID) instanceof Fetch){
            //get the article associated with the matched query(referenced by commandID)
            Article theObject = Data.objectsFromTxt.get(commandID);
            ArrayList<Attribute> theLimiters;
            //parse through for the proper limiters
            if (Data.limiters.get(commandID) == null) {
                theLimiters=null;
            } else {
                theLimiters = new ArrayList<>(Data.limiters.get(commandID));
            }
//            System.out.println(Arrays.toString(theLimiters.toArray()));
            if(theLimiters!=null) {
                for (int i = theLimiters.size() - 1; i >= 0; i--) {
                    if (theLimiters.get(i) != null) {
                        if (theLimiters.get(i) instanceof ADate) {
                            if (theLimiters.get(i).toBeInputted) {
                                int id = ((ADate) theLimiters.get(i)).id;
                                if (id == -1) {
                                    ADate date = new ADate(words[datePlacements.get(dateCounter)]);
                                    dateCounter++;
                                    theLimiters.remove(i);
                                    theLimiters.add(i, date);
                                }
                                if (id == 0) {
                                    int num = Integer.parseInt(words[numPlacement.get(numCounter)]);
                                    numCounter++;
                                    ADate date;
                                    if(num>=0) {
                                        date = new ADate(Data.today.date.plusDays(num));
                                    }else{
                                        num=Math.abs(num);
                                        date= new ADate(Data.today.date.minusDays(num));
                                    }
                                    theLimiters.remove(i);
                                    theLimiters.add(i, date);
                                }
                                if (id == 1) {
                                    String theDay = words[dayPlacement.get(dayCounter)];
                                    dayCounter++;
                                    theDay=theDay.toUpperCase();
                                    DayOfWeek day = DayOfWeek.valueOf(theDay);
                                    LocalDate date = Data.today.date.plusDays(1);
                                    while (!date.getDayOfWeek().equals(day)) {
                                        date = date.plusDays(1);
                                    }
                                    ADate date1 = new ADate(date);
                                    theLimiters.remove(i);
                                    theLimiters.add(i, date1);
                                }
                            }
                        }
                        if(theLimiters.get(i) instanceof Course){
                            if(theLimiters.get(i).toBeInputted){
                                Course c = new Course(words[coursePlacement.get(courseCounter)]);
                                courseCounter++;
                                theLimiters.remove(i);
                                theLimiters.add(i,c);
                            }
                        }
                    }
                }
            }
            //create the fetch object and execute its action
            Fetch f = new Fetch(theObject,theLimiters,Data.attributeIndexes.get(commandID));
            return f.action();
        }else if(Data.toCall.get(commandID) instanceof Open){
            Article theObject = Data.objectsFromTxt.get(commandID);

            if(theObject instanceof Webpage){
                if(webPageTagPlacement.size()==1) {
                    theObject = new Webpage(new WebpageTag(words[webPageTagPlacement.get(0)]));

                }
            }
            Open o= new Open(theObject);
            try {
                return o.action();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        System.out.println(Arrays.toString(Data.toCall.toArray()));
        return "No match found";
    }
}
