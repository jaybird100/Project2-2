package A;

import java.io.*;
import java.util.ArrayList;

public abstract class Skill {
    public ArrayList<Attribute> attributes= new ArrayList<>();
    public Skill(){
    }
    public Skill(ArrayList<Attribute>a){
        this.attributes=a;
    }
    public ArrayList<Attribute> getAttributes(){return attributes;}
    public abstract Skill fromTextFile(String fileName);//each skill is responsible for turning a text file into an object of itself
    public abstract void action();


    @Override
    public String toString() {
        String att ="\n";
        for (int i=0;i<attributes.size();i++){
            att+=attributes.get(i).toString()+"\n";
        }
        return "Skill{" +
                "attributes=" + att +
                '}';
    }

    public void toTextFile(String fileName){
        String str= toString();
        File file= new File(Variables.DEFAULT_TEXT_FILE_PATH+fileName);


        PrintWriter writer;
        try {


            file.delete();
            file.createNewFile();
            writer = new PrintWriter(new FileWriter(file));
            writer.println(str);
            writer.close();


        } catch (IOException e) {
            System.err.println("Given file ("+file+") is invalid");
            e.printStackTrace();
        }
    }


    public static void toFile(String filename, Skill s) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Variables.DEFAULT_CLASS_FILE_PATH+filename));
        oos.writeObject(s);
        oos.flush();
        oos.close();
    }


    public static Skill fromFile(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Variables.DEFAULT_CLASS_FILE_PATH+filename));
        Skill net = (Skill) ois.readObject();
        ois.close();
        return net;
    }




}
