package Articles;

import Attributes.Attribute;
import Attributes.FolderTag;
import Utils.Data;

import java.util.ArrayList;

public class FolderLocation extends Article{
    FolderTag tag;
    String path;
    public FolderLocation(){super(new ArrayList<>());}
    public FolderLocation(ArrayList<Attribute> attributes) {
        super(attributes);
    }
    public FolderLocation(FolderTag tag,String path){
        super(new ArrayList<>());
        this.tag=tag;
        this.path=path;
        attributes.add(tag);
    }
    public FolderLocation(String path){
        super(new ArrayList<>());
        this.path=path;
    }
    public FolderLocation(FolderTag possTag){
        super(new ArrayList<>());
        for(FolderLocation cur: Data.folderLocations){
            if(cur.tag.equalsTo(possTag)){
                this.tag=cur.tag;
                this.path=cur.path;
                attributes.add(tag);
                break;
            }
        }
    }
    public FolderLocation(String t, String u){
        super(new ArrayList<>());
        this.tag = new FolderTag(t);
        this.path=u;
        attributes.add(tag);

    }
    public void setPath(String u){path=u;}
    public String getPath(){return path;}
    public String toString(){
        if(path==null&&tag==null){
            return "FolderLocation";
        }
        return tag+" "+path;
    }

}
