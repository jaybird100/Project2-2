package sample;

public class Number extends Attribute{
    int num;
    public Number(int n) {
        super(false);
        num=n;
    }
    public Number(){
        super(true);
    }
    @Override
    public boolean equalsTo(Attribute input) {
        return false;
    }
}
