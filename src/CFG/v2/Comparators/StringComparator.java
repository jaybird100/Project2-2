package CFG.v2.Comparators;

public class StringComparator implements Comparator {

    @Override
    public boolean equals(String o1, String o2) {
        //TODO more functionality?
        return o1.equalsIgnoreCase(o2);
    }

    @Override
    public boolean isSameDType(String o) {
        return true;
    }
}
