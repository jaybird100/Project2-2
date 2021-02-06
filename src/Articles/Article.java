package Articles;

import Attributes.Attribute;

import java.util.ArrayList;

public abstract class Article {
     public ArrayList<Attribute> attributes;
    public Article(ArrayList<Attribute> attributes){
        this.attributes=attributes;
    }
}
