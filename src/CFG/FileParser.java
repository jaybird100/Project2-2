package CFG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

    public static String loadFile(File file){
        return usingBufferedReader(file);
    }
    public static void addSkill(File file){
        addSkill(loadFile(file));
    }
    public static void addSkill(String skill){
        HashMap<String, List<String>> d = FileParser.extractData(skill);
        HashMap<String, Rule> toAdd = FileParser.createRules(d.get("rule"));
        InputParser.add(toAdd, createActions(d.get("action")));
    }
    private static HashMap<String, List<String>> extractData(String skill){
        HashMap<String, List<String>> data = new HashMap<>();
        data.put("action", new ArrayList<>());
        data.put("rule", new ArrayList<>());
        skill = skill.toLowerCase();

        String[] lines = skill.split("\n");
        for (String line : lines) {
            String[] arr = line.split(" ", 2);
            String type = arr[0].trim();
            String rest = arr[1].trim();
            data.get(type).add(rest);
        }
        return data;
    }
    private static Rule createRule(String rule){
        String[] data = rule.split(":", 2);
        Rule r = new Rule(data[0].trim());
        String[] replacements = data[1].split("\\|");
        for (String replacement : replacements) {
            r.add(replacement.trim());
        }
        return r;
    }
    private static HashMap<String, Rule> createRules(List<String> rules){
        HashMap<String , Rule> ruleList = new HashMap<>();
        for (String rule : rules) {
            Rule r = createRule(rule);
            ruleList.put(r.id, r);
        }
        return ruleList;
    }
    private static Action createAction(String action){
        //TODO
        System.out.println("TODO createAction(String action)");
        return new Action("", null);
    }
    private static List<Action> createActions(List<String> actions){
        List<Action> actionList = new ArrayList<>();
        for (String action : actions) {
            actionList.add(createAction(action));
        }
        return actionList;
    }

    public static boolean addSkillRegex(String skill){
        Matcher m = Pattern.compile("<.+>").matcher(skill);

        StringBuilder sb = new StringBuilder();
        int last = 0;
        while (m.find()) {
            sb.append(skill, last, m.start());
            sb.append(m.group(0).toLowerCase());
            last = m.end();
        }
        sb.append(skill.substring(last));
        skill = sb.toString();

        HashMap<String, Rule> toAdd = FileParser.ruleRegex(skill);
        InputParser.add(toAdd, FileParser.actionRegex(skill));
        return true;
    }
    public static HashMap<String, Rule> ruleRegex(String skill){
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

    public static List<Action> actionRegex(String skill){
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
}
