package A;

public abstract class Attribute<T>{
    public T data;
    public Attribute(T data){
        this.data=data;
    }
    public String toString(){return data.toString(); }

}
