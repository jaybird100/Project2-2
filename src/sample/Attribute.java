package sample;

public abstract class Attribute {
    boolean toBeInputted;
    public abstract boolean equalsTo(Attribute input);
    public Attribute(boolean toBeInputted){
        this.toBeInputted=toBeInputted;
    }
}
