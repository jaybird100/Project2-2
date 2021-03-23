package Actions;
import Calc.Calculator_command;

import java.util.regex.Pattern;

public class Calc extends Action{
    public boolean isCalc=true;
    String input;
    public Calc(String in){
        if (in.matches(".*[a-z].*")) {
            isCalc=false;
        }else {
            input = formatting(in);
        }
    }
    @Override
    public String action() {
        try {
            return String.valueOf(Calculator_command.get_result(input));
        } catch (Exception e) {
            isCalc=false;
            return "Could not parse calculation";
        }
    }
    public static String formatting(String s){
        for(int i = s.length()-1;i>0;i--){
            if(!new Character(s.charAt(i)).equals(' ')&&!new Character(s.charAt(i-1)).equals(' ')){
                if(!Character.isDigit(s.charAt(i))||(Character.isDigit(s.charAt(i))&&!Character.isDigit(s.charAt(i-1)))){
                    if(!new Character(s.charAt(i-1)).equals(" ")){
                        String one = s.substring(0,i);
                        String two = s.substring(i);
                        s= one + " " + two;
                    }
                }
            }
        }
        return s;
    }
}
