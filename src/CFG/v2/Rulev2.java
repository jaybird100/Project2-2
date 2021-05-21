package CFG.v2;

import CFG.v2.Comparators.Comparator;
import CFG.v2.Comparators.DoubleComparator;
import CFG.v2.Comparators.IntComparator;
import CFG.v2.Comparators.StringComparator;

import java.util.ArrayList;
import java.util.List;

public class Rulev2 {
    private final String key;
    private final String dtype;
    private final Comparator comparator;
    private List<String> options;
    public Rulev2(String key, String dtype){
        this.key = key;
        this.dtype = dtype==null? "string": dtype;
        options = new ArrayList<>();
        switch (this.dtype){
            case "int":
                comparator = new IntComparator();
                break;
            case "double":
                comparator = new DoubleComparator();
                break;
            default:
                comparator = new StringComparator();
        }
    }
    public void options(List<String> options){
        this.options = options;
    }

    public boolean has(String compare){
        if(!comparator.isSameDType(compare)){
            return false;
        }
        for (String option : options) {
            if(comparator.equals(option, compare)){
                return true;
            }
        }
        return false;
    }
    public String toString(){
        return "key=" + key + " && dtype=" + dtype + " && options: " + options.toString();
    }
}
