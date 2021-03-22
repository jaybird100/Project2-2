package UI;

import Actions.Create;
import Actions.Fetch;
import Actions.Open;
import Calc.Calculator;
import Inputs.SEFetchArticles;
import Actions.Action;
import Utils.Data;
import Utils.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SkillEditor {

    final int skillEditorWidth=450;
    final int skillEditorHeight=450;

    private Calendar calendar;

    Stage primaryStage;
    Group root;

    public SkillEditor(Stage primaryStage, Group root){
        this.primaryStage = primaryStage;
        this.root = root;
        createEditor();
    }

    public void createEditor(){
        Label label = new Label("");
        label.setWrapText(true);
        label.setTranslateX(100);
        label.setTranslateY(150);

        TextField textField = new TextField();
        textField.setPrefWidth(300);
        root.getChildren().add(textField);
        textField.setTranslateX(90);
        textField.setTranslateY(100);

        Button button = new Button("Enter");
        root.getChildren().add(button);
        button.setTranslateX(390);
        button.setTranslateY(100);
        button.setOnAction(action -> {
            label.setText(Parser.parse(textField.getText()));
        });

        textField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                label.setText(Parser.parse(textField.getText()));
            }
        });

        Button calendarButton = new Button("Calendar");
        root.getChildren().add(calendarButton);
        calendarButton.setTranslateX(450);
        calendarButton.setTranslateY(100);
        calendarButton.setOnAction(e -> {
            try {
                calendar = new Calendar(primaryStage, root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button clarify = new Button("?");
        root.getChildren().add(clarify);
        clarify.setTranslateX(520);
        clarify.setTranslateY(100);
        clarify.setOnAction(e -> {
            Group root = new Group();
            Stage hint = new Stage();
            hint.setTitle("???");

            Label explain = new Label("Displays the entire calendar");
            explain.setTranslateX(20);
            explain.setTranslateY(20);
            root.getChildren().add(explain);

            Scene scene = new Scene(root, 200, 100, Color.LAVENDER);
            hint.setScene(scene);
            hint.setX(700);
            hint.setY(250);
            hint.show();
        });

        ScrollPane sp = new ScrollPane();
        sp.setContent(label);
        root.getChildren().add(sp);
        sp.setTranslateX(90);
        sp.setTranslateY(150);
        sp.setPrefWidth(330);
        sp.setPrefHeight(70);

        Button skillEditor = new Button("Skill editor");
        root.getChildren().add(skillEditor);
        skillEditor.setTranslateX(15);
        skillEditor.setTranslateY(100);
        skillEditor.setOnAction(action -> {
            Stage stage = new Stage();
            TextField commandInput = new TextField();
            commandInput.setPrefWidth(skillEditorWidth-150);
            Label cm = new Label("Enter command");
            ObservableList<Action> skillList = FXCollections.observableList(Data.allSkills);
            ComboBox skills = new ComboBox(skillList);
            final Action[] ac = new Action[1];
            ComboBox objects = new ComboBox();
            ComboBox inputs = new ComboBox();
            ComboBox limiters = new ComboBox();
            EventHandler<ActionEvent> setAc =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            ac[0] = (Action) skills.getValue();
                            if(skills.getValue() instanceof Fetch){
                                ObservableList<SEFetchArticles> objectList = FXCollections.observableArrayList(Data.seFetch.articles);
                                objects.setItems(objectList);
                            }
                            if(skills.getValue() instanceof Open){
                                ObservableList<String> inpus = FXCollections.observableArrayList(Data.seOpen.inputs);
                                inputs.setItems(inpus);
                            }
                            if(skills.getValue() instanceof Create){
                                ArrayList<String> inp = new ArrayList<>();
                                inp.add("<ARTICLE>");
                                ObservableList<String> inpus = FXCollections.observableArrayList(inp);
                                inputs.setItems(inpus);
                            }
                        }
                    };
            skills.setOnAction(setAc);
            final SEFetchArticles[] in = {null};
            inputs.setMinWidth(200);

            EventHandler<ActionEvent> event =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            in[0] = (SEFetchArticles)(objects.getValue());
                            ObservableList<String> inputList = FXCollections.observableArrayList(in[0].inputs);
                            inputs.setItems(inputList);
                            ObservableList<String> initialLimits = FXCollections.observableArrayList(in[0].eachInputsLimiters.get(0));
                            limiters.setItems(initialLimits);
                        }
                    };

            // Set on action
            objects.setOnAction(event);
            EventHandler<ActionEvent> event2 =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            commandInput.setText(commandInput.getText().trim()+" "+inputs.getValue());
                        }
                    };

            Button enterInput = new Button("Enter key");
            enterInput.setOnAction(event2);
            enterInput.setMinWidth(100);
            EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ArrayList<Integer> possibleLimiters = new ArrayList<>();
                    String[] toAnalyze = commandInput.getText().split(" ");
                    for(int p=0;p<in[0].inputs.size();p++){
                        for(String s:toAnalyze){
                            if(s.equalsIgnoreCase(in[0].inputs.get(p))){
                                possibleLimiters.add(p+1);
                            }
                        }
                    }
                    ArrayList<String> limits = in[0].eachInputsLimiters.get(0);
                    for(Integer i:possibleLimiters){
                        limits.addAll(in[0].eachInputsLimiters.get(i));
                    }
                    ObservableList<String> limits2 = FXCollections.observableArrayList(limits);
                    limiters.setItems(limits2);
                }
            };
            Button interpret = new Button("Interpret");
            interpret.setOnAction(event3);
            Label limiterText = new Label();
            Button addLimiter = new Button("Add");
            EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(limiterText.getText().length()>1){
                        limiterText.setText(limiterText.getText().trim()+" && "+limiters.getValue().toString());
                    }else{
                        limiterText.setText(limiters.getValue().toString());
                    }
                }
            };
            addLimiter.setOnAction(event4);
            Button removeLimiter = new Button("Remove");
            EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String[] temp = limiterText.getText().split("&&");
                    String toEnter = "";
                    for(int i =0;i<temp.length-2;i++){
                        toEnter+=temp[i];
                        if(i!=temp.length-2){
                            toEnter+=" && ";
                        }
                    }
                    limiterText.setText(toEnter);
                }
            };
            removeLimiter.setOnAction(event5);
            GridPane root = new GridPane();
            root.add(cm,0,0,1,1);
            root.add(commandInput,1,0);
            root.add(interpret,2,0);
            root.add(skills,0,1);
            root.add(objects,0,2);
            root.add(inputs,0,3);
            root.add(enterInput,2,3);
            root.add(limiters,0,5);
            root.add(limiterText,0,6,5,2);
            root.add(addLimiter,1,5);
            root.add(removeLimiter,2,5);
            Button generate = new Button("Generate");
            EventHandler<ActionEvent> event6 = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FileWriter writer = new FileWriter("src/SkillParserFiles/skills.txt",true);
                        if(ac[0] instanceof Fetch) {
                            writer.write("\n" + commandInput.getText() + "\n");
                            writer.write(skills.getValue().toString() + "\n");
                            writer.write(objects.getValue().toString() + "\n");
                            writer.write(limiterText.getText() + "\n");
                            writer.write("all" + "\n");
                            writer.write("-");
                        }
                        if(ac[0] instanceof Open){
                            writer.write("\n"+commandInput.getText()+"\n");
                            writer.write(ac[0].toString()+"\n");
                            writer.write("Webpage"+"\n");
                            writer.write("-");
                        }
                        if(ac[0] instanceof Create){
                            writer.write("\n"+commandInput.getText()+"\n");
                            writer.write(ac[0].toString()+"\n");
                            writer.write("-");
                        }
                        writer.close();
                        System.out.println("Data length: "+Data.commands.size());
                        Data.fillData();
                        System.out.println("Data length2:"+Data.commands.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
            generate.setOnAction(event6);
            root.add(generate,0,8);
            stage.setTitle("Skill Editor");
            stage.setScene(new Scene(root, skillEditorWidth, skillEditorHeight));
            stage.show();
        });
        Button calc = new Button("Calculator");
        EventHandler<ActionEvent> event7 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Calculator calc2 = new Calculator();
                Stage sta = new Stage();
                calc2.start(sta);
                sta.show();
            }
        };
        calc.setOnAction(event7);
    }


}
