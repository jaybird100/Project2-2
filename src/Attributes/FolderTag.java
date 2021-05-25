package Attributes;

public class FolderTag extends Attribute{
    String tag;
    public FolderTag(){super(true);}
    public FolderTag(String a){
        super(false);
        this.tag=a;
    }

    public String getTag() {
        return tag;
    }


    @Override
    public boolean equalsTo(Attribute input) {
        return input instanceof FolderTag &&tag.equalsIgnoreCase(((FolderTag)input).getTag());
    }
    @Override
    public String toString() {
        if(tag==null){
            return "<FOLDERTAG>";
        }
        return tag;
    }
}
