package A;

import java.util.ArrayList;

public class Lecture extends Attribute<String> {
    static String[] lectureNames= {"Project2-2","Mathematical Modelling","Theoretical Computer Science","Human Computer Inter. & Affect Comp."};
    static ArrayList<Lecture> defLectures = getDefLectures(lectureNames);
    private static ArrayList<Lecture>getDefLectures(String[] lectureNames) {
        ArrayList<Lecture> res =new ArrayList<>();
        for(int i=0;i<lectureNames.length;i++){
            res.add(new Lecture(lectureNames[i]));
        }
        return res;
    }

    public Lecture(String name) {
        super(name);

    }


    public static boolean isType(String o) {
       if(defLectures.contains(new Lecture(o))){
           return true;
       }
       return false;
    }



    @Override
    public boolean equals(Object o){
        if(o instanceof Lecture){
            if(this.data.equals(((Lecture) o).data)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return 0;
    }




}
