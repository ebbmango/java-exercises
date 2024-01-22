import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class CalculateFile {
    public static void main(String[] args) {
        //reads the file "text_file.txt" and prints out the solved ones in the terminal:

        try (BufferedReader br = new BufferedReader(new FileReader("text_file.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    double result = evalExpr(line);
                    System.out.println(line + result);
                } catch (Exception e) {
                    System.err.println("'" + line + "': " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't read file: " + e.getMessage());
        }
    }


    private static double evalExpr(String expr) {
        char[] t = expr.toCharArray();  //makes an array of characters from a String
        Stack<Double> v = new Stack<>();  //value
        Stack<Character> o = new Stack<>();  //operator
        int openParenCount = 0; //looks for unopened/unclosed parenthesis

        for (int i = 0; i < t.length; i++) {
            if (Character.isDigit(t[i]) || t[i] == '.') {  //checks for a decimal point
                StringBuilder s = new StringBuilder();
                while (i < t.length && (Character.isDigit(t[i]) || t[i] == '.')) s.append(t[i++]);

                double num = Double.parseDouble(s.toString());

                if (num > Integer.MAX_VALUE || num < Integer.MIN_VALUE) {//checks if the number exceeds the limits of a 32-bit integer
                    throw new IllegalArgumentException("Number exceeds 32-bit integer limit");
                }
                v.push(num);
                i--;
            } else if (t[i] == '(') {
                o.push(t[i]);// if the character is an open parenthesis, push it onto the operators stack
                openParenCount++;
            } else if (t[i] == ')') {
                if (openParenCount == 0) {

                    throw new IllegalArgumentException("Missing parenthesis"); // Check for mismatched parentheses
                }
                //pops operators and applies them until an opening parenthesis is encountered
                while (!o.isEmpty() && o.peek() != '(') v.push(applyOp(o.pop(), v.pop(), v.pop()));
                o.pop(); //removes the opening parenthesis
                openParenCount--;
            } else if (isOp(t[i])) {
                while (!o.isEmpty() && num(t[i]) <= num(o.peek())) v.push(applyOp(o.pop(), v.pop(), v.pop()));
                o.push(t[i]);//pushes the operator onto the operators stack if the character is an operator
            } else {
                // If the character is illegal:
                if (t[i] != '=') {
                    // Throw an exception for unknown characters (excluding '=')
                    throw new IllegalArgumentException("Unknown character: " + t[i]);
                }
            }
        }

        if (openParenCount > 0) {
            throw new IllegalArgumentException("Missing parenthesis");
        }

        while (!o.isEmpty()){
            v.push(applyOp(o.pop(), v.pop(), v.pop()));
        }

        if (t.length == 0 || t[t.length - 1] != '=') {
            throw new IllegalArgumentException("Expression must end with '='");
        }

        return v.pop();
    }

    private static boolean isOp(char c) { //to check if the character is an operater
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int num(char op) { //number of the operation
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Can't divide by zero.");
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid entry: " + op);
        }
    }
}
