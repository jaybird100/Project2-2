package Inputs;

import Articles.Event;
import Articles.Lecture;
import Actions.Fetch;
import Articles.Notification;
import Articles.Medication;
import Articles.Webpage;

import java.util.ArrayList;

public class SEFetch extends SE{
    Fetch skill = new Fetch();
    public ArrayList<SEFetchArticles> articles = new ArrayList<>();

    public SEFetch() {
        super(new Fetch());
        articles.add(new SEFetchArticles(new Lecture()));
        articles.add(new SEFetchArticles(new Event()));
        articles.add(new SEFetchArticles(new Notification()));
        articles.add(new SEFetchArticles(new Medication()));
    }


}
