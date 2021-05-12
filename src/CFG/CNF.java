package CFG;

import java.util.*;

public class CNF {

    private static  HashMap<String, Rule> CFGrule;
    ArrayList<Rule> newRules;
    HashSet<String> dummyrep=new HashSet<>();
    static String startSymb="<s>";
    static String emptSymb="<empt>";//TODO check if this representation is of with everyone

    public CNF(HashMap<String, Rule> a){
        this.CFGrule=a;

    }


    public ArrayList<Rule> cnfSplit() {
        newRules = new ArrayList<>(CFGrule.values());
        applyStartRule();
        eliminateEmpty();
        unitAndLongProduction();

        //convert for cyk

        return newRules;
    }

    private void unitAndLongProduction() {
        HashSet<Rule> dummy=new HashSet<>();
        for(Rule r :newRules)
            dummy.addAll(applyRule(r));


        System.out.println("_________________");
        System.out.println(dummy);
        System.out.println("_________________");
        System.out.println(newRules);
        System.out.println("_________________");
        System.out.println("_________________");
        newRules=new ArrayList<>(dummy);


    }
    private String[] cykConvert(){
        String[] res=new String[2];

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

    private void eliminateEmpty() {

        ArrayList<Rule> needModification;
        Rule r;
        for(int i=0;i<newRules.size();i++) {
            r= newRules.get(i);
            for (String rep : r.replacements()) {
                if(rep.contains(emptSymb)){//if the replacement contains the empty symbol
                    //the rule is nullable, therefore we have to consider other rules in the grammar that contain it
                    needModification=findRulesContaining(r);
                    //remove old rule
                    for(Rule a: needModification){
                        newRules.remove(a);
                    }
                    //we need to find the combinations where r is null for each od the concerned rules
                    needModification= modifyForEmpt(needModification,r);
                    // add modified rule
                    for(Rule a: needModification){
                        newRules.add(a);
                    }
                    break;//a rule can only have one empty replacement, so if we fix it for one, it will apply for all
                    //concerning this rule, so we can move to the next rule
                }

            }

        }

        for(Rule p:newRules) {
            p.replacements().remove("");
            p.replacements().remove(emptSymb);
        }
    }

    private ArrayList<Rule> findRulesContaining(Rule r) {
        ArrayList res= new ArrayList();
        for(Rule m:newRules){
            for( String mrep:m.replacements()){
                if(mrep.contains(r.id)){
                    res.add(m);
                }
            }
        }
        return res;
    }

    private ArrayList<Rule> modifyForEmpt(ArrayList<Rule> needModification, Rule nullableItem) {
        ArrayList<Rule> res= new ArrayList<>();
        String[] d;
        for(Rule r: needModification){
            dummyrep = new HashSet<>();
            for(String rep: r.replacements()) {
                //we add the original regardless of whether it is the nullable replacement
                dummyrep.add(rep);
                d=rep.split(" ");
                dummyrep.addAll(getCombinations(d,nullableItem, new HashSet<>(),0));
            }

            res.add(new Rule(r.id,new ArrayList<>(dummyrep)));
        }

        return res;
    }

    private Collection<String> getCombinations(String []rep, Rule nullableItem, HashSet<String> res, int occNum) {
        ArrayList<Integer> indexCurOcc;
        if(!hasItem(rep,nullableItem)){
            res.add(join(rep," "));
        }else{
            indexCurOcc=getOccurance(rep,nullableItem);
            res.add(join(rep," "));
            for(int i:indexCurOcc){
                res.addAll(getCombinations(stringArrWithout(rep,i),nullableItem,res,0));
            }

        }

        return res;
    }

    private String[] stringArrWithout(String[] rep, int i) {
        String res="";
        for(int m=0;m< rep.length;m++){
            if(m!=i){
                res+=rep[m]+" ";
            }
        }

        return res.split(" ");
    }

    private ArrayList<Integer> getOccurance(String[] rep, Rule nullableItem) {
        ArrayList<Integer> res=new ArrayList<>();
        int count=0;
        for(int i=0;i< rep.length;i++){
            if(rep[i].equals(nullableItem.id)){
                res.add(i);
            }
        }
        return res;
    }

    private String join(String[] arr, String joinpt ){
        String res="";
        for(String r:arr)
            res+=r+joinpt;
        return res.trim();
    }
    private boolean hasItem(String[] rep, Rule nullableItem) {
        for(String s:rep){
            if(s.equals(nullableItem.id)){
                return true;
            }
        }
        return false;
    }


    public ArrayList<Rule> applyRule(Rule rul) {

        ArrayList<Rule> addi=new ArrayList<>();
        String dummy="";
        for(String rep: rul.replacements()) {
            int numNonterminal = 0;
            dummy = rep;
            //count the number of nonterminals

            while (dummy.matches("(?s).*<.+>(?s).*")) {
                ++numNonterminal;
                dummy = dummy.substring(dummy.indexOf('>') + 1);
            }

            if (numNonterminal == 0) {
                //no non terminal terms
                //split the non terminals
                addi.addAll(split(rep, rul.id));
            } else if (numNonterminal <= 2) {
                //if there are atmost 2 non terminals
                //keep the rule as is
                addi.add(rul);

            } else {
                //there are more than 2 non terminals
                //split non terminals  A->BCDE to
                //A->B(A1)
                //A1->C(A2)
                //A2->DE
                addi.addAll(split(rep, numNonterminal - 1,rul.id));
            }
        }

        return addi;

    }

    private ArrayList<Rule> split(String s, String id){
        ArrayList<Rule> res = new ArrayList<>();
        ArrayList<String> dummyrep = new ArrayList<>();
        dummyrep.add(s);
        res.add(new Rule(addIndex(id), dummyrep));

        return res;
    }
    private ArrayList<Rule> split(String s, int numSplits, String id){
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
            res[i]=r[i]+"> ";
        }
        res[res.length-1]+=r[r.length-1]+"> ";

        return res;
    }
    int keyIndex=0;
    private String addIndex(String id) {
        String res = "";
        int m=0;

        String before;
        while (id.matches("(?s).*<.+>(?s).*")) {
            m=id.indexOf('>');
            before=id.substring(0,m);
            res+= before+keyIndex+"> ";
            id=id.substring(m);
            ++keyIndex;

        }

        return res;
    }
    private String addIndex(String id, int num) {
        String res = "";
        int m=0;
        String before;
        m=id.indexOf('>');
        before=id.substring(0,m);
        res+= before+keyIndex+"> ";
        ++keyIndex;
        return res;
    }
}
