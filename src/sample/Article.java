package sample;

import java.util.ArrayList;

public abstract class Article {
    ArrayList<Attribute> attributes;
    public Article(ArrayList<Attribute> attributes){
        this.attributes=attributes;
    }
}
