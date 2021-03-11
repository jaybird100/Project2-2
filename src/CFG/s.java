package CFG;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class s{
    static HashMap<String, Rule> ruleDatabase;
    static List<Action> actionDatabase;
    /*
    Format:
    \nrule <id>: value1|value2|... // <id> can take value1, value2, ...
    \naction <id>*: <id1>-value1 <id2>-value2 ... -> return value // if <id> has <id1> = value1, <id2> = value2, etc... send return value
    \naction->return value // if all else fails, send return value
     */
    public static void main(String[] args){
        String skill = "RULE    <S>:    <ACTION>\n" +
                "RULE   <ACTION>:   <LOCATION> | <SCHEDULE>\n" +
                "RULE   <SCHEDULE>: WHICH LECTURES ARE THERE <TIMEEXPRESSION> | <TIMEEXPRESSION> WHICH LECTURES ARE THERE\n"+
                "RULE   <TIMEEXPRESSION>:   ON <DAY> AT <TIME> | AT <TIME> ON <DAY>\n" +
                "RULE   <TIME>: 12 | 9 \n" +
                "RULE   <LOCATION>: WHERE IS <ROOM> | HOW DO <PRO> GET TO <ROOM> | WHERE IS <ROOM> LOCATED\n" +
                "RULE   <PRO>: I | YOU | HE | SHE\n" +
                "RULE   <ROOM>: DEEPSPACE | SPACEBOX\n" +
                "RULE   <DAY>: MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY | SUNDAY\n" +
                "Action <SCHEDULE> *,  <DAY>  Saturday|Sunday :  There are no lectures on Saturday\n" +
                "Action <SCHEDULE> *,  <DAY>  Monday, <TIME> 9 : We start the week with math\n" +
                "Action <SCHEDULE> *,  <DAY>  Monday, <TIME> 12: On Monday noon we have Theoratical Computer Science\n" +
                "Action <LOCATION> *,  <ROOM> DeepSpace : DeepSpace is the first room after the entrance\n" +
                "Action <LOCATION> * : <ROOM> is in the first floor\n" +
                "Action : I have no idea";

        FileParser.addSkillRegex(skill);
        List<Action> responses = InputParser.parse("which lectures are there at 12 on monday");
        for (Action response : responses) {
            System.out.println(response);
        }

    }
}