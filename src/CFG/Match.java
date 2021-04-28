package CFG;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Match implements Comparable<Match>{
    private static final double baseThreshold = 0.5;
    public final HashMap<String, String> map;
    public final Action action;
    public final String input;
    public final double matchPerc;
    public final double preReqPerc;

    public Match(HashMap<String, String> map, Action action, String input, String reconstructed){
        this.map = map;
        this.action = action;
        this.input = input;
        matchPerc = reconstructed.length()/(double)input.length();
        preReqPerc = preReqPerc();
    }

    public double preReqPerc(){
        int found= 0;
        for (String s : action.preRequisites.keySet()) {
            for (String possibility : action.preRequisites.get(s)) {
                if(possibility.equalsIgnoreCase(map.get(s))){
                    found++;
                    break;
                }
            }
        }
        return found/(double)action.preRequisites.size();
    }
    @Override
    public int compareTo(Match o) {
        double dif = o.value()-value();
        if(dif==0){
            return 0;
        }
        return dif<0? -1 : 1;
    }

    /**
     * Gives more value to pre req match %
     * @return if: threshold not met, 0 else: 2*percPreReqMatch+percInputMatch
     */
    public double value(){
        return matchPerc+preReqPerc*2;
    }
    public boolean isValid(double valueThreshold){
        return value()>=valueThreshold;
    }
    public String getResponse(){
        String s = action.response;
        while(s.matches(".*<.*>.*")) {
            for (String key : map.keySet()) {
                s = s.replace(key, map.get(key));
            }
        }
        return StringUtils.capitalize(s);
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
    public String toString(){
        return "Action: " + action.toString() +
                " --- Found: " + map.toString() +
                " --- Match %= " + matchPerc+" --- Pre Req Match % = "+preReqPerc;
    }

}