package UI;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Calendar {

    Stage primaryStage;
    Group root;
    LocalDate date;
    Label week;
    Date[] dates;
    Label[] dateLabels;
    Label[] lectureLabels;
    Label[] deadlineLabels;
    Rectangle[][] miniAgenda;
    int width;
    int height;
    int weekCurrent;
    int weekTemp;
    DateTimeFormatter formatter;
    final String[] DAYS = {"", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    //

    public Calendar(Stage primaryStage, Group root) throws IOException {
        this.primaryStage = primaryStage;
        this.root = root;
        date = LocalDate.now(ZoneId.of("Europe/Amsterdam"));
        dates = new Date[7];
        dateLabels = new Label[7];
        lectureLabels = new Label[7];
        deadlineLabels = new Label[7];
        miniAgenda = new Rectangle[7][2]; //set of rectangles in the calendar
        width = 70;
        height = 50;
        formatter = DateTimeFormatter.ofPattern("dd/MM");
        InitializeCalendar();
        fillMiniAgenda();
        SetButtons();
    }

    public void InitializeCalendar() throws IOException {
        WeekFields wf = WeekFields.of(Locale.getDefault()) ;
        weekCurrent = Integer.parseInt(String.format("%02d",date.get(wf.weekOfWeekBasedYear())));
        weekTemp = weekCurrent;
        week = new Label("Week " + weekCurrent);
        week.setTranslateX(270);
        week.setTranslateY(65);
        week.setFont(Font.font("Arial", FontWeight.BOLD, 20.0));
        root.getChildren().add(week);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle;
                if(j==0){
                    rectangle = new Rectangle(15, 100 + (height*i), width*1.2, height);
                }
                else{
                    rectangle = new Rectangle(15 + (width*0.2) + (width * j), 100 + (height*i), width, height);
                }
                rectangle.setFill(Color.LAVENDERBLUSH);
                rectangle.setStroke(Color.DARKGREY);
                rectangle.setStrokeWidth(3);
                rectangle.setArcWidth(10.0);
                rectangle.setArcHeight(10.0);
                root.getChildren().add(rectangle);
                if (i == 0) {
                    Label day = new Label(DAYS[j]);
                    day.setLayoutX(50 + (width * j));
                    day.setLayoutY(90);
                    day.setPrefSize(width, height);
                    root.getChildren().add(day);
                } else if (i == 1 && j == 0) {
                    Label heading = new Label("Lectures");
                    heading.setLayoutX(25);
                    heading.setLayoutY(height * 3.3);
                    //heading.setPrefSize(width, height*2);
                    root.getChildren().add(heading);
                } else if (i == 2 && j == 0) {
                    Label heading = new Label("Events");
                    heading.setLayoutX(25);
                    heading.setLayoutY(height * 4.3);
                    //heading.setPrefSize(width, height*2);
                    root.getChildren().add(heading);
                }
                else{
                    miniAgenda[j-1][i-1] = rectangle;
                }
            }
        }
        for (int i = 0; i < 7; i++) {
            if (date.getDayOfWeek().toString().substring(0, 3).equalsIgnoreCase(DAYS[i + 1])) {
                dates[i] = new Date(date, miniAgenda[i]);
                dateLabels[i] = new Label(formatter.format(dates[i].getDate()));
                dateLabels[i].setLayoutX(115 + (width * i));
                dateLabels[i].setLayoutY(120);
                root.getChildren().add(dateLabels[i]);
                int j = i;
                int k = 1;
                while (j > 0) {
                    dates[--j] = new Date(date.minusDays(k++), miniAgenda[j]);
                    dateLabels[j] = new Label(formatter.format(dates[j].getDate()));
                    dateLabels[j].setLayoutX(115 + (width * j));
                    dateLabels[j].setLayoutY(120);
                    root.getChildren().add(dateLabels[j]);
                }
                k = 1;
                i++;
                while (i < 7) {
                    dates[i] = new Date(date.plusDays(k++), miniAgenda[i]);
                    dateLabels[i] = new Label(formatter.format(dates[i].getDate()));
                    dateLabels[i].setLayoutX(115 + (width * i));
                    dateLabels[i].setLayoutY(120);
                    root.getChildren().add(dateLabels[i++]);
                }
            }
        }
        for (int i = 0; i < 7; i++){
            lectureLabels[i] = new Label(Integer.toString(dates[i].getLectures().size()));
            lectureLabels[i].setLayoutX(128 + (width * i));
            lectureLabels[i].setLayoutY(115 + height);
            root.getChildren().add(lectureLabels[i]);
        }

    }



    private void SetButtons(){
        Button previous = new Button();
        previous.setGraphic(new ImageView(new Image("Skins/Previous.jpg", 25, 25, true, true)));
        previous.setTranslateX(20);
        previous.setTranslateY(63);
        previous.setPrefSize(25, 25);
        previous.setOnAction(e -> {
            week.setText("Week " + (--weekTemp));
            for (int i = 0; i < 7; i++) {
                try {
                    dates[i] = new Date(dates[i].getDate().minusWeeks(1), miniAgenda[i]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dateLabels[i].setText(formatter.format(dates[i].getDate()));
                lectureLabels[i].setText(Integer.toString(dates[i].getLectures().size()));
            }
        });
        root.getChildren().add(previous);

        Button next = new Button();
        next.setGraphic(new ImageView(new Image("Skins/Next.jpg", 25, 25, true, true)));
        next.setTranslateX(545);
        next.setTranslateY(63);
        next.setPrefSize(25, 25);
        root.getChildren().add(next);
        next.setOnAction(e -> {
            week.setText("Week " + (++weekTemp));
            for (int i = 0; i < 7; i++) {
                try {
                    dates[i] = new Date(dates[i].getDate().plusWeeks(1), miniAgenda[i]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dateLabels[i].setText(formatter.format(dates[i].getDate()));
                lectureLabels[i].setText(Integer.toString(dates[i].getLectures().size()));
            }
        });

        Button reset = new Button("RESET");
        reset.setTextAlignment(TextAlignment.CENTER);
        reset.setTranslateX(535);
        reset.setTranslateY(37);
        reset.setPrefSize(50, 10);
        root.getChildren().add(reset);
        reset.setOnAction(e -> {
            week.setText("Week " + (weekCurrent));
            for (int i = 0; i < 7; i++) {
                try {
                    if (date.getDayOfWeek().toString().substring(0, 3).equalsIgnoreCase(DAYS[i + 1])) {
                        dates[i] = new Date(date, miniAgenda[i]);
                        dateLabels[i].setText(formatter.format(dates[i].getDate()));
                        lectureLabels[i].setText(Integer.toString(dates[i].getLectures().size()));
                        int j = i;
                        int k = 1;
                        while (j > 0) {
                            dates[--j] = new Date(date.minusDays(k++), miniAgenda[j]);
                            dateLabels[j].setText(formatter.format(dates[j].getDate()));
                            lectureLabels[j].setText(Integer.toString(dates[j].getLectures().size()));
                        }
                        k = 1;
                        i++;
                        while (i < 7) {
                            dates[i] = new Date(date.plusDays(k++), miniAgenda[i]);
                            dateLabels[i].setText(formatter.format(dates[i].getDate()));
                            lectureLabels[i].setText(Integer.toString(dates[i++].getLectures().size()));
                        }
                    }} catch (IOException ex) {
                    ex.printStackTrace();
                }
                dateLabels[i].setText(formatter.format(dates[i].getDate()));
                lectureLabels[i].setText(Integer.toString(dates[i].getLectures().size()));
            }
        });

        Button backButton = new Button("Back");
        backButton.setTextAlignment(TextAlignment.CENTER);
        backButton.setTranslateX(20);
        backButton.setTranslateY(20);
        backButton.setPrefSize(50, 10);
        root.getChildren().add(backButton);
        backButton.setOnAction(e -> {
            root.getChildren().clear();
            root.getChildren().add(Main.title);
            new SkillEditor(primaryStage, root);
        });

    }

//temp
    private void fillMiniAgenda(){
        for(int i=0; i<dates.length; i++){
            Rectangle rectangle = dates[i].getMiniAgenda()[0];
            int finalI = i;  //finalI was just suggested by intellij
            rectangle.setOnMouseClicked(event -> {
                Group smallRoot = new Group();
                Scene lectures = new Scene(smallRoot, 500, 30*((dates[finalI].getLectures().size())+2), Color.LAVENDER);
                for(int j=0; j<dates[finalI].getLectures().size(); j++) {
                    Label lectureDetails = new Label("You have " + dates[finalI].getLectures().get(j)[0] + " on " + dates[finalI].getLectures().get(j)[3] + " at " + dates[finalI].getLectures().get(j)[1]);
                    lectureDetails.setFont(Font. font("Verdana"));
                    smallRoot.getChildren().add(lectureDetails);
                    lectureDetails.setLayoutX(5);
                    lectureDetails.setLayoutY(10 + j*20);
                }
                if(dates[finalI].getLectures().size() == 0){
                    Label lectureDetails = new Label("No lectures today!");
                    smallRoot.getChildren().add(lectureDetails);
                    lectureDetails.setLayoutX(5);
                    lectureDetails.setLayoutY(20);
                }
                Stage expandedLectures = new Stage();
                expandedLectures.setTitle("Lectures on " + formatter.format(dates[finalI].getDate()));
                expandedLectures.setScene(lectures);
                expandedLectures.setX(rectangle.getX() + primaryStage.getX());
                expandedLectures.setY(rectangle.getY() + primaryStage.getY() + height / 1.8);
                expandedLectures.show();
            });
        }
    }
}
