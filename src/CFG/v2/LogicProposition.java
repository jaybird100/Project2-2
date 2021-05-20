package CFG.v2;

import java.util.ArrayList;
import java.util.List;

// <%dType *proposition* %>
// where dType = int or dbl
// Rules:
// 1. No spaces whatsoever bc CNFConverter.CNFTerm splits based on spaces
// 2. set comparator to x (ex: x<0, 23<=x, etc...)
// 3. "and" = and comparator (no && but could be added tho)
// 4. "or" = or comparator (no || because CNFConverter.addRule splits based on | )
public abstract class LogicProposition {

    private final List<String> logic;
    public final String full;
    protected LogicProposition(String full,List<String> logic){
        this.logic = logic;
        this.full = full;
    }

    public boolean match(String s) {
        List<Boolean> confirmed = new ArrayList<>();
        for (String s1 : logic) {
            if (s1.matches("[0-9]+")) {
                confirmed.add(confirmed.get(Integer.parseInt(s1)));
            } else if (s1.matches("[0-9]+\\s*or\\s*[0-9]+")) {
                String[] idx = s1.split("\\s*or\\s*");
                confirmed.add(confirmed.get(Integer.parseInt(idx[0].trim())) || confirmed.get(Integer.parseInt(idx[1].trim())));
            } else if (s1.matches("[0-9]+\\s*and\\s*[0-9]+")) {
                String[] idx = s1.split("\\s*and\\s*");
                confirmed.add(confirmed.get(Integer.parseInt(idx[0].trim())) && confirmed.get(Integer.parseInt(idx[1].trim())));
            } else {
                String temp = s1.replace("x", s);
                String comp = getComparator(temp);
                boolean result = eval(comp, temp.split(comp));
                confirmed.add(result);
            }
        }
        return confirmed.get(confirmed.size() - 1);
    }
    private String getComparator(String prop){
        char[] c = prop.toCharArray();
        StringBuilder comparator = new StringBuilder();
        for (char c1 : c) {
            if(!Character.isDigit(c1)){
                comparator.append(c1);
            }
            else if(comparator.length()!=0){
                break;
            }
        }
        return comparator.toString();
    }
    protected abstract boolean eval(String comparator, String[] values);

    public static LogicProposition create(String logicString){
        if(logicString.matches("<%[a-zA-Z]{3}.+%>")) {
            String dType = logicString.substring(2, 5);
            String logic = logicString.substring(5, logicString.length() - 2);
            List<String> parentheses = simplify(logic);
            switch (dType) {
                case "int":
                    return new IntLogic(logicString, parentheses);
                case "dbl":
                    return new DoubleLogic(logicString, parentheses);
            }
        }
        return null;
    }
    private static List<String> simplify(String logic) {
        List<String> par = new ArrayList<>();
        Integer s = null;
        String l = logic;
        while(l.matches(".*\\(.*\\).*")) {
            String l2 = l;
            char[] c = l2.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == '(') {
                    s = i;
                }
                if (s != null && c[i] == ')') {
                    String subS = unitize(par, l2.substring(s+1, i));
                    separate(par, subS);
                    par.add(subS);
                    l = l.replace(l2.substring(s, i + 1), String.valueOf((par.size()-1)));
                    s = null;
                }
            }
        }
        String full = unitize(par, l.trim());
        separate(par, full);
        return par;
    }
    private static String unitize(List<String> list, String s){
        String[] temp = s.split("\\s*or\\s*");
        for (String s1 : temp) {
            String[] temp1 = s1.split("\\s*and\\s*");
            for (String s2 : temp1) {
                s = s.replace(s2, String.valueOf(list.size()));
                list.add(s2.trim());
            }
        }
        return s;
    }
    private static void separate(List<String> list, String s){
        while(s.matches("[0-9]*(and|or)[0-9]*(and|or).*")) {
            String l2 = s;
            char[] c = l2.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < c.length; i++) {
                if(!Character.isDigit(c[i])){
                    sb.append(c[i]);
                }
                if((sb.toString().startsWith("and") && sb.length()==4) || (sb.toString().startsWith("or") && sb.length()==3)){
                    s = s.replace(l2.substring(0, i), String.valueOf(list.size()));
                    list.add(l2.substring(0, i));
                    break;
                }
            }
        }
        list.add(s);
    }
}

class IntLogic extends LogicProposition{
    public IntLogic(String full, List<String> logic){
        super(full,logic);
    }

    @Override
    public boolean match(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return super.match(s);
    }

    @Override
    protected boolean eval(String comparator, String[] values) {
        switch(comparator){
            case ">=":
            case "=>":
                return Integer.parseInt(values[0])>=Integer.parseInt(values[1]);
            case  ">":
                return Integer.parseInt(values[0])>Integer.parseInt(values[1]);
            case "<=":
            case "=<":
                return Integer.parseInt(values[0])<=Integer.parseInt(values[1]);
            case  "<":
                return Integer.parseInt(values[0])<Integer.parseInt(values[1]);
            case "==":
                return Integer.parseInt(values[0])==Integer.parseInt(values[1]);
        }
        return false;
    }
}

class DoubleLogic extends LogicProposition{
    public DoubleLogic(String full, List<String> logic){
        super(full, logic);
    }

    @Override
    public boolean match(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return super.match(s);
    }

    @Override
    protected boolean eval(String comparator, String[] values) {
        switch(comparator){
            case ">=":
            case "=>":
                return Double.parseDouble(values[0])>=Double.parseDouble(values[1]);
            case  ">":
                return Double.parseDouble(values[0])>Double.parseDouble(values[1]);
            case "<=":
            case "=<":
                return Double.parseDouble(values[0])<=Double.parseDouble(values[1]);
            case  "<":
                return Double.parseDouble(values[0])<Double.parseDouble(values[1]);
            case "==":
                return Double.parseDouble(values[0])==Double.parseDouble(values[1]);
        }
        return false;
    }
}
