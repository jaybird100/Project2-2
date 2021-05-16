package CFG;

public class CYK {
	public static int grammar_len;
    public static String[][] grammar;
    public static String[][] CYK_matrix;
    public static String[] input;
    
    public static void setGrammar(String[][] rules) {
    	grammar = rules;
    	grammar_len = grammar.length;
    	
    }
    
    public static void setInput(String str) {
    	input = str.split(" ");
    }
    
    public static void createMatrix(int input_length) {
    	CYK_matrix = new String[input_length][input_length];
    }
    
    public static String getTerminals(String str){
        String terminals = "";
        for(int i = 0; i < grammar_len; i++) {
            for(int count = 0; count < grammar[i].length; count++){
                if(grammar[i][count].equals(str)){
                	terminals += grammar[i][0];
                }
            }
        }
        return terminals;
    }

    public static String getCombination(String str1, String str2){
        String combination = "";
        String temp_comb = "";
            for(int i = 0; i < str1.length(); i++){
                for(int j = 0; j < str2.length(); j++){
                	temp_comb = "";
                	temp_comb += str1.charAt(i) + "" +  str2.charAt(j);
                    combination += getTerminals(temp_comb);
                }
            }
        return combination;
    }
    public static boolean checkString(String[] str_lst) {
        String[] str = str_lst;
        String str1 = "";
        String str2 = "";
        for(int i = 0; i < str.length; i++){
        	str2 = "";
            str1 = str[i];
            for(int j = 0; j < grammar_len; j++){
                for(int index = 1; index < grammar[j].length; index++){
                    if(grammar[j][index].equals(str1)){
                    	str2 += grammar[j][0];
                    }
                }      
            }
            CYK_matrix[i][i] = str2;
        }
        for(int i = 1; i < str.length; i++){
            for(int j = i; j < str.length; j++){
            	str2 = "";
                for(int k = j - i; k < j; k++){
                	String t1 = CYK_matrix[j - i][k];
                	String t2 = CYK_matrix[k + 1][j];
                	str2 += getCombination(t1, t2);
                }
                CYK_matrix[j - i][j] = str2;
            }
        }
        return CYK_matrix[0][str.length - 1].contains("S");       
    }
       
    public static void printMatrix(){
        try{
            int rows = CYK_matrix.length;
            int columns = CYK_matrix.length;
            String str = "|\t";

            for(int i=0;i<rows;i++){
                for(int j=0;j<columns;j++){
                    str += CYK_matrix[i][j] + "\t";
                }

                System.out.println(str + "|");
                str = "|\t";
            }

        }catch(Exception e){System.out.println("Matrix is empty!!");}
    }
    
    public static void main(String[] args) {
    	String[][] rules = {
    						{"S", "AB", "BC", "CC"}, 
    						{"A", "BA", "CC", "1"}, 
    						{"B", "CC", "CA", "2"}, 
    						{"C", "AB", "1"}
    						};
    	String str = "2 1 1 2 1";
    	setGrammar(rules);
    	setInput(str);
    	createMatrix(input.length);
    	boolean if_valid = checkString(input);
    	if(if_valid)
    		System.out.println("String is ACCEPTED by this language");
    	else
    		System.out.println("String is REJECTED by this language");
    	System.out.println("");
    	printMatrix();
    }  
}
