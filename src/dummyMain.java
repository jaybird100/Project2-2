import Articles.Timer;
import Articles.Webpage;
import Attributes.Time;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class dummyMain {
    public static void main(String [] args) throws InterruptedException {
        Time i= new Time("00:05:00");
        Timer test = new Timer(i);
        System.out.println(test.start());
    }
}
