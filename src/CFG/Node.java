package CFG;

import CFG.CNForm.CNFRule;

import java.util.*;

public class Node implements Comparable<Node>{
    List<String> format;
    List<String> toFillIn;
    String full;
    public Node(List<String> format, List<String> toFillIn, HashMap<String, String> map) {
        this.format = format;
        this.toFillIn = toFillIn;
        StringBuilder sb = new StringBuilder();
        for (String s : format) {
            if(map.containsKey(s)){
                toFillIn.remove(s);
                sb.append(map.get(s));
            }
            else if(CFGSystem.dataBase.rule(s).size()==1){
                toFillIn.remove(s);
                sb.append(CFGSystem.dataBase.rule(s).get(0));
            }else{
                sb.append(s);
            }
            sb.append(" ");
        }
        full = sb.toString();
    }

    public int distinction(){
        return toFillIn.size();
    }
    @Override
    public int compareTo(Node o) {
        if(distinction() == o.distinction()){
            return 1;
        }else{
            return Integer.compare(distinction(), o.distinction());
        }
    }

    public String toString(){
        return "Need "+toFillIn+ " for '" +full+"'";
    }

    public String question() {
        return full;
    }

    public String[] nextAsk(String input) {
        if(CFGSystem.dataBase.rule(toFillIn.get(0)).accepts(input)){
            full = full.replace(toFillIn.get(0), input);
            toFillIn.remove(0);
        }
        if(toFillIn.size()==0){
            return new String[]{"continue",full};
        }
        return new String[]{"return",toFillIn.get(0)+"?"};
    }
}
