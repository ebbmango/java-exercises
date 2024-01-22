import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static List<String> loadExpressions(String filePath) {
        ArrayList<String> expressions = new ArrayList<>();
        try (var bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                expressions.add(line);
            }
        } catch (IOException e) {
            System.out.println("Could not read from file \"" + filePath + "\"");
        }
        return expressions;
    }


    private static List<Pair> catchParentheses(String[] array) {
        ArrayList<int[]> pairs = new ArrayList<>();

        Stack<Integer> stack = new Stack<>();
        // we loop through each element in the input array.
        for (int i = 0; i < array.length; i++) {
            // if we find an opening parentheses, we push it into the stack
            if (array[i].equals("(")) {
                stack.push(i);
            } else if (array[i].equals(")")) { // if we find a closing one...
                // if there are currently opening parentheses in the stack...
                if (!stack.isEmpty()) {
                    int openIndex = stack.pop();
                    pairs.add(new int[]{openIndex, i}); // we pair the two of them! :)
                    System.out.println("Pair found: (" + openIndex + ", " + i + ")");
                } else { // this means that there are unopened parentheses: the expression is faulty!
                    throw new IllegalArgumentException("syntax error: ')' expected");
                }
            }
        }
        if (!stack.isEmpty()) { // this means that there are unclosed parentheses: the expression is faulty!
            throw new IllegalArgumentException("syntax error: '(' expected");
        }

        // Transforms the ArrayList<int[]> into a List<Pair>
        return pairs.stream().map(pair -> new Pair(pair[0], pair[1])).collect(Collectors.toList());
    }

    private static List<Pair> sortPairs(List<Pair> pairs) {
        int pairsAmount = pairs.size();
        // we compare the positions of each pair of parentheses
        for (int i = 0; i < pairsAmount; i++) {
            var firstPair = pairs.get(i);
            // with each other pair of parentheses
            for (var SecondPair : pairs) {
                // if one of them is inside the other
                if (SecondPair.start > firstPair.start && firstPair.end > SecondPair.end) {
                    // we increment the number of nestings of the encapsulating pair
                    firstPair.nestings++;
                }
            }
        }
        // we then sort the pairs in ascending order according to their numbers of nestings
        pairs.sort(Comparator.comparingInt(pair -> pair.nestings));
        return pairs;
    }


    private static String[] serialize(String expression) {
        return expression.split("(?=[+\\-*/()=])|(?<=[+\\-*/()=])");
    }

    private static double operate(String operator, double operand1, double operand2) {
        return switch (operator) {
            case "/" -> {
                if (operand2 == 0) throw new ArithmeticException("Zero Division Error");
                yield operand1 / operand2;
            }
            case "*" -> operand1 * operand2;
            case "-" -> operand1 - operand2;
            case "+" -> operand1 + operand2;
            default -> throw new IllegalArgumentException("Non-existent operator: " + operator);
        };
    }

    private static String[] performOperation(String[] serializedString, String operator) {
        int index = Utilities.indexOf(serializedString, operator);
        if (index == -1) {
            // if there is none of the operator we are looking for, we don't bother
            return serializedString;
        } else { // if there is...
            // we take the value that comes before it...
            String operand1 = serializedString[index - 1];
            // and the one that comes after it...
            String operand2 = serializedString[index + 1];
            // and we operate them!
            double result = operate(operator, Double.parseDouble(operand1), Double.parseDouble(operand2));

            // we create a new array with size - 2 (three elements deleted, one inserted)
            String[] newArray = new String[serializedString.length - 2];
            // we copy into it all the elements before the index of the first operator
            System.arraycopy(serializedString, 0, newArray, 0, index - 1);
            newArray[index - 1] = "" + result;
            // and also all the elements after the index of the last operator
            System.arraycopy(serializedString, index + 2, newArray, index, newArray.length - index);
            // finally, we return the copied array
            if (Utilities.indexOf(newArray, operator) != -1) {
                // if there is still more work to do, repeat:
                return performOperation(newArray, operator);
            } else {
                // return new array
                return newArray;
            }
        }
    }

    public static void main(String[] args) {
        String expression = "(5+3*6/5+5*4/5+5=";

        // first perform all divisions
        // them perform all multiplications
        // then perform all subtractions
        // then perform all additions

        var serializedString = serialize(expression);
        var parentheses = sortPairs(catchParentheses(serializedString));

        System.out.println(Arrays.toString(serializedString));
        System.out.println(Arrays.toString(performOperation(serializedString, "+")));

//
//        int index;
//        do {
//            index = Utilities.indexOf(serialized, "/");
//            double result =
//        } while (index != -1);
//
//
//        System.out.println(Arrays.toString(newArray));


//        for (var pair : parentheses) {

//        }

    }
}