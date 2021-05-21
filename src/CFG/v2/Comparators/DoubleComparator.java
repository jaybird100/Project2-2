package CFG.v2.Comparators;

public class DoubleComparator implements Comparator{

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
            return Double.parseDouble(p) >= Double.parseDouble(s);
        }
        if(proposition.contains("bigger")){
            String p = proposition.replace("bigger", "").trim();
            return Double.parseDouble(p) <= Double.parseDouble(s);
        }
        return false;
    }

    @Override
    public boolean isSameDType(String s) {
        return isDouble(s);
    }

    private static final String doubleRegex = "^[0-9]+(.[0-9]+)?$";
    private static boolean isDouble(String s){
        return s.matches(doubleRegex);
    }
}
