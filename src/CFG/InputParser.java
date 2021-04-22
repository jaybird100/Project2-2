package CFG;

import java.util.*;

public class InputParser {
    public static HashMap<String, Rule> ruleDatabase = new HashMap<>();
    public static List<Action> actionDatabase = new ArrayList<>();

    public static void init(){
        ruleDatabase.put("<s>", new Rule("<s>"));
    }
    protected static void add(HashMap<String, Rule> rules, List<Action> actions){
        for (Map.Entry<String, Rule> e : rules.entrySet()) {
            if(InputParser.ruleDatabase.containsKey(e.getKey())){
                InputParser.ruleDatabase.get(e.getKey()).addAll(e.getValue().replacements());
            }
            else{
                InputParser.ruleDatabase.put(e.getKey(), e.getValue());
            }
        }
        InputParser.actionDatabase.addAll(actions);
    }


    public static Match parse(String action){
        HashMap<String, String> map = decode(action);
        List<Match> matches = analyse(map, action);
        for (Match match : matches) {
            System.out.println(match);
        }
        if(matches.size()!=0 && matches.get(0).isValid(2)){
            return matches.get(0);
        }
        return null;
    }

    public static HashMap<String, String> decode(String action){
        action = action.toLowerCase();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", action);
        decode(action, "<s>", map);
        return map;
    }

    private static String decode(String action, String key, HashMap<String, String> map){
        Rule r = new Rule(ruleDatabase.get(key));
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
                if(keyChecked.equals("")){
                    map.put(key, partMatched);
                    return nonChecked;
                }
                String updatedAction = decode(nonChecked, keyChecked, map);
                if(!updatedAction.equals(action)){
                    String s = r.replacements().get(i).split(">", 2)[1];
                    r.replacements().set(i, s);
                    if(updatedAction.equals("") && r.replacements().get(i).equals("")){
                        map.put(key, ruleDatabase.get(key).replacements().get(i));
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


    public static List<Match> analyse(HashMap<String, String> map, String input){
        List<Match> matches = new ArrayList<>();
        for (Action action : actionDatabase) {
            matches.add(new Match(map, action, input));
        }
        Collections.sort(matches);
        return matches;
    }


    public static void toCNF(){
        HashMap<String,Rule> cnfdatabase= new HashMap<>();
        System.out.println("__________________________");
        System.out.println(ruleDatabase.entrySet());
        System.out.println("__________________________");
        for(Map.Entry<String,Rule> r: ruleDatabase.entrySet()){
            // System.out.println(r.getValue().replacements());
            //for every r in rule database
            // r can have only on of
            //Nonterminal-terminal
            //Nonterminal-Nonterminal, non terminal

            r.getValue().cnfSplit();

        }
    }
}
