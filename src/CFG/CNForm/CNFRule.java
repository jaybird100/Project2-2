package CFG.CNForm;

import java.util.ArrayList;
import java.util.List;

public class CNFRule implements Comparable<CNFRule> {
    public final String id;
    int index;
    List<String> options = new ArrayList<>();

    public CNFRule(String id) {
        this.id = id;
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
        return options.get(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(index).append(")").append(id);
        sb.append(" -> ");
        for (int i = 0; i < options.size(); i++) {
            if (i != 0) {
                sb.append(" | ");
            }
            sb.append(options.get(i));
        }
        return sb.toString();
    }

    @Override
    public int compareTo(CNFRule o) {
        return o.index - index;
    }
}