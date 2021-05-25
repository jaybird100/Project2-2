package Actions;

import Articles.Article;
import Articles.Timer;
import Utils.Variables;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class Set extends Action{
    Article type;
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
            final LocalTime[] a = {LocalTime.of(t.hr, t.min, t.sec)};


            Label timeInterval= new Label("Time remaining: "+ a[0].toString());
            Label timeNow=new Label("Time at start: "+LocalTime.now().toString());
            Label endTime =new Label("Time at end: " +t.tf.toString());


            root.add(timeInterval,0,0);
            root.add(timeNow, 0 ,1);
            root.add(endTime,0,2);
            final long[] lastTime = {System.nanoTime()};

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if(a[0].equals(LocalTime.of(0,0,0))){
                        this.stop();
                        System.out.println("done");
                        stage.close();
                    }else{
                        long s =System.nanoTime()-lastTime[0];
                        if(TimeUnit.SECONDS.convert(s,TimeUnit.NANOSECONDS)>=1) {
                            timeInterval.setText("Time remaining: " + a[0].minusSeconds(1));
                            a[0] = a[0].minusSeconds(1);
                            lastTime[0]= System.nanoTime();
                        }

                    }
                }

            };
            timer.start();
            stage.setScene(new Scene(root, Variables.SCREEN_WIDTH/5, Variables.SCREEN_HEIGHT/10));
            stage.show();


        }

        return "Timer started";
    }

    public String toString(){return "Set";}
}
