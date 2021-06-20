package CFG.CNForm;

import CFG.CFGSystem;

import java.util.ArrayList;
import java.util.List;
//TODO make options a Set not a list??
public class CNFRule implements Comparable<CNFRule> {
    public final String id;
    public final boolean fromConversion;
    int extraRules;
    int index;
    List<String> options = new ArrayList<>();

    public CNFRule(String id) {
        extraRules = 0;
        this.id = id;
        fromConversion = id.endsWith(CNFConverter.converterID+">");
    }

    public void add(String toAdd) {
        if (options.contains(toAdd)) {
            return;
        }
        options.add(toAdd);
    }

    public void add(List<String> toAdd) {
        for (String s : toAdd) {
            add(s);
        }
    }

    public int size(){
        return options.size();
    }

    public String get(int i){
        if(options.get(i).equals("*epsilon*")){
            return "";
        }
        return options.get(i);
    }

    public String simpleString(){
        StringBuilder sb= new StringBuilder();
        sb.append(id).append(" -> ");
        for (int i = 0; i < options.size(); i++) {
            if (i != 0) {
                sb.append(" | ");
            }
            sb.append(expand(options.get(i)));
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("idx=").append(index).append(", ");
        if(!fromConversion){
            sb.append("extra=").append(extraRules).append(", ");
        }
        sb.append(simpleString());
        return sb.toString();
    }

    @Override
    public int compareTo(CNFRule o) {
        return o.index - index;
    }

    public boolean accepts(String input) {
        return options.contains(input);
    }

    public static String expand(String opt){
        if(opt.matches("<\\w+?"+CNFConverter.converterID+">")){
            return expand(CFGSystem.dataBase.rule(opt).get(0));
        }
        if(opt.matches("<\\w+><\\w+>")){
            String[] a = opt.replace("><","> <").split(" ");
            return expand(a[0])+" "+expand(a[1]);
        }
        return opt;
    }
}