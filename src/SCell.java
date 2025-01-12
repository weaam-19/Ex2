import java.util.LinkedList;
import java.util.Set;

public class SCell implements Cell {
    private String data;
    private int type;
    private int order;

    public static final int TEXT = 1;
    public static final int NUMBER = 2;
    public static final int FORM = 3;
    public static final int ERR_CYCLE_FORM = -1;
    public static final int ERR_WRONG_FORM = -2;

    // Constructor: Initializes the cell with data
    public SCell(String data) {
        this.data = data;
        this.type = determineType(data);
        this.order = 0;
    }

    // Returns the data stored in the cell
    @Override
    public String getData() {
        return data;
    }

    // Updates the cell data and re-evaluates its type
    @Override
    public void setData(String data) {
        this.data = data;
        this.type = determineType(data);
    }

    // Retrieves the type of the cell (TEXT, NUMBER, FORM, or error)
    @Override
    public int getType() {
        return type;
    }

    // Sets the type of the cell
    @Override
    public void setType(int type) {
        this.type = type;
    }

    // Retrieves the order of computation for the cell
    @Override
    public int getOrder() {
        return order;
    }

    // Updates the computation order of the cell
    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    // Determines the type of the cell based on its data
    private int determineType(String data) {
        if (data == null || data.isBlank()) return TEXT;
        if (isNumber(data)) return NUMBER;
        return data.startsWith("=") ? (isForm(data) ? FORM : ERR_WRONG_FORM) : TEXT;
    }

    // Checks if the provided value is a valid number
    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Validates if the given string is a correct formula
    private boolean isForm(String formula) {
        if (formula == null || !formula.startsWith("=")) return false;
        String expression = formula.substring(1).trim();
        return !expression.isEmpty() && !"+-*/".contains(String.valueOf(expression.charAt(expression.length() - 1)));
    }

    // Evaluates the value of the cell based on its type
    public String evaluate(Ex2Sheet sheet, int currentX, int currentY, Set<String> visited) {
        if (type == ERR_CYCLE_FORM) return "ERR_CYCLE!!!";
        return switch (type) {
            case NUMBER, TEXT -> data;
            case FORM -> evaluateFormula(sheet, currentX, currentY, visited);
            default -> "ERR_FORM!!!";
        };
    }

    // Evaluates a formula and handles cell references.
    private String evaluateFormula(Ex2Sheet sheet, int currentX, int currentY, Set<String> visited) {
        String currentCell = getCellName(currentX, currentY);

        // Detects cycles in cell references
        if (visited.contains(currentCell)) {
            this.type = ERR_CYCLE_FORM;
            return "ERR_CYCLE!!!";
        }

        visited.add(currentCell);

        try {
            double result = computeForm(data, sheet, currentX, currentY, visited);
            visited.remove(currentCell);
            return String.valueOf(result);
        } catch (Exception e) {
            visited.remove(currentCell);
            this.type = e.getMessage() != null && e.getMessage().contains("cycle") ? ERR_CYCLE_FORM : ERR_WRONG_FORM;
            return e.getMessage() != null && e.getMessage().contains("cycle") ? "ERR_CYCLE!!!" : "ERR_FORM!!!";
        }
    }

    // Converts cell coordinates to a name (e.g., A1, B2)
    private String getCellName(int x, int y) {
        return (char) ('A' + x) + String.valueOf(y);
    }

    // Computes the formula for a cell by replacing references with values
    private static double computeForm(String formula, Ex2Sheet sheet, int currentX, int currentY, Set<String> visited) {
        if (formula == null || !formula.startsWith("=")) throw new IllegalArgumentException("Invalid form");

        String expression = formula.substring(1).replaceAll("\\s", "");
        expression = replaceRefWithValues(expression, sheet, currentX, currentY, visited);

        return evaluateExpression(expression);
    }

    // Replaces cell references in a formula with their evaluated values
    private static String replaceRefWithValues(String formula, Ex2Sheet sheet, int currentX, int currentY, Set<String> visited) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < formula.length()) {
            char c = formula.charAt(i);

            if (Character.isLetter(c)) {
                String cellName = extractCell(formula, i);
                int[] coords = sheet.parseEntry(cellName);

                if (coords == null) throw new IllegalArgumentException("Invalid reference: " + cellName);

                if (coords[0] == currentX && coords[1] == currentY) throw new IllegalArgumentException("cycle detected");

                SCell referencedCell = sheet.get(coords[0], coords[1]);
                String cellValue = (referencedCell != null) ? referencedCell.evaluate(sheet, coords[0], coords[1], visited) : "0";

                if (cellValue.equals("ERR_CYCLE!!!")) throw new IllegalArgumentException("cycle detected");

                result.append(cellValue);
                i += cellName.length();
            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }

    // Extracts the cell name (e.g., A1, B2) from a formula
    private static String extractCell(String formula, int startIndex) {
        StringBuilder cellName = new StringBuilder().append(formula.charAt(startIndex));
        int i = startIndex + 1;

        while (i < formula.length() && Character.isDigit(formula.charAt(i))) {
            cellName.append(formula.charAt(i));
            i++;
        }

        return cellName.toString().toUpperCase();
    }

    // Evaluates a mathematical expression without replacing references
    private static double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s", "");
        return evaluateParentheses(expression);
    }

    // Evaluates expressions with parentheses
    private static double evaluateParentheses(String expression) {
        while (expression.contains("(")) {
            int openIndex = expression.lastIndexOf('(');
            int closeIndex = expression.indexOf(')', openIndex);

            if (closeIndex == -1) throw new IllegalArgumentException("Mismatched parentheses");

            double innerResult = evaluateWithoutParentheses(expression.substring(openIndex + 1, closeIndex));
            expression = expression.substring(0, openIndex) + innerResult + expression.substring(closeIndex + 1);
        }

        return evaluateWithoutParentheses(expression);
    }

    // Evaluates expressions without parentheses
    private static double evaluateWithoutParentheses(String expression) {
        LinkedList<Double> numbers = new LinkedList<>();
        LinkedList<Character> operators = new LinkedList<>();
        StringBuilder currentNumber = new StringBuilder();

        if (expression == null || expression.isBlank()) throw new IllegalArgumentException("Empty expression");

        for (int i = 0; i <= expression.length(); i++) {
            char c = (i < expression.length()) ? expression.charAt(i) : '\0';

            if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || "+-*/".contains("" + expression.charAt(i - 1))))) {
                currentNumber.append(c);
            } else if ("+-*/".indexOf(c) != -1 || c == '\0') {
                if (!currentNumber.isEmpty()) {
                    numbers.add(Double.parseDouble(currentNumber.toString()));
                    currentNumber.setLength(0);
                }

                while (!operators.isEmpty() && preced(operators.getLast()) >= preced(c)) {
                    double b = numbers.removeLast();
                    double a = numbers.removeLast();
                    char op = operators.removeLast();
                    numbers.add(applyOp(a, op, b));
                }

                if (c != '\0') operators.add(c);
            } else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
        }

        return numbers.getFirst();
    }

    // Determines operator precedence
    private static int preced(char operator) {
        return operator == '+' || operator == '-' ? 1 : operator == '*' || operator == '/' ? 2 : -1;
    }

    // Applies a mathematical operator to two numbers
    private static double applyOp(double a, char operator, double b) {
        if (operator == '/' && b == 0) throw new ArithmeticException("Division by zero");
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    // Converts the cell's data to a string
    @Override
    public String toString() {
        return data;
    }
}
