package UI;

import Articles.Article;
import Attributes.Attribute;
import Inputs.Input;
import Inputs.SEArticles;
import Skills.Skill;
import Utils.Data;
import Utils.Parser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    final int windowWidth= 700;
    final int windowHeight = 500;

    final int skillEditorWidth=450;
    final int skillEditorHeight=450;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project 2-2");
        primaryStage.initStyle(StageStyle.DECORATED);
        TextField textField = new TextField();
        textField.setPrefWidth(windowWidth-150);
        Button button = new Button("Enter");
        Label label = new Label("");
        label.setWrapText(true);
        button.setOnAction(action -> {
            label.setText(Parser.parse(textField.getText()));

        });
        textField.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER))
            {
                label.setText(Parser.parse(textField.getText()));
            }
        });
        ScrollPane sp = new ScrollPane();
        sp.setContent(label);
        Button skillEditor = new Button("Skill editor");
        skillEditor.setOnAction(action -> {
            Stage stage = new Stage();
            TextField commandInput = new TextField();
            commandInput.setPrefWidth(skillEditorWidth-150);
            Label cm = new Label("Enter command");
            ObservableList<Skill> skillList = FXCollections.observableList(Data.allSkills);
            ComboBox skills = new ComboBox(skillList);
            ObservableList<SEArticles> objectList = FXCollections.observableArrayList(Data.seFetch.articles);
            ComboBox objects = new ComboBox(objectList);
            ComboBox inputs = new ComboBox();
            ComboBox limiters = new ComboBox();
            final SEArticles[] in = {null};
            inputs.setMinWidth(200);
            EventHandler<ActionEvent> event =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            in[0] = (SEArticles)(objects.getValue());
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
                        writer.write("\n"+commandInput.getText()+"\n");
                        writer.write(skills.getValue().toString()+"\n");
                        writer.write(objects.getValue().toString()+"\n");
                        writer.write(limiterText.getText()+"\n");
                        writer.write("all"+"\n");
                        writer.write("-");
                        writer.close();
                        Data.fillData();
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
        GridPane root = new GridPane();
        root.add(textField,0,0,1,5);
        root.add(button,6,0,1,3);
        root.add(sp,0,3,9,5);
        root.add(skillEditor,7,0,3,2);
        primaryStage.setScene(new Scene(root,windowWidth,windowHeight));
        primaryStage.show();
    }



    public static void main(String[] args) throws IOException {
        //premptive preparation of data for query answering
        Data.fillData();
        System.out.println(Arrays.deepToString(Data.commands.toArray()));
        System.out.println(Arrays.deepToString(Data.toCall.toArray()));
        System.out.println(Arrays.deepToString(Data.objectsFromTxt.toArray()));
        System.out.println(Arrays.deepToString(Data.limiters.toArray()));
        System.out.println(Arrays.deepToString(Data.attributeIndexes.toArray()));
        //System.out.println(Arrays.deepToString(Data.lectures.toArray()));
        launch(args);
    }
}
