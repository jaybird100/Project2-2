package Inputs;

import Actions.Action;
import Actions.Open;
import Attributes.URLAtt;
import Attributes.WebpageTag;

import java.util.ArrayList;

public class SEOpen extends SE{
    public ArrayList<String> inputs = new ArrayList<>();

    public SEOpen() {
        super(new Open());
        inputs.add(new URLAtt().toString());
        inputs.add(new WebpageTag().toString());
    }
}
