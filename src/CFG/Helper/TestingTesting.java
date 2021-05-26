package CFG.Helper;

import CFG.CFGSystem;
import CFG.CNForm.CNFConverter;
import CFG.temp.FileParser;
import UI.Pages.MainPage.Message;
import UI.Pages.MainPage.MessagingBoard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestingTesting {
    public static void main(String[] args) {
        CNFConverter.loadAsCNF(FileParser.loadFile(new File("src/skillParserFiles/CFGSkill.txt")), CFGSystem.dataBase);
        CFGSystem.fullFeatures(true);
        CFGSystem.print = 4;
        System.out.println(CFGSystem.run("which lectures are there on monday at 12"));
    }
}
