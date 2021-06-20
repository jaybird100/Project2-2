package CFG.Eval;

import CFG.CFGSystem;
import CFG.ChatBot;
import CFG.Helper.FileReader;

import java.io.File;
import java.util.Arrays;

public class res {
    public static void main(String[] args){
        //Grammar test
        CFGSystem.load(new File("src/SkillParserFiles/CFGSkill.txt"));
        String utterances=FileReader.loadFile(new File("res/utterances.csv"));
        String[] s= utterances.split("\n");
        int len=s.length;
        String[][] g= new String[len][4];
        double[][] r= {
                //{0,0,0,0},
                { 0.0 , 0.0 , 0.0 , 0.5 },
                { 0.0 , 0.0 , 0.5 , 0.0 },
                { 0.0 , 0.0 , 0.5 , 0.5 },
                { 0.0 , 0.5 , 0.0 , 0.0 },
                { 0.0 , 0.5 , 0.0 , 0.5 },
                { 0.0 , 0.5 , 0.5 , 0.0 },
                { 0.0 , 0.5 , 0.5 , 0.5 },
                { 0.5 , 0.0 , 0.0 , 0.0 },
                { 0.5 , 0.0 , 0.0 , 0.5 },
                { 0.5 , 0.0 , 0.5 , 0.0 },
                { 0.5 , 0.0 , 0.5 , 0.5 },
                { 0.5 , 0.5 , 0.0 , 0.0 },
                { 0.5 , 0.5 , 0.0 , 0.5 },
                { 0.5 , 0.5 , 0.5 , 0.0 },
                { 0.5 , 0.5 , 0.5 , 0.5 }};
        for(double[] u:r) {
            double tp = 0;
            double tn = 0;
            double fp = 0;
            double fn = 0;
            //Original input    |Original Output    |Scrambled input    |Scrambled output
            //fill out col 1, record col 2, generate col3, record col 4
            ErrorGen e = new ErrorGen(u);
            for (int i = 0; i < len; i++) {
                g[i][0] = s[i];
                g[i][1] = CFGSystem.run(s[i]);
                g[i][2] = e.generateErr(s[i]);
                g[i][3] = CFGSystem.run(g[i][2]);

                if (g[i][0].equals(g[i][2])) {//input before/after scramble is same
                    if (g[i][1].equals(g[i][3])) {//output before.after scramble is same
                        ++fp;
                    } else {//output before/after scramble is changed

                        ++tn;
                    }
                } else {//input before/after scramble is changed
                    if (g[i][1].equals(g[i][3])) {//output before/after scramble is same
                        ++tp;
                    } else {//output before/after scramble is changed
                        ++fn;
                        System.out.println("FN"+ Arrays.toString(g[i]));

                    }
                }
            }
            System.out.println(Arrays.toString(u));
            System.out.println("tp: " + tp + "\ntn: " + tn + "\nfp: " + fp + "\nfn: " + fn);
            System.out.println("Recall: " + (tp / (tp + fn)));
            System.out.println("Percision: " + (tp / (tp + fp)));
            System.out.println("Accuracy: " + ((tp + tn) / (tp + tn + fn + fp)));
            System.out.println("F1: " + (tp / (tp + (.5 * (fp + fn)))));
            System.out.println("___________________________");
        }




    }

    public static void print2d(String[][] r){
        for(int i=0;i<r.length;i++){
            for(int j=0;j<r[i].length;j++){
                System.out.print(r[i][j]+"|");
            }
            System.out.println("");
        }
    }
}
