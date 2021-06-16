package CFG;

import CFG.CNForm.CNFConverter;
import CFG.CNForm.CNFRule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
    1.If ChatBot dialogue happening -> ChatBot.dialogue()
    2.Apply CYK on input
    3.If(!has final result(s))
        give reigns to ChatBot until dialogue over
    4.return getResponse()

    ChatBot Initiation:
    1.For all order of unit rules in grammar o get similarity of o with given unit rules in he bottom row (order matters)
        Optimization: Only orders that have unit rules already in the input
    2.If similarity of o big enough to be considered, ask if that is what is meant
    3.Save chosen o

    ChatBot Dialogue:
    1.For every piece of info that could be assumed, assume it and ask if it is correct
    2.For every piece of info that needs external input, ask what was meant
    3.Go to step 1. with completed o
*/
public class ChatBot {

    private static final Set<String> cancelCommands = new HashSet<>(Arrays.asList("n", "no", "nope", "cancel"));
    private static final Set<String> acceptCommands = new HashSet<>(Arrays.asList("y", "yes", "yep", "ye", "yeah"));
    private static List<ChatBotOption> memory;
    private static ChatBotOption chosen;
    public static boolean isActive(){
        return memory!=null;
    }
    public static void deactivate(){
        memory = null;
        chosen = null;
    }

    /*
    ChatBot Initiation:
    1.For all order of unit rules in grammar o get similarity of o with given unit rules in he bottom row (order matters)
    Optimization: Only orders that have unit rules already in the input
    2.If similarity of o big enough to be considered, save to memory
    3.return asking which one was meant
    */
    public static String initiate(List<List<List<CYKNode>>> r){
        HashMap<String, List<String>> map = toHashMap(r);
        // Get every option
        Set<ChatBotOption> possible = getPossibilities(map);
        // Get every permutations of the bottom row
        List<List<CYKNode>> botRowPermutations = permutations(r.get(0));
        // For every permutations of the bottom row and every option, set similarity
        botRowPermutations.forEach(perm ->{
            List<String> temp = perm.stream().map(n->n.id).collect(Collectors.toList());
            possible.forEach(opt -> opt.setSimilarity(temp));
        });
        // Filter for 70% similarity
        List<ChatBotOption> options = possible.stream().filter(o -> o.similarity()>0.7).collect(Collectors.toList());
        // Fill options with known/derivable info
        options.forEach(o -> o.fillUnknown(map));
        if(options.size()==0){
            return null;
        }
        memory = options;
        return getResponse(memory);
    }

    private static HashMap<String, List<String>> toHashMap(List<List<List<CYKNode>>> r){
        HashMap<String, List<String>> map = new HashMap<>();
        r.forEach(row -> row.forEach(cell -> cell.forEach(node ->{
            if(!map.containsKey(node.id)){
                map.put(node.id, new ArrayList<>());
            }
            map.get(node.id).add(node.fullCorrespondence);
        })));
        return map;
    }

    private static Set<ChatBotOption> getPossibilities(HashMap<String, List<String>> map){
        // Get the parent rules for every rule found from input
        Set<String> possibleRoots = new HashSet<>();
        map.keySet().forEach(id -> addRoots(id, possibleRoots));
        // Get all branches that could be made from each parent rule
        Set<String> branches = new HashSet<>();
        possibleRoots.forEach(id -> branches.addAll(getBranches(id)));
        // Fill all branches with what is known/can be assumed
        Set<ChatBotOption> options = new HashSet<>();
        branches.forEach(branch -> options.add(new ChatBotOption(Arrays.asList(branch.split(" ")))));
        return options;
    }
    private static void addRoots(String id, Set<String> results){
        if(!id.endsWith(CNFConverter.converterID+">")){
            results.add(id);
            return;
        }
        Set<String> valid = validOptions(id);
        for (String s : valid) {
            String makes = CFGSystem.dataBase.rulesForOption(s).first().id;
            addRoots(makes, results);
        }
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
    private static Set<String> getBranches(String opt){
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
                    temp.addAll(getBranches(r.get(i)));
                }
            }
        }
        if(opt.matches("<\\w+><\\w+>")){
            String[] split = opt.replace("><","> <").split(" ");
            temp.addAll(getBranches(split[0]));
            Set<String> temp1 = new HashSet<>();
            Set<String> temp2 = getBranches(split[1]);
            for (String s : temp) {
                for (String s1 : temp2) {
                    temp1.add((s+" "+s1).trim());
                }
            }
            temp = temp1;
        }
        return temp;
    }

    private static List<List<CYKNode>> permutations(List<List<CYKNode>> botRow){
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

    private static String getResponse(List<ChatBotOption> opt){
        if(opt.size()==0){
            return null;
        }
        if(opt.size()==1){
            return "Did you mean '"+opt.get(0).filled()+"'?";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Did you mean: ");
        AtomicInteger i = new AtomicInteger(1);
        opt.forEach(o -> sb.append("\n\t").append(i.getAndIncrement()).append(".").append(o.filled()));
        return sb.toString();
    }
    /*
    ChatBot Dialogue:
    1.For every piece of info that could be assumed, assume it and ask if it is correct
    2.For every piece of info that needs external input, ask what was meant
    3.Go to step 1. with completed o
     */

    /**
     * @param input to analyse
     * @return dialogue stuff or null if nothing
     */
    public static String dialogue(String input) {
        if(signalsCanceling(input)){
            deactivate();
            return "";
        }
        if(chosen==null){
            int idx = getChosenIndex(input);
            if(idx==-1 || idx>memory.size()){
                return "Please give a number between 1 and "+memory.size();

            }
            chosen = memory.get(idx);
        }
        List<String> missing = chosen.missing();
        if(missing.size()!=0){
            if(CFGSystem.dataBase.rule(missing.get(0)).accepts(input)){
                chosen.fill(missing.get(0), input);
            }
            if(missing.size()!=0){
                return missing.get(0)+"?";
            }
        }
        String r = chosen.filled();
        deactivate();
        return r;
    }

    private static boolean signalsCanceling(String input) {
        return cancelCommands.contains(input);
    }
    private static int getChosenIndex(String input) {
        if(memory.size()==1 && acceptCommands.contains(input)){
            return 0;
        }
        if(input.matches("[0-9]+")){
            return Integer.parseInt(input)-1;
        }
        return -1;
    }

    static class ChatBotOption{
        private double similarity;
        private final List<String> order;
        private String filled;
        protected Edits e;
        public ChatBotOption(List<String> branch){
            this.order = branch;
            filled ="";
            order.forEach(id -> filled+= id+" ");
            filled = filled.trim();
        }

        /**
         * Order matters. Gets smallest edit distance for each possible branch and normalize it.
         * @param botRowOrder order to test
         */
        public void setSimilarity(List<String> botRowOrder) {
            TreeSet<Edits> l = new TreeSet<>();
            similarity(order, botRowOrder, l, new Edits(), Integer.MAX_VALUE);
            if(l.size()!=0 && (e==null || e.edits()>l.first().edits())) {
                e = l.first();
                similarity = 1 - e.edits() / (double) (order.size() + botRowOrder.size());
            }
        }

        /**
         * Order matters
         * @param o list of IDs left to find
         * @param botRowOrder list of IDs left to check
         * @param list of all branch explored
         * @param cur all edits for cur branch
         * @param minEdits optimization feature. If current branch will lead to more edits than the cur minimum, skip it
         * @return min nb of edits found in all branches explored
         */
        private int similarity(List<String> o, List<String> botRowOrder, Set<Edits> list, Edits cur, int minEdits) {
            if(botRowOrder.size()==0){
                cur.add(o, null);
                list.add(cur);
                return cur.edits();
            }
            for (int j = 0; j < o.size(); j++) {
                for (int i = 0; i < botRowOrder.size(); i++) {
                    if (botRowOrder.get(i).equals(o.get(j))) {
                        Edits branch = new Edits(cur);
                        branch.add(o.subList(0, j), botRowOrder.subList(0, i));
                        if(branch.edits()<minEdits){
                            minEdits = Math.min(minEdits, similarity(o.subList(j+1, o.size()), botRowOrder.subList(i + 1, botRowOrder.size()), list, branch, minEdits));
                        }
                    }
                }
            }
            return minEdits;
        }
        public void fillUnknown(HashMap<String, List<String>> map) {
            for (String s : order) {
                if(map.containsKey(s)){
                    fill(s, map.get(s).get(0));
                }
                else if(CFGSystem.dataBase.rule(s).size()==1){
                    fill(s, CFGSystem.dataBase.rule(s).get(0));
                }
            }
        }
        public void fill(String id, String corresponding){
            filled = filled.replace(id, corresponding);
            e.found(id);
        }

        public List<String> missing() {
            return e.missing();
        }

        public double similarity(){
            return similarity;
        }
        public String filled(){
            return filled;
        }
        public String toString(){
            StringBuilder sb = new StringBuilder();
            order.forEach(sb::append);
            sb.append(" -> ").append(filled);
            sb.append(" -> ").append(e);
            return sb.toString();
        }
        static class Edits implements Comparable<Edits>{
            private final List<String> added;
            private final List<String> skipped;
            private int edits;
            public Edits(List<String> added, List<String> skipped){
                this.added = new ArrayList<>(added);
                this.skipped = new ArrayList<>(skipped);
                edits+= added.size();
                edits+= skipped.size();
            }
            public Edits(){
                this(new ArrayList<>(), new ArrayList<>());
            }
            public Edits(Edits e){
                this(e.added, e.skipped);
            }

            public void add(List<String> added ,List<String> skipped){
                if(added!=null){
                    this.added.addAll(added);
                    edits+= added.size();
                }
                if(skipped!=null){
                    this.skipped.addAll(skipped);
                    edits+= skipped.size();
                }
            }
            public void found(String id){
                if(added.remove(id)) edits--;
            }

            public List<String> missing(){
                return added;
            }
            public int edits(){
                return edits;
            }
            public String toString(){
                return "Changed: "+edits+", Ask for: "+added+", Skipped: "+skipped;
            }

            @Override
            public int compareTo(Edits o) {
                if(edits == o.edits){
                    return 1;
                }
                return Integer.compare(edits, o.edits);
            }
        }
    }
}
