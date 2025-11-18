package Sprint4.GameLogic;

/**
 * Manages the game board grid and cell operations.
 */
public class Board {
    private final int size;
    private final Cell[][] cells;

    public Board(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
        initializeCells();
    }

    private void initializeCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public Cell getCell(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return cells[row][col];
    }

    public boolean placeLetterAt(int row, int col, char letter) {
        Cell cell = getCell(row, col);
        if (cell == null || !cell.isEmpty()) {
            return false;
        }
        cell.setContent(letter);
        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].clear();
            }
        }
    }

    public int getSize() {
        return size;
    }

    public char[][] toCharArray() {
        char[][] grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = cells[i][j].getContent();
            }
        }
        return grid;
    }
}