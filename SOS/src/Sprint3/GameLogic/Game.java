package Sprint3.GameLogic;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for SOS game logic.
 */
public abstract class Game {
    protected Board board;
    protected Player bluePlayer;
    protected Player redPlayer;
    protected Player currentPlayer;
    protected SOSDetector detector;
    protected List<SOSSequence> allSequences;
    protected boolean gameFinished;

    public Game(int boardSize) {
        this.board = new Board(boardSize);
        this.bluePlayer = new Player("Blue", Color.BLUE);
        this.redPlayer = new Player("Red", Color.RED);
        this.currentPlayer = bluePlayer;
        this.detector = new SOSDetector(board);
        this.allSequences = new ArrayList<>();
        this.gameFinished = false;
    }

    public boolean makeMove(int row, int col) {
        if (gameFinished) {
            return false;
        }

        char letter = currentPlayer.getSelectedLetter();
        if (!board.placeLetterAt(row, col, letter)) {
            return false;
        }

        // Find new SOS sequences created by this move
        List<SOSSequence> newSequences = detector.findSequencesForMove(row, col, currentPlayer);

        if (!newSequences.isEmpty()) {
            // Add all new sequences to the list
            allSequences.addAll(newSequences);

            // Award points for each sequence found
            currentPlayer.addScore(newSequences.size());

            // Let the game mode decide what happens after scoring
            handleScoringMove();
        } else {
            // No score, switch to other player
            switchPlayer();
        }

        checkGameEnd();
        return true;
    }

    protected void switchPlayer() {
        currentPlayer = (currentPlayer == bluePlayer) ? redPlayer : bluePlayer;
    }

    public void checkGameEnd() {
        if (shouldEndGame()) {
            gameFinished = true;
        }
    }

    public void reset() {
        board.clear();
        bluePlayer.resetScore();
        redPlayer.resetScore();
        currentPlayer = bluePlayer;
        allSequences.clear();
        gameFinished = false;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getBluePlayer() {
        return bluePlayer;
    }

    public Player getRedPlayer() {
        return redPlayer;
    }

    public List<SOSSequence> getAllSequences() {
        return allSequences;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    // Abstract methods for game variants
    protected abstract void handleScoringMove();
    protected abstract boolean shouldEndGame();
    public abstract String determineWinner();
    public abstract String getGameMode();
}