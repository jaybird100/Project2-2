package UI.Pages.Calendar;

import Utils.Variables;
import javafx.scene.shape.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Date {
    LocalDate date;
    ArrayList<String[]> lectures;
    Rectangle[] miniAgenda;

    public Date(LocalDate date, Rectangle[] miniAgenda) throws IOException {
        this.date = date;
        lectures = new ArrayList<>();
        this.miniAgenda = miniAgenda;
        setLectures();
    }

    public LocalDate getDate() {
        return date;
    }

    private void setLectures() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        File file = new File(Variables.DEFAULT_CSV_FILE_PATH+"Lectures.csv");
        Scanner sc = new Scanner(file);
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] lecture = line.split(",");
                if(lecture[2].equalsIgnoreCase(formatter.format(date))){
                    lectures.add(lecture);
                }
            }
            sc.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Rectangle[] getMiniAgenda() {
        return miniAgenda;
    }

    public ArrayList<String[]> getLectures() {
        return lectures;
    }
}