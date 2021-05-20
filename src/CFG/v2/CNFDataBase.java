package CFG.v2;

import java.util.*;

public class CNFDataBase{
    public final RuleDataBase rdb;
    public final ActionDataBase adb;

    public CNFDataBase(RuleDataBase rdb, ActionDataBase adb){
        this.rdb = rdb;
        this.adb = adb;
    }
    public CNFDataBase(){
        this(new RuleDataBase(true), new ActionDataBase());
    }

    public void clear(){
        rdb.clear();
        adb.clear();
    }
    public void add(CNFDataBase db){
        add(db.rdb, db.adb);
    }
    public void add(RuleDataBase rdb, ActionDataBase adb){
        if(rdb!=null){
            this.rdb.add(rdb);
        }
        if(adb!=null){
            this.adb.add(adb);
        }
    }
    // Rule methods
    public void addRule(Rulev2 r) {
        addRule(r.id, r.options);
    }
    public void addRule(String key, List<String> replacements) {
        rdb.addRule(key, replacements);
    }
    public void addRule(String key, String replacement) {
        rdb.addRule(key, replacement);
    }
    public void removeRule(Rulev2 r) {
        rdb.removeRule(r.id);
    }
    public void removeRule(String key) {
        rdb.removeRule(key);
    }
    public int grammarSize() {
        return rdb.grammarSize();
    }
    public Set<String> keySet() {
        return rdb.keySet();
    }
    public Rulev2 rule(String key) {
        return rdb.rule(key);
    }
    public Rulev2 rule(int idx) {
        return rdb.rule(idx);
    }
    public TreeSet<Rulev2> rulesForOption(String opt) {
        return rdb.rulesForOption(opt);
    }

    // Action methods
    public void addAction(Actionv2 a) {
        adb.addAction(a);
    }
    public void removeRule(Actionv2 a) {
        adb.removeRule(a);
    }
    public int responseSize() {
        return adb.responseSize();
    }
    public Actionv2 action(int idx) {
        return adb.action(idx);
    }
    public List<Actionv2> actionForRule(String ruleID) {
        return adb.actionForRule(ruleID);
    }

    @Override
    public String toString() {
        return rdb+"\n"+adb;
    }
}

class RuleDataBase {
    /*
    Rule:
        - key |<- String
        - values it can take |<- Arraylist of Strings

    For simplicity, you'd need to be able to:
        - get a rule by ID |<- using a hashmap that maps an ID to a rule (1 to 1)
        - get a rule by index (for CYK?) |<- Arraylist of rules (1 to 1)
        - get all rules that can be replaced by an option |<- Hashmap that maps an option to a list of rules (1 to many)
        - get all options that a rule can take |<- in rule object (1 to many)
     */
    protected final HashMap<String, Rulev2> keyToRule; // ID -> Rule
    protected final HashMap<String, TreeSet<Rulev2>> optionToRule; // Option -> Rules
    protected final List<Rulev2> ruleList; // Index -> Rule

    protected boolean advanced; // True if you want to work with logical components (<%dType *logical prop*%>)
    protected final HashMap<String, LogicProposition> logicOption; // Logic Option <%dType *logical prop*%>-> Rules

    public RuleDataBase(boolean advanced) {
        this.advanced = advanced;
        keyToRule = new HashMap<>();
        ruleList = new ArrayList<>();
        optionToRule = new HashMap<>();
        // Logical add-on
        if(advanced) {
            logicOption = new HashMap<>();
        }else{
            logicOption = null;
        }
        CNFConverter.addSpecialChar(this);
    }

    public void clear(){
        keyToRule.clear();
        optionToRule.clear();
        ruleList.clear();
        logicOption.clear();
    }
    public void add(RuleDataBase rdb) {
        for (Rulev2 r : rdb.ruleList) {
            addRule(r);
        }
    }
    public void addRule(Rulev2 r) {
        addRule(r.id, r.options);
    }
    public void addRule(String key, List<String> replacements) {
        replacements.forEach(r -> addRule(key, r));
    }
    public void addRule(String key, String replacement) {
        if (keyToRule.containsKey(key)) {
            Rulev2 r1 = keyToRule.get(key);
            r1.add(replacement);
        } else {
            Rulev2 r = new Rulev2(key);
            r.add(replacement);
            r.index = ruleList.size();
            ruleList.add(r);
            keyToRule.put(key, r);
        }
        if (!optionToRule.containsKey(replacement)) {
            optionToRule.put(replacement, new TreeSet<>());
        }
        optionToRule.get(replacement).add(rule(key));
        // Logical add-on
        if(advanced){
            addRuleLogic(replacement);
        }
    }
    public void removeRule(Rulev2 r) {
        removeRule(r.id);
    }
    public void removeRule(String key) {
        Rulev2 r = keyToRule.remove(key);
        for (int j = r.index; j < ruleList.size(); j++) {
            ruleList.get(j).index--;
        }
        ruleList.remove(r.index);
        r.options.forEach(o -> optionToRule.get(o).remove(r));
    }
    private void addRuleLogic(String replacement){
        if(replacement.matches("<%.+%>")){
            logicOption.put(replacement,LogicProposition.create(replacement));
        }
    }

    public int grammarSize() {
        return ruleList.size();
    }
    public Set<String> keySet() {
        return keyToRule.keySet();
    }
    public Rulev2 rule(String key) {
        return keyToRule.get(key);
    }
    public Rulev2 rule(int idx) {
        return ruleList.get(idx);
    }
    public TreeSet<Rulev2> rulesForOption(String opt) {
        TreeSet<Rulev2> rules = new TreeSet<>();
        TreeSet<Rulev2> r = optionToRule.get(opt);
        if(r!=null) {
            rules.addAll(r);
        }
        if(advanced && !opt.matches(".*<\\w+>.*")){ // dk why !opt.matches(".*<\\w+>.*")
            rules.addAll(rulesForOptionLogic(opt));
        }
        return rules;
    }
    private TreeSet<Rulev2> rulesForOptionLogic(String opt){
        TreeSet<Rulev2> rules = new TreeSet<>();
        for (String k : logicOption.keySet()) {
            if(logicOption.get(k).match(opt)){
                rules.addAll(optionToRule.get(logicOption.get(k).full));
            }
        }
        return rules;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RULES (").append(ruleList.size()).append("):\n");
        for (Rulev2 rule : ruleList) {
            sb.append("\t-").append(rule).append("\n");
        }
        return sb.toString();
    }
    public static List<String> ruleIDs(TreeSet<Rulev2> r) {
        if (r == null || r.size()==0) {
            return new ArrayList<>();
        }
        List<String> s = new ArrayList<>();
        r.forEach(rule -> s.add(rule.id));
        return s;
    }

}

class ActionDataBase{
    /*
    Action:
        - response |<- String (!!! can take variables ex: I am not <adjective>; find a way to deal with this)
        - pre requisites |<- Arraylist of PreRequisites (Stores rule ID, wanted value and strength of this pre req)

    For simplicity, you'd need to be able to:
        - get action by index |<- LinkedList of Actions (1 to 1)
        - get all actions that use a given rule as pre req |<- Hashmap String (rule ID) to List of Actions (1 to many)
        - get all pre reqs needed for an action |<- in action object (many to 1)
        - get response for a given action |<- in action object (1 to 1)
     */
    protected final LinkedList<Actionv2> actionList;
    protected final HashMap<String, List<Actionv2>> ruleToAction;

    public ActionDataBase() {
        actionList = new LinkedList<>();
        ruleToAction = new HashMap<>();
    }

    public void clear() {
        actionList.clear();
        ruleToAction.clear();
    }
    public void add(ActionDataBase adb) {
        for (Actionv2 a : adb.actionList) {
            addAction(a);
        }
    }
    public void addAction(Actionv2 a) {
        actionList.add(a);
        for (PreRequisite preRequisite : a.preRequisites) {
            if(!ruleToAction.containsKey(preRequisite.ruleID)){
                ruleToAction.put(preRequisite.ruleID, new ArrayList<>());
            }
            ruleToAction.get(preRequisite.ruleID).add(a);
        }
    }
    public void removeRule(Actionv2 a) {
        actionList.remove(a);
        for (PreRequisite preRequisite : a.preRequisites) {
            ruleToAction.get(preRequisite.ruleID).remove(a);
        }
    }

    public int responseSize() {
        return actionList.size();
    }
    public Actionv2 action(int idx) {
        return actionList.get(idx);
    }
    public List<Actionv2> actionForRule(String ruleID) {
        return ruleToAction.get(ruleID);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Actions (").append(actionList.size()).append("):\n");
        for (Actionv2 a : actionList) {
            sb.append("\t-").append(a).append("\n");
        }
        return sb.toString();
    }

}
