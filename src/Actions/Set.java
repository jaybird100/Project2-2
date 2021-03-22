package Actions;

import Articles.Article;
import Articles.Timer;
import Utils.Variables;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalTime;

public class Set extends Action{
    Article type;
    int ti=0;
    public Set(){}
    public Set(Article obj){
        type=obj;
    }

    @Override
    public String action() {
        if(type instanceof Timer){
            Timer t=(Timer)type;
            Stage stage = new Stage();
            stage.setTitle("Timer");
            GridPane root= new GridPane();
            LocalTime a= LocalTime.of(t.hr,t.min,t.sec);
            Label timeInterval= new Label("Time remaining: "+a.toString());
            Label timeNow=new Label("Time at start: "+LocalTime.now().toString());
            Label endTime =new Label("Time at end: " +t.tf.toString());


            root.add(timeInterval,0,0);
            root.add(timeNow, 0 ,1);
            root.add(endTime,0,2);


            stage.setScene(new Scene(root, Variables.SCREEN_WIDTH/10, Variables.SCREEN_HEIGHT/10));
            stage.show();
//            while(t.tf.getTime().isAfter(LocalTime.now())) {
//                if (ti == 1000) {
//                    timeInterval.setText("Time remaining: " + a.minusSeconds(1));
//                } else {
//                    ti = LocalTime.now().getSecond() + ti;
//                }
//
//            }
        }

        return "reached outside";
    }

    public String toString(){return "Set";}
}
