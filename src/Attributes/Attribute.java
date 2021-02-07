package Attributes;

public abstract class Attribute {
    public boolean toBeInputted;//for empty constructors so we can avoid nullpointers in runtime
    public abstract boolean equalsTo(Attribute input);//Given an Attribute check if it is equal to to the instance
    public Attribute(boolean toBeInputted){
        this.toBeInputted=toBeInputted;
    }//default empty constructor
}
