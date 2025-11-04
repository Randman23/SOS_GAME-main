package Sprint3;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SOSGUI extends Application {

    SOSGame game;
    private final double BOARD_SIZE = 400;
    private List<Text> letters = new ArrayList<>();
    private List<Line> sosLines = new ArrayList<>();
    private Pane boardPane;

    private char currentLetterP1 = 'S';
    private char currentLetterP2 = 'S';

    private boolean isRecording = false;
    boolean isSimpleMode = true;
    private ComboBox<Integer> sizeSelector;

    private Label turnLabel;
    private Label player1ScoreLabel;
    private Label player2ScoreLabel;
    private Label gameStatusLabel;

    @Override
    public void start(Stage stage) {
        game = new SOSGame(5);

        boardPane = new Pane();
        boardPane.setPrefSize(BOARD_SIZE, BOARD_SIZE);
        boardPane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

        boardPane.setOnMouseClicked(e -> handleBoardClick(e.getX(), e.getY()));

        drawGrid();

        // Create score labels
        player1ScoreLabel = new Label("Score: 0");
        player1ScoreLabel.setFont(Font.font(18));
        player1ScoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #cc0000;");

        player2ScoreLabel = new Label("Score: 0");
        player2ScoreLabel.setFont(Font.font(18));
        player2ScoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0000cc;");

        // Create turn label
        turnLabel = new Label("Player 1's Turn (S)");
        turnLabel.setFont(Font.font(18));
        turnLabel.setAlignment(Pos.CENTER);

        // Create game status label
        gameStatusLabel = new Label("");
        gameStatusLabel.setFont(Font.font(20));
        gameStatusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        gameStatusLabel.setAlignment(Pos.CENTER);

        // Player controls
        VBox leftControls = createPlayerControls("Player 1", true);
        VBox rightControls = createPlayerControls("Player 2", false);

        // Bottom controls
        CheckBox recordGameCheckBox = new CheckBox("Record game");
        recordGameCheckBox.setOnAction(e -> isRecording = recordGameCheckBox.isSelected());

        CheckBox gameModeCheckBox = new CheckBox("Simple Mode");
        gameModeCheckBox.setSelected(true);
        gameModeCheckBox.setOnAction(e -> {
            isSimpleMode = gameModeCheckBox.isSelected();
            gameModeCheckBox.setText(isSimpleMode ? "Simple Mode" : "General Mode");
            resetBoard();
        });

        // Size selection
        sizeSelector = new ComboBox<>();
        for (int i = 3; i <= 10; i++) sizeSelector.getItems().add(i);
        sizeSelector.setValue(5);
        sizeSelector.setOnAction(e -> {
            int newSize = sizeSelector.getValue();
            game = new SOSGame(newSize);
            resetBoard();
        });

        Label sizeLabel = new Label("Board size:");
        HBox sizeBox = new HBox(5, sizeLabel, sizeSelector);
        sizeBox.setAlignment(Pos.CENTER);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetBoard());

        HBox bottomControls = new HBox(20, recordGameCheckBox, gameModeCheckBox, sizeBox, resetButton);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setStyle("-fx-padding: 10;");

        HBox centerLayout = new HBox(40, leftControls, boardPane, rightControls);
        centerLayout.setAlignment(Pos.CENTER);

        // Top panel with turn and status
        VBox topPanel = new VBox(5, turnLabel, gameStatusLabel);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setStyle("-fx-padding: 10;");

        BorderPane root = new BorderPane();
        root.setTop(topPanel);
        root.setCenter(centerLayout);
        root.setBottom(bottomControls);

        Scene scene = new Scene(root, 850, 550);
        stage.setTitle("SOS Game");
        stage.setScene(scene);
        stage.show();

        updateTurnLabel();
    }

    private VBox createPlayerControls(String name, boolean isPlayerOne) {
        Label label = new Label(name);
        label.setFont(Font.font(18));
        label.setStyle("-fx-font-weight: bold;");

        Label scoreLabel = isPlayerOne ? player1ScoreLabel : player2ScoreLabel;

        RadioButton sButton = new RadioButton("S");
        RadioButton oButton = new RadioButton("O");
        ToggleGroup group = new ToggleGroup();
        sButton.setToggleGroup(group);
        oButton.setToggleGroup(group);
        sButton.setSelected(true);

        if (isPlayerOne) {
            sButton.selectedProperty().addListener((obs, was, isNow) -> {
                if (isNow) currentLetterP1 = 'S';
                updateTurnLabel();
            });
            oButton.selectedProperty().addListener((obs, was, isNow) -> {
                if (isNow) currentLetterP1 = 'O';
                updateTurnLabel();
            });
        } else {
            sButton.selectedProperty().addListener((obs, was, isNow) -> {
                if (isNow) currentLetterP2 = 'S';
                updateTurnLabel();
            });
            oButton.selectedProperty().addListener((obs, was, isNow) -> {
                if (isNow) currentLetterP2 = 'O';
                updateTurnLabel();
            });
        }

        VBox vbox = new VBox(15, label, scoreLabel, sButton, oButton);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle(isPlayerOne ?
                "-fx-background-color: #ffdddd; -fx-padding: 30; -fx-border-color: #cc0000; -fx-border-width: 2;" :
                "-fx-background-color: #ddeeff; -fx-padding: 30; -fx-border-color: #0000cc; -fx-border-width: 2;");
        return vbox;
    }

    private void handleBoardClick(double x, double y) {
        if (game.isGameOver()) {
            showAlert("Game Over", game.getWinner());
            return;
        }

        int size = game.getSize();
        double cellWidth = BOARD_SIZE / size;
        double cellHeight = BOARD_SIZE / size;

        int col = (int) (x / cellWidth);
        int row = (int) (y / cellHeight);

        if (col < 0 || col >= size || row < 0 || row >= size) return;

        char letter = game.getCurrentPlayerLetter(currentLetterP1, currentLetterP2);

        int scoresBefore = game.getPlayer1Score() + game.getPlayer2Score();

        if (game.placeLetter(row, col, letter)) {
            double centerX = col * cellWidth + cellWidth / 2;
            double centerY = row * cellHeight + cellHeight / 2;

            double fontSize = Math.max(12, Math.min(36, cellWidth * 0.5));
            Text t = new Text(centerX - fontSize * 0.3, centerY + fontSize * 0.3, String.valueOf(letter));
            t.setFont(Font.font(fontSize));
            t.setFill(Color.BLACK);
            letters.add(t);
            boardPane.getChildren().add(t);

            if (isRecording)
                System.out.println("Placed " + letter + " at (" + row + "," + col + ")");

            // Check if score changed (SOS was formed)
            int scoresAfter = game.getPlayer1Score() + game.getPlayer2Score();
            if (scoresAfter > scoresBefore) {
                // Draw SOS lines
                drawSOSLines();

                // In Simple Mode, end game after first SOS
                if (isSimpleMode) {
                    game.endGameSimpleMode();
                    updateScoreLabels();
                    updateTurnLabel();
                    showAlert("Game Over - Simple Mode", game.getWinner());
                    return;
                }
            }

            // Update display
            updateScoreLabels();
            updateTurnLabel();

            // Check if game ended
            if (game.isGameOver()) {
                showAlert("Game Over", game.getWinner());
            }
        }
    }

    private void drawSOSLines() {
        // Clear old lines
        boardPane.getChildren().removeAll(sosLines);
        sosLines.clear();

        int size = game.getSize();
        double cellWidth = BOARD_SIZE / size;
        double cellHeight = BOARD_SIZE / size;

        // Draw lines for each SOS sequence
        for (SOSGame.SOSSequence seq : game.getSosSequences()) {
            double x1 = seq.startCol * cellWidth + cellWidth / 2;
            double y1 = seq.startRow * cellHeight + cellHeight / 2;
            double x2 = seq.endCol * cellWidth + cellWidth / 2;
            double y2 = seq.endRow * cellHeight + cellHeight / 2;

            Line line = new Line(x1, y1, x2, y2);
            line.setStroke(seq.player == 1 ? Color.RED : Color.BLUE);
            line.setStrokeWidth(3);
            sosLines.add(line);
            boardPane.getChildren().add(line);
        }
    }

    private void drawGrid() {
        boardPane.getChildren().clear();
        letters.clear();
        sosLines.clear();

        double rowHeight = BOARD_SIZE / game.getSize();
        double colWidth = BOARD_SIZE / game.getSize();

        for (int i = 0; i <= game.getSize(); i++) {
            double y = i * rowHeight;
            double x = i * colWidth;

            Line h = new Line(0, y, BOARD_SIZE, y);
            Line v = new Line(x, 0, x, BOARD_SIZE);

            h.setStroke(Color.GRAY);
            v.setStroke(Color.GRAY);
            h.setStrokeWidth(1);
            v.setStrokeWidth(1);

            boardPane.getChildren().addAll(h, v);
        }
    }

    private void resetBoard() {
        game.resetGame();
        drawGrid();
        updateScoreLabels();
        updateTurnLabel();
        gameStatusLabel.setText("");
        System.out.println("Board reset. Mode: " + (isSimpleMode ? "Simple" : "General")
                + ", Size: " + game.getSize());
    }

    private void updateTurnLabel() {
        if (game.isGameOver()) {
            turnLabel.setText("Game Over!");
            gameStatusLabel.setText(game.getWinner());
        } else {
            String player = game.isPlayerOneTurn() ? "Player 1" : "Player 2";
            char letter = game.isPlayerOneTurn() ? currentLetterP1 : currentLetterP2;
            turnLabel.setText(player + "'s Turn (" + letter + ")");
            gameStatusLabel.setText("");
        }
    }

    private void updateScoreLabels() {
        player1ScoreLabel.setText("Score: " + game.getPlayer1Score());
        player2ScoreLabel.setText("Score: " + game.getPlayer2Score());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}