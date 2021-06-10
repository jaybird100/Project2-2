package UI.Pages.PopUpWindows;

import javafx.stage.Stage;

public class Calculator {
    public static void open(){
        Calc.Calculator calc2 = new Calc.Calculator();
        Stage sta = new Stage();
        calc2.start(sta);
        sta.getScene().getStylesheets().add("UI/CSS/theme.css");
        sta.show();
    }
}
