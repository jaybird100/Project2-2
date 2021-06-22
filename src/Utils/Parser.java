package Utils;

import Actions.*;
import Articles.Article;
import Articles.FolderLocation;
import Articles.Timer;
import Articles.Webpage;
import Attributes.*;
import CFG.CFGSystem;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Parser {
    public static String parse(String input) {
        String cfg = CFGSystem.run(input);
        if (cfg != null) {
            return cfg;
        }
        return "try again";
    }


}
