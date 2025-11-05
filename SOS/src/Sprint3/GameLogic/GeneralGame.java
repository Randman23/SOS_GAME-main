package Sprint3.GameLogic;

/**
 * General game mode - play until board is full, highest score wins.
 */
public class GeneralGame extends Game {

    public GeneralGame(int boardSize) {
        super(boardSize);
    }

    @Override
    protected void handleScoringMove() {
        // Player keeps their turn after scoring
        // Don't switch players
    }

    @Override
    protected boolean shouldEndGame() {
        return board.isFull();
    }

    @Override
    public String determineWinner() {
        if (!gameFinished) {
            return null;
        }

        if (bluePlayer.getScore() > redPlayer.getScore()) {
            return "Blue player wins!";
        } else if (redPlayer.getScore() > bluePlayer.getScore()) {
            return "Red player wins!";
        }

        return "Draw";
    }

    @Override
    public String getGameMode() {
        return "General";
    }
}