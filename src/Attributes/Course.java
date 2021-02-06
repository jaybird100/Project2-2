package Attributes;

public class Course extends Attribute {
    String courseName;
    public Course(String cN){
        super(false);
        courseName=cN;
    }
    public Course(){
        super(true);
    }
    @Override
    public String toString() {
        return courseName;
    }

    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof Course){
            Course c = (Course)(input);
            return courseName.equalsIgnoreCase(c.courseName);
        }
        return false;
    }
}
