package Utils;

import Actions.Create;
import Actions.Open;
import Actions.Set;
import Articles.*;
import Attributes.*;
import Actions.Fetch;
import Attributes.Attribute;

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
        ArrayList<Integer> extraPlacement = new ArrayList<>();
        ArrayList<Integer> webPageTagPlacement = new ArrayList<>();
        ArrayList<Integer> urlPlacenment = new ArrayList<>();
        ArrayList<Integer> pathPlacement = new ArrayList<>();
        ArrayList<Integer> folderTagPlacement = new ArrayList<>();
        ArrayList<Integer> articlePlacement = new ArrayList<>();
        System.out.println("W: "+Arrays.deepToString(words));
        for(int i = 0; i< Data.commands.size(); i++){
            ArrayList<Integer> tempDatePlacements= new ArrayList<>();
            ArrayList<Integer> tempCoursePlacements= new ArrayList<>();
            ArrayList<Integer> tempTimePlacements= new ArrayList<>();
            ArrayList<Integer> tempNumPlacements= new ArrayList<>();
            ArrayList<Integer> tempDayPlacements = new ArrayList<>();
            ArrayList<Integer> tempExtraPlacements = new ArrayList<>();
            ArrayList<Integer> tempWebPageTagPlacement = new ArrayList<>();
            ArrayList<Integer> tempURLPlacement = new ArrayList<>();
            ArrayList<Integer> tempPathPlacement = new ArrayList<>();
            ArrayList<Integer> tempFolderTagPlacement = new ArrayList<>();
            ArrayList<Integer> tempArticlePlacement = new ArrayList<>();
            ArrayList<Integer> codeIDs = new ArrayList<>();
            //split the current query
            String[] command = Data.commands.get(i).split(" ");
            if(words.length==command.length) {
                System.out.println(Arrays.deepToString(command));
                for (int q = 0; q < command.length; q++) {
                    for (int a = 0; a < Data.codes.size(); a++) {
                        if (command[q].equalsIgnoreCase(Data.codes.get(a))) {
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
                            if(command[q].equalsIgnoreCase("<TIME>")){
                                tempExtraPlacements.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<WEBTAG>")){
                                tempWebPageTagPlacement.add(q);
                            }
                            if(command[q].equalsIgnoreCase(("<URL>"))){
                                tempURLPlacement.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<ARTICLE>")){
                                tempArticlePlacement.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<PATH>")){
                                tempPathPlacement.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<FOLDERTAG>")){
                               tempFolderTagPlacement.add(q);
                            }
                            if(command[q].equalsIgnoreCase("<EXTRA>")){
                                tempFolderTagPlacement.add(q);
                            }

                            codeIDs.add(q);
                        }
                    }
                }
                boolean everyWordMatch = true;
                for (int v = 0; v < command.length; v++) {
                    System.out.println("Comm" +command[v]);
                    if (!codeIDs.contains(v)) {
                        //if the index v in command is not a code, it should match the index v in the query(words),
                        // otherwise, every word does not match
                        if (!words[v].equalsIgnoreCase(command[v])) {
                            everyWordMatch = false;
                            break;
                        }
                    }
                }
                if (everyWordMatch) {
                    System.out.println("EWM: " + Data.commands.get(i) + " == " + input);
                    //these store the indices of code types
                    datePlacements=tempDatePlacements;
                    coursePlacement=tempCoursePlacements;
                    numPlacement=tempNumPlacements;
                    timePlacement=tempTimePlacements;
                    dayPlacement=tempDayPlacements;
                    extraPlacement=tempExtraPlacements;
                    webPageTagPlacement=tempWebPageTagPlacement;
                    urlPlacenment=tempURLPlacement;
                    articlePlacement=tempArticlePlacement;
                    folderTagPlacement=tempFolderTagPlacement;
                    pathPlacement=tempPathPlacement;
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
        int extraCounter =0;
        if(commandID==-1){
            return "No command recognized";
        }
        System.out.println(Data.toCall.size());
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
            if(theLimiters!=null) {
                for (int i = theLimiters.size() - 1; i >= 0; i--) {
                    if (theLimiters.get(i) != null) {
                        if (theLimiters.get(i) instanceof ADate) {
                            if (theLimiters.get(i).toBeInputted) {
                                int id = ((ADate) theLimiters.get(i)).id;
                                if (id == -1) {
                                    ADate date = new ADate(words[datePlacements.get(dateCounter)]);
                                    if(!date.recognizedDate){
                                        return "Date not recognized, must be in dd/MM/yy format";
                                    }
                                    dateCounter++;
                                    theLimiters.remove(i);
                                    theLimiters.add(i, date);
                                }
                                if (id == 0) {
                                    int num;
                                    try{
                                        num = Integer.parseInt(words[numPlacement.get(numCounter)]);
                                    }catch (NumberFormatException a){
                                        return "Number not recognized";
                                    }
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
                                    DayOfWeek day;
                                    try{
                                        day = DayOfWeek.valueOf(theDay);
                                    }catch (IllegalArgumentException f){
                                        return "Does not recognize day";
                                    }
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
                        if(theLimiters.get(i) instanceof Time){
                            if(theLimiters.get(i).toBeInputted){
                                Time t = new Time(words[timePlacement.get(timeCounter)]);
                                timeCounter++;
                                theLimiters.remove(i);
                                theLimiters.add(i,t);
                            }
                        }
                        if(theLimiters.get(i) instanceof ExtraText){
                            if(theLimiters.get(i).toBeInputted){
                                ExtraText e = new ExtraText(words[extraPlacement.get(extraCounter)]);
                                extraCounter++;
                                theLimiters.remove(i);
                                theLimiters.add(i,e);
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
                System.out.println("WEBPAGE: "+webPageTagPlacement.size()+" URL: "+urlPlacenment.size());
                if(webPageTagPlacement.size()==1) {
                    theObject = new Webpage(new WebpageTag(words[webPageTagPlacement.get(0)]));
                }
                if(urlPlacenment.size()==1){
                    theObject=new Webpage(words[urlPlacenment.get(0)]);
                }
            }

            else if(theObject instanceof FolderLocation){
                System.out.println("FOLDERLOCATION: "+folderTagPlacement.size()+" PATH: "+pathPlacement.size());
                if(folderTagPlacement.size()==1) {
                    theObject = new FolderLocation(new FolderTag(words[folderTagPlacement.get(0)]));
                }
                if(pathPlacement.size()==1){
                    theObject=new FolderLocation(words[pathPlacement.get(0)]);
                }
            }

            Open o= new Open(theObject);
            return o.action();


        }else if(Data.toCall.get(commandID) instanceof Create){
            String word = words[articlePlacement.get(0)];
            if(word.equalsIgnoreCase("Lecture")){
                Create c = new Create(new Lecture());
                return c.action();
            }
            if(word.equalsIgnoreCase("Event")){
                Create c = new Create(new Event());
                return c.action();
            }
            if(word.equalsIgnoreCase("Notification")){
                Create n=new Create(new Notification());
                return n.action();
            }

        }else if(Data.toCall.get(commandID) instanceof Set){
            Article theObject = Data.objectsFromTxt.get(commandID);
            if(theObject instanceof Timer &&timePlacement.size()==1){
                Time time = new Time(words[timePlacement.get(0)]);
                if(!time.couldParseTime){
                    return "Couldn't parse time correctly, make sure it's in hh:mm or hh:mm:ss format";
                }
                theObject= new Timer(time);
            }
            Set s = new Set(theObject);
            return s.action();


        }
        return "No match found";
    }


}
