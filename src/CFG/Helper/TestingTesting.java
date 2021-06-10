package CFG.Helper;

import java.io.File;
import java.util.List;

public class TestingTesting {
    public static void main(String[] args) {
        List<String> generated = new Generator(new File("src/skillParserFiles/CFGSkill.txt"), 0).generate(100);
        Generator.histogram(generated);
    }
}
