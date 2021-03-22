package Attributes;

public class Path extends Attribute{
    String path;
    public Path(){super(true);}
    public Path(String path){
        super(false);
        this.path=path;
    }
    public Path(boolean toBeInputted) {
        super(toBeInputted);
    }

    @Override
    public boolean equalsTo(Attribute input) {
        return false;
    }
    @Override
    public String toString(){
        if(path==null){
            return "<PATH>";
        }
        return path;
    }
}
