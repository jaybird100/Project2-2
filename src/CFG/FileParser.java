package CFG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO complete singleCreateAction
//TODO failing to load? Can it even happen?
public class FileParser {

    // Using splitting and key words/characters
    /**
     * Loads the skill by splitting the input by key words and characters.
     * @return true if load was successful, else false
     */

    //DOESNT WORK ATM
    public static boolean addSkillKeyChar(File file){
        return addSkillKeyChar(loadFile(file));
    }
    public static boolean addSkillKeyChar(String skill){
        HashMap<String, List<String>> d = FileParser.extractData(skill);
        HashMap<String, Rule> toAdd = FileParser.createRules(d.get("rule"));
        DataBase.mergeWithDatabase(toAdd, createActions(d.get("action")));
        return true;
    }
    private static HashMap<String, Rule> createRules(List<String> rules){
        HashMap<String , Rule> ruleList = new HashMap<>();
        for (String rule : rules) {
            Rule r = singleCreateRule(rule);
            ruleList.put(r.id, r);
        }
        return ruleList;
    }
    private static Rule singleCreateRule(String rule){
        String[] data = rule.split(":", 2);
        Rule r = new Rule(data[0].trim());
        String[] replacements = data[1].split("\\|");
        for (String replacement : replacements) {
            r.add(replacement.trim());
        }
        return r;
    }
    private static List<Action> createActions(List<String> actions){
        List<Action> actionList = new ArrayList<>();
        for (String action : actions) {
            actionList.add(singleCreateAction(action));
        }
        return actionList;
    }
    private static Action singleCreateAction(String action){
        System.out.println("TODO singleCreateAction(String action)");
        return new Action("", null);
    }

    // Using Java REGEX
    /**
     * Loads the skill using Java Regex.
     * @return true if load was successful, else false
     */

    public static boolean addSkillRegex(File file){
        return addSkillRegex(loadFile(file));
    }
    public static boolean addSkillRegex(String skill){
        Matcher m = Pattern.compile("<\\w+>").matcher(skill);

        StringBuilder sb = new StringBuilder();
        int last = 0;
        // Lower-cases the keys
        while (m.find()) {
            sb.append(skill, last, m.start());
            sb.append(m.group(0).toLowerCase());
            last = m.end();
        }
        sb.append(skill.substring(last));
        skill = sb.toString();
        HashMap<String, Rule> toAdd = FileParser.ruleRegex(skill);
        DataBase.mergeWithDatabase(toAdd, FileParser.actionRegex(skill));
        return true;
    }
    private static HashMap<String, Rule> ruleRegex(String skill){
        HashMap<String, Rule> rules = new HashMap<>();
        String keyRegex = "<.+?>"; // <*at least 1 character*>
        String repRegex = "[[^\\|]*?\\|?]+?"; // something (| something)*
        String ruleRegex = "(?im)^\\s*rule\\s*(?<key>"+keyRegex+")\\s*:\\s*(?<replacements>"+repRegex+")$";
        Matcher m = Pattern.compile(ruleRegex).matcher(skill);
        while(m.find()) {
            String key = m.group("key");
            String[] temp = m.group("replacements").toLowerCase().split("\\|");
            Rule r = new Rule(key);
            for (String s : temp) {
                r.add(s.trim());
            }
            rules.put(r.id, r);
        }
        return rules;
    }
    private static List<Action> actionRegex(String skill){
        List<Action> actions = new ArrayList<>();
        String ruleRegex = "(?im)^\\s*action\\s*.*?:.*?$";
        Matcher m = Pattern.compile(ruleRegex).matcher(skill);
        while(m.find()) {
            String action = m.group().replaceFirst("(?im)action", "").trim();
            String[] temp = action.split(":", 2);
            String[] prereqs = temp[0].split(",");
            String response = temp[1].trim();
            HashMap<String, String[]> h = new HashMap<>();
            for (String prereq : prereqs) {
                if(prereq.equals("")){
                    continue;
                }
                temp = prereq.split(">",2);
                String key = (temp[0]+">").trim();
                String[] values = temp.length==1? null:temp[1].split("\\|");
                for (int i = 0; i < Objects.requireNonNull(values).length; i++) {
                    values[i] = values[i].trim();
                }
                h.put(key, values);
            }
            Action a = new Action(response, h);
            actions.add(a);
        }
        return actions;
    }

    public static boolean addSkillRegex2(File file){
        return addSkillRegex2(loadFile(file));
    }
    public static boolean addSkillRegex2(String skill){
        HashMap<String, List<String>> data = extractData(skill);
        System.out.println("RULES");
        for (String rule : data.get("rule")) {
            System.out.println(rule);
        }
        System.out.println("ACTION");
        for (String action : data.get("action")) {
            System.out.println(action);
        }
        return true;
    }
    // QOL: Quality Of Life

    /**
     * Loads the file that has been passed through
     * @param file path
     * @return input of the file as a String
     */
    public static String loadFile(File file){
        return usingBufferedReader(file);
    }
    private static String usingBufferedReader(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    /**
     * Separates the file into 2 classes: The rules and the actions.
     * @param skill to load
     * @return Hashmap with action and rule as keys.
     */
    private static HashMap<String, List<String>> extractData(String skill){
        HashMap<String, List<String>> data = new HashMap<>();
        data.put("action", new ArrayList<>());
        data.put("rule", new ArrayList<>());

        String[] lines = skill.split("\n");
        for (String line : lines) {
            String[] d = line.split(" ", 2);
            if(d[0].equalsIgnoreCase("rule")){
                d[1] = d[1].toLowerCase();
                data.get("rule").add(d[1].toLowerCase().trim());
            }
            else if(d[0].equalsIgnoreCase("action")) {
                Matcher m = Pattern.compile("<\\w+>").matcher(d[1]);
                StringBuilder sb = new StringBuilder();
                int last = 0;
                while (m.find()) {
                    sb.append(d[1], last, m.start());
                    sb.append(m.group(0).toLowerCase());
                    last = m.end();
                }
                sb.append(d[1].substring(last));
                d[1] = sb.toString();
                data.get("action").add(d[1].trim());
            }
        }
        return data;
    }
}
