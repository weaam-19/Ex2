import java.io.*;
import java.util.HashSet;

/**
 * Represents a 2D sheet of cells with rows and columns.
 * Provides functionalities to manipulate and evaluate the sheet.
 */
public class Ex2Sheet implements Sheet {
    private final SCell[][] table;
    private final int width;
    private final int height;

    /**
     * Constructs a new sheet with the specified dimensions.
     * Initializes all cells as empty.
     * @param width  the number of columns in the sheet
     * @param height the number of rows in the sheet
     */
    public Ex2Sheet(int width, int height) {
        this.width = width;
        this.height = height;
        table = new SCell[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                table[col][row] = new SCell("");
            }
        }
    }

    /**
     * Checks if the given coordinates (x, y) are within the sheet's boundaries.
     * @param x the column index
     * @param y the row index
     * @return true if the coordinates are within bounds, false otherwise
     */
    @Override
    public boolean isIn(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Returns the width (number of columns) of the sheet.
     * @return the width of the sheet
     */
    @Override
    public int width() {
        return width;
    }

    /**
     * Returns the height (number of rows) of the sheet.
     * @return the height of the sheet
     */
    @Override
    public int height() {
        return height;
    }

    /**
     * Updates the content of a cell at the specified coordinates.
     * @param x the column index
     * @param y the row index
     * @param c the new content for the cell
     */
    @Override
    public void set(int x, int y, String c) {
        if (isIn(x, y)) {
            table[x][y].setData(c);
        }
    }

    /**
     * Retrieves the cell at the specified coordinates.
     * @param x the column index
     * @param y the row index
     * @return the cell at the given coordinates, or null if out of bounds
     */
    @Override
    public SCell get(int x, int y) {
        return isIn(x, y) ? table[x][y] : null;
    }

    /**
     * Retrieves the cell based on a string entry (e.g., "A1").
     * @param entry the cell's string representation
     * @return the cell at the specified entry, or null if invalid
     */
    @Override
    public SCell get(String entry) {
        int[] coords = parseEntry(entry);
        return (coords != null && isIn(coords[0], coords[1])) ? table[coords[0]][coords[1]] : null;
    }

    /**
     * Computes the value of a cell at the given coordinates.
     * @param x the column index
     * @param y the row index
     * @return the computed value of the cell, or an error if invalid
     */
    @Override
    public String value(int x, int y) {
        return isIn(x, y) ? table[x][y].evaluate(this, x, y, new HashSet<>()) : "ERR_Cycle!!!";
    }

    /**
     * Alias for the `value` method. Evaluates the content of a cell.
     * @param x the column index
     * @param y the row index
     * @return the evaluated content of the cell
     */
    @Override
    public String eval(int x, int y) {
        return value(x, y);
    }

    /**
     * Evaluates all cells in the sheet.
     */
    @Override
    public void eval() {
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                value(col, row);
            }
        }
    }

    /**
     * Computes the depth of dependencies for each cell in the sheet.
     * @return a 2D array of dependency depths
     */
    @Override
    public int[][] depth() {
        int[][] depths = new int[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                depths[col][row] = computeDepth(col, row, new boolean[width][height]);
            }
        }
        return depths;
    }

    /**
     * Recursively computes the dependency depth for a specific cell.
     * @param x       the column index
     * @param y       the row index
     * @param visited tracks visited cells to prevent infinite loops
     * @return the depth of the cell's dependencies, or -1 if a cycle is detected
     */
    private int computeDepth(int x, int y, boolean[][] visited) {
        if (!isIn(x, y) || visited[x][y]) return -1;
        visited[x][y] = true;

        SCell cell = table[x][y];
        if (cell.getType() != SCell.FORM) return 0;

        String formula = cell.getData().substring(1);
        int maxDepth = 0;

        for (String ref : parse(formula)) {
            int[] coords = parseEntry(ref);
            if (coords != null) {
                maxDepth = Math.max(maxDepth, 1 + computeDepth(coords[0], coords[1], visited));
            }
        }

        visited[x][y] = false;
        return maxDepth;
    }

    /**
     * Splits a formula into individual cell references.
     *
     * @param formula the formula to parse
     * @return an array of cell references
     */
    private String[] parse(String formula) {
        return formula.split("[^A-Za-z0-9]");
    }

    /**
     * Parses a string entry (e.g., "A1") into column and row coordinates.
     *
     * @param entry the string representation of a cell
     * @return an array with column and row indices, or null if invalid
     */
    public int[] parseEntry(String entry) {
        if (entry == null || entry.length() < 2) return null;

        char column = Character.toUpperCase(entry.charAt(0));
        String rowPart = entry.substring(1);

        try {
            int row = Integer.parseInt(rowPart);
            int col = column - 'A';
            if (col < 0 || col >= width || row < 0 || row >= height) return null;
            return new int[]{col, row};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Saves the sheet's content to a file.
     * @param fileName the name of the file to save to
     * @throws IOException if an I/O error occurs,
     */
    @Override
    public void save(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("\n");
            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    String data = table[col][row].getData();
                    if (!data.isEmpty()) {
                        writer.write(col + "," + row + "," + data + "\n");
                    }
                }
            }
        }
    }

    /**
     * Loads the sheet's content from a file.
     * @param fileName the name of the file to load from
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Skip the first empty line
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    try {
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        set(x, y, parts[2]);
                    } catch (NumberFormatException e) {
                        // Ignore invalid lines
                    }
                }
            }
        }
    }
}
