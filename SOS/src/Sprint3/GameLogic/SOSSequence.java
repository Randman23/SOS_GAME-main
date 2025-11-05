package Sprint3.GameLogic;

/**
 * Represents a completed SOS sequence on the board.
 */
public class SOSSequence {
    private final int startRow;
    private final int startCol;
    private final int endRow;
    private final int endCol;
    private final Player player;

    public SOSSequence(int startRow, int startCol, int endRow, int endCol, Player player) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.player = player;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public Player getPlayer() {
        return player;
    }
}