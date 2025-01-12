// Represents a cell entry in a 2D index system (e.g., A1, B2)
public class CellEntry implements Index2D {
    private final String index;

    // Constructor: Initializes the CellEntry with the given index.
    public CellEntry(String index) {
        this.index = index;
    }

    /**
     * Checks if the current index is valid.
     * A valid index starts with a letter (A-Z) followed by a numeric part (1-99).
     * @return true if the index is valid, false otherwise.
     */
    @Override
    public boolean isValid() {
        if (index == null || index.isBlank()) return false;

        char column = Character.toUpperCase(index.charAt(0));
        String rowPart = index.substring(1);

        return Character.isLetter(column)
                && rowPart.matches("\\d+")
                && isRowInRange(rowPart)
                && isColumnInR(column);
    }

    /**
     * Validates if the row part of the index is within range (1-99).
     * @param rowPart The numeric part of the index.
     * @return true if the row is valid, false otherwise.
     */
    private boolean isRowInRange(String rowPart) {
        try {
            int row = Integer.parseInt(rowPart);
            return row >= 1 && row <= 99;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if the column part of the index is within range (A-Z).
     * @param column The column character to check.
     * @return true if the column is valid, false otherwise.
     */
    private boolean isColumnInR(char column) {
        return column >= 'A' && column <= 'Z';
    }

    /**
     * Retrieves the column index (X-coordinate) as a zero-based integer.
     * Converts the column letter to its corresponding index (e.g., A -> 0, B -> 1).
     * @return the column index as an integer.
     * @throws IllegalStateException if the index is not valid.
     */
    @Override
    public int getX() {
        ensureValidIndex();
        return Character.toUpperCase(index.charAt(0)) - 'A';
    }

    /**
     * Retrieves the row index (Y-coordinate) as a zero-based integer.
     * Converts the row part of the index to an integer (e.g., 1 -> 0, 2 -> 1).
     * @return the row index as an integer.
     * @throws IllegalStateException if the index is not valid.
     */
    @Override
    public int getY() {
        ensureValidIndex();
        return Integer.parseInt(index.substring(1)) - 1;
    }

    /**
     * Ensures that the current index is valid.
     * Throws an exception if the index is invalid.
     * @throws IllegalStateException if the index is not valid.
     */
    private void ensureValidIndex() {
        if (!isValid()) {
            throw new IllegalStateException("Invalid index: " + index);
        }
    }

    /**
     * Converts the index to its string representation.
     * Returns the index in uppercase if it is valid, or an empty string if invalid.
     * @return the string representation of the index.
     */
    @Override
    public String toString() {
        return isValid() ? index.toUpperCase() : "";
    }
}
