package CFG.Helper;

import CFG.CNForm.CNFConverter;
import CFG.CNForm.CNFDataBase;
import CFG.CNForm.CNFRule;
import CFG.temp.FileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

//TODO make generate method that can create utterances that aren't accepted by grammar
// Disclaimer: letting loadAsCNF do the del and unit rules will give some rules way bigger odds of happening.
// Method added to allows loadAsCNF to skip those rules. Generator(File, Integer) automatically skips them as well.
public class Generator {
    private final Random rd;
    private final CNFDataBase db;

    /**
     * @param db database you want to generate utterances for
     * @param seed for randomizer (if null = no seed)
     */
    public Generator(CNFDataBase db, Integer seed){
        if(seed==null){
            rd = new Random();
        }else {
            rd = new Random(seed);
        }
        this.db = db;
    }
    /**
     * @param fileOfSkill skill file you want to generate utterances for
     * @param seed for randomizer (if null = no seed)
     */
    public Generator(File fileOfSkill, Integer seed){
        this(CNFConverter.loadAsCNF(FileReader.loadFile(fileOfSkill),new CNFDataBase() ,false, false), seed);
    }

    /**
     * @return 1 random utterance from whole grammar
     */
    public String generate(){
        return generate("<s>");
    }
    /**
     * Recursive/branch/tree parsing from root rule down to unit rules
     * @param rule to start from
     * @return 1 random utterance (ex: if <schedule> rule passed, will only return random schedule utterances)
     */
    public String generate(String rule){
        CNFRule r = db.rule(rule);
        String opt = r.get(rd.nextInt(r.size()));
        //If opt == logic do something
        if(opt.matches("<%.+%>")){
            System.out.println("Logical rules not supported. Check rule "+rule);
            return opt;
        }
        if(opt.equals("*epsilon*")){
            opt = "";
        }
        if(!opt.matches(".*<\\w+>.*")){
            return opt;
        }
        List<String> keys = RegexHelper.extract(opt, "<\\w+>");
        for (String key : keys) {
            String rep = generate(key);
            opt = opt.replace(key, rep+" ");
        }
        return opt.trim();
    }

    /**
     * Generate multiple utterances from whole grammar. Helper method.
     * @param nb of utterances to generate
     * @return list of generated utterances
     */
    public List<String> generate(int nb){
        return generate(nb, "<s>");
    }
    /**
     * Generate multiple utterances. Helper method.
     * @param nb of utterances to generate
     * @param root where to generate them from
     * @return list of generated utterances
     */
    public List<String> generate(int nb, String root){
        List<String> l = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            l.add(generate(root));
        }
        return l;
    }

    public static void write(List<String> utterances, String fileName){
        try (PrintWriter writer = new PrintWriter("res/"+fileName+".csv")) {
            StringBuilder sb = new StringBuilder();
            utterances.forEach(u -> sb.append(u).append('\n'));
            writer.write(sb.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void histogram(List<String> utterances){
        HashMap<String, Integer> counts = new HashMap<>();
        for (String s : utterances) {
            if(!counts.containsKey(s)){
                counts.put(s, 0);
            }
            counts.put(s, counts.get(s)+1);
        }
        counts.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));
    }

}
