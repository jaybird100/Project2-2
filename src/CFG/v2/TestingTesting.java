package CFG.v2;

import CFG.FileParser;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class TestingTesting {
    public static void main(String[] args) {
        CNFConverter.loadAsCNF(FileParser.loadFile(new File("src/CFG/v2/dtypeTest.txt")), CFGSystem.dataBase);
        CFGSystem.fullFeatures(true);
        System.out.println(CFGSystem.run("which lectures are there on monday at 12", 0));
    }
}
