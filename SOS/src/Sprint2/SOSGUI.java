package Sprint2;

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
    private final double BOARD_SIZE = 400; // board pixel size
    private List<Text> letters = new ArrayList<>();
    private Pane boardPane;

    private char currentLetterP1 = 'S';
    private char currentLetterP2 = 'S';

    private boolean isRecording = false;
    boolean isSimpleMode = true;
    private ComboBox<Integer> sizeSelector;

    private Label turnLabel; // NEW: Label to show current player's turn

    @Override
    public void start(Stage stage) {
        game = new SOSGame(5);

        boardPane = new Pane();
        boardPane.setPrefSize(BOARD_SIZE, BOARD_SIZE);
        boardPane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

        // Attach mouse handler once (will continue to work after redraws)
        boardPane.setOnMouseClicked(e -> handleBoardClick(e.getX(), e.getY()));

        drawGrid(); // initial draw

        // Create turn label
        turnLabel = new Label("Player 1's Turn (S)");
        turnLabel.setFont(Font.font(18));
        turnLabel.setAlignment(Pos.CENTER);

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

        // Size selection box
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

        BorderPane root = new BorderPane();
        root.setTop(turnLabel); // Add turn label at the top
        BorderPane.setAlignment(turnLabel, Pos.CENTER);
        root.setCenter(centerLayout);
        root.setBottom(bottomControls);

        Scene scene = new Scene(root, 850, 500);
        stage.setTitle("SOS Game");
        stage.setScene(scene);
        stage.show();

        updateTurnLabel(); // Initialize turn label text
    }

    private VBox createPlayerControls(String name, boolean isPlayerOne) {
        Label label = new Label(name);
        label.setFont(Font.font(16));

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

        VBox vbox = new VBox(10, label, sButton, oButton);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle(isPlayerOne ? "-fx-background-color: #ffdddd; -fx-padding: 20;" :
                "-fx-background-color: #ddeeff; -fx-padding: 20;");
        return vbox;
    }

    private void handleBoardClick(double x, double y) {
        int size = game.getSize();
        double cellWidth = BOARD_SIZE / size;
        double cellHeight = BOARD_SIZE / size;

        int col = (int) (x / cellWidth);
        int row = (int) (y / cellHeight);

        if (col < 0 || col >= size || row < 0 || row >= size) return;

        char letter = game.getCurrentPlayerLetter(currentLetterP1, currentLetterP2);
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

            // Update to next player's turn
            game.switchTurn();
            updateTurnLabel();
        } else {
            System.out.println("Cannot place at (" + row + "," + col + ") - occupied or invalid.");
        }
    }

    private void drawGrid() {
        boardPane.getChildren().clear();
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
        letters.clear();
        drawGrid();
        updateTurnLabel();
        System.out.println("Board reset. Mode: " + (isSimpleMode ? "Simple" : "General")
                + ", Size: " + game.getSize());
    }

    private void updateTurnLabel() {
        String player = game.isPlayerOneTurn() ? "Player 1" : "Player 2";
        char letter = game.isPlayerOneTurn() ? currentLetterP1 : currentLetterP2;
        turnLabel.setText(player + "'s Turn (" + letter + ")");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
