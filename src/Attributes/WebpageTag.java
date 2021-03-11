package Attributes;

import Articles.Webpage;

public class WebpageTag extends Attribute{
    String tag;
    public WebpageTag(){super(true); }
    public WebpageTag(String a){
        super(false);
        this.tag=a;
    }
    public String getTag(){return tag;}
    @Override
    public boolean equalsTo(Attribute input) {
        return input instanceof WebpageTag &&tag.equalsIgnoreCase(((WebpageTag)input).getTag());
    }

    @Override
    public String toString() {
        return tag;
    }
}
