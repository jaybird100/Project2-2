package Articles;

import Attributes.ADeadline;
import Attributes.ExtraText;

import java.util.ArrayList;

public class Medication extends Article {
    ExtraText medicationName;
    ExtraText courseName;
    ADeadline deadline;

    public Medication(){
        super(new ArrayList<>());
    }
    public Medication(ExtraText medicationName, ExtraText courseName, ADeadline deadline){
        super(new ArrayList<>());
        attributes.add(medicationName);
        attributes.add(courseName);
        attributes.add(deadline);
        this.medicationName = medicationName;
        this.courseName = courseName;
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        if (medicationName == null) {
            return "Medication";
        }
        return medicationName.toString() + "| " + courseName.toString() + "| " + deadline.toString();
    }
}
