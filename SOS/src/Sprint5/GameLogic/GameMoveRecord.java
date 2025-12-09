package Sprint5.GameLogic;

/**
 * Represents a single recorded move in the game.
 */
public class GameMoveRecord {
    private final int row;
    private final int col;
    private final char letter;
    private final String playerName;
    private final int moveNumber;

    public GameMoveRecord(int moveNumber, int row, int col, char letter, String playerName) {
        this.moveNumber = moveNumber;
        this.row = row;
        this.col = col;
        this.letter = letter;
        this.playerName = playerName;
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

    public String getPlayerName() {
        return playerName;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    @Override
    public String toString() {
        return moveNumber + "," + row + "," + col + "," + letter + "," + playerName;
    }

    /**
     * Parse a move from a CSV line.
     */
    public static GameMoveRecord fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid move record format: " + line);
        }

        int moveNum = Integer.parseInt(parts[0].trim());
        int row = Integer.parseInt(parts[1].trim());
        int col = Integer.parseInt(parts[2].trim());
        char letter = parts[3].trim().charAt(0);
        String playerName = parts[4].trim();

        return new GameMoveRecord(moveNum, row, col, letter, playerName);
    }
}