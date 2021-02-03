package diffApp;

import java.io.*;


public abstract class Skill<T> {
    private String entry;

    public Skill(String a){
        this.entry=a;
    }

    public abstract Skill fromTextFile(String fileName);//each skill is responsible for turning a text file into an object of itself

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
