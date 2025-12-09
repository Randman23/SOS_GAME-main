package Sprint5.GameLogic;

/**
 * Represents a single cell on the game board.
 */
public class Cell {
    private char content;
    private final int row;
    private final int column;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.content = ' ';
    }

    public boolean isEmpty() {
        return content == ' ';
    }

    public void setContent(char letter) {
        if (letter == 'S' || letter == 'O' || letter == ' ') {
            this.content = letter;
        }
    }

    public char getContent() {
        return content;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void clear() {
        content = ' ';
    }
}