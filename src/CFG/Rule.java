package CFG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {

    public String id;//rhs
    public int index;
    private final List<String> replacements; // *id* can be replaced by any in sentences

    public Rule(String id, List<String> replacements) {
        this.id = id;
        this.replacements = replacements;
    }

    public Rule(String id) {
        this(id, new ArrayList<>());
        this.id = id;
    }

    public Rule(Rule cpy) {
        this(cpy.id, new ArrayList<>(cpy.replacements));
    }

    public void add(String toAdd) {
        if (replacements.contains(toAdd)) {
            return;
        }
        replacements.add(toAdd);
    }

    public void add(List<String> toAdd) {
        for (String s : toAdd) {
            add(s);
        }
    }

    public List<String> replacements() {
        return replacements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(index).append(")").append(id).append(" ");
        sb.append("-> ");
        for (int i = 0; i < replacements.size(); i++) {
            if (i != 0) {
                sb.append("|");
            }
            sb.append(replacements.get(i));
        }
        return sb.toString();
    }
    
    //S->AB    [S,AB]
    public String[] getArrayRep(){
        String[] res=new String[replacements.size()+1];//num replacements+ id
        res[0]=id;
        for(int i=1;i< res.length;i++){
            res[i]=replacements.get(i-1);
        }
        return res;
    }

}