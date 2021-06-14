package CFG.CNForm;

import CFG.Helper.LogicProposition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//TODO make multiple responses possible for randomization
public class CNFAction {
    public final String response; // response split into a list with every String in the list is either terminal or a rule ID
    // ex: [We have math, <timeexpression>, for students in group 1]
    public final List<PreRequisite> preRequisites; // Pre Requisites needed for this action to trigger

    public CNFAction(String response, List<PreRequisite> preRequisites){
        this.response = response;
        this.preRequisites = preRequisites;
    }

    /**
     * For all pre requisites, for all values found for rule of the pre req, check for a match
     * @param mapFound maps ruleID to all possible correspondents found i nthe input string
     * @return Match class that has all the important stats needed to determine how strong the match
     */
    public CNFMatch matches(HashMap<String, String> mapFound){
        int strengthNeeded = 0;
        int strengthFound = 0;
        int nbFound = 0;
        for (PreRequisite pr : preRequisites) { // for all pre req
            strengthNeeded+= pr.strength;
            if(!mapFound.containsKey(pr.ruleID)){
                continue; // If this rule doesn't exist go next
            }
            if(pr.matches(mapFound.get(pr.ruleID))) { // check if it matches with the pre req
                nbFound++;
                strengthFound += pr.strength;
            }
        }
        return new CNFMatch(this,mapFound,strengthFound/(double)strengthNeeded,nbFound/(double)preRequisites.size(), nbFound/(double)mapFound.size());
    }

    @Override
    public String toString() {
        return preRequisites + " -> " + response;
    }
}

class PreRequisite{
    public final double strength; // Strength of pre req based off value given when loading (base value = 1 for each pre req)
    public final String ruleID; // ruleID that pre req needs
    public final String[] valuesWanted; // all possible values the pre req can take

    public PreRequisite(String ruleID, String[] valuesWanted, double strength){
        this.ruleID = ruleID;
        this.valuesWanted = valuesWanted;
        this.strength = strength;
    }
    public PreRequisite(String ruleID, String[] valuesWanted){
        this(ruleID, valuesWanted, 1);
    }

    /**
     * Loops over all possible values the pre req can take and checks if it matches.
     * Options for pre req:
     *      - 1 to 1 with found
     *      - * = anything
     *      - !<insert non-wanted value> = found is different from string after '!'
     *      - <%dType *proposition *> (go to top of LogicProposition for more info)
     * !!! Negation overrides rest
     * @param found string you want to check if it matches
     * @return true if match, else false
     */
    public boolean matches(String found){
        boolean accepted = false;
        for (String s : valuesWanted) { // for each wanted values
            if(s.startsWith("!")){ // check negation
                if(matches(found, s)){
                    return false;
                }
            }else{
                accepted = accepted || matches(found, s);
            }
        }
        return accepted;
    }
    private boolean matches(String found, String preReq){
        if(preReq.equals("*") || preReq.equals(found)){ // check * and 1 to 1
            return true;
        }else if(preReq.endsWith("%>")){
            LogicProposition l = LogicProposition.create(preReq);
            if(l!=null) {
                 return l.match(found);
            }
        }
        return false;
    }
    @Override
    public String toString(){
        return ruleID + " (" + strength + ") -> " + Arrays.toString(valuesWanted);
    }
}
