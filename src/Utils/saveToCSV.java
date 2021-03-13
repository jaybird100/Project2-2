package Utils;


import Articles.Lecture;
import Articles.Notification;
import Attributes.ADate;
import Attributes.Course;
import Attributes.ExtraText;
import Attributes.Time;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class saveToCSV {



    //Takes in: File Name, Text that you want to store in it
    //Generates a txt file(if one doesnt exist with the same File Name) and stores the text in it.
    public static void saveToFile(String fileName, String information)throws
            IOException{


        String filePath="src/CSVFiles/"+fileName+".csv";
        try{
            File csvOutputFile=new File(filePath);
            FileWriter fw=new FileWriter(csvOutputFile,true);
            PrintWriter pw=new PrintWriter(fw);
            if (!information.equals("")) {

                pw.println();
                pw.print(information);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    //converts the string from the text field to a CSV format
    public static void convertToCSV(String data) throws IOException {
        String finalData=" ";
        String[] dataArray=data.split(" ");
        String fileName=dataArray[1];

        for(int i=2; i<dataArray.length-1; i++){
            finalData=finalData+dataArray[i]+" ,";
        }
        finalData=finalData+dataArray[dataArray.length-1];
        saveToFile(fileName,finalData);
    }

    //Turn the added string into an Article Type and add them to their respective array lists
    //Currently we have Lecture/Notiffication if we get more we can add them here in the else if
    public static void addObjects(String data){
        String[]dataArray=data.split(" ");
        if(data.trim().equalsIgnoreCase("Lecture")){
            Lecture lecture;
            Course c = new Course(dataArray[3].trim());
            Time t = new Time(dataArray[4].trim());
            ADate d = new ADate(dataArray[5].trim());
            if(dataArray.length >6){
                ExtraText ex=new ExtraText(dataArray[6].trim());
                lecture=new Lecture(c,t,d,ex);}
            else{
                lecture=new Lecture(c,t,d);
            }
            Data.lectures.add(lecture);
        }
        else if(data.trim().equalsIgnoreCase("Notifications")){
            Notification notification;
            Course c = new Course(dataArray[3].trim());
            Time t = new Time(dataArray[4].trim());
            ADate d = new ADate(dataArray[5].trim());
            ExtraText ex=new ExtraText(dataArray[6].trim());
            notification=new Notification(c,t,d,ex);

            Data.notifications.add(notification);
        }

    }
}

