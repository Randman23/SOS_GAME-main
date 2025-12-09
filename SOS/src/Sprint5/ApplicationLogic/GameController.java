package Sprint5.ApplicationLogic;

import Sprint5.GameLogic.*;
import Sprint5.GUILogic.GameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

/**
 * Controls game flow and coordinates between game logic and GUI.
 * Now includes recording and replay functionality with start/stop controls.
 */
public class GameController {
    private Game currentGame;
    private final GameView view;
    private boolean processingComputerMove = false;
    private GameRecorder recorder;
    private GameReplayer replayer;
    private boolean replayMode = false;
    private boolean gamePaused = false;
    private boolean gameInProgress = false;

    public GameController(GameView view, Game initialGame) {
        this.view = view;
        this.currentGame = initialGame;
        this.recorder = new GameRecorder();
        this.replayer = new GameReplayer();
        setupHandlers();
        setupReplayCallback();
        updateView();
    }

    private void setupHandlers() {
        view.getBoardPanel().setClickHandler(this::handleMove);
        view.getControlPanel().setNewGameHandler(this::startNewGame);
        view.getControlPanel().setPlayerTypeChangeHandler(this::updatePlayerTypes);
        view.getControlPanel().setRecordingChangeHandler(this::handleRecordingToggle);
        view.getControlPanel().setReplayHandler(this::loadAndReplayGame);
        view.getControlPanel().setStartHandler(this::handleStart);
        view.getControlPanel().setStopHandler(this::handleStop);
    }

    private void setupReplayCallback() {
        replayer.setCallback(new GameReplayer.ReplayCallback() {
            @Override
            public void onReplayUpdate() {
                Platform.runLater(() -> updateView());
            }

            @Override
            public void onReplayComplete() {
                Platform.runLater(() -> {
                    updateView();
                    view.getControlPanel().setStartButtonText("▶ Start");
                    showAlert("Replay Complete", "The game replay has finished.");
                });
            }
        });
    }

    private void handleStart() {
        if (replayMode) {
            handleStartReplay();
        } else {
            handleStartGame();
        }
    }

    private void handleStop() {
        if (replayMode) {
            handleStopReplay();
        } else {
            handleStopGame();
        }
    }

    private void handleStartGame() {
        if (!gameInProgress) {
            showAlert("No Game", "Please start a new game first.");
            return;
        }

        if (gamePaused) {
            gamePaused = false;
            view.getControlPanel().setStartButtonText("⏸ Pause");
            System.out.println("Game resumed");

            if (!currentGame.isGameFinished()) {
                checkAndTriggerComputerMove();
            }
        } else {
            gamePaused = true;
            view.getControlPanel().setStartButtonText("▶ Resume");
            System.out.println("Game paused");
        }
    }

    private void handleStopGame() {
        if (!gameInProgress) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Stop Game");
        confirm.setHeaderText("Are you sure you want to stop the current game?");
        confirm.setContentText("This will end the game and you can start a new one.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                gamePaused = false;
                gameInProgress = false;
                view.getControlPanel().setStartButtonText("▶ Start");

                if (recorder.isRecording()) {
                    recorder.stopRecording();
                }

                System.out.println("Game stopped");
                showAlert("Game Stopped", "The game has been stopped. Click 'New game' to start a new game.");
            }
        });
    }

    private void handleStartReplay() {
        if (!replayMode) {
            showAlert("No Replay Loaded", "Please load a replay file first using the Replay button.");
            return;
        }

        if (replayer.isPlaying()) {
            replayer.pause();
            view.getControlPanel().setStartButtonText("▶ Resume");
            System.out.println("Replay paused");
        } else {
            replayer.play();
            view.getControlPanel().setStartButtonText("⏸ Pause");
            System.out.println("Replay started/resumed");
        }
    }

    private void handleStopReplay() {
        if (!replayMode) {
            return;
        }

        replayer.stop();
        view.getControlPanel().setStartButtonText("▶ Start");
        updateView();
        System.out.println("Replay stopped and reset");
    }

    private void handleMove(int row, int col) {
        if (replayMode || gamePaused) {
            return;
        }

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
            if (recorder.isRecording()) {
                recorder.recordMove(row, col, playerBefore.getSelectedLetter(),
                        playerBefore.getName());
            }

            int scoreAfter = playerBefore.getScore();

            if (scoreAfter > scoreBefore) {
                System.out.println(playerBefore.getName() + " scored! " +
                        "Points earned: " + (scoreAfter - scoreBefore) +
                        ", Total score: " + scoreAfter);
            }

            updateView();

            if (currentGame.isGameFinished()) {
                gameInProgress = false;
                gamePaused = false;
                view.getControlPanel().setStartButtonText("▶ Start");

                if (recorder.isRecording()) {
                    recorder.stopRecording();
                    promptSaveRecording();
                }
                showGameOverAlert();
            } else {
                checkAndTriggerComputerMove();
            }
        }
    }

    private void checkAndTriggerComputerMove() {
        if (gamePaused || currentGame.isGameFinished()) {
            return;
        }

        if (currentGame.getCurrentPlayer().isComputer() && !currentGame.isGameFinished()) {
            processingComputerMove = true;

            new Thread(() -> {
                try {
                    Thread.sleep(500);

                    if (!gamePaused) {
                        Platform.runLater(this::executeComputerMove);
                    } else {
                        processingComputerMove = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void executeComputerMove() {
        if (currentGame.isGameFinished() || gamePaused) {
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
        if (replayMode) {
            view.getBoardPanel().setGame(replayer.getGame());
        }
        view.getBoardPanel().redraw();
        view.updatePlayerScores();
        view.updateTurn();
    }

    private void startNewGame() {
        int size = view.getControlPanel().getSelectedSize();
        boolean isSimple = view.getControlPanel().isSimpleMode();

        if (replayMode) {
            replayer.stop();
            replayMode = false;
        }

        gamePaused = true;
        gameInProgress = true;

        if (isSimple) {
            currentGame = new SimpleGame(size);
        } else {
            currentGame = new GeneralGame(size);
        }

        updatePlayerTypes();

        System.out.println("New " + currentGame.getGameMode() + " game created. Board size: " + size);
        System.out.println("Click 'Start' to begin the game.");

        view.setGame(currentGame);
        view.getBoardPanel().setClickHandler(this::handleMove);
        view.resetView();

        view.getControlPanel().enableReplayControls(true);
        view.getControlPanel().setStartButtonText("▶ Start");
    }

    private void updatePlayerTypes() {
        PlayerType blueType = view.getControlPanel().getBluePlayerType();
        PlayerType redType = view.getControlPanel().getRedPlayerType();

        Player bluePlayer = currentGame.getBluePlayer();
        Player redPlayer = currentGame.getRedPlayer();

        bluePlayer.setPlayerType(blueType);
        redPlayer.setPlayerType(redType);

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

        view.updatePlayerPanels();
    }

    private void handleRecordingToggle(boolean isRecording) {
        if (isRecording) {
            String gameMode = currentGame.getGameMode();
            int boardSize = currentGame.getBoard().getSize();
            String blueType = currentGame.getBluePlayer().isComputer() ? "Computer" : "Human";
            String redType = currentGame.getRedPlayer().isComputer() ? "Computer" : "Human";

            recorder.startRecording(gameMode, boardSize, blueType, redType);
        } else {
            if (recorder.isRecording()) {
                recorder.stopRecording();
                promptSaveRecording();
            }
        }
    }

    private void promptSaveRecording() {
        if (recorder.getMoves().isEmpty()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Recording");
        alert.setHeaderText("Do you want to save this game recording?");
        alert.setContentText("The game will be saved to a text file.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                saveRecording();
            }
        });
    }

    private void saveRecording() {
        try {
            String filepath = recorder.saveToFile("game_recordings");
            showAlert("Recording Saved", "Game recording saved successfully to:\n" + filepath);
        } catch (IOException e) {
            showAlert("Error", "Failed to save recording: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAndReplayGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Recording");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File recordingsDir = new File("game_recordings");
        if (recordingsDir.exists()) {
            fileChooser.setInitialDirectory(recordingsDir);
        }

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                GameRecorder.GameRecording recording = GameRecorder.loadFromFile(file.getAbsolutePath());
                replayer.loadRecording(recording);
                replayMode = true;
                gamePaused = false;
                gameInProgress = false;

                view.getControlPanel().enableReplayControls(true);
                view.getControlPanel().setStartButtonText("▶ Start");

                view.setGame(replayer.getGame());
                view.resetView();

                showAlert("Replay Loaded", "Replay loaded successfully. Click 'Start' to begin playback.");

            } catch (IOException e) {
                showAlert("Error", "Failed to load recording: " + e.getMessage());
                e.printStackTrace();
            }
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public GameReplayer getReplayer() {
        return replayer;
    }
}