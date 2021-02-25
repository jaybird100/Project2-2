package Inputs;

import Articles.Article;
import Articles.Lecture;
import Skills.Fetch;
import Skills.Skill;

import java.util.ArrayList;

public class SEFetch extends SE{
    Fetch skill = new Fetch();
    public ArrayList<SEArticles> articles = new ArrayList<>();

    public SEFetch() {
        super(new Fetch());
        articles.add(new SEArticles(new Lecture()));

    }


}
