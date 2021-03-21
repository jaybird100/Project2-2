package Inputs;

import Articles.Article;

import java.util.ArrayList;

public abstract class Input{
    Article object;
    public ArrayList<String> inputs;
    public ArrayList<ArrayList<String>> eachInputsLimiters;
    public Input(Article object, ArrayList<String> in, ArrayList<ArrayList<String>> ea){
        this.object=object;
        inputs=in;
        eachInputsLimiters=ea;
    }

}
