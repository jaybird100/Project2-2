package UI;

import javafx.scene.shape.Rectangle;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");
        File file = new File("C:\\Users\\ranja\\Desktop\\DKE\\Year 2\\Semester 2\\Project 2-2\\Project2-2\\src\\CSVFiles/Lectures.csv");
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