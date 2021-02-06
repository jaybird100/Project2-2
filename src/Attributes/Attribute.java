package Attributes;

public abstract class Attribute {
    public boolean toBeInputted;
    public abstract boolean equalsTo(Attribute input);
    public Attribute(boolean toBeInputted){
        this.toBeInputted=toBeInputted;
    }
}
