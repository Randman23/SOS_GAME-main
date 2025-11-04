package Sprint3;

import java.util.ArrayList;
import java.util.List;

public class SOSGame {

    private final int size;
    private final char[][] board;
    private boolean playerOneTurn = true;

    // Scoring variables
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean gameOver = false;
    private String winner = null;

    // Store SOS sequences for visualization
    private List<SOSSequence> sosSequences = new ArrayList<>();

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

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public List<SOSSequence> getSosSequences() {
        return sosSequences;
    }

    public char getCurrentPlayerLetter(char p1Letter, char p2Letter) {
        return playerOneTurn ? p1Letter : p2Letter;
    }

    /**
     * Place a letter on the board. Returns true if placement was successful.
     */
    public boolean placeLetter(int row, int col, char letter) {
        if (gameOver) return false;
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        if (board[row][col] != '\0') return false;

        board[row][col] = letter;

        // Check for SOS sequences formed by this move
        int sosCount = checkForSOS(row, col);

        if (sosCount > 0) {
            // Player scored, update score
            if (playerOneTurn) {
                player1Score += sosCount;
            } else {
                player2Score += sosCount;
            }
            // Player who scored gets another turn (don't switch)
        } else {
            // No score, switch turns
            playerOneTurn = !playerOneTurn;
        }

        // Check if game is over
        checkGameOver();

        return true;
    }

    /**
     * Check for SOS sequences formed by placing a letter at (row, col)
     * Returns the number of SOS sequences formed
     */
    private int checkForSOS(int row, int col) {
        int count = 0;
        char letter = board[row][col];

        // Define 8 directions: N, NE, E, SE, S, SW, W, NW
        int[][] directions = {
                {-1, 0}, {-1, 1}, {0, 1}, {1, 1},
                {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        };

        // If we placed an 'S', check if it forms SOS as the first or last S
        if (letter == 'S') {
            for (int[] dir : directions) {
                // Check SOS pattern (current S at start)
                if (isValid(row + dir[0], col + dir[1]) &&
                        board[row + dir[0]][col + dir[1]] == 'O' &&
                        isValid(row + 2*dir[0], col + 2*dir[1]) &&
                        board[row + 2*dir[0]][col + 2*dir[1]] == 'S') {

                    sosSequences.add(new SOSSequence(
                            row, col,
                            row + 2*dir[0], col + 2*dir[1],
                            playerOneTurn ? 1 : 2
                    ));
                    count++;
                }
            }
        }

        // If we placed an 'O', check if it forms SOS as the middle O
        if (letter == 'O') {
            for (int[] dir : directions) {
                // Check SOS pattern (current O in middle)
                if (isValid(row - dir[0], col - dir[1]) &&
                        board[row - dir[0]][col - dir[1]] == 'S' &&
                        isValid(row + dir[0], col + dir[1]) &&
                        board[row + dir[0]][col + dir[1]] == 'S') {

                    sosSequences.add(new SOSSequence(
                            row - dir[0], col - dir[1],
                            row + dir[0], col + dir[1],
                            playerOneTurn ? 1 : 2
                    ));
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Check if coordinates are valid
     */
    private boolean isValid(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Check if the game is over (board is full)
     */
    private void checkGameOver() {
        // Check if board is full
        boolean isFull = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == '\0') {
                    isFull = false;
                    break;
                }
            }
            if (!isFull) break;
        }

        if (isFull) {
            gameOver = true;
            determineWinner();
        }
    }

    /**
     * Determine the winner based on scores
     */
    private void determineWinner() {
        if (player1Score > player2Score) {
            winner = "Player 1 Wins!";
        } else if (player2Score > player1Score) {
            winner = "Player 2 Wins!";
        } else {
            winner = "It's a Draw!";
        }
    }

    /**
     * End game in Simple Mode (first to score wins)
     */
    public void endGameSimpleMode() {
        gameOver = true;
        if (player1Score > 0 && player2Score == 0) {
            winner = "Player 1 Wins!";
        } else if (player2Score > 0 && player1Score == 0) {
            winner = "Player 2 Wins!";
        } else if (player1Score > player2Score) {
            winner = "Player 1 Wins!";
        } else if (player2Score > player1Score) {
            winner = "Player 2 Wins!";
        } else {
            winner = "It's a Draw!";
        }
    }

    public void resetGame() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = '\0';
        playerOneTurn = true;
        player1Score = 0;
        player2Score = 0;
        gameOver = false;
        winner = null;
        sosSequences.clear();
    }

    public void switchTurn() {
        playerOneTurn = !playerOneTurn;
    }

    /**
     * Inner class to represent an SOS sequence
     */
    public static class SOSSequence {
        public final int startRow, startCol, endRow, endCol;
        public final int player; // 1 or 2

        public SOSSequence(int startRow, int startCol, int endRow, int endCol, int player) {
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
            this.player = player;
        }
    }
}