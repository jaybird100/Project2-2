package Articles;

import Attributes.Time;

import java.time.LocalTime;
import java.util.ArrayList;

public class Timer extends Article{
    Time tf;
    public Timer(){super(new ArrayList<>());}
    public Timer(Time interval){
        super(new ArrayList<>());
        LocalTime a = LocalTime.now();
        //System.out.println("A"+a);
        int hr=interval.getTime().getHour();
        a=a.plusHours(hr);
       // System.out.println("B"+a+" "+hr);
        int min= interval.getTime().getMinute();
        a=a.plusMinutes(min);
      //  System.out.println("C"+a+" "+min);
        int sec = interval.getTime().getSecond();
        a=a.plusSeconds(sec);
       // System.out.println("D"+a+" "+sec);


       // System.out.println(a);
        tf= new Time(a);
        attributes.add(tf);
       // System.out.println(tf);
       // System.out.println(interval);
    }

    public String start() throws InterruptedException {
        while(tf.getTime().isAfter(LocalTime.now())){
            System.out.println(LocalTime.now());
            //Thread.sleep(1000l);
        }
        return "done";
    }
    public String toString() {
        if(tf==null){
            return "Timer";
        }
        return "";
    }
}


