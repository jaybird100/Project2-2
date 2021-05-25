package Calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Calculator_command {
	
    // Operators mapping function
	
    public static Map<String, int[]> Operators;
    
    public static void map_operators() {
    	Operators = new HashMap<String, int[]>();
    	Operators.put("+", new int[] { 1, 0 });
    	Operators.put("-", new int[] { 1, 0 });
    	Operators.put("*", new int[] { 2, 0 });
    	Operators.put("/", new int[] { 2, 0 });  
    }
  
    // Check if character is an operator
    public static boolean checkOperator(String ch) {
        return Operators.containsKey(ch);
    }
  
    // Check associativity of operator char
    public static boolean check_associativity(String ch, int type) throws Exception {
        if (!Operators.containsKey(ch)) {
            throw new Exception(ch + "is an undefined operator");
        } 
        if (Operators.get(ch)[1] == type) {
            return true;
        }
        return false;
    }
  
    // Check order of operators   
    public static final int operator_order_check(String ch1, String ch2) {
        if (!checkOperator(ch1) || !checkOperator(ch2)) {
            throw new IllegalArgumentException("Invalid char: " + ch1
                    + " " + ch2);
        }
        return Operators.get(ch1)[0] - Operators.get(ch2)[0];
    }
  
    // push chars into the stack 
    public static String[] char_stacking(String[] inputChars) throws Exception {
        Stack<String> main_stack = new Stack<String>();
        ArrayList<String> output_stack = new ArrayList<String>();

        for (String ch : inputChars) {
            if (checkOperator(ch)) {  
                while (!main_stack.empty() && checkOperator(main_stack.peek())) {                    
                    if ((check_associativity(ch, 0) && operator_order_check(ch, main_stack.peek()) <= 0) || (check_associativity(ch, 1) && operator_order_check(ch, main_stack.peek()) < 0)) {
                    	output_stack.add(main_stack.pop());   
                        continue;
                    }
                    break;
                }
                main_stack.push(ch);
            } 
            else if (ch.equals("(")) {
            	main_stack.push(ch);  // 
            } 
            else if (ch.equals(")")) {                
                while (!main_stack.empty() && !main_stack.peek().equals("(")) {
                	output_stack.add(main_stack.pop()); 
                }
                main_stack.pop(); 
            } 
            else{
            	output_stack.add(ch); 
            }
        }
        while (!main_stack.empty()){
        	output_stack.add(main_stack.pop()); 
        }
        String[] output = new String[output_stack.size()];
        return output_stack.toArray(output);
    }
     
    public static double calculation(String[] chs){        
        Stack<String> stack = new Stack<String>();
        for (String ch : chs) {
            if (!checkOperator(ch)) {
                stack.push(ch);                
            }
            else{
               
                double number2 = Double.valueOf(stack.pop());
                double number1 = Double.valueOf(stack.pop());
                double result = 0;
                if(ch.compareTo("+") == 0) {
                	result = number1+number2;
                }
                else if(ch.compareTo("-") == 0) {
                	result = number1-number2;
                }
                else if(ch.compareTo("*") == 0) {
                	result = number1*number2;
                }
                else if(ch.compareTo("/") == 0) {
                	result = number1/number2;
                }
                stack.push(String.valueOf(result));                                                
            }                        
        }           
        return Double.valueOf(stack.pop());
    }
  
    public static double get_result(String str) throws Exception {
    	map_operators();
        String[] formula = str.split(" ");       
        String[] string = char_stacking(formula);
        double result = calculation(string); 
        return result;
    }
    
//    public static void main(String args[]) throws Exception {
//    	System.out.println(get_result("( 2 + 4 ) * ( 3 - 7 )"));
//    }
}