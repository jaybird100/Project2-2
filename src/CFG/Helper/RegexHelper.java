package CFG.Helper;

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

    public static double levenshteinDifference(String string1, String string2){
        final int length1 = string1.length();
        final int length2 = string2.length();
        int[][] distance = new int[length1 + 1][length2 + 1];

        for (int i=0; i<=length1; i++)
        {
            distance[i][0] = i;
        }
        for (int j = 0; j <= length2; j++)
        {
            distance[0][j] = j;
        }

        for (int i = 1; i <= length1; i++)
        {
            for (int j = 1; j <= length2; j++)
            {
                final char char1 = string1.charAt(i - 1);
                final char char2 = string2.charAt(j - 1);
                final int offset = (char1 == char2) ? 0 : 1;
                distance[i][j] = Math.min(Math.min(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1),
                        distance[i - 1][j - 1] + offset);
            }
        }
        return 1-distance[length1][length2]/(double)Math.max(length1, length2);
    }
}
