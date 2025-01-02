// Add your documentation below:
public class CellEntry implements Index2D {
    private String index;

    public CellEntry(String index) {
        this.index = index;
    }


    @Override
    public boolean isValid() {
        if (index == null || index.isEmpty()) {
            return false;
        }
        char column = index.toUpperCase().charAt(0);
        String rowPart = index.substring(1);

        if (!Character.isLetter(column) || !rowPart.matches("\\d+")) {
            return false;
        }

        int row;
        try {
            row = Integer.parseInt(rowPart);
        } catch (NumberFormatException e) {
            return false;
        }

        return column >= 'A' && column <= 'Z' && row >= 1 && row <= 99;
    }



    @Override
    public int getX() {
        if (!isValid()) {
            throw new IllegalStateException("" + index);
        }
        return index.toUpperCase().charAt(0) - 'A';
    }



    @Override
    public int getY() {
        if (!isValid()) {
            throw new IllegalStateException("" + index);
        }
        return Integer.parseInt(index.substring(1)) - 1;
    }



    @Override
    public String toString() {
        if (!isValid()) {
            return "";
        }
        return index.toUpperCase();
    }
}
