package Sprint2;

public class SOSGame {

    private final int size;
    private final char[][] board;
    private boolean playerOneTurn = true;

    public SOSGame(int size) {
        this.size = size;
        this.board = new char[size][size];
    }

    public int getSize() {
        return size;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public char getCurrentPlayerLetter(char p1Letter, char p2Letter) {
        return playerOneTurn ? p1Letter : p2Letter;
    }

    /**
     * Place a letter on the board. Returns true if placement was successful.
     */
    public boolean placeLetter(int row, int col, char letter) {
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        if (board[row][col] != '\0') return false;

        board[row][col] = letter;
        playerOneTurn = !playerOneTurn; // switch turn
        return true;
    }

    public void resetGame() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = '\0';
        playerOneTurn = true;
    }

    public void switchTurn() {
    }
}
