package Articles;

import Attributes.Time;
import Utils.Variables;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import java.time.LocalTime;
import java.util.ArrayList;

public class Timer extends Article {
   public Time tf;
    public int hr,min,sec;
    public Timer(){super(new ArrayList<>());}
    public Timer(Time interval){
        super(new ArrayList<>());
        LocalTime a = LocalTime.now();
         hr=interval.getTime().getHour();
        a=a.plusHours(hr);
         min= interval.getTime().getMinute();
        a=a.plusMinutes(min);
         sec = interval.getTime().getSecond();
        a=a.plusSeconds(sec);

        tf= new Time(a);
        attributes.add(tf);
    }

    public String toString() {
        if(tf==null){
            return "Timer";
        }
        return "";
    }
}


