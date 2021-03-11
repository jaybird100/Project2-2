package CFG;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    public String id;
    private final List<String> replacements; // *id* can be replaced by any in sentences

    private Rule(String id, List<String> replacements){
        this.id = id;
        this.replacements = replacements;
    }
    public Rule(String id){
        this(id, new ArrayList<>());
        this.id = id;
    }
    public Rule(Rule cpy){
        this(cpy.id, new ArrayList<>(cpy.replacements));
    }

    public void add(String toAdd){
        if(replacements.contains(toAdd)){
            return;
        }
        replacements.add(toAdd);
    }

    public void addAll(List<String> toAdd){
        for (String s : toAdd) {
            add(s);
        }
    }

    public List<String> replacements(){
        return replacements;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(" ");
        sb.append("-> ");
        for (int i = 0; i < replacements.size(); i++) {
            if(i!=0){
                sb.append("|");
            }
            sb.append(replacements.get(i));
        }
        return sb.toString();
    }
}