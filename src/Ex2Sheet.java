import java.io.IOException;
// Add your documentation below:

import java.io.*;

public class Ex2Sheet implements Sheet {
    private SCell[][] table;
    private int width;
    private int height;

    public Ex2Sheet(int width, int height) {
        this.width = width;
        this.height = height;
        this.table = new SCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                table[i][j] = new SCell("");
            }
        }
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public boolean isIn(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public void set(int x, int y, String c) {
        if (isIn(x, y)) {
            table[x][y].setData(c);
        }
    }

    @Override
    public SCell get(int x, int y) {
        if (isIn(x, y)) {
            return table[x][y];
        }
        return null;
    }

    @Override
    public SCell get(String entry) {
        int[] coords = parseEntry(entry);
        if (coords != null && isIn(coords[0], coords[1])) {
            return table[coords[0]][coords[1]];
        }
        return null;
    }

    @Override
    public String value(int x, int y) {
        if (isIn(x, y)) {
            return table[x][y].evaluate();
        }
        return "ERR";
    }

    @Override
    public String eval(int x, int y) {
        return value(x, y);
    }

    @Override
    public void eval() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                value(i, j);
            }
        }
    }

    @Override
    public int[][] depth() {
        int[][] depths = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                depths[i][j] = computeDepth(i, j, new boolean[width][height]);
            }
        }
        return depths;
    }

    private int computeDepth(int x, int y, boolean[][] visited) {
        if (!isIn(x, y)) {
            return -1;
        }
        if (visited[x][y]) {
            return -1; // מעגל תלות
        }
        visited[x][y] = true;
        SCell cell = table[x][y];
        if (cell.getType() != SCell.FORM) {
            return 0;
        }

        String formula = cell.getData().substring(1);
        int maxDepth = 0;

        for (String ref : parseReferences(formula)) {
            int[] coords = parseEntry(ref);
            if (coords != null) {
                maxDepth = Math.max(maxDepth, 1 + computeDepth(coords[0], coords[1], visited));
            }
        }
        visited[x][y] = false;
        return maxDepth;
    }

    private String[] parseReferences(String formula) {
        return formula.split("[^A-Za-z0-9]");
    }

    private int[] parseEntry(String entry) {
        if (entry == null || entry.length() < 2) {
            return null;
        }
        char column = entry.toUpperCase().charAt(0);
        int row;
        try {
            row = Integer.parseInt(entry.substring(1)) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
        return new int[]{column - 'A', row};
    }

    @Override
    public void save(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("I2CS ArielU: SpreadSheet (Ex2) assignment\n");
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    String data = table[i][j].getData();
                    if (!data.isEmpty()) {
                        writer.write(i + "," + j + "," + data + "\n");
                    }
                }
            }
        }
    }

    @Override
    public void load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    try {
                        int x = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        String data = parts[2];
                        set(x, y, data);
                    } catch (NumberFormatException e) {
                        // Ignore invalid lines
                    }
                }
            }
        }
    }
}
