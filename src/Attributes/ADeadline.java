package Attributes;

import Utils.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;

public class ADeadline extends Attribute {
    public boolean recognizedDate=true;
    public int id=-1;
    public LocalDate getDate() {
        return date;
    }

    public LocalDate date;
    public ADeadline(LocalDate d){
        super(false);
        date=d;
    }
    public ADeadline(String s){
        super(false);
        try {
            date = LocalDate.parse(s, Data.dateFormatter);
        }catch(DateTimeParseException e){
            recognizedDate=false;
        }
    }
    public ADeadline(){
        super(true);
    }

    public String getDay(){
        DayOfWeek day = date.getDayOfWeek();
        return day.getDisplayName(TextStyle.FULL,new Locale("en"));
    }
    @Override
    public String toString() {
        if(date==null){
            return "<DEADLINE>";
        }
        return "till " + date.getDayOfMonth()+"/"+date.getMonth()+"/"+date.getYear();
    }

    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof ADeadline){
            LocalDate d1 = date;
            ADeadline o = (ADeadline)(input);
            LocalDate d2 = o.getDate();
            return d1.isBefore(d2);
        }
        return false;
    }

}
