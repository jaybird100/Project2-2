package Attributes;

public class Course extends Attribute {
    String courseName;
    String courseCode;

    public Course(String cN){
        super(false);
        courseName=cN;
        String[] courseWords = courseName.split(" ");
        courseCode="";
        for(String s:courseWords){
            courseCode+=s.charAt(0);
        }
    }
    public Course(){
        super(true);
    }
    @Override
    public String toString() {
        if(courseName==null){
            return "<COURSE>";
        }
        return courseName;
    }

    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof Course){
            Course c = (Course)(input);
            if(courseName.equalsIgnoreCase(c.courseName)){
                return true;
            }else{
                return courseCode.equalsIgnoreCase(c.courseName);
            }
        }
        return false;
    }
}
