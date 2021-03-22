package Attributes;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Time extends Attribute {
    public boolean couldParseTime=true;
    LocalTime time;
    public Time(LocalTime t){
        super(false);
        time=t;
    }
    public Time(String input){
        super(false);
        try{
            time=LocalTime.parse(input);
        }catch(DateTimeParseException e){
            couldParseTime=false;
        }
    }
    public Time(){
        super(true);
    }
    @Override
    public String toString() {
        if(time==null){
            return "<TIME>";
        }
        return time.toString();
    }
    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof Time){
            Time c = (Time)(input);
            return time.equals(c.time);
        }
        return false;
    }
    public LocalTime getTime(){return time;}

}
