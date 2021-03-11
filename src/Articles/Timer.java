package Articles;

import Attributes.Time;

import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;

public class Timer extends Article{
    Time tf;
    public Timer(){super(new ArrayList<>());}
    public Timer(Time interval){
        super(new ArrayList<>());
        LocalTime a = LocalTime.now();
        System.out.println(interval.getTime().getHour());
        System.out.println(interval.getTime().getMinute());
        System.out.println(interval.getTime().getSecond());
        a.plusHours(interval.getTime().getHour());
        a.plusMinutes(interval.getTime().getMinute());
        a.plusSeconds(interval.getTime().getSecond());
        System.out.println(a);
        tf= new Time(a);
        attributes.add(tf);
        System.out.println(tf);
        System.out.println(interval);
    }

    public String start() throws InterruptedException {
        while(tf.getTime().isAfter(LocalTime.now())){
            Thread.sleep(1000l);
        }
        return "done";
    }

}
