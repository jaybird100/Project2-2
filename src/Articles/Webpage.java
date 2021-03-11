package Articles;

import Attributes.Attribute;
import Attributes.ExtraText;
import Attributes.WebpageTag;
import Utils.Data;
import org.w3c.dom.Attr;

import java.util.ArrayList;

public class Webpage extends Article{
    WebpageTag tag;
    String url;
    public Webpage(){super(new ArrayList<>());}
    public Webpage(ArrayList<Attribute> a) {super(a);}
    public Webpage(WebpageTag tag, String url){
        super(new ArrayList<>());
        this.tag=tag;
        this.url=url;
        attributes.add(tag);
    }
    public Webpage(String url){
        super(new ArrayList<>());
        this.url=url;
    }
    public Webpage(WebpageTag possTag){
        super(new ArrayList<>());
        for(Webpage cur: Data.webpages){
            if(cur.tag.equalsTo(possTag)){
                this.tag=cur.tag;
                this.url=cur.url;
                attributes.add(tag);
                break;
            }
        }

    }
    public Webpage(String t, String u){
        super(new ArrayList<>());
        this.tag = new WebpageTag(t);
        this.url=u;
        attributes.add(tag);

    }
    public void setUrl(String u){url=u;}
    public String getUrl(){return url;}
    public String toString(){return tag+" "+url;}
}
