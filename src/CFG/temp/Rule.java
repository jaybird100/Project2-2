package CFG.temp;

import java.util.ArrayList;
import java.util.List;

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
        sb.append("\n");
        return sb.toString();
    }

    protected void setId(String i){this.id=i;}
    //S->AB    [S,AB]
    public String[] getArrayRep(){
        String[] res=new String[replacements.size()+1];//num replacements+ id
        res[0]=id;
        for(int i=1;i< res.length;i++){
            res[i]=replacements.get(i-1);
        }
        return res;
    }

    public boolean isTerminalRule(){
        for(String s: replacements){
            if(s.contains("<")){
                return false;
            }
        }

        return true;
    }


    @Override
    public boolean equals(Object other){
        if(other instanceof Rule){
            Rule o= (Rule) other;
            if(o.id.equals(id) && o.replacements().size()==replacements.size()){
                for(int i=0;i<replacements.size();i++){
                    if(!replacements.get(i).equals(o.replacements().get(i))){
                        return false;
                    }
                }

            }
        }else
            return false;
        return true;
    }
    @Override
    public int hashCode(){
        return replacements.size();
    }

}