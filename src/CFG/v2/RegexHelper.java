package CFG.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {

    public static final String[][] specialChar = new String[][]{
            {":", "colon"},{",", "comma"},{"-", "dash"}, {".", "dot"}, {"?", "questionmark"}
    };

    /**
     * Replaces special characters with their name.
     * CURRENT ISSUE: will end up creating a rule like <comma> => "comma" instead of <comma> => ","
     * @param s string to convert
     * @return converted string
     */
    public static String convertToSplitFriendly(String s){
        if(s.matches("<%.*?%>")){
            return s;
        }
        for (String[] chars : specialChar) {
            s = s.replace(chars[0], " "+chars[1]+" ");
        }
        return s;
    }

    public static List<String> orderedCombinations(List<String> r0, List<String> r1, String separator){
        List<String> s = new ArrayList<>();
        for (String s1 : r0) {
            for (String s2 : r1) {
                s.add(s1+separator+s2);
            }
        }
        return s;
    }

    public static List<String> extract(String full, String regexToExtract){
        Matcher matcher = Pattern.compile("(.*?)("+regexToExtract+")(.*)").matcher(full);
        List<String> separated = new ArrayList<>();
        if(matcher.find()){
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if(!matcher.group(i).equals("")){
                    if(i!=2) {
                        separated.addAll(extract(matcher.group(i), regexToExtract));
                    }
                    else{
                        separated.add(matcher.group(i));
                    }
                }
            }
        }else{
            separated.add(full.trim());
        }
        return separated;
    }
}
