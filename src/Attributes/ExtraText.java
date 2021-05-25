package Attributes;

public class ExtraText extends Attribute {
    String extraText;
    String extraCode;
    public ExtraText(String e){
        super(false);
        extraText=e;
        extraCode="";
        String[] extraWords = extraText.split(" ");
        if(extraWords.length>1) {
            for (String s : extraWords) {
                if(s.length()>0) {
                    extraCode += s.charAt(0);
                }
            }
        }
    }
    public ExtraText(){
        super(true);
    }
    @Override
    public String toString() {
        if(extraText==null){
            return "<EXTRA>";
        }
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
