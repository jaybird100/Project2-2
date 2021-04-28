package CFG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
    private int keyIndex = 0;
    public String id;
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
//METHODS ADDED FOR CNF_______________________________________
    public ArrayList<Rule> cnfSplit() {
        ArrayList<Rule> newRules = new ArrayList<>();
        String dummy="";
        for (String rep : replacements) {
            int numNonterminal = 0;
            dummy=rep;
            while (dummy.matches("(?s).*<.+>(?s).*")) {
                ++numNonterminal;
                dummy = dummy.substring(dummy.indexOf('>') + 1);
            }
            if(rep.isEmpty()) {
                ArrayList<String> rrr = new ArrayList<>();
                rrr.add(id);
                newRules.add(new Rule(addIndex(id),rrr ));
            } else
                if(numNonterminal==0){
                    //no non terminal terms
                    //split the non terminals
                    newRules.addAll(split(rep));
                }else if (numNonterminal <= 2) {
                    //if there are atmost 2 non terminals
                    //keep the rule as is
                    newRules.add(this);
                    break;
                }else{
                    //there are more than 2 non terminals
                    //split non terminals  A->BCDE to
                    //A->B(A1)
                    //A1->C(A2)
                    //A2->DE
                    newRules.addAll(split(rep, numNonterminal-1));
                }

        }
        System.out.println(newRules);
        System.out.println("________________");
        return newRules;
    }

    private ArrayList<Rule> split(String s){
        ArrayList<Rule> res = new ArrayList<>();
        ArrayList<String> dummyrep = new ArrayList<>();
        dummyrep.add(s);
        res.add(new Rule(addIndex(id), dummyrep));

        return res;
    }
    private ArrayList<Rule> split(String s, int numSplits){
        ArrayList<Rule> res=new ArrayList<>();
        ArrayList<String>dummyRep;
        String[] sep= s.split(">");
        sep=preprocess(sep);
        for(int i= sep.length-1;i>=0;i--){
            dummyRep=new ArrayList<>();
            if(res.size()==0){
                dummyRep.add(sep[i]);
            }else {
                dummyRep.add(sep[i]+res.get(0).id );
            }
            res.add(0,new Rule(addIndex(id,i),dummyRep));
        }
        return res;
    }
    public String[] preprocess(String[] r){
        String[] res= new String[r.length-1];
        for(int i=0;i<r.length-1;i++){
            res[i]=r[i]+">";
        }
        res[res.length-1]+=r[r.length-1]+">";

        return res;
    }

    private String addIndex(String id) {
        String res = "";
        int m=0;
        String before,after;
        while (id.matches("(?s).*<.+>(?s).*")) {
            m=id.indexOf('>');
            before=id.substring(0,m);
            res+= before+keyIndex+">";
            id=id.substring(m);
            ++keyIndex;
        }

        return res;
    }
    private String addIndex(String id, int num) {
        String res = "";
        int m=0;
        String before,after;
        m=id.indexOf('>');
        before=id.substring(0,m);
        res+= before+num+">";
        return res;
    }

}