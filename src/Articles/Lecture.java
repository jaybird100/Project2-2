package Articles;

import Attributes.ADate;
import Attributes.Course;
import Attributes.ExtraText;
import Attributes.Time;

import java.util.ArrayList;

public class Lecture extends Article {
    // attributes: 0=course, 1=time, 2= date, 3= extra
    Course course;
    Time time;
    ADate date;
    ExtraText extraText;
    public Lecture(){
        super(new ArrayList<>());
    }
    public Lecture(Course c, Time t, ADate da){
        super(new ArrayList<>());
        attributes.add(c);
        attributes.add(t);
        attributes.add(da);
        course=c;
        time=t;
        date=da;
    }
    public Lecture(Course c, Time t, ADate da, ExtraText extra){
        super(new ArrayList<>());
        attributes.add(c);
        attributes.add(t);
        attributes.add(da);
        attributes.add(extra);
        course=c;
        time=t;
        date=da;
        extraText=extra;
    }

    @Override
    public String toString() {
        if(course==null){
            return "Lecture2";
        }
        if(extraText!=null) {
            return course.toString() + "| " + time.toString() + "| " + date.toString() + "| " + extraText.toString();
        }else{
            return course.toString() + "| " + time.toString() + "| " + date.toString();
        }
    }
}
