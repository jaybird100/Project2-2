package Skills;

import Articles.Lecture;
import Articles.Article;
import Attributes.Attribute;
import Utils.Data;

import java.util.ArrayList;

public class Fetch extends Skill {
    public void setType(Article type) {
        this.type = type;
    }

    public void setLimiter(Attribute limiter) {
        this.limiters.add(limiter);
        noLimit=false;
    }

    public void setAttributeIDs(ArrayList<Integer> attributeIDs) {
        this.attributeIDs = attributeIDs;
        allAtt=false;
    }

    public void setLimiters(ArrayList<Attribute> limiters) {
        this.limiters = limiters;
    }

    Article type;
    ArrayList<Attribute> limiters = new ArrayList<>();
    boolean noLimit=true;
    ArrayList<Integer> attributeIDs;
    boolean allAtt=true;
    public Fetch(){}
    public Fetch(Article a,ArrayList<Attribute> l, ArrayList<Integer> at){
        type=a;
        limiters=l;
        attributeIDs=at;
        noLimit=false;
        allAtt=false;
        if(l==null){
            noLimit=true;
        }
        if(at==null){
            allAtt=true;
        }
    }
    public String action(){
        ArrayList<Article> items = new ArrayList<>();
        if(type instanceof Lecture) {
            ArrayList<ArrayList<Boolean>> checklist = new ArrayList<>();
            for (Lecture l : Data.lectures) {
                if(noLimit){
                    items.add(l);
                }else {
                    ArrayList<Boolean> temp = new ArrayList<>();
                    for (Attribute limit : limiters) {
                        boolean t = false;
                        for (Attribute a : l.attributes) {
                            if(a.equalsTo(limit)){
                                t=true;
                            }
                        }
                        temp.add(t);
                    }
                    checklist.add(temp);
                }
            }
            for(int i=0;i<checklist.size();i++){
                boolean allTrue=true;
                for(boolean b:checklist.get(i)){
                    if(!b){
                        allTrue=false;
                    }
                }
                if(allTrue){
                    items.add(Data.lectures.get(i));
                }
            }
        }
        ArrayList<String> toReturn = new ArrayList<>();
        for(Article a:items){
            String toAdd = "";
            if(!allAtt) {
                for (Integer i : attributeIDs) {
                    if(i<a.attributes.size()) {
                        toAdd += a.attributes.get(i).toString() + " ";
                    }
                }
            }else{
                for(Attribute at:a.attributes){
                    toAdd+=at.toString()+" ";
                }
            }
            toReturn.add(toAdd);
        }
        String realToReturn="";
        for(String s:toReturn){
            realToReturn+=s+'\n';
        }
        return realToReturn;
    }
    @Override
    public String toString(){
        return "FETCH";
    }
}
