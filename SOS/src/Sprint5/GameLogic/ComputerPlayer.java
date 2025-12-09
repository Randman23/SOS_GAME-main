package Sprint5.GameLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract base class for computer player strategies.
 */
public abstract class ComputerPlayer {
    protected Random random;

    public ComputerPlayer() {
        this.random = new Random();
    }

    /**
     * Determines the best move for the computer player.
     * @param game The current game state
     * @param player The player making the move
     * @return The chosen move
     */
    public abstract Move makeMove(Game game, Player player);

    /**
     * Gets all available empty cells on the board.
     * Returns BOTH S and O options for each empty cell.
     */
    public List<Move> getAvailableMoves(Game game) {
        List<Move> moves = new ArrayList<>();
        Board board = game.getBoard();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getCell(row, col).isEmpty()) {
                    // Add both S and O as options for each empty cell
                    moves.add(new Move(row, col, 'S'));
                    moves.add(new Move(row, col, 'O'));
                }
            }
        }

        return moves;
    }

    /**
     * Simulates a move and checks if it creates an SOS.
     * @return Number of SOS sequences this move would create
     */
    public int evaluateMove(Game game, Move move, Player player) {
        // Temporarily place the letter
        Cell cell = game.getBoard().getCell(move.getRow(), move.getCol());
        char originalContent = cell.getContent();
        cell.setContent(move.getLetter());

        // Detect SOS sequences
        SOSDetector detector = new SOSDetector(game.getBoard());
        List<SOSSequence> sequences = detector.findSequencesForMove(
                move.getRow(), move.getCol(), player);

        int score = sequences.size();

        // Restore original state
        cell.setContent(originalContent);

        return score;
    }
}