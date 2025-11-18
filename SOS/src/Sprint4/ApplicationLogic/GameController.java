package Sprint4.ApplicationLogic;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import Sprint4.GUILogic.*;
import Sprint4.GameLogic.*;
/**
 * Controls game flow and coordinates between game logic and GUI.
 */
public class GameController {
    private Game currentGame;
    private final GameView view;
    private boolean processingComputerMove = false;

    public GameController(GameView view, Game initialGame) {
        this.view = view;
        this.currentGame = initialGame;
        setupHandlers();
        updateView();
    }



    private void setupHandlers() {
        view.getBoardPanel().setClickHandler(this::handleMove);
        view.getControlPanel().setNewGameHandler(this::startNewGame);
        view.getControlPanel().setPlayerTypeChangeHandler(this::updatePlayerTypes);
    }

    private void handleMove(int row, int col) {
        // Ignore clicks if computer is thinking or if current player is computer
        if (processingComputerMove || currentGame.getCurrentPlayer().isComputer()) {
            return;
        }

        if (currentGame.isGameFinished()) {
            showGameOverAlert();
            return;
        }

        makeMove(row, col);
    }

    private void makeMove(int row, int col) {
        Player playerBefore = currentGame.getCurrentPlayer();
        int scoreBefore = playerBefore.getScore();

        if (currentGame.makeMove(row, col)) {
            int scoreAfter = playerBefore.getScore();

            if (scoreAfter > scoreBefore) {
                System.out.println(playerBefore.getName() + " scored! " +
                        "Points earned: " + (scoreAfter - scoreBefore) +
                        ", Total score: " + scoreAfter);
            }

            updateView();

            if (currentGame.isGameFinished()) {
                showGameOverAlert();
            } else {
                // Check if next player is computer
                checkAndTriggerComputerMove();
            }
        }
    }

    private void checkAndTriggerComputerMove() {
        if (currentGame.getCurrentPlayer().isComputer() && !currentGame.isGameFinished()) {
            // Delay computer move slightly for better UX
            processingComputerMove = true;

            new Thread(() -> {
                try {
                    Thread.sleep(500); // Half second delay
                    Platform.runLater(this::executeComputerMove);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void executeComputerMove() {
        if (currentGame.isGameFinished()) {
            processingComputerMove = false;
            return;
        }

        Player currentPlayer = currentGame.getCurrentPlayer();

        if (currentPlayer.isComputer()) {
            ComputerPlayer strategy = currentPlayer.getComputerStrategy();
            Move move = strategy.makeMove(currentGame, currentPlayer);

            if (move != null) {
                currentPlayer.setSelectedLetter(move.getLetter());
                makeMove(move.getRow(), move.getCol());
            }
        }

        processingComputerMove = false;
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

        // Set up player types
        updatePlayerTypes();

        System.out.println("New " + currentGame.getGameMode() + " game started. Board size: " + size);

        view.setGame(currentGame);
        view.getBoardPanel().setClickHandler(this::handleMove);
        view.resetView();

        // If blue player is computer, trigger first move
        checkAndTriggerComputerMove();
    }

    private void updatePlayerTypes() {
        PlayerType blueType = view.getControlPanel().getBluePlayerType();
        PlayerType redType = view.getControlPanel().getRedPlayerType();

        Player bluePlayer = currentGame.getBluePlayer();
        Player redPlayer = currentGame.getRedPlayer();

        bluePlayer.setPlayerType(blueType);
        redPlayer.setPlayerType(redType);

        // Set computer strategies based on game mode
        if (blueType == PlayerType.COMPUTER) {
            if (currentGame instanceof SimpleGame) {
                bluePlayer.setComputerStrategy(new SimpleComputerPlayer());
            } else {
                bluePlayer.setComputerStrategy(new GeneralComputerPlayer());
            }
        }

        if (redType == PlayerType.COMPUTER) {
            if (currentGame instanceof SimpleGame) {
                redPlayer.setComputerStrategy(new SimpleComputerPlayer());
            } else {
                redPlayer.setComputerStrategy(new GeneralComputerPlayer());
            }
        }

        // Update UI to reflect player types
        view.updatePlayerPanels();

        // Check if current player is computer and should make a move
        if (!currentGame.isGameFinished()) {
            checkAndTriggerComputerMove();
        }
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