import java.util.LinkedList;

public class SCell implements Cell {
        private String data; // הנתונים של התא
        private int type;    // סוג התא
        private int order;   // סדר החישוב של התא

        public static final int TEXT = 1;
        public static final int NUMBER = 2;
        public static final int FORM = 3;
        public static final int ERR_CYCLE_FORM = -1;
        public static final int ERR_WRONG_FORM = -2;

        public SCell(String data) {
            this.data = data;
            this.type = determineType(data);
            this.order = 0; // ברירת מחדל לסדר החישוב
        }

        @Override
        public String getData() {
            return data;
        }

        @Override
        public void setData(String data) {
            this.data = data;
            this.type = determineType(data);
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public void setType(int type) {
            this.type = type;
        }

        @Override
        public int getOrder() {
            return order;
        }

        @Override
        public void setOrder(int order) {
            this.order = order;
        }

        private int determineType(String data) {
            if (data == null || data.isEmpty()) {
                return TEXT; // ברירת מחדל לתא ריק
            }
            if (isNumber(data)) {
                return NUMBER; // מספר תקין
            }
            if (isForm(data)) {
                return FORM; // נוסחה תקינה
            }
            return TEXT; // כל דבר אחר נחשב כטקסט
        }
        private boolean isNumber(String s) {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private boolean isForm(String s) {
            return s != null && s.startsWith("=");
        }

        public String evaluate() {
            if (type == NUMBER) {
                return data; // מספר תקין
            }
            if (type == FORM) {
                try {
                    // בדיקה אם הנוסחה מכילה תווים לא חוקיים
                    if (data.matches(".*[a-zA-Z]+.*") && !data.matches(".*[A-Z]\\d+.*")) {
                        type = ERR_WRONG_FORM;
                        return "ERR"; // טקסט בתוך נוסחה לא חוקית
                    }
                    double result = computeForm(data);
                    return String.valueOf(result); // ערך מחושב
                } catch (Exception e) {
                    type = ERR_WRONG_FORM; // שגיאה בנוסחה
                    return "ERR";
                }
            }
            if (type == TEXT) {
                return data; // טקסט רגיל
            }
            return "ERR";
        }




        private static double computeForm(String input) {
            if (input == null || !input.startsWith("=")) {
                throw new IllegalArgumentException("Invalid formula");
            }
            String formula = input.substring(1);
            return evaluateExpression(formula);
        }

        private static double evaluateExpression(String expression) {
            expression = expression.replaceAll("\\s", ""); // הסרת רווחים
            return evaluateWithParentheses(expression);
        }

        private static double evaluateWithParentheses(String expression) {
            while (expression.contains("(")) {
                int openIndex = expression.lastIndexOf('(');
                int closeIndex = expression.indexOf(')', openIndex);
                if (closeIndex == -1) {
                    throw new IllegalArgumentException("Mismatched parentheses in formula");
                }
                double innerResult = evaluateWithoutParentheses(expression.substring(openIndex + 1, closeIndex));
                expression = expression.substring(0, openIndex) + innerResult + expression.substring(closeIndex + 1);
            }
            return evaluateWithoutParentheses(expression);
        }

        private static double evaluateWithoutParentheses(String expression) {
            LinkedList<Double> numbers = new LinkedList<>();
            LinkedList<Character> operators = new LinkedList<>();
            StringBuilder currentNumber = new StringBuilder();

            for (int i = 0; i <= expression.length(); i++) {
                char c = (i < expression.length()) ? expression.charAt(i) : '\0';

                if (Character.isDigit(c) || c == '.') {
                    currentNumber.append(c);
                } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '\0') {
                    if (currentNumber.length() > 0) {
                        numbers.add(Double.parseDouble(currentNumber.toString()));
                        currentNumber.setLength(0);
                    }
                    while (!operators.isEmpty() && precedence(operators.getLast()) >= precedence(c)) {
                        double b = numbers.removeLast();
                        double a = numbers.removeLast();
                        char op = operators.removeLast();
                        numbers.add(applyOperator(a, op, b));
                    }
                    if (c != '\0') operators.add(c);
                } else {
                    throw new IllegalArgumentException("Invalid character in formula: " + c);
                }
            }

            while (!operators.isEmpty()) {
                double b = numbers.removeLast();
                double a = numbers.removeLast();
                char op = operators.removeLast();
                numbers.add(applyOperator(a, op, b));
            }

            return numbers.getLast();
        }

        // פונקציית עזר לעדיפות אופרטור
        private static int precedence(char operator) {
            if (operator == '+' || operator == '-') return 1; // עדיפות נמוכה
            if (operator == '*' || operator == '/') return 2; // עדיפות גבוהה
            return -1; // תו לא חוקי
        }


        private static double applyOperator(double a, char operator, double b) {
            switch (operator) {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    if (b == 0) throw new ArithmeticException("Division by zero");
                    return a / b;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + operator);
            }
        }

        @Override
        public String toString() {
            return data; // מחזיר את הנתונים של התא במקום תצוגת ברירת המחדל של אובייקט
        }

    }

