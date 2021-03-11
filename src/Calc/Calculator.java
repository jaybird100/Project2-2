package Calc;


import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Calculator extends Application {
	private int number1;
	private int number2;
	private String logic;
	private TextField view;
	private boolean flag;
	private boolean eq;
	public void start(Stage primaryStage) {
		try {

			AnchorPane root = new AnchorPane();
//			root.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			primaryStage.setWidth(228);
			primaryStage.setHeight(260);

			primaryStage.setResizable(false);
			

			addComp(root);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void addComp(AnchorPane root) {

		view = new TextField("0");
		view.setMinSize(200, 50);
		view.setLayoutX(10);
		view.setLayoutY(15);

		view.setEditable(false);

		view.setAlignment(Pos.CENTER_RIGHT);

		view.setFont(new Font("Consolas", 20));
		root.getChildren().add(view);

		GridPane gridPane = new GridPane();

		gridPane.setHgap(20);

		gridPane.setVgap(10);
		gridPane.setLayoutY(65);
		gridPane.setPrefWidth(228);
		gridPane.setPrefHeight(185);
//		gridPane.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		gridPane.setPadding(new Insets(10));

		root.getChildren().add(gridPane);
		

		NumberEvent numberEvent = new NumberEvent();

		Button btn_7 = new MyButton("7");
		btn_7.setOnMouseClicked(numberEvent);
		gridPane.add(btn_7,0,0);
		
		Button btn_8 = new MyButton("8");
		btn_8.setOnMouseClicked(numberEvent);
		gridPane.add(btn_8,1,0);
		
		Button btn_9 = new MyButton("9");
		btn_9.setOnMouseClicked(numberEvent);
		gridPane.add(btn_9,2,0);
		
		Button btn_4 = new MyButton("4");
		btn_4.setOnMouseClicked(numberEvent);
		gridPane.add(btn_4,0,1);
		
		Button btn_5 = new MyButton("5");
		btn_5.setOnMouseClicked(numberEvent);
		gridPane.add(btn_5,1,1);
		
		Button btn_6 = new MyButton("6");
		btn_6.setOnMouseClicked(numberEvent);
		gridPane.add(btn_6,2,1);
		
		Button btn_1 = new MyButton("1");
		btn_1.setOnMouseClicked(numberEvent);
		gridPane.add(btn_1,0,2);
		
		Button btn_2 = new MyButton("2");
		btn_2.setOnMouseClicked(numberEvent);
		gridPane.add(btn_2,1,2);
		
		Button btn_3 = new MyButton("3");
		btn_3.setOnMouseClicked(numberEvent);
		gridPane.add(btn_3,2,2);
		
		Button btn_0 = new MyButton("0");
		btn_0.setOnMouseClicked(numberEvent);
		gridPane.add(btn_0,0,3);
		
		Button btn_C = new MyButton("C");
		btn_C.setOnMouseClicked(new EventHandler<Event>() {
			public void handle(Event event) {
				clean();
			}
		});
		gridPane.add(btn_C,1,3);
		
		Button btn_eq = new MyButton("=");
		btn_eq.setOnMouseClicked(new EventHandler<Event>() {
			public void handle(Event event) {

				switch (logic) {
				case "+":
					number1 = number1+number2;
					break;
				case "-":
					number1 = number1-number2;
					break;
				case "*":
					number1 = number1*number2;
					break;
				case "/":
					number1 = number1/number2;
					break;
 
				default:
					break;
				}

				view.setText(number1+"");

				eq = true;
			}
		});
		gridPane.add(btn_eq,2,3);

		LogicEvent logicEvent = new LogicEvent();
		Button btn_add = new MyButton("+");

		btn_add.setOnMouseClicked(logicEvent);
		gridPane.add(btn_add,3,0);
		Button btn_sub = new MyButton("-");
		btn_sub.setOnMouseClicked(logicEvent);
		gridPane.add(btn_sub,3,1);
		Button btn_mul = new MyButton("*");
		btn_mul.setOnMouseClicked(logicEvent);
		gridPane.add(btn_mul,3,2);
		Button btn_div = new MyButton("/");
		btn_div.setOnMouseClicked(logicEvent);
		gridPane.add(btn_div,3,3);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	class NumberEvent implements EventHandler<Event>{
		public void handle(Event event) {

			if (eq) {
				clean();
			}


			String text = view.getText();

			MyButton btn = (MyButton)event.getSource();

			String btnNumber = btn.getText();

			if (flag) {
				text = "0";
				flag = false;
			}
			text+=btnNumber;

			int num = Integer.parseInt(text);

			if (logic==null) {
				number1 = num;
				view.setText(number1+"");

			}else {
				number2 = num;
				view.setText(number2+"");
			}
		}
	}

	class LogicEvent implements EventHandler<Event>{
 
		public void handle(Event event) {
			MyButton logicBtn = (MyButton)event.getSource();
			logic = logicBtn.getText();
			flag = true;
			eq = false;
		}
		
	}

	private void clean() {
		number1 = 0;
		number2 = 0;
		logic = null;
		flag = false;
		eq = false;
		view.setText("0");
	}
}
