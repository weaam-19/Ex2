import java.util.LinkedList;

public class SCell implements Cell {
        private String data;
        private int type;
        private int order;
        public static final int TEXT = 1;
        public static final int NUMBER = 2;
        public static final int FORM = 3;
        public static final int ERR_CYCLE_FORM = -1;
        public static final int ERR_WRONG_FORM = -2;

        public SCell(String data) {
            this.data = data;
            this.type = determineType(data);
            this.order = 0;
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
                return TEXT;
            }
            if (isNumber(data)) {
                return NUMBER;
            }
            if (isForm(data)) {
                return FORM;
            }
            return TEXT;
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



    public String evaluate(Ex2Sheet sheet, int currentX, int currentY) {
        if (type == NUMBER) {
            return data;
        }
        if (type == FORM) {
            try {
                double result = computeForm(data, sheet, currentX, currentY);
                return String.valueOf(result);
            } catch (Exception e) {
                type = ERR_WRONG_FORM;
                return "ERR_Form";
            }
        }
        if (type == TEXT) {
            return data;
        }
        return "ERR_Form";
    }


    private static double computeForm(String input, Ex2Sheet sheet, int currentX, int currentY) {
        if (input == null || !input.startsWith("=")) {
            throw new IllegalArgumentException("");
        }
        String formula = input.substring(1).replaceAll("\\s", "");
        formula = replaceReferencesWithValues(formula, sheet, currentX, currentY);
        return evaluateExpression(formula);
    }


    private static String replaceReferencesWithValues(String formula, Ex2Sheet sheet, int currentX, int currentY) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < formula.length()) {
            char c = formula.charAt(i);

            if (Character.isLetter(c)) {
                StringBuilder cellName = new StringBuilder();
                cellName.append(c);
                i++;
                while (i < formula.length() && Character.isDigit(formula.charAt(i))) {
                    cellName.append(formula.charAt(i));
                    i++;
                }

                String cell = cellName.toString().toUpperCase();
                int[] coords = sheet.parseEntry(cell);
                if (coords == null || (coords[0] == currentX && coords[1] == currentY)) {
                    throw new IllegalArgumentException("Invalid or circular reference: " + cell);
                }

                SCell referencedCell = sheet.get(coords[0], coords[1]);
                String cellValue = (referencedCell != null) ? referencedCell.evaluate(sheet, coords[0], coords[1]) : "0";

                result.append(cellValue);
            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }



    private static double evaluateExpression(String expression) {
            expression = expression.replaceAll("\\s", "");
            return evaluateWithParentheses(expression);
        }


        private static double evaluateWithParentheses(String expression) {
            while (expression.contains("(")) {
                int openIndex = expression.lastIndexOf('(');
                int closeIndex = expression.indexOf(')', openIndex);
                if (closeIndex == -1) {
                    throw new IllegalArgumentException("");
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
                    throw new IllegalArgumentException("" + c);
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


        private static int precedence(char operator) {
            if (operator == '+' || operator == '-') return 1;
            if (operator == '*' || operator == '/') return 2;
            return -1;
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
                    if (b == 0) throw new ArithmeticException("");
                    return a / b;
                default:
                    throw new IllegalArgumentException("" + operator);
            }
        }


        @Override
        public String toString() {
            return data;
        }

    }

