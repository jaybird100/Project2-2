package Articles;

import Attributes.ADate;
import Attributes.Attribute;
import Attributes.Time;

import java.util.ArrayList;

public abstract class Article {

     public ArrayList<Attribute> attributes;
    public Article(ArrayList<Attribute> attributes){
        this.attributes=attributes;
    }
    public Article(){this.attributes= new ArrayList<>();}
}
