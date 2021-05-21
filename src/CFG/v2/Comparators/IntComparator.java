package CFG.v2.Comparators;

public class IntComparator implements Comparator {
    @Override
    public boolean equals(String s1, String s2) {
        if(s1.matches("<%.+%>")){
            if(s1.matches("<%\\s*\\*\\s*%>")){
                return true;
            }
            s1 = s1.replace("<%","").replace("%>", "");
            String[] and = s1.split("and");
            boolean equals = true;
            for (String s : and) {
                s = s.trim();
                equals = isTrue(s, s2);
                if(!equals){
                    break;
                }
            }
            //TODO more functionality?
            return equals;
        }else{
            return s1.equals(s2);
        }
    }

    private boolean isTrue(String proposition, String s){
        if(proposition.contains("smaller")){
            String p = proposition.replace("smaller", "").trim();
            return Integer.parseInt(p) >= Integer.parseInt(s);
        }
        if(proposition.contains("bigger")){
            String p = proposition.replace("bigger", "").trim();
            return Integer.parseInt(p) <= Integer.parseInt(s);
        }
        return false;
    }

    @Override
    public boolean isSameDType(String s) {
        return isInteger(s);
    }

    private static final String intRegex = "^[0-9]+$";
    private static boolean isInteger(String s) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
    }
}