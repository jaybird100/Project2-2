package Articles;

import Attributes.*;

import java.util.ArrayList;

public class MedicationCourse extends Article {
    ExtraText title;
    Medication medication;
    ADate startDate;
    ADate endDate;
    ExtraText notes;

    public MedicationCourse(){
        super(new ArrayList<>());
    }

    public MedicationCourse(ExtraText title, Medication medication, ADate startDate, ADate endDate){
        super(new ArrayList<>());
        attributes.add(title);
        attributes.add(medication);
        attributes.add(startDate);
        attributes.add(endDate);
        this.title = title;
        this.medication = medication;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public MedicationCourse(ExtraText title, Medication medication, ADate startDate, ADate endDate, ExtraText notes){
        this(title, medication, startDate, endDate);
        this.notes = notes;
    }

    @Override
    public String toString() {
        if(title==null){
            return "MedicationCourse";
        }
        String result = medication.toString() + "| " + startDate.toString() + "| " + endDate.toString();
        if (notes != null)
            return result + "| " + notes.toString();
        return result;
    }
}
