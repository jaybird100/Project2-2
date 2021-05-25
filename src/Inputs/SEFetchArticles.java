package Inputs;

import Articles.*;
import Attributes.Number;
import Attributes.*;

import java.util.ArrayList;

public class SEFetchArticles {
    Article article;
    public ArrayList<String> inputs = new ArrayList<>();
    public ArrayList<ArrayList<String>> eachInputsLimiters = new ArrayList<>();
    public boolean nonInputLimiter=false;

    public SEFetchArticles(Article a){
        article=a;
        if(a instanceof Lecture){
            nonInputLimiter=true;
            inputs.add(new ADate().toString());
            inputs.add(new Course().toString());
            inputs.add(new Time().toString());
            inputs.add(new ExtraText().toString());
            inputs.add(new Day().toString());
            inputs.add(new Number().toString());
            // Non-input needed limiters
            ArrayList<String> to = new ArrayList<>();
            to.add("Date<TODAY>");
            to.add("Date<YESTERDAY>");
            to.add("Date<TOMORROW>");
            eachInputsLimiters.add(to);
            // <DATE> limiters
            ArrayList<String> date = new ArrayList<>();
            date.add("<DATE>");
            eachInputsLimiters.add(date);
            // <COURSE> limiters
            ArrayList<String> course = new ArrayList<>();
            course.add("<COURSE>");
            eachInputsLimiters.add(course);
            // <TIME> limiters
            ArrayList<String> time = new ArrayList<>();
            time.add("<TIME>");
            eachInputsLimiters.add(time);
            // <EXTRA> limiters
            ArrayList<String> extra = new ArrayList<>();
            extra.add("<EXTRA>");
            eachInputsLimiters.add(extra);
            // <DAY> limiters
            ArrayList<String> day = new ArrayList<>();
            day.add("NEXT<DAY>");
            eachInputsLimiters.add(day);
            // <NUM> limiters
            ArrayList<String> num = new ArrayList<>();
            num.add("<DATE><TODAY+<NUM>>");
            eachInputsLimiters.add(num);
        }
        if(a instanceof Event){
            nonInputLimiter=true;
            inputs.add(new ADate().toString());
            inputs.add(new Time().toString());
            inputs.add(new ExtraText().toString());
            inputs.add(new Day().toString());
            inputs.add(new Number().toString());
            // Non-input needed limiters
            ArrayList<String> to = new ArrayList<>();
            to.add("Date<TODAY>");
            to.add("Date<YESTERDAY>");
            to.add("Date<TOMORROW>");
            eachInputsLimiters.add(to);
            // <DATE> limiters
            ArrayList<String> date = new ArrayList<>();
            date.add("<DATE>");
            eachInputsLimiters.add(date);
            // <TIME> limiters
            ArrayList<String> time = new ArrayList<>();
            time.add("<TIME>");
            eachInputsLimiters.add(time);
            // <EXTRA> limiters
            ArrayList<String> extra = new ArrayList<>();
            extra.add("<EXTRA>");
            eachInputsLimiters.add(extra);
            // <DAY> limiters
            ArrayList<String> day = new ArrayList<>();
            day.add("NEXT<DAY>");
            eachInputsLimiters.add(day);
            // <NUM> limiters
            ArrayList<String> num = new ArrayList<>();
            num.add("<DATE><TODAY+<NUM>>");
            eachInputsLimiters.add(num);
        }
        if(a instanceof Notification){
            nonInputLimiter=true;
            inputs.add(new ADate().toString());
            inputs.add(new Course().toString());
            inputs.add(new Time().toString());
            inputs.add(new ExtraText().toString());
            inputs.add(new Day().toString());
            inputs.add(new Number().toString());
            // Non-input needed limiters
            ArrayList<String> to = new ArrayList<>();
            to.add("Date<TODAY>");
            to.add("Date<YESTERDAY>");
            to.add("Date<TOMORROW>");
            eachInputsLimiters.add(to);
            // <DATE> limiters
            ArrayList<String> date = new ArrayList<>();
            date.add("<DATE>");
            eachInputsLimiters.add(date);
            // <COURSE> limiters
            ArrayList<String> course = new ArrayList<>();
            course.add("<COURSE>");
            eachInputsLimiters.add(course);
            // <TIME> limiters
            ArrayList<String> time = new ArrayList<>();
            time.add("<TIME>");
            eachInputsLimiters.add(time);
            // <EXTRA> limiters
            ArrayList<String> extra = new ArrayList<>();
            extra.add("<EXTRA>");
            eachInputsLimiters.add(extra);
            // <DAY> limiters
            ArrayList<String> day = new ArrayList<>();
            day.add("NEXT<DAY>");
            eachInputsLimiters.add(day);
            // <NUM> limiters
            ArrayList<String> num = new ArrayList<>();
            num.add("<DATE><TODAY+<NUM>>");
            eachInputsLimiters.add(num);
        }
        if (a instanceof Medication) {
            inputs.add(new ExtraText().toString());
            inputs.add(new ADeadline().toString());
            // <EXTRA> limiters
            ArrayList<String> extra = new ArrayList<>();
            extra.add("<EXTRA>");
            eachInputsLimiters.add(extra);
            // <DEADLINE> limiters
            ArrayList<String> dl = new ArrayList<>();
            dl.add("<DEADLINE>");
            eachInputsLimiters.add(dl);


        }
    }

    @Override
    public String toString() {
        if(article instanceof Lecture){
            return "Lecture";
        }
        if(article instanceof Event){
            return "Event";
        }
        if(article instanceof Notification){
            return "Notification";
        }
        if (article instanceof Medication) {
            return "Medication";
        }
        return "";
    }
}
