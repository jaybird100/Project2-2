package sample;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CalenderLectures extends Skill {
    public CalenderLectures(){
        super(new ArrayList<>(),new ArrayList<>());
        keywords=new ArrayList<>(Arrays.asList("Lectures"));
        wordPlacements=new ArrayList<>(Arrays.asList(0));
    }

}
