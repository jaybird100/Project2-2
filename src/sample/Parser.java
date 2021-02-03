package sample;

import java.text.ParseException;
import java.util.Arrays;

public class Parser {
    String parse(String input) throws ParseException {
        String[] in = input.split(" ");
       Skill sk = findSkill(in);
       if(sk==null){
           return "No match found";
       }else{
           return sk.action(in);
       }
    }
    Skill findSkill(String[] input){
     //   System.out.println("input: "+ Arrays.toString(input));
      //  System.out.println(Data.everySkill.size());
        for(Skill sk:Data.everySkill){
            boolean theOne=true;
            //System.out.println(Arrays.toString(sk.keywords.toArray()));
            for(int i=0;i<sk.keywords.size();i++){
                if (!(input.length > sk.wordPlacements.get(i) && input[sk.wordPlacements.get(i)].equalsIgnoreCase(sk.keywords.get(i)))) {
                    theOne = false;
                    break;
                }
            }
            if(theOne){
                return sk;
            }
        }
        return null;
    }
}
