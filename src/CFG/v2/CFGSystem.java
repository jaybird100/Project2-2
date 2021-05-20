package CFG.v2;

import java.util.*;
import java.util.stream.Collectors;

//TODO
// 1. To speed formatInput approximation up:
//      -create sorted list of all possible options and stop once you reach anything with <
//      -extra speed: if sorted, look for perfect match. If found, end search else get similarity
// 2. Redo parseResult bc it's a shit show

public class CFGSystem {

    public final static CNFDataBase dataBase = new CNFDataBase();
    public static double similarityNeeded = 0.7;
    private static boolean approximate = false;

    /**
     * Approximate input + data types
     * @param b true for on, false for off
     */
    public static void fullFeatures(boolean b){
        approximate = b;
        dataBase.rdb.advanced = b;
    }

    /**
     * Make the parsing of the input try to match with the logic propositions in the grammar
     * automatically true
     */
    public static void useDataTypesInSearch(boolean b){
        dataBase.rdb.advanced = b;
    }
    /**
     * If set to true, formatInput() will look for similarities in all
     * @param b true to check for similarities, false for not
     */
    public static void approximateInputWords(boolean b){
        approximate = b;
    }

    /**
     * Executes CYK search on input, then parses the results of the CYK to final return the response of what was said
     * (if the input is in the grammar)
     * @param input string to analyse
     * @param printSearch 0 = no print, 1 = print only original grammar, 2 = print all including rules added by CNF conversion
     * @return response from grammar
     */
    public static String run(String input, int printSearch){
        List<TreeSet<String>> words = formatInput(input);
        List<List<List<String>>> r = CYK(words);
        List<HashMap<String, List<String>>> map = parseResultMultiple(r);
        List<Matchv2> m = findMatches(map);
        switch(printSearch){
            case 0:break;
            case 1:printResultsAsMatrix(r, true);break;
            case 2:printResultsAsMatrix(r, false);break;
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
                for (String s : dataBase.rdb.optionToRule.keySet()) {
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

    private static List<List<List<String>>> CYK(List<TreeSet<String>> words){
        List<List<List<String>>> w = new ArrayList<>();
        w.add(new ArrayList<>());
        w.add(new ArrayList<>());
        for (TreeSet<String> word : words) { // INIT first 2 rows (row 0 = input, row 1 = unit rules)
            w.get(0).add(new ArrayList<>(word));
            TreeSet<Rulev2> t = new TreeSet<>();
            for (String s : word) {
                t.addAll(dataBase.rulesForOption(s));
            }
            w.get(1).add(RuleDataBase.ruleIDs(t));
        }
        for (int i = 2; i < words.size()+1; i++) { // For every row (iteration)
            w.add(new ArrayList<>());
            for (int j = 0; j < words.size()-i+1; j++) { // For every col (rules found)
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
    private static void addAcceptedCombinations(List<List<List<String>>> w, int row, int col){
        List<int[][]> idxCombinations = getIdxCombinations(row, col); // Every permutation that row i, col j
        for (int[][] idx : idxCombinations) {
            List<String> combo = getIDCombinations(w, idx, "");
            for (String s : combo) {
                w.get(row).get(col).addAll(RuleDataBase.ruleIDs(dataBase.rulesForOption(s))); // Add all rule IDs that combo s can take
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
        for(int k=1; k<row; k++) {
            idxCombinations.add(new int[][]{{row-k, col}, {k, col+row-(k-1)-1}});
        }
        return idxCombinations;
    }
    /**
     * Combine all IDs in position idx[0] to all IDs in position idx[1] in a way that keeps order
     * @param w current working memory
     * @param idx indices of first and second part
     * @return list of all ID combinations (order is preserved)
     */
    private static List<String> getIDCombinations(List<List<List<String>>> w, int[][] idx, String separator){
        if (w.get(idx[0][0]).get(idx[0][1]).size() == 0 || w.get(idx[1][0]).get(idx[1][1]).size() == 0) {
            return new ArrayList<>();
        }
        return RegexHelper.orderedCombinations(w.get(idx[0][0]).get(idx[0][1]), w.get(idx[1][0]).get(idx[1][1]), separator);
    }

    //Extracting IDs and their equalities from Result

    private static List<HashMap<String, List<String>>> parseResultMultiple(List<List<List<String>>> s){
        List<HashMap<String, List<String>>> maps = new ArrayList<>();
        for (int i = 1; i < s.size(); i++) {
            for (int j = 0; j < s.get(i).size(); j++) {
                for (String key : s.get(i).get(j)) {
                    if(key.equals("<s>")){
                        maps.add(removeConverterID(parseResult(s, i, j)));
                        break;
                    }
                }
            }
        }
        return maps;
    }
    private static HashMap<String, List<String>> parseResult(List<List<List<String>>> s, int maxSize, int startIndex){
        HashMap<String, List<String>> h = new HashMap<>();
        for (int i = 1; i < s.size(); i++) {
            for (int j = startIndex; j < startIndex+maxSize-i+1; j++) {
                if(s.get(i).get(j).size()!=0){
                    for (String id : s.get(i).get(j)) {
                        if(!h.containsKey(id)) {
                            h.put(id, new ArrayList<>());
                        }
                    }
                    parseResult(s, i, j, h);
                }
            }
        }
        return h;
    }
    private static void parseResult(List<List<List<String>>> w, int i, int j, HashMap<String, List<String>> h){
        if(i==1){
            h.get(w.get(i).get(j).get(0)).add(w.get(0).get(j).get(0));
            return;
        }
        int[][][] c = canBeEqualTo(i, j);
        Set<String> all = new TreeSet<>();
        for (int[][] coords : c) {
            List<String> k1 = w.get(coords[0][0]).get(coords[0][1]);
            List<String> k2 = w.get(coords[1][0]).get(coords[1][1]);
            List<String> s = new ArrayList<>();
            if(k1.size()!=0 && k2.size()!=0){
                for (String key1 : k1) {
                    for (String key2 : k2) {
                        if(key1.endsWith(CNFConverter.converterID+">")){
                            s.addAll(h.get(key1));
                        }else{
                            s.add(key1);
                        }
                        if(key2.endsWith(CNFConverter.converterID+">")){
                            for (String value : s) {
                                for (int z2 = 0; z2 < h.get(key2).size(); z2++) {
                                    all.add(value +" "+ h.get(key2).get(z2));
                                }
                            }
                        }else{
                            for (String value : s) {
                                all.add(value+" "+key2);
                            }
                        }
                    }
                }
            }
        }
        for (String curKey : w.get(i).get(j)) {
            h.get(curKey).addAll(all);
        }
    }
    // Can be merged with for(int[][] coords : c){} loop in parseResult
    private static int[][][] canBeEqualTo(int i, int j){
        int[][][] coords = new int[i-1][2][2];
        for (int k = 1; k < i; k++) {
            coords[k-1][0] = new int[]{i-k, j};
            coords[k-1][1] = new int[]{k, j+i-(k-1)-1};
        }
        return coords;
    }
    private static HashMap<String, List<String>> removeConverterID(HashMap<String, List<String>> m){
        m.keySet().removeIf(certification -> certification.endsWith(CNFConverter.converterID + ">"));
        return m;
    }

    // Getting response from CYK results

    private static List<Matchv2> findMatches(List<HashMap<String, List<String>>> maps){
        List<Matchv2> matches = new ArrayList<>();
        for (HashMap<String, List<String>> map : maps) {
            Set<Actionv2> possible = new LinkedHashSet<>();
            for (String s : map.keySet()) {
                List<Actionv2> a = dataBase.actionForRule(s);
                if (a != null) {
                    possible.addAll(a);
                }
            }
            List<Matchv2> m = possible.stream().map(action -> action.matches(map)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            for (Matchv2 m1 : m) {
                if(m1.isValid()){
                    matches.add(m1);
                }else{
                    break;
                }
            }
        }
        return matches;
    }
    private static String getResponse(List<Matchv2> confirmedMatches){
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

    private static void printResultsAsMatrix(List<List<List<String>>> w, boolean removeCNF){
        w = removeCNF? removeConverterID(w) : w;
        int max =0;
        for (List<List<String>> rows : w) {
            for (List<String> point : rows) {
                int size = point.toString().length();
                if(size>max){
                    max = size;
                }
            }
        }
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < max; i++) {
            line.append("_");
        }

        for (List<List<String>> strings : w) {
            StringBuilder l1 = new StringBuilder();
            for (List<String> string : strings) {
                StringBuilder s = new StringBuilder(string.toString());
                while(s.length()<max){
                    s.append(" ");
                }
                System.out.print(s+"|");
                l1.append("_").append(line);
            }
            System.out.println("\n"+l1);
        }
    }
    /**
     * Should only be used for printing. Messes with parseResult
     * @param w list to remove converter IDs from
     * @return a copy of w that has converter IDs removed
     */
    private static List<List<List<String>>> removeConverterID(List<List<List<String>>> w){
        List<List<List<String>>> temp = new ArrayList<>();
        for (int i = 0; i < w.size(); i++) {
            temp.add(new ArrayList<>());
            for (int j = 0; j < w.get(i).size(); j++) {
                temp.get(i).add(new ArrayList<>());
                for (int k = 0; k < w.get(i).get(j).size(); k++) {
                    if(!w.get(i).get(j).get(k).endsWith(CNFConverter.converterID+">")){
                        temp.get(i).get(j).add(w.get(i).get(j).get(k));
                    }
                }
            }
        }
        return temp;
    }
}
