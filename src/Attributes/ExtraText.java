package Attributes;

public class ExtraText extends Attribute {
    String extraText;
    public ExtraText(String e){
        super(false);
        extraText=e;
    }
    public ExtraText(){
        super(true);
    }
    @Override
    public String toString() {
        return extraText;
    }
    @Override
    public boolean equalsTo(Attribute input) {
        if(input instanceof ExtraText){
            ExtraText c = (ExtraText)(input);
            return extraText.equalsIgnoreCase(c.extraText);
        }
        return false;
    }
}
