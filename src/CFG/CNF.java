package CFG;
import CFG.Rule;

import java.util.*;

public class CNF {

    private static  HashMap<String, Rule> CFGrule;
    ArrayList<Rule> newRules;
    HashSet<String> dummyrep=new HashSet<>();
    static String startSymb="<S>";
    static String wordsSymbol="<wrds>";
    static int index=0;
    String[][] db;

    public CNF(HashMap<String, Rule> a){
        this.CFGrule=a;
    }


    public String[][] cnfSplit() {
        newRules = new ArrayList<>(CFGrule.values());

        makeTerminals();
        applyStartRule();
        unitAndLongProduction();

        //convert for cyk
        db=cykConvert();
        return db;
    }


    private void unitAndLongProduction() {
        ArrayList<Rule> dummy=new ArrayList<>();
        for(Rule r :newRules)
            dummy.addAll(applyRule(r));
        newRules=dummy;


    }

    HashSet<String> wordsRep= new HashSet<>();
    private void makeTerminals(){
        //we need to remove loose words and add them to the words rule
        //we do not want to have repeating replacements or rules
        ArrayList<Rule> dummyRule=new ArrayList<>();
        for(Rule r :newRules) {
            dummyrep = new HashSet<>();

            if (r.isTerminalRule()) {
                dummyRule.add(r);
            } else {
                for (String rep : r.replacements()) {
                    dummyrep.add(split(rep));
                }
                dummyRule.add(new Rule(r.id, new ArrayList<>(dummyrep)));
            }
        }
        newRules=dummyRule;
        newRules.add(new Rule(wordsSymbol,new ArrayList<>(wordsRep)));
        printDB();
    }

    private String split(String rep){
        String res="";
        String [] split= rep.split(" ");
        for(int i=0;i<split.length;i++){
            if(split[i].contains("<")&&split[i].contains(">")){
                res+=split[i]+" ";
            }else{
                res+=wordsSymbol+" ";
                wordsRep.add(split[i]);
            }
        }
        return res;

    }

    private String[][] cykConvert(){
        String[][] res=new String[newRules.size()][];
        for(int i=0;i<newRules.size();i++){
            res[i]=newRules.get(i).getArrayRep();
        }

        return res;
    }

    private void applyStartRule() {
        dummyrep.add(startSymb);
        Rule altStart= new Rule(addIndex(startSymb,0),new ArrayList<>(dummyrep));
        boolean add=false;
        for(Rule r: newRules){
            for(String rep:r.replacements()){
                if(rep.contains(startSymb)){//if the start symbol is on the RHS
                    //introduce new rule s0->s
                    add=true;//to avoid concurrent modification exception and to avoid having duplicate alt start statements
                }
            }
        }
        if(add)
            newRules.add(altStart);
    }




    public ArrayList<Rule> applyRule(Rule rul) {

        String[] s;
        ArrayList<Rule>res= new ArrayList<>();
        ArrayList<String> reprep;
        if(!rul.isTerminalRule()) {
            reprep= new ArrayList<>();
            for (String rep : rul.replacements()) {
                s = rep.split(" ");
                if(s.length<=2){
                    reprep.add(rep);
                }else{
                    reprep.add(s[0]+" "+addIndex(rul.id, index));
                    ArrayList<String> otherRulRep;
                    Rule otherRul;
                    for(int i=1;i<s.length-2;i++){
                        otherRulRep=new ArrayList<>();
                        otherRul=new Rule(addIndex(rul.id, index));
                        otherRulRep.add(s[i]+" "+ addIndex(rul.id, (++index)));
                        otherRul =new Rule(otherRul.id,otherRulRep);
                        res.add(otherRul);
                    }
                    otherRulRep=new ArrayList<>();
                    otherRulRep.add(s[s.length-2]+" "+s[s.length-1]);
                    otherRul=new Rule(addIndex(rul.id, index),otherRulRep);
                    res.add(otherRul);
                    ++index;
                }
            }
            res.add(new Rule(rul.id,reprep));
        }else{
            res.add(rul);
        }

        return res;

    }


    private ArrayList<Rule> split(String s, int startSplit, String id){
        //at this point we know that is atleast <> <> <> with no loose words in between
        ArrayList<Rule> res=new ArrayList<>();
        ArrayList<String> rep;
        String[] sep= s.trim().split(" ");

        // System.out.println(Arrays.toString(sep));

        for(int i=0;i<sep.length-1;i++){
            rep=new ArrayList<>();
//            if(i+1< sep.length-1) {
//                rep.add(sep[i] + " " + addIndex(id,(i+1+startSplit)));
//            }else {//only the last 2 elements
//                rep.add(sep[i] + " " + sep[i + 1]);
//            }
            if(i+1< sep.length-1) {
                rep.add(sep[i] + " " + id);
            }else {//only the last 2 elements
                rep.add(sep[i] + " " + sep[i + 1]);
            }
            res.add(new Rule(id,rep));

//            System.out.println("AAAAAA"+res.get(res.size()-1));
        }
//        System.out.println("AAAABBBBA"+Arrays.toString(res.toArray()));
        return res;
    }



    private String addIndex(String id, int num) {
        return  id.substring(0,id.indexOf('>'))+num+">";
    }

    public void printDB(){
        for(Rule r:newRules){
            System.out.print(r);
        }
    }

}
