package CFG;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Action {
    public final String response;
    final HashMap<String, String[]> preRequisites;
    public Action(String response, HashMap<String, String[]> preRequisites){
        this.response = response;
        this.preRequisites = preRequisites;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> e : preRequisites.entrySet()) {
            sb.append(e.getKey()).append("=").append(Arrays.toString(e.getValue())).append(" ");
        }
        return sb.append("-> ").append(response).toString();
    }

    //Util
    public boolean in(String s, String[] arr){
        if(s == null){
            return false;
        }
        for (String s1 : arr) {
            if(s1.equals("*")){
                return true;
            }
            if(s1.equals(s)){
                return true;
            }
        }
        return false;
    }
}
