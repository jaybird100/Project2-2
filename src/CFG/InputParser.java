package CFG;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {

    // TODO CYK

    // Recursion
    /**
     * Extracts terminals from input string using the recursion algorithm and returns best match
     * @param input string you want to parse with CFG
     * @return best match if it is viable else null
     */
    public static Match parseRecursion(String input){
        HashMap<String, String> map = extractTerminalRecursion(input);
        return findBestReply(map, input);
    }
    
    protected static HashMap<String, String> extractTerminalRecursion(String action){
        action = action.toLowerCase();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", action);
        if(DataBase.rules().containsKey("<s>")) {
            extractTerminalRecursion(action, "<s>", map);
        }
        return map;
    }
    private static String extractTerminalRecursion(String action, String key, HashMap<String, String> map){
        Rule r = new Rule(DataBase.rules().get(key));
        for (int i = 0; i < r.replacements().size(); i++) {
            String firstPartOfString = r.replacements().get(i).split(">", 2)[0];
            String[] toCheck = firstPartOfString.split("<", 2);
            String regex = toCheck[0] + ".*";
            String keyChecked;
            if(toCheck.length==2) {
                keyChecked = "<" + toCheck[1] + ">";
            }
            else {
                keyChecked = "";
            }
            if (action.matches(regex)) {
                String partMatched = regex.replace(".*", "");
                String nonChecked = action.replaceFirst(partMatched, "");
                if(nonChecked.equals("") || keyChecked.equals("")){
                    map.put(key, partMatched);
                    return nonChecked;
                }
                String updatedAction = extractTerminalRecursion(nonChecked, keyChecked, map);
                if(!updatedAction.equals(action)){
                    String s = r.replacements().get(i).split(">", 2)[1];
                    r.replacements().set(i, s);
                    if(updatedAction.equals("") && r.replacements().get(i).equals("")){
                        map.put(key, DataBase.rules().get(key).replacements().get(i));
                        return updatedAction;
                    }
                    if((updatedAction.equals("") && !r.replacements().get(i).equals(""))
                            || !updatedAction.equals("") && r.replacements().get(i).equals("")){
                        map.clear();
                        continue;
                    }
                    i--;
                    action = updatedAction;
                }
            }
        }
        return action;
    }

    // Post terminal extraction method
    /**
     * Calls analyseTerminals and only returns the best one.
     * Best one means the only with the highest value that also passes a threshold
     * @param map terminals found
     * @param input input string
     * @return best match if it is viable else null
     */
    private static Match findBestReply(HashMap<String, String> map, String input){
        List<Match> matches = analyseTerminals(map, input);
        if(matches.size()!=0 && matches.get(0).isValid(2.5)){
            return matches.get(0);
        }
        return null;
    }
    /**
     * For every "action" in the database,
     * create a match and score it
     * @param map terminals found
     * @param input input string
     * @return sorted list of all matches.
     */
    private static List<Match> analyseTerminals(HashMap<String, String> map, String input){
        List<Match> matches = new ArrayList<>();
        String reconstructed = reconstruct(map);
        for (Action action : DataBase.actions()) {
            matches.add(new Match(map, action, input, reconstructed));
        }
        Collections.sort(matches);
        return matches;
    }

    public static String reconstruct(HashMap<String, String> map){
        StringBuilder s = new StringBuilder(map.get("<s>"));
        while(s.toString().matches(".*<\\w+>.*")){
            List<String> s1 = RegexHelper.extract(s.toString(), "<\\w+>");
            s = new StringBuilder();
            for (String s2 : s1) {
                if(s2.matches("<\\w+>")){
                    s.append(map.get(s2));
                }
                else{
                    s.append(s2);
                }
            }
        }
        return s.toString();
    }

    public static void toCNF(){
        HashMap<String,Rule> cnfdatabase= new HashMap<>();
        System.out.println("__________________________");
        System.out.println(DataBase.rules().entrySet());
        System.out.println("__________________________");
        for(Map.Entry<String,Rule> r: DataBase.rules().entrySet()){
            // System.out.println(r.getValue().replacements());
            //for every r in rule database
            // r can have only on of
            //Nonterminal-terminal
            //Nonterminal-Nonterminal, non terminal

            r.getValue().cnfSplit();

        }
    }
}