package A;



import java.util.Date;

public class Event extends Skill {

    public Event(String l, Date d, String i){
        this.attributes.add(new Lecture(l));
        this.attributes.add(new Day(d));
        this.attributes.add(new Info(i));
    }
    public Event(String l, Day d, String i){
        this.attributes.add(new Lecture(l));
        this.attributes.add(d);
        this.attributes.add(new Info(i));
    }
   Event(Lecture l, Day d, Info i){
       this.attributes.add(l);
       this.attributes.add(d);
       this.attributes.add(i);
   }

    @Override
    public Event fromTextFile(String fileName) {

        return null;
    }

    @Override
    public void action() {

    }


}
