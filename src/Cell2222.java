import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cell2222 {
//function to check if the input is Number
    public static boolean isNumber (String s) {
        boolean ans = true;
        try {
            double d = Double.parseDouble(s);
        }
        catch (Exception e) {
            ans = false;
        }
        return ans;
    }
    //function to check if the input is Text
    public static boolean isText(String s){
        boolean ans=false;
        try {
            double d=Double.parseDouble(s);

        }
        catch (Exception e){
            ans=true;
        }
        return ans;
    }

    public static boolean isForm(String text) {
        if (text == null || text.length() < 2 || text.charAt(0) != '=') {
            return false;
        }

        String expression = text.substring(1).trim();

        if (expression.isEmpty()) {
            return false;
        }


        int openParentheses = 0;
        boolean lastCharWasOperator = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                lastCharWasOperator = false;
                continue;
            }

            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (lastCharWasOperator) {
                    return false;
                }
                lastCharWasOperator = true;
                continue;
            }

            if (c == '(') {
                openParentheses++;
                lastCharWasOperator = true;
                continue;
            }

            if (c == ')') {
                openParentheses--;
                if (openParentheses < 0 || lastCharWasOperator) {
                    return false;
                }
                continue;
            }

            return false;
        }

        return openParentheses == 0 && !lastCharWasOperator;
    }

    public static double computeForm(String input) {
        if (input == null || input.length() < 2 || input.charAt(0) != '=') {
            throw new IllegalArgumentException("Invalid formula. Formula must start with '='.");
        }

        return evaluate(input.substring(1).replaceAll("\\s", ""));
    }

    private static double evaluate(String expression) {
        if (expression.contains("(")) {
            int openIndex = expression.lastIndexOf('(');
            int closeIndex = expression.indexOf(')', openIndex);
            if (closeIndex == -1) {
                throw new IllegalArgumentException("");
            }
            double innerResult = evaluate(expression.substring(openIndex + 1, closeIndex));
            return evaluate(expression.substring(0, openIndex) + innerResult + expression.substring(closeIndex + 1));
        }

        return calculate(expression);
    }

    private static double calculate(String expression) {
        double result = 0;
        char operator = '+';
        StringBuilder currentNumber = new StringBuilder();

        for (int i = 0; i <= expression.length(); i++) {
            char c = (i < expression.length()) ? expression.charAt(i) : '\0';

            if (Character.isDigit(c) || c == '.') {
                currentNumber.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '\0') {
                if (currentNumber.length() > 0) {
                    double number = Double.parseDouble(currentNumber.toString());
                    result = applyOperator(result, operator, number);
                    currentNumber.setLength(0);
                }
                operator = c;
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        return result;
    }

    private static double applyOperator(double a, char operator, double b) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            default: throw new IllegalArgumentException("Unknown operator: " + operator);

        }
    }

}




