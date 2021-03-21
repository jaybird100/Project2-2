package UI.PopUpWindows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class Calculator {
    public static void open(){
        Calc.Calculator calc2 = new Calc.Calculator();
        Stage sta = new Stage();
        calc2.start(sta);
        sta.show();
    }
}
