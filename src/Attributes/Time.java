package Attributes;

import java.time.LocalTime;

public class Time extends Attribute {
    LocalTime time;
    public Time(LocalTime t){
        super(false);
        time=t;
    }
    public Time(String input){
        super(false);
        time=LocalTime.parse(input);
    }
    public Time(){
        super(true);
    }
    @Override
    public String toString() {
        if(time==null){
            return "null time";
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
}
