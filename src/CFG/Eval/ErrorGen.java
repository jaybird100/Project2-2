package CFG.Eval;

import java.util.Random;

public class ErrorGen {
    double[] probs;//4 element array
    Random r;
    public ErrorGen(double [] d){
        this.probs=d;
        this.r=new Random();
    }

    public void setProbs(double[] d){
        this.probs=d;
    }



    public String generateErr(String s){
        String[]  words= s.split(" ");
        String res="";

        for(String w:words){
            w=duplicateLetter(w);
            w=insertLetter(w);
            w=swapLetter(w);
            w=removeLetter(w);
            res+=w+" ";


        }
        return res.trim();
    }

    private String duplicateLetter(String s){
        double n=r.nextDouble();
        if(n<probs[0]){
            int index=r.nextInt(s.length());
            s=s.substring(0,index)+s.charAt(index)+s.substring(index);
        }
        return s;
    }

    private String removeLetter(String s){
        double n=r.nextDouble();
        StringBuilder ns= new StringBuilder(s);
        if(n<probs[1]){
            int index=r.nextInt(s.length());
            s=ns.deleteCharAt(index).toString();
        }
        return s;
    }

    private String insertLetter(String s){
        double n=r.nextDouble();
        if(n<probs[2]){
            int index=r.nextInt(s.length());
            char c= (char)(r.nextInt(26)+'a');
            s=s.substring(0,index)+c+s.substring(index);
        }
        return s;
    }
    private String swapLetter(String s){
        double n=r.nextDouble();
        if(n<probs[3]){
            int index1=r.nextInt(s.length());
            int index2=r.nextInt(s.length());
            char[] c=s.toCharArray();
            char temp= c[index1];
            c[index1]=c[index2];
            c[index2]=temp;
            s=new String(c);
        }
        return s;
    }

}
