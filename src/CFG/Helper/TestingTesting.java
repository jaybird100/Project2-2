package CFG.Helper;

import CFG.CFGSystem;
import CFG.CNForm.CNFConverter;
import CFG.temp.FileParser;

import java.io.File;

public class TestingTesting {
    public static void main(String[] args) {
        CNFConverter.loadAsCNF(FileParser.loadFile(new File("src/skillParserFiles/CFGSkill.txt")), CFGSystem.dataBase);
        CFGSystem.fullFeatures(true);
        CFGSystem.print = 0;
        System.out.println(CFGSystem.run("which lectures are there on saturday"));
    }
}
