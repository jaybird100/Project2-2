package CFG.CNForm;

import java.util.HashMap;

public class CNFMatch implements Comparable<CNFMatch>{
    public final CNFAction action; // Action this match represents
    public final double strength; // Strength based off loading of skill
    public final double neededOverFound; // Needed/Found <- determines how many pre requisites of this action were in the input
    public final double foundOverNeeded; // Found/Needed <- determines how much of the input is used for this action
    private final HashMap<String, String> map;
    public CNFMatch(CNFAction action, HashMap<String, String> map , double strength, double neededOverFound, double foundOverNeeded){
        this.action = action;
        this.strength = strength;
        this.neededOverFound = neededOverFound;
        this.foundOverNeeded = foundOverNeeded;
        this.map = map;
    }

    /**
     * strength > Found/Needed > Needed/Found
     * @return value of match based off line above
     */
    public double value(){
        return strength*10000+foundOverNeeded*100+neededOverFound;
    }

    public boolean isValid(){
        return strength==1;
    }
    /**
     * Returns the response action this match represents by replacing the rule IDs
     * in the response by what is found
     * @return response string substituted converted
     */
    public String response(){
        String s = action.response;
        while(s.matches(".*<\\w+>.*")) { // iteratively replace all rule IDs that might be embedded
            for (String s1 : map.keySet()) {
                s = s.replace(s1, map.get(s1));
            }
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    @Override
    public String toString(){
        return action+", value="+value()+", strength="+strength+", "+" Needed/Found="+ neededOverFound +", Found/Needed="+ foundOverNeeded;
    }

    @Override
    public int compareTo(CNFMatch o) {
        return Double.compare(value(), o.value());
    }
}
