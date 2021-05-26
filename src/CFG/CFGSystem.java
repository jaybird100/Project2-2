package CFG;

import CFG.CNForm.*;
import CFG.Helper.RegexHelper;
import CFG.temp.FileParser;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

//TODO
// 1. To speed formatInput approximation up:
//      -create sorted list of all possible options and stop once you reach anything with <
//      -extra speed: if sorted, look for perfect match. If found, end search else get similarity

public class CFGSystem {

    public final static CNFDataBase dataBase = new CNFDataBase();
    public static double similarityNeeded = 0.7;
    public static int print = 0;
    // 0 = no print,
    // 1 = print only original rules,
    // 2 = print all including rules added by CNF conversion
    // 3 = print results
    private static boolean approximate = false;

    /**
     * Approximate input + data types
     * @param b true for on, false for off
     */
    public static void fullFeatures(boolean b){
        useDataTypesInSearch(b);
        approximateInputWords(b);
    }

    /**
     * Make the parsing of the input try to match with the logic propositions in the grammar
     * automatically true
     */
    public static void useDataTypesInSearch(boolean b){
        dataBase.advanced(b);
    }
    /**
     * If set to true, formatInput() will look for similarities in all
     * @param b true to check for similarities, false for not
     */
    public static void approximateInputWords(boolean b){
        approximate = b;
    }
    public static void load(String skill){
        CNFConverter.loadAsCNF(skill, dataBase);
    }
    public static void load(File f){
        CNFConverter.loadAsCNF(FileParser.loadFile(f), dataBase);
    }

    /**
     * Executes CYK search on input, then parses the results of the CYK to final return the response of what was said
     * (if the input is in the grammar)
     * @param input string to analyse
     * @return response from grammar
     */
    public static String run(String input){
        List<TreeSet<String>> words = formatInput(input);
        List<List<List<CYKNode>>> r = CYK(words);
        List<HashMap<String, String>> map = parseResults(r);
        List<CNFMatch> m = findMatches(map);
        switch(print){
            case 0:break;
            case 1:printResultsAsMatrix(r, true);
            case 2:printResultsAsMatrix(r, false);
            case 3:map.forEach(System.out::println);
            case 4:m.forEach(System.out::println);
        }
        return getResponse(m);
    }

    /**
     * Converts input to appropriate format:
     *  -special characters become their name
     *  -input is split by spaces
     *  -everything is put into a List of TreeSets
     *
     * If approximateInput is true, will look for similarities in all possible options
     * (similarityNeeded % of similarity needed to flag 2 words as similar)
     * @param input string to format
     * @return formatted input string
     */
    private static List<TreeSet<String>> formatInput(String input){
        String[] words = RegexHelper.convertToSplitFriendly(input.toLowerCase()).split("\\s+");
        List<TreeSet<String>> w = new ArrayList<>();
        for(int i=0; i<words.length;i++){
            w.add(new TreeSet<>());
            w.get(i).add(words[i]);
            if(approximate) {
                for (String s : dataBase.keySet()) {
                    double diff = RegexHelper.levenshteinDifference(words[i], s);
                    if (similarityNeeded <= diff) {
                        w.get(i).add(s);
                    }
                }
            }
        }
        return w;
    }

    // CYK

    private static List<List<List<CYKNode>>> CYK(List<TreeSet<String>> words){
        List<List<List<CYKNode>>> w = new ArrayList<>();
        w.add(new ArrayList<>());
        for (TreeSet<String> word : words) { // INIT first 2 rows (row 0 = input, row 1 = unit rules)
            ArrayList<CYKNode> l = new ArrayList<>();
            for (String s : word) {
                TreeSet<CNFRule> rules = dataBase.rulesForOption(s);
                for (CNFRule r : rules) {
                    l.add(new CYKNode(r.id, s, null));
                }
            }
            w.get(0).add(l);
        }
        for (int i = 1; i < words.size(); i++) { // For every row (iteration)
            w.add(new ArrayList<>());
            for (int j = 0; j < words.size()-i; j++) { // For every col (rules found)
                w.get(i).add(new ArrayList<>());
                addAcceptedCombinations(w, i, j); // Add all permutations accepted by the grammar for words array [j] to [j+i-1] (-1 bc the algo starts at index 2)
            }
        }return w;
    }
    /**
     * Adds all combinations that position [row, col] can take given input string and grammar
     * to working memory
     * @param w working memory
     * @param row index
     * @param col index
     */
    private static void addAcceptedCombinations(List<List<List<CYKNode>>> w, int row, int col){
        List<int[][]> idxCombinations = getIdxCombinations(row, col); // Every permutation that row i, col j
        for (int[][] idx : idxCombinations) {
            int[] idx1 = new int[]{ idx[0][0], idx[0][1]};
            int[] idx2 = new int[]{ idx[1][0], idx[1][1]};
            for (CYKNode node1 : w.get(idx1[0]).get(idx1[1])) {
                for (CYKNode node2 : w.get(idx2[0]).get(idx2[1])) {
                    String combo = node1.id+node2.id;
                    TreeSet<CNFRule> rules = dataBase.rulesForOption(combo);
                    for (CNFRule r : rules) {
                        w.get(row).get(col).add(new CYKNode(r.id,combo, new CYKNode[]{node1, node2}));
                    }
                }
            }
        }
    }
    /**
     * Working your way down from row (start at k=1, up to row-k = 1)
     * 1st part = [row-k, col]: determines the height AKA how far from current col you look
     * 2nd part = [k, col+row-k-2]: determines what needs to be compensated for the loss of height on first part
     * @param row current row
     * @param col current col
     * @return list of all index Combinations
     */
    private static List<int[][]> getIdxCombinations(int row, int col){
        List<int[][]> idxCombinations = new ArrayList<>();
        for(int k=1; k<=row; k++) {
            idxCombinations.add(new int[][]{{row-k, col}, {k-1, col+row-k+1}});
        }
        return idxCombinations;
    }


    //Extracting IDs and their equalities from Result

    private static List<HashMap<String, String>> parseResults(List<List<List<CYKNode>>> r){
        List<HashMap<String, String>> maps = new ArrayList<>();
        for (List<List<CYKNode>> l1 : r) {
            for (List<CYKNode> l2 : l1) {
                for (CYKNode cykNode : l2) {
                    if(cykNode.id.equals("<s>")){
                        HashMap<String, String> map = new HashMap<>();
                        recursiveAdd(map, cykNode);
                        for (CYKNode node : l2) {
                            if(node!=cykNode){
                                map.put(node.id, map.get("<s>"));
                            }
                        }
                        maps.add(map);
                    }
                }
            }
        }
        return maps;
    }
    private static String recursiveAdd(HashMap<String, String> map, CYKNode node){
        if(node.nodes==null){
            if(node.id.endsWith(CNFConverter.converterID+">")){
                return node.correspondence;
            }
            map.put(node.id, node.correspondence.trim());
            return node.id;
        }
        for (CYKNode cykNode : node.nodes) {
            String s = recursiveAdd(map, cykNode);
            node.correspondence = node.correspondence.replace(cykNode.id, s+" ");
        }
        if(!node.id.endsWith(CNFConverter.converterID+">")) {
            map.put(node.id, node.correspondence.trim());
        }
        return node.correspondence;
    }

    // Getting response from CYK results

    private static List<CNFMatch> findMatches(List<HashMap<String, String>> maps){
        List<CNFMatch> matches = new ArrayList<>();
        for (HashMap<String, String> map : maps) {
            Set<CNFAction> possible = new LinkedHashSet<>();
            for (String s : map.keySet()) {
                List<CNFAction> a = dataBase.actionForRule(s);
                if (a != null) {
                    possible.addAll(a);
                }
            }
            List<CNFMatch> m = possible.stream().map(action -> action.matches(map)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (CNFMatch m1 : m) {
                if(m1.isValid()){
                    matches.add(m1);
                }else{
                    break;
                }
            }
        }
        return matches;
    }
    private static String getResponse(List<CNFMatch> confirmedMatches){
        if(confirmedMatches.size()==0){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(confirmedMatches.get(0).response());
        for (int i = 1; i < confirmedMatches.size(); i++) {
            if(i!=confirmedMatches.size()-1){
                sb.append(", ");
            }else{
                sb.append(" and ");
            }
            sb.append(confirmedMatches.get(i).response());
        }
        return sb.append(".").toString().trim();
    }

    // Printing

    private static void printResultsAsMatrix(List<List<List<CYKNode>>> w, boolean removeCNF){
        int max =0;
        boolean one = true;
        for (List<List<CYKNode>> rows : w) {
            for (List<CYKNode> point : rows) {
                int size;
                if(one){
                    size = point.stream().mapToInt(n -> (n.toString().length() + 1)).sum();
                }
                else {
                    size = point.stream().mapToInt(n -> (n.id.length() + 1)).sum();
                }
                if (size > max) {
                    max = size;
                }
            }
            one = false;
        }
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < max; i++) {
            line.append("_");
        }
        one = true;
        for (List<List<CYKNode>> strings : w) {
            StringBuilder l1 = new StringBuilder();
            for (List<CYKNode> string : strings) {
                StringBuilder s = new StringBuilder();
                if(string.size()!=0) {
                    if(one){
                        string.forEach(n -> s.append(n.toString()).append(","));
                    }else {
                        string.forEach(n -> s.append(n.id).append(","));
                    }
                    s.delete(s.length() - 1, s.length());
                }
                while(s.length()<max){
                    s.append(" ");
                }
                System.out.print(s+"|");
                l1.append("_").append(line);
            }
            one = false;
            System.out.println("\n"+l1);
        }
    }

}

class CYKNode{
    final String id;
    String correspondence;
    final CYKNode[] nodes;
    CYKNode(String id, String correspondence, CYKNode[] nodes){
        this.id = id;
        this.correspondence = correspondence;
        this.nodes = nodes;
    }
    public String toString(){
        return id+"="+correspondence;
    }
}
