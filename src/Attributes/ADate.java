package Attributes;

import Utils.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Locale;

public class ADate extends Attribute {
    public boolean recognizedDate=true;
    public int id=-1;
    public LocalDate getDate() {
        return date;
    }

    public LocalDate date;
    public ADate(LocalDate d){
        super(false);
        date=d;
    }
    public ADate(String s){
        super(false);
        try {
            date = LocalDate.parse(s, Data.dateFormatter);
        }catch(DateTimeParseException e){
            recognizedDate=false;
        }
    }
    public ADate(){
        super(true);
    }
    public ADate(int id){
        super(true);
        this.id=id;
    }
    public String getDay(){
        DayOfWeek day = date.getDayOfWeek();
        return day.getDisplayName(TextStyle.FULL,new Locale("en"));
    }
    @Override
    public String toString() {
        if(date==null){
            return "<DATE>";
        }
        return date.getDayOfMonth()+"/"+date.getMonth()+"/"+date.getYear();
    }

    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof ADate){
            LocalDate d1 = date;
            ADate o = (ADate)(input);
            LocalDate d2 = o.getDate();
            return d1.isEqual(d2);
        }
        return false;
    }

}
