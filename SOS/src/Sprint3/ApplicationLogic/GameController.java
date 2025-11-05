package Sprint3.ApplicationLogic;

import javafx.scene.control.Alert;
import Sprint3.GameLogic.*;
import Sprint3.GUILogic.*;
/**
 * Controls game flow and coordinates between game logic and GUI.
 */
public class GameController {
    private Game currentGame;
    private final GameView view;

    public GameController(GameView view, Game initialGame) {
        this.view = view;
        this.currentGame = initialGame;
        setupHandlers();
        updateView();
    }

    private void setupHandlers() {
        view.getBoardPanel().setClickHandler(this::handleMove);
        view.getControlPanel().setNewGameHandler(this::startNewGame);
    }

    private void handleMove(int row, int col) {
        if (currentGame.isGameFinished()) {
            showGameOverAlert();
            return;
        }

        Player playerBefore = currentGame.getCurrentPlayer();
        int scoreBefore = playerBefore.getScore();

        if (currentGame.makeMove(row, col)) {
            int scoreAfter = playerBefore.getScore();

            // Debug output
            if (scoreAfter > scoreBefore) {
                System.out.println(playerBefore.getName() + " scored! " +
                        "Points earned: " + (scoreAfter - scoreBefore) +
                        ", Total score: " + scoreAfter);
                System.out.println("Total sequences: " + currentGame.getAllSequences().size());
            }

            updateView();

            if (currentGame.isGameFinished()) {
                showGameOverAlert();
            }
        }
    }

    private void updateView() {
        view.getBoardPanel().redraw();
        view.updatePlayerScores();
        view.updateTurn();
    }

    private void startNewGame() {
        int size = view.getControlPanel().getSelectedSize();
        boolean isSimple = view.getControlPanel().isSimpleMode();

        if (isSimple) {
            currentGame = new SimpleGame(size);
        } else {
            currentGame = new GeneralGame(size);
        }

        System.out.println("New " + currentGame.getGameMode() + " game started. Board size: " + size);

        view.setGame(currentGame);

        // Reconnect the click handler after changing game
        view.getBoardPanel().setClickHandler(this::handleMove);

        view.resetView();
    }

    private void showGameOverAlert() {
        String result = currentGame.determineWinner();
        System.out.println("Game Over: " + result);
        System.out.println("Blue Score: " + currentGame.getBluePlayer().getScore());
        System.out.println("Red Score: " + currentGame.getRedPlayer().getScore());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(result + "\n\nBlue: " + currentGame.getBluePlayer().getScore() +
                " | Red: " + currentGame.getRedPlayer().getScore());
        alert.showAndWait();
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}