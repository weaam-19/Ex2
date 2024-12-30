// Add your documentation below:

public class SCell implements Cell {
    private String line; // הנתונים של התא
    private int type;    // סוג התא
    private int order;   // סדר החישוב של התא (עומק תלות)

    // קבועים לסוגי תא (מתוך Ex2Utils או הגדרה מקומי

    // Constructor
    public SCell(String s) {
        setData(s); // קובע את הנתונים ומגדיר את סוג התא
        this.order = 0  ; // ברירת מחדל לסדר החישוב
    }

    // קובע את סוג התא בהתבסס על הנתונים
    private int determineType(String data) {
        if (isNumber(data)) return 2;
        if (isText(data)) return 1;
        if (isForm(data)) return 3;
        return -2;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int t) {
        this.order = t;
    }

    @Override
    public void setData(String s) {
        this.line = s;
        this.type = determineType(s); // קובע את סוג התא בכל פעם שהנתונים משתנים
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        this.type = t;
    }

    @Override
    public String toString() {
        return getData();
    }

    // מתודות עזר לזיהוי סוגי נתונים
    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isText(String s) {
        try {
            Double.parseDouble(s);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isForm(String text) {
        if (text == null || text.length() < 2 || text.charAt(0) != '=') {
            return false;
        }

        String expression = text.substring(1).trim();
        if (expression.isEmpty()) return false;

        int openParentheses = 0;
        boolean lastCharWasOperator = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                lastCharWasOperator = false;
                continue;
            }

            if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (lastCharWasOperator) return false;
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
                if (openParentheses < 0 || lastCharWasOperator) return false;
                continue;
            }

            return false; // תו לא חוקי
        }

        return openParentheses == 0 && !lastCharWasOperator;
    }
}
