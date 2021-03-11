package CFG;

import java.util.HashMap;

public class Match {
    public final HashMap<String, String> map;
    public final Action action;
    public final String input;
    public final double percPreReqMatch;
    public final double percInputMatch;

    public Match(HashMap<String, String> map, Action action, String input){
        this.map = map;
        this.action = action;
        this.input = input;
        this.percPreReqMatch = setPreReqMatch();
        this.percInputMatch = setInputMatch();
    }

    private double setPreReqMatch(){
        System.out.println(action);
        System.out.println(map);
        return 0;
    }

    private double setInputMatch(){
        return 0;
    }

    public String toString(){
        return "Map: " + map.toString() +
                "Action: " + action.toString() + " " + percPreReqMatch + "%" +
                "Input: " + input + " " + percInputMatch + "%";
    }
}