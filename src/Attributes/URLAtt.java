package Attributes;

public class URLAtt extends Attribute{
    String url;

    public URLAtt() {
        super(true);
    }
    public URLAtt(String url) {
        super(false);
        this.url=url;
    }

    @Override
    public boolean equalsTo(Attribute input) {
        return false;
    }

    public String toString() {
        if(url==null){
            return "<URL>";
        }
        return url.toString();
    }
}
