package sample;

public class Lecture {
    public String course;
    public String time;
    public String date;
    public String day;
    public String extraText;
    public Lecture(String c, String t,String da, String d){
        course=c;
        time=t;
        date=da;
        day=d;
    }
    public Lecture(String c, String t,String da, String d, String extra){
        course=c;
        time=t;
        date=da;
        day=d;
        extraText=extra;
    }

    @Override
    public String toString() {
        String toReturn =  course  +
                " at " + time +
                " on " + date +
                " which is a " + day;
        if(extraText!=null){
            toReturn+=" / " + extraText;
        }
        return toReturn;
    }
}
