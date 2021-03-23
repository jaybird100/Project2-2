package Actions;

import Articles.Article;
import Articles.FolderLocation;
import Articles.Webpage;
import Utils.Variables;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Open extends Action {
    Article type;
    public Open(){}
    public Open(Article theObject) {
        type=theObject;
    }


    @Override
    public String action(){
        if(type instanceof Webpage) {
            Webpage temp = (Webpage) type;
            if(temp.getUrl()==null){
                return "Doesn't recognize webpage";
            }
            if(temp.getUrl().length()>0) {
                System.out.println("getURL: " + temp.getUrl());
                if(Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(URI.create(temp.getUrl()));
                    } catch (IOException e) {
                        return "Not valid URL";
                    }
                }else{
                    //for macOS
                    try {
                        Runtime.getRuntime().exec("xdg-open " + ((Webpage) type).getUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Not Valid URL";
                    }
                }
            }

        }else if( type instanceof FolderLocation){
            try {
                FolderLocation temp = (FolderLocation) type;
                if(temp.getPath().length()>0) {
                    System.out.println("getPath: " + temp.getPath());
                    Desktop.getDesktop().open(new File(Variables.USER_HOME_PATH+temp.getPath()));

                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Not valid Path";
            }
        }
        return null;
    }

    public String toString(){return "Open";}
}
