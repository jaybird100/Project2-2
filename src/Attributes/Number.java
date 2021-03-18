package Attributes;

public class Number extends Attribute {
    int num;
    boolean init=false;
    public Number(int n) {
        super(false);
        init=true;
        num=n;
    }
    public Number(){
        super(true);
    }

    @Override
    public String toString(){
        if(!init){
            return "<NUM>";
        }
        return String.valueOf(num);
    }
    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof Number){
            Number n = (Number)(input);
            return num==n.num;
        }
        return false;
    }
}
