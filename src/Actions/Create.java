package Actions;

import Articles.*;
import Attributes.ADate;
import Utils.Data;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Create extends Action {
    final int createMenuWidth=400;
    final int createMenuHeight=250;
    public Create(){}
    public Create(Article a){
        itemToCreate=a;
    }
    Article itemToCreate;


    @Override
    public String action() {
        Stage stage = new Stage();
        stage.setTitle("Create Menu");
        if(itemToCreate instanceof Lecture){
            TextField course = new TextField();
            TextField time = new TextField();
            TextField date = new TextField();
            TextField extraText = new TextField();
            Label lCourse = new Label("Course");
            Label lTime = new Label("Time");
            Label lDate = new Label("Date (dd/MM/yy)");
            Label lET = new Label("Extra Text");
            GridPane root = new GridPane();
            root.add(course,1,0);
            root.add(lCourse,0,0);
            root.add(time,1,1);
            root.add(lTime,0,1);
            root.add(date,1,2);
            root.add(lDate,0,2);
            root.add(extraText,1,3);
            root.add(lET,0,3);
            Button add = new Button("Add");
            EventHandler<ActionEvent> addE = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FileWriter writer = new FileWriter("src/CSVFiles/Lectures.csv",true);
                        String day = new ADate(date.getText()).getDay();
                        if(newLineExists(new File("src/CSVFiles/Lectures.csv"))){
                            writer.write(course.getText()+","+time.getText()+","+date.getText()+","+day+","+extraText.getText());
                        }else {
                            writer.write('\n' + course.getText() + "," + time.getText() + "," + date.getText() + "," + day + "," + extraText.getText());
                        }
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Data.fillData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            add.setOnAction(addE);
            root.add(add,0,4);
            stage.setScene(new Scene(root, createMenuWidth, createMenuHeight));
            stage.show();
        }
        if(itemToCreate instanceof Event){
            TextField title = new TextField();
            TextField time = new TextField();
            TextField date = new TextField();
            TextField notes = new TextField();
            Label lCourse = new Label("Title");
            Label lTime = new Label("Time");
            Label lDate = new Label("Date (dd/MM/yy)");
            Label lET = new Label("Notes");
            GridPane root = new GridPane();
            root.add(title,1,0);
            root.add(lCourse,0,0);
            root.add(time,1,1);
            root.add(lTime,0,1);
            root.add(date,1,2);
            root.add(lDate,0,2);
            root.add(notes,1,3);
            root.add(lET,0,3);
            Button add = new Button("Add");
            EventHandler<ActionEvent> addE = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FileWriter writer = new FileWriter("src/CSVFiles/Events.csv",true);
                        if(newLineExists(new File("src/CSVFiles/Lectures.csv"))){
                            writer.write(title.getText()+","+time.getText()+","+date.getText()+","+notes.getText());
                        }
                        writer.write('\n'+title.getText()+","+time.getText()+","+date.getText()+","+notes.getText());
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            add.setOnAction(addE);
            root.add(add,0,4);
            stage.setScene(new Scene(root, createMenuWidth, createMenuHeight));
            stage.show();
        }
        if(itemToCreate instanceof Notification){
            TextField course = new TextField();
            TextField time = new TextField();
            TextField date = new TextField();
            TextField extraText = new TextField();
            Label lCourse = new Label("Course");
            Label lTime = new Label("Time");
            Label lDate = new Label("Date (dd/MM/yy)");
            Label lET = new Label("Details");
            GridPane root = new GridPane();
            root.add(course,1,0);
            root.add(lCourse,0,0);
            root.add(time,1,1);
            root.add(lTime,0,1);
            root.add(date,1,2);
            root.add(lDate,0,2);
            root.add(extraText,1,3);
            root.add(lET,0,3);
            Button add = new Button("Add");
            EventHandler<ActionEvent> addE = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FileWriter writer = new FileWriter("src/CSVFiles/Notifications.csv",true);
                        String day = new ADate(date.getText()).getDay();
                        if(newLineExists(new File("src/CSVFiles/Notifications.csv"))){
                            writer.write(course.getText()+","+time.getText()+","+date.getText()+","+day+","+extraText.getText());
                        }else {
                            writer.write('\n' + course.getText() + "," + time.getText() + "," + date.getText() + "," + day + "," + extraText.getText());
                        }
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Data.fillData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            add.setOnAction(addE);
            root.add(add,0,4);
            stage.setScene(new Scene(root, createMenuWidth, createMenuHeight));
            stage.show();
        }
        if(itemToCreate instanceof Webpage || itemToCreate instanceof FolderLocation){
            TextField tag= new TextField();
            TextField rul= new TextField();
            Label ltag= new Label("Tag");
            Label lurl= new Label("URL");
            if(itemToCreate instanceof FolderLocation)
                lurl.setText("Path");

            GridPane root = new GridPane();
            root.add(ltag,0,0);
            root.add(tag,1,0);
            root.add(lurl,0,1);
            root.add(rul,1,1);
            String filePath="src/CSVFiles/Links.csv";
            if(itemToCreate instanceof FolderLocation)
                filePath="src/CSVFiles/Paths.csv";
            Button add = new Button("Add");
            String finalFilePath = filePath;
            EventHandler<ActionEvent> addE = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FileWriter writer = new FileWriter(finalFilePath,true);
                        if(newLineExists(new File(finalFilePath))){
                            writer.write(tag.getText()+","+rul.getText());
                        }else {
                            writer.write('\n' + tag.getText()+","+rul.getText());
                        }
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Data.fillData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            add.setOnAction(addE);
            root.add(add,0,4);
            stage.setScene(new Scene(root, createMenuWidth, createMenuHeight));
            stage.show();


        }

        return "Opening create menu";
    }
    @Override
    public String toString(){
        return "Create";
    }

    public static boolean newLineExists(File file) throws IOException {
        RandomAccessFile fileHandler = new RandomAccessFile(file, "r");
        long fileLength = fileHandler.length() - 1;
        if (fileLength < 0) {
            fileHandler.close();
            return true;
        }
        fileHandler.seek(fileLength);
        byte readByte = fileHandler.readByte();
        fileHandler.close();

        if (readByte == 0xA || readByte == 0xD) {
            return true;
        }
        return false;
    }
}
