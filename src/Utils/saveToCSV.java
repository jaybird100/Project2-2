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

                pw.println(information);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //converts the string from the text field to a CSV format
    public static void convertToCSV(String data) throws IOException {
        String finalData="";
        String tempData="";
        String[] tempArray=data.split("-");
        //You need the tempArray[3] since the time also has a : in betweent it
        tempData=tempArray[2];
        String[] dataArray=tempData.split(",");
        String fileName=tempArray[1];

        for(int i=0; i<dataArray.length-1; i++){
            finalData=finalData+dataArray[i]+",";
        }
        finalData=finalData+dataArray[dataArray.length-1];
        saveToFile(fileName,finalData);
        addObjects(data);
    }

    //Turn the added string into an Article Type and add them to their respective array lists
    //Currently we have Lecture/Notiffication if we get more we can add them here in the else if
    public static void addObjects(String data){
        String[]dataArray=data.split("-");
        String[]finalDataArray=dataArray[2].split(",");
        if(data.trim().equalsIgnoreCase("Lectures")){
            Lecture lecture;
            Course c = new Course(finalDataArray[0].trim());
            Time t = new Time(finalDataArray[1].trim());
            ADate d = new ADate(finalDataArray[2].trim());
            if(finalDataArray.length >4){
                ExtraText ex=new ExtraText(dataArray[3].trim());
                lecture=new Lecture(c,t,d,ex);}
            else{
                lecture=new Lecture(c,t,d);
            }
            Data.lectures.add(lecture);
        }
        else if(data.trim().equalsIgnoreCase("Notifications")){
            Notification notification;
            Course c = new Course(finalDataArray[0].trim());
            Time t = new Time(finalDataArray[1].trim());
            ADate d = new ADate(finalDataArray[2].trim());
            ExtraText ex=new ExtraText(finalDataArray[3].trim());
            notification=new Notification(c,t,d,ex);

            Data.notifications.add(notification);
        }

    }
    public static void checkText(String data) throws IOException {
        if(data.contains("Add")){
            convertToCSV(data);
        }
    }
}


