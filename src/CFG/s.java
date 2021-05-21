package CFG;

import CFG.v2.Rulev2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class s{
    /*
    Atomic members
     */
    static String key = "(?<key><\\w+?>)"; // any word made of [a-zA-Z_0-9]+ between angle brackets
    static String dtype = "(?<dtype>\\(\\(\\w+\\)\\))?"; //optional (int, double or string). Defaults to string
    /*
    int and double specifics: <% functions %>
    functions:
        smaller *value* (all ints/doubles smaller than value inclusive),
        bigger *value* (all ints/doubles bigger than value inclusive),
        * (all ints/doubles),
        and,
        or (to implement),
        not (to implement)

     */
    static String type = "(?<type>rule|action)"; //rule or action
    static String ruleOption = "([^\\|]*?)"; // anything that isn't |
    static String ruleAllOptions = "(?<options>"+ruleOption+"[\\|"+ruleOption+"]*)"; // at least 1 option, then repeat (| option) for any additional
    /*
    Composite members
     */
    static String surfaceLvl = "(?im)^\\s*"+type+"(?<rest>.*)$";
    // line that starts with a type
    static String rule = "\\s*"+key+"\\s*"+dtype+"\\s*:\\s*"+ruleAllOptions;
    // base structure (any spaces can [0,inf], will recognize): rule <key> ((dtype)) : op1 [| op2]*

    public static void main(String[] args){
        String fileString = "RULE    <S>:    <ACTION>\n" +
                "RULE   <ACTION>:   <LOCATION> | <SCHEDULE>\n" +
                "RULE   <SCHEDULE>: WHICH LECTURES ARE THERE <TIMEEXPRESSION> | <TIMEEXPRESSION> WHICH LECTURES ARE THERE\n" +
                "RULE   <TIMEEXPRESSION>:   ON <DAY> AT <TIME> | AT <TIME> ON <DAY>\n" +
                "RULE   <TIME>: 12 | 9\n" +
                "RULE   <LOCATION>: WHERE IS <ROOM> | HOW DO <PRO> GET TO <ROOM> | WHERE IS <ROOM> LOCATED\n" +
                "RULE   <PRO>: I | YOU | HE | SHE\n" +
                "RULE   <ROOM>: DEEPSPACE | SPACEBOX\n" +
                "RULE   <DAY>: MONDAY | TUESDAY | WEDNESDAY | THURSDAY | FRIDAY | SATURDAY | SUNDAY\n" +
                "Action <SCHEDULE> *, <DAY> Monday, <TIME> 9: We have math <TIMEEXPRESSION>\n" +
                "Action <SCHEDULE> *,  <DAY>  Saturday|Sunday :  There are no lectures on Saturday\n" +
                "Action <SCHEDULE> *,  <DAY>  Monday, <TIME> 9 : We start the week with math\n" +
                "Action <SCHEDULE> *,  <DAY>  Monday, <TIME> 12: On Monday at noon we have Theoratical Computer Science\n" +
                "Action <LOCaTION> *,  <ROOM> DeepSpace : DeepSpace is the first room after the entrance\n" +
                "Action <LOCATION> * : <ROOM> is in the first floor\n" +
                "Action : I have no idea\n" +
                "Action <GREETING> *: Hello :)";

        FileParser.addSkillRegex(fileString);
        String input = "which lectures are there on monday AT 9";
        Match response = InputParser.parse(input);
        System.out.println(response.getResponse());
        System.out.println(response);
    }
    //CYK algorithm
    public static void parseSkill(String skill, List<Rulev2> rList, List<Action> aList){
        Matcher surfaceMatcher = Pattern.compile(surfaceLvl).matcher(skill);
        while(surfaceMatcher.find()){
            String t = surfaceMatcher.group("type");
            String r = surfaceMatcher.group("rest");
            if(t.equalsIgnoreCase("rule")){
                rList.add(extractRule(r));
            }
            else if(t.equalsIgnoreCase("action")){
                aList.add(extractAction(r));
            }
        }
    }
    public static Rulev2 extractRule(String line){
        Matcher m = Pattern.compile(rule).matcher(line);
        if(m.find()){
            String key = m.group("key").toLowerCase();
            String dtype = m.group("dtype");
            if(dtype!=null){
                dtype = dtype.replace("((","").replace("))", "");
            }
            String[] options = m.group("options").split("\\|");
            Rulev2 r = new Rulev2(key, dtype);
            r.options(Arrays.stream(options).map(String::trim).collect(Collectors.toList()));
            return r;
        }
        return null;
    }
    public static Action extractAction(String line){
        String[] l = line.split(":", 2);
        String[] preReq = l[0].split(",");
        String response = l[1].trim();
        HashMap<String, String[]> map = new HashMap<>();
        for (String s : preReq) {
            System.out.println(s);
        }
        Action a = new Action(response, map);
        System.out.println(response+" "+Arrays.toString(preReq));
        return a;
    }
}