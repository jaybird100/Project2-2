package Articles;

import Attributes.ADate;
import Attributes.Course;
import Attributes.ExtraText;
import Attributes.Time;

import java.util.ArrayList;

public class Medication extends Article {
    ExtraText medicationName;
    ExtraText courseName;

    public Medication(){
        super(new ArrayList<>());
    }
    public Medication(ExtraText medicationName, ExtraText courseName){
        super(new ArrayList<>());
        attributes.add(medicationName);
        attributes.add(courseName);
        this.medicationName = medicationName;
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        if (medicationName == null) {
            return "Medication";
        }
        return medicationName.toString() + "| " + courseName.toString();
    }
}
