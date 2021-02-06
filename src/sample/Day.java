package sample;

import java.time.DayOfWeek;

public class Day extends Attribute{
    DayOfWeek day;

    public Day(DayOfWeek d){
        super(false);
        day=d;
    }
    public Day() {
        super(true);
    }

    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof Day){
            DayOfWeek d1 = day;
            Day o = (Day)(input);
            DayOfWeek d2 = o.day;
            return d1.equals(d2);
        }
        return false;
    }
}
