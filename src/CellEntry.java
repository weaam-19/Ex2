// Add your documentation below:
public class CellEntry implements Index2D {
    private String index;

    public CellEntry(String index) {
        this.index = index;
    }

    @Override
    public boolean isValid() {
        if (index == null || index.isBlank()) return false;

        char column = Character.toUpperCase(index.charAt(0));
        String rowPart = index.substring(1);

        return Character.isLetter(column) && rowPart.matches("\\d+") && isRow(rowPart) && columnInRange(column);
    }

    private boolean isRow(String rowPart) {
        int row;
        try {
            row = Integer.parseInt(rowPart);
        } catch (NumberFormatException e) {
            return false;
        }
        return row >= 1 && row <= 99;
    }

    private boolean columnInRange(char column) {
        return column >= 'A' && column <= 'Z';
    }

    @Override
    public int getX() {
        validIndex();
        return Character.toUpperCase(index.charAt(0)) - 'A';
    }

    @Override
    public int getY() {
        validIndex();
        return Integer.parseInt(index.substring(1)) - 1;
    }

    private void validIndex() {
        if (!isValid()) {
            throw new IllegalStateException(" " + index);
        }
    }

    @Override
    public String toString() {
        return isValid() ? index.toUpperCase() : "";
    }

}
