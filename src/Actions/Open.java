package Actions;

import Articles.Article;
import Articles.Webpage;
import Attributes.Attribute;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Open extends Action {
    Article type;
    public Open(){}
    public Open(Article theObject) {
        type=theObject;
    }


    @Override
    public String action() throws URISyntaxException, IOException {
        if(type instanceof Webpage) {
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(((Webpage)type).getUrl()));
                    return "Webpage open";
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                    return "Webpage not found";
                }
            }else{
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec("xdg-open " + ((Webpage) type).getUrl());
                    return "Webpage open";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Webpage not found";
                }
            }
        }
        return null;
    }

    public String toString(){return "OPEN";}
}
