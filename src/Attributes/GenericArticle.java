package Attributes;

import Articles.Article;

public class GenericArticle extends Attribute{
    Article a;
    public GenericArticle() {
        super(true);
    }
    public GenericArticle(Article a){
        super(false);
        this.a=a;
    }

    @Override
    public String toString(){
        return "<ARTICLE>";
    }

    @Override
    public boolean equalsTo(Attribute input) {
        return false;
    }
}
