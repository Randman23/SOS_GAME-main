package Sprint4.GameLogic;

/**
 * Simple game mode - first player to create an SOS wins.
 */
public class SimpleGame extends Game {

    public SimpleGame(int boardSize) {
        super(boardSize);
    }

    @Override
    protected void handleScoringMove() {
        // In simple mode, scoring ends the game
        gameFinished = true;
    }

    @Override
    protected boolean shouldEndGame() {
        return !allSequences.isEmpty() || board.isFull();
    }

    @Override
    public String determineWinner() {
        if (!gameFinished) {
            return null;
        }

        if (bluePlayer.getScore() > 0 && redPlayer.getScore() == 0) {
            return "Blue player wins!";
        } else if (redPlayer.getScore() > 0 && bluePlayer.getScore() == 0) {
            return "Red player wins!";
        } else if (bluePlayer.getScore() > redPlayer.getScore()) {
            return "Blue player wins!";
        } else if (redPlayer.getScore() > bluePlayer.getScore()) {
            return "Red player wins!";
        } else if (bluePlayer.getScore() > 0) {
            return "Draw";
        }

        return "Draw";
    }

    @Override
    public String getGameMode() {
        return "Simple";
    }
}