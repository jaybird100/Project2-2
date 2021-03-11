package Articles;

import Attributes.ADate;
import Attributes.Attribute;
import Attributes.ExtraText;
import Attributes.Time;

import java.util.ArrayList;

public class Event extends Article{
    ExtraText title;
    ExtraText notes;
    Time time;
    ADate date;
    public Event(ExtraText title, Time t, ADate d, ExtraText notes){
        super(new ArrayList<>());
        attributes.add(title);
        attributes.add(notes);
        attributes.add(d);
        attributes.add(t);
        this.title=title;
        this.time=t;
        this.date=d;
        this.notes=notes;
    }
    public Event(ExtraText title, Time t, ADate d){
        super(new ArrayList<>());
        attributes.add(title);
        attributes.add(d);
        attributes.add(t);
        this.title=title;
        this.time=t;
        this.date=d;
    }
    public Event(ArrayList<Attribute> attributes) {
        super(attributes);
    }
    public Event(){
        super(new ArrayList<>());
    }
}
