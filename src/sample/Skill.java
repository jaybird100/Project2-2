package sample;

import java.text.ParseException;
import java.util.ArrayList;

public abstract class Skill {
    ArrayList<String> keywords;
    ArrayList<Integer> wordPlacements;
    abstract String action(String[] input) throws ParseException;
    public Skill(ArrayList<String> keyword,ArrayList<Integer> word){
        this.keywords=keyword;
        wordPlacements=word;
    }
}
