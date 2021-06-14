package CFG;

import CFG.CNForm.CNFConverter;
import CFG.CNForm.CNFDataBase;
import CFG.CNForm.CNFMatch;
import CFG.CNForm.CNFRule;
import CFG.Helper.RegexHelper;

import java.util.*;

public class ChatBot {

    private static List<Node> cache = new ArrayList<>();
    private static Node chosen = null;
    public static String run(String input){
        if(cache.size()!=0){
            String[] step = handleMultiTurn(input);
            if(step[0].equals("return")){
                return step[1];
            }
            cache = new ArrayList<>();
            chosen = null;
            input = step[1];
        }
        String temp = CFGSystem.extraCommands(input);
        if(temp!=null){
            return temp;
        }
        List<TreeSet<String>> words = CFGSystem.formatInput(input);
        List<List<List<CYKNode>>> r = CFGSystem.CYK(words);
        List<HashMap<String, String>> map = CFGSystem.parseResults(r);
        List<CNFMatch> m = CFGSystem.findMatches(map);

        if(m.size()==0) {
            List<Node> resp = ChatBot.check(r);
            return ChatBot.getResponse(resp);
        }
        return CFGSystem.getResponse(m);
    }

    private static String[] handleMultiTurn(String input) {
        if(input.equals("n")){
            cache.clear();
            return new String[]{"return", null};
        }
        if(input.equals("y")){
            input = "1";
        }
        try{
            if(chosen==null) {
                int i = Integer.parseInt(input) - 1;
                chosen = cache.get(i);
                input = "";
            }
            return chosen.nextAsk(input);
        }catch (NumberFormatException | IndexOutOfBoundsException e){
            return new String[]{"return","Please choose a number between 1 and "+cache.size()+". Or type 'n'"};
        }
    }

    private static String getResponse(List<Node> resp) {
        if(resp.size()==0){
            return null;
        }
        cache.addAll(resp);
        if(resp.size()==1){
            return "Did you mean \'"+resp.get(0).question()+"\'?";
        }
        StringBuilder temp1 = new StringBuilder();
        temp1.append("Did you mean:\n");
        for (int i = 0; i < resp.size(); i++) {
            temp1.append("\t").append(i+1).append(". ").append(resp.get(i).question()).append("?\n");
        }
        return temp1.toString();
    }

    public static List<Node> check(List<List<List<CYKNode>>> results){
        List<CYKNode> unused = new ArrayList<>();
        for (List<List<CYKNode>> row : results) {
            for (List<CYKNode> col : row) {
                CYKNode temp = null;
                boolean used = false;
                for (CYKNode node : col) {
                    if(node.used){
                        used = true;
                    }else{
                        temp = node;
                    }
                }
                if(!used && temp!=null){
                    unused.add(temp);
                }
            }
        }
        return new ArrayList<>(complete(unused, bottomRowPermutations(results.get(0))));
    }
    private static Set<Node> complete(List<CYKNode> unused, List<List<CYKNode>> permutations){
        Set<Node> acceptable = new HashSet<>();
        for (List<CYKNode> l : permutations) {
            List<String> l1 = new ArrayList<>();
            l.forEach(n -> l1.add(n.id));
            HashMap<String, String> map = new HashMap<>();
            l.forEach(n -> map.put(n.id, n.correspondence));
            for (CYKNode node : unused) {
                HashSet<String> s = possibleMissingInfo(node.id);
                for (String s1 : s) {
                    Set<String> r = getAllPossible(s1);
                    TreeSet<Node> sorted = new TreeSet<>();
                    for (String opt : r) {
                        String[] t = opt.split(" ");
                        List<String> toFillIn = recurse(Arrays.asList(t), new ArrayList<>(l1), new ArrayList<>());
                        if (toFillIn != null) {
                            sorted.add(new Node(Arrays.asList(t), toFillIn, map));
                        }
                    }
                    if(sorted.size()!=0) {
                        acceptable.add(sorted.first());
                    }
                }
            }
        }
        return acceptable;
    }
    private static List<List<CYKNode>> bottomRowPermutations(List<List<CYKNode>> botRow){
        List<List<CYKNode>> l = new ArrayList<>();
        for (List<CYKNode> n : botRow) {
            List<List<CYKNode>> temp = new ArrayList<>();
            for (CYKNode s : n) {
                if (l.size() == 0) {
                    temp.add(Collections.singletonList(s));
                }else{
                    for (List<CYKNode> s1 : l) {
                        List<CYKNode> t = new ArrayList<>(s1);
                        t.add(s);
                        temp.add(t);
                    }
                }
            }
            l = temp;
        }
        return l;
    }
    /**
     * @param id that you want to make original rule out of
     * @return Hashmap of all original rules that could be made from given ID and what would be needed to make it
     */
    static HashSet<String> possibleMissingInfo(String id){
        return possibleMissingInfo(id, new HashSet<>());
    }
    private static HashSet<String> possibleMissingInfo(String id, HashSet<String> results){
        if(!id.endsWith(CNFConverter.converterID+">")){
            results.add(id);
            return results;
        }
        HashSet<String> valid = validOptions(id);
        for (String s : valid) {
            String makes = CFGSystem.dataBase.rulesForOption(s).first().id;
            possibleMissingInfo(makes, results);
        }
        return results;
    }
    private static HashSet<String> validOptions(String id){
        HashSet<String> valid = new HashSet<>();
        Set<String> options = CFGSystem.dataBase.keySet(false);
        for (int i = 0; i < CFGSystem.dataBase.grammarSize(); i++) {
            for (String option : options) {
                if(option.contains(id)){
                    valid.add(option);
                }
            }
        }
        return valid;
    }
    private static Set<String> getAllPossible(String opt){
        if(!opt.matches(".*?<\\w+>.*")){
            return Collections.singleton(opt);
        }
        Set<String> temp = new HashSet<>();
        if(opt.matches("<\\w+>")){
            CNFRule r = CFGSystem.dataBase.rule(opt);
            for (int i = 0; i < r.size(); i++) {
                if(r.get(i).equals("")){
                    continue;
                }
                if(!r.get(i).matches(".*?<\\w+>.*")){
                    temp.add(r.id);
                }else {
                    Set<String> temp1 = getAllPossible(r.get(i));
                    temp.addAll(temp1);
                }
            }
        }
        if(opt.matches("<\\w+><\\w+>")){
            String[] split = opt.replace("><","> <").split(" ");
            temp.addAll(getAllPossible(split[0]));
            Set<String> temp2 = getAllPossible(split[1]);
            Set<String> temp1 = new HashSet<>();
            for (String s : temp) {
                for (String s1 : temp2) {
                    temp1.add((s+" "+s1).trim());
                }
            }
            temp = temp1;
        }
        return temp;
    }
    private static List<String> recurse(List<String> toCheck, List<String> notFound, List<String> toFillIn){
        if(notFound.size()==0){
            toFillIn.addAll(toCheck);
            return toFillIn;
        }
        for (int i = 0; i < toCheck.size(); i++) {
            if(toCheck.get(i).equals(notFound.get(0))) {
                List<String> temp = new ArrayList<>(toFillIn);
                temp.addAll(toCheck.subList(0, i));
                List<String> toFillIn1 = recurse(toCheck.subList(i + 1, toCheck.size()), notFound.subList(1, notFound.size()), temp);
                if(toFillIn1!=null){
                    return toFillIn1;
                }
            }
        }
        return null;
    }
}
