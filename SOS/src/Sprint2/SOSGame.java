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
    };