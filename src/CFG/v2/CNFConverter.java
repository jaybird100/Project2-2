package CFG.v2;

import java.util.ArrayList;
import java.util.List;

public class CNFConverter {
    static final String converterID = "cnf"; //lower case might not be necessary

    /**
     * Converts the String of a skill into CNF
     * @param skill skill to add
     * @return database in which the skill has been pit
     */
    public static CNFDataBase loadAsCNF(String skill, boolean advanced){
        return loadAsCNF(skill, new CNFDataBase(new RuleDataBase(advanced), new ActionDataBase()));
    }
    public static CNFDataBase loadAsCNF(String skill, CNFDataBase dataBase){
        List<String> recentlyAdded = new ArrayList<>(); // saves the IDs of the rules the have been added this method call
        String[] lines = skill.split("\n");
        for (String line : lines) { // for each line in skill String
            String rule = line.toLowerCase();
            if(rule.startsWith("rule")){ // if line is rule
                recentlyAdded.add(addRule(rule, dataBase.rdb));
            }
            if(rule.startsWith("action")){ // if line is action
                addAction(line, dataBase.adb);
            }
        }
        CNFUnit(dataBase.rdb, recentlyAdded); // Apply unit rule to database at the end to make sure you don't miss a rule
        return dataBase;
    }

    /**
     * Converts string of a rule into CNF (except Unit rule). Automatically adds to database
     * @param rule to convert to rule object(s)
     * @param ruleDataBase to add to
     * @return main ID of rule added (for optimisation purposes)
     */
    private static String addRule(String rule, RuleDataBase ruleDataBase){
        int i=0;
        String[] s = rule.split("<", 2);
        String key = "<"+s[1].split(">", 2)[0]+">";
        String[] replacements = s[1].split(":", 2)[1].split("\\|");
        for (String replacement : replacements) { // for each replacement found for this rule
            replacement = replacement.trim();
            replacement = CNFTerm(replacement, ruleDataBase); // Apply Term rule
            i = CNFBin(replacement, key, i, ruleDataBase); // Apply Bin rule
        }
        return key;
    }

    /**
     * Converts String of action to Action object. Automatically adds to database
     * @param action to convert to action object
     * @param actionDataBase to add to
     */
    private static void addAction(String action, ActionDataBase actionDataBase){
        action = action.replaceFirst("(?im)action", "").trim();
        String[] temp = action.split(":", 2); // before ':' = pre reqs, after ';' = response
        temp[0] = temp[0].toLowerCase();
        String[] preReqs = temp[0].split(","); // split all pre reqs (separated by ',')
        List<String> response = RegexHelper.extract(temp[1].trim(), "<\\w+>"); // split response s.t rule IDs are separated. Keeps order
        // Set all rule IDs to lower case (for hashmap)
        for (int i = 0; i < response.size(); i++) {
            if(response.get(i).matches("<\\w+>")){
                response.set(i, response.get(i).toLowerCase());
            }
        }
        // Load all pre requisites
        List<PreRequisite> preRequisites = new ArrayList<>();
        for (String preReq : preReqs) {
            if (preReq.equals("")) {
                continue;
            }
            temp = preReq.split(">", 2);
            String key = (temp[0] + ">").trim();
            String[] values = temp[1].split("\\|");
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }
            preRequisites.add(new PreRequisite(key, values));
        }
        actionDataBase.addAction(new Actionv2(response, preRequisites));
    }

    /**
     * If replacement has terminal words separate replacement in individual words and create rules for each (includes punctuation)
     * Adds intermediary rules to database immediately
     * ex:
     * in: which lectures are there <timeexpression>
     * out: <which+convertedID><lectures+convertedID><are+convertedID><there+convertedID><timeexpression>
     * @param replacement rule to convert to cnf
     * @param ruleDataBase database to for cnf rules
     * @return converted rule
     */
    public static String CNFTerm(String replacement, RuleDataBase ruleDataBase){
        String[] split = RegexHelper.convertToSplitFriendly(replacement).trim().split("\\s+");
        if(split.length!=1) { // if replacement is more than 1 word create rules
            StringBuilder newRep = new StringBuilder();
            for (String s : split) {
                s = s.trim();
                if (!s.matches("(\\s*<\\w+>\\s*)+")) { // if it is a terminal word, create a new terminal rule for the word
                    // CREATE RULE s => s
                    ruleDataBase.addRule("<"+s+converterID+">", s);
                    newRep.append("<").append(s).append(converterID).append(">");
                } else { // if one of the words is a rule ID, simply append it
                    newRep.append(s);
                }
            }
            replacement = newRep.toString();
        }
        return replacement;
    }
    /**
     * If replacement has more than 2 rule IDs, split and create new inner rules with 2 or less IDs
     * Adds intermediary rules to database immediately
     * ex:
     * in: <id> = <id1><id2><id3>....
     * out: <id> = <id1><id4> and <id4> = <id2><id3>
     * @param replacement rule to convert to cnf
     * @param mainKey key to help making new derived rules
     * @param additionalKey number to help making new derived rules
     * @return number of rules added
     */
    public static int CNFBin(String replacement, String mainKey, int additionalKey, RuleDataBase ruleDataBase){
        String[] keys = replacement.replace("><", "> <").split(" ");
        String previousRule =keys.length==1? "":keys[keys.length-1];
        for (int i = keys.length-2; i >0; i--) { // for every rule ID in the replacement create new rules that pair the current one with the previous
            String newKey = new StringBuilder(mainKey).insert(mainKey.length() - 1, additionalKey+converterID).toString();
            ruleDataBase.addRule(newKey, keys[i]+previousRule);
            additionalKey++;
            previousRule= newKey;
        }
        ruleDataBase.addRule(mainKey, keys[0]+previousRule);
        return additionalKey;
    }
    /**
     * Loops through all recently added rules and if they are unit rules (rule ID1 -> rule ID2), propagate it's possible replacements up
     * Does so immediately into ruleDataBase
     * @param recentlyAdded list of recently added rules
     */
    public static void CNFUnit(RuleDataBase ruleDataBase, List<String> recentlyAdded){
        for (String s : recentlyAdded) { // for each recently added rule
            Rulev2 rule = ruleDataBase.keyToRule.get(s);
            for (int i = 0; i < rule.options.size(); i++) { // for each option of this rule
                String o = rule.options.get(i);
                if(o.matches("<\\w+>")){ // if the option is a unit (only <id>)
                    Rulev2 r2 = ruleDataBase.rule(o);
                    rule.add(r2.options);
                    for (String option : r2.options) {
                        if(ruleDataBase.optionToRule.containsKey(option)) {
                            ruleDataBase.optionToRule.get(option).add(rule);
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds all special characters from array in RegexHelper to database
     * @param ruleDataBase to add to
     */
    public static void addSpecialChar(RuleDataBase ruleDataBase){
        for (String[] strings : RegexHelper.specialChar) {
            ruleDataBase.addRule("<"+strings[1]+">", strings[1]);
        }
    }
}
