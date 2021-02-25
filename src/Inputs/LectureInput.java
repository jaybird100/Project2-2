package Inputs;

import Articles.Lecture;
import Attributes.*;
import Attributes.Number;

import java.util.ArrayList;

public class LectureInput extends Input {

    public LectureInput() {
        super(new Lecture(),new ArrayList<>(),new ArrayList<>());


    }

    public String toString(){
        return "Lecture";
    }
}
