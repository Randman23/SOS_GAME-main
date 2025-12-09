package Sprint5.GameLogic;

/**
 * Represents a move in the game (position and letter).
 */
public class Move {
    private final int row;
    private final int col;
    private final char letter;

    public Move(int row, int col, char letter) {
        this.row = row;
        this.col = col;
        this.letter = letter;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public String toString() {
        return "Move(" + row + "," + col + "," + letter + ")";
    }
}