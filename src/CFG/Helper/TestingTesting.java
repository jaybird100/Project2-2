package CFG.Helper;

import CFG.CFGSystem;
import CFG.ChatBot;

import java.io.File;
import java.util.*;

public class TestingTesting {
    public static void main(String[] args) {
        CFGSystem.load(new File("src/skillParserFiles/CFGSkill.txt"));
        System.out.println(CFGSystem.run("which lectures are there at 9"));
        System.out.println(CFGSystem.run("what is <day>?"));
        System.out.println(CFGSystem.run("y"));
        System.out.println(CFGSystem.run("monday"));
    }
}
