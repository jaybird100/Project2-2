package CFG.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static boolean initialized = false;
    private static final HashMap<String, Rule> ruleDatabase = new HashMap<>();
    private static final List<Action> actionDatabase = new ArrayList<>();

    public static HashMap<String, Rule> rules(){
        return ruleDatabase;
    }
    public static List<Action> actions(){
        return actionDatabase;
    }

    // Adding to the database
    public static void mergeWithDatabase(HashMap<String, Rule> rules, List<Action> actions) {
        mergeRulesWithDatabase(rules);
        mergeActionsWithDatabase(actions);
    }
    public static void mergeRulesWithDatabase(HashMap<String, Rule> rules) {
        if (rules == null) {
            return;
        }
        if (!initialized) {
            ruleDatabase.put("<s>", new Rule("<s>"));
            initialized = true;
        }
        for (Map.Entry<String, Rule> e : rules.entrySet()) {
            if (ruleDatabase.containsKey(e.getKey())) {
                ruleDatabase.get(e.getKey()).add(e.getValue().replacements());
            } else {
                ruleDatabase.put(e.getKey(), e.getValue());
            }
        }
    }
    public static void mergeActionsWithDatabase(List<Action> actions) {
        if (actions == null) {
            return;
        }
        actionDatabase.addAll(actions);
    }
}
