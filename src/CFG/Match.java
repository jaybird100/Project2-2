package CFG;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Match implements Comparable<Match>{
    private static final double baseThreshold = 0.5;
    public final HashMap<String, String> map;
    public final Action action;
    public final String input;
    public final double percPreReqMatch;
    public final double percInputMatch;

    public Match(HashMap<String, String> map, Action action, String input){
        this.map = map;
        this.action = action;
        this.input = input;
        int matchingPreReqs = matchingPreReqs();
        this.percPreReqMatch = setPreReqMatch(matchingPreReqs);
        this.percInputMatch = setInputMatch(matchingPreReqs);
    }

    @Override
    public int compareTo(Match o) {
        double dif = o.value()-value();
        if(dif==0){
            return 0;
        }
        return dif<0? -1 : 1;
    }
    private int matchingPreReqs(){
        int matching = 0;
        for (Map.Entry<String, String[]> e : action.preRequisites.entrySet()) {
            if(!in(map.get(e.getKey()), e.getValue())){
                continue;
            }
            matching++;
        }
        return matching;
    }
    /**
     * @return (nb of matching pre reqs)/(nb of "rules" found)
     */
    private double setPreReqMatch(int matchingPreReqs){
        if(map.size()==0){
            return 0;
        }
        return matchingPreReqs/(double)map.size();
    }
    /**
     * @return (nb of matching pre reqs)/(nb of pre reqs)
     */
    private double setInputMatch(int matchingPreReqs){
        if(action.preRequisites.size()==0){
            return 1;
        }
        return matchingPreReqs/(double)action.preRequisites.size();
    }
    /**
     * Gives more value to pre req match %
     * @return if: threshold not met, 0 else: 2*percPreReqMatch+percInputMatch
     */
    public double value(double minPreReqMatch){
        if(percInputMatch<minPreReqMatch){
            return 0;
        }
        return 2*percInputMatch+percPreReqMatch;
    }
    public double value(){
        return value(baseThreshold);
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
                " && Found: " + map.toString() +
                " #matches/|found|=" + percPreReqMatch + "% #matches/|preReq|" + percInputMatch + "%" +
                " && Value: "+value();
    }

}