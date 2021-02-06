package Utils;

import Attributes.ADate;
import Skills.Fetch;
import Articles.Article;
import Attributes.Attribute;
import Utils.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Parser {
    public static String parse(String input){
        String[] words = input.split(" ");
        int commandID=-1;
        for(int i = 0; i< Data.commands.size(); i++){
            ArrayList<Integer> codeIDs = new ArrayList<>();
            String[] command = Data.commands.get(i).split(" ");
            if(words.length==command.length) {
                for (int q = 0; q < command.length; q++) {
                    for (int a = 0; a < Data.codes.size(); a++) {
                        if (command[q].equalsIgnoreCase(Data.codes.get(a))) {
                            codeIDs.add(q);
                        }
                    }
                }
                boolean everyWordMatch = true;
                for (int v = 0; v < command.length; v++) {
                    if (!codeIDs.contains(v)) {
                        if (!words[v].equalsIgnoreCase(command[v])) {
                            everyWordMatch = false;
                        }
                    }
                }
                if (everyWordMatch) {
                    System.out.println("EWM: " + Data.commands.get(i) + " == " + input);
                    commandID = i;
                    break;
                }
            }
        }
        if(Data.toCall.get(commandID) instanceof Fetch){
            Article theObject = Data.objectsFromTxt.get(commandID);
            ArrayList<Attribute> theLimiters;
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
                                    ADate date = new ADate(words[3]);
                                    theLimiters.remove(i);
                                    theLimiters.add(i, date);
                                }
                                if (id == 0) {
                                    int num = Integer.parseInt(words[2]);
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
                                    String theDay = words[2];
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
                    }
                }
            }
            Fetch f = new Fetch(theObject,theLimiters,Data.attributeIndexes.get(commandID));
            return f.action();
        }
        return "No match found";
    }
}
