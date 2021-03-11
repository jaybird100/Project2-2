package UI;

public class Helper {
    public static boolean in(String s, String[] list){
        for (String s1 : list) {
            if(s.equals(s1)){
                return true;
            }
        }
        return false;
    }
}
