package CFG.Helper;

import CFG.CFGSystem;
import CFG.ChatBot;

import java.io.File;
import java.util.Scanner;

public class TestingTesting {
    public static void main(String[] args) {
        CFGSystem.load(new File("src/skillParserFiles/CFGSkill.txt"));

        Scanner s = new Scanner(System.in);
        while(true) {
            String input = s.nextLine();
            System.out.println(ChatBot.run(input));
        }
    }

}
