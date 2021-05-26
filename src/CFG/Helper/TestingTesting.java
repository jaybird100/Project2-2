package CFG.Helper;

import CFG.CFGSystem;
import CFG.CNForm.CNFConverter;
import CFG.FileParser;

import java.io.File;

public class TestingTesting {
    public static void main(String[] args) {
        CNFConverter.loadAsCNF(FileParser.loadFile(new File("src/skillParserFiles/CFGSkill.txt")), CFGSystem.dataBase);
        CFGSystem.fullFeatures(true);
        CFGSystem.print = 2;
        System.out.println(CFGSystem.run("which lectures are there on monday at 12"));
    }
}
