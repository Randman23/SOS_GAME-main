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

    private SOSGame game;
    private final double boardWidth = 300;
    private final double boardHeight = 300;
    private List<Text> letters = new ArrayList<>();
    private Pane boardPane;

    private char currentLetterP1 = 'S';
    private char currentLetterP2 = 'S';

    private boolean isRecording = false;
    private boolean isSimpleMode = true; // true = Simple, false = General

    private int boardSize = 5; // default

    @Override
    public void start(Stage stage) {
        showStartupDialog(stage);
    }

    /**
     * Shows a startup dialog to select board size and game mode.
     */
    private void showStartupDialog(Stage stage) {
        Label sizeLabel = new Label("Select Board Size:");
        Spinner<Integer> sizeSpinner = new Spinner<>(3, 10, 5);
        sizeSpinner.setEditable(true);

        CheckBox modeCheckBox = new CheckBox("Simple Mode");
        modeCheckBox.setSelected(true);
        modeCheckBox.setOnAction(e -> isSimpleMode = modeCheckBox.isSelected());

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            boardSize = sizeSpinner.getValue();
            isSimpleMode = modeCheckBox.isSelected();
            stage.close();
            initializeGame(stage);
        });

        VBox dialogLayout = new VBox(15, sizeLabel, sizeSpinner, modeCheckBox, startButton);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.setStyle("-fx-padding: 30; -fx-background-color: lightgray;");

        Scene scene = new Scene(dialogLayout, 250, 200);
        stage.setTitle("SOS Game Options");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the main game GUI based on selected options.
     */
    private void initializeGame(Stage stage) {
        game = new SOSGame(boardSize);

        boardPane = new Pane();
        boardPane.setPrefSize(boardWidth, boardHeight);
        boardPane.setStyle("-fx-background-color: white; -fx-border-color: black;");

        drawGrid();

        VBox leftControls = createPlayerControls("Player 1", true);
        VBox rightControls = createPlayerControls("Player 2", false);

        CheckBox recordGameCheckBox = new CheckBox("Record game");
        recordGameCheckBox.setOnAction(e -> isRecording = recordGameCheckBox.isSelected());

        CheckBox gameModeCheckBox = new CheckBox(isSimpleMode ? "Simple Mode" : "General Mode");
        gameModeCheckBox.setSelected(isSimpleMode);
        gameModeCheckBox.setOnAction(e -> {
            isSimpleMode = gameModeCheckBox.isSelected();
            gameModeCheckBox.setText(isSimpleMode ? "Simple Mode" : "General Mode");
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetBoard());

        HBox bottomControls = new HBox(15, recordGameCheckBox, gameModeCheckBox, resetButton);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setStyle("-fx-padding: 10;");

        boardPane.setOnMouseClicked(e -> handleBoardClick(e.getX(), e.getY()));

        HBox centerLayout = new HBox(40, leftControls, boardPane, rightControls);
        centerLayout.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(centerLayout);
        root.setBottom(bottomControls);

        Scene scene = new Scene(root, 750, 400);
        stage.setTitle("SOS Game - " + boardSize + "x" + boardSize);
        stage.setScene(scene);
        stage.show();
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
            sButton.setOnAction(e -> currentLetterP1 = 'S');
            oButton.setOnAction(e -> currentLetterP1 = 'O');
        } else {
            sButton.setOnAction(e -> currentLetterP2 = 'S');
            oButton.setOnAction(e -> currentLetterP2 = 'O');
        }

        VBox vbox = new VBox(10, label, sButton, oButton);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle(isPlayerOne ? "-fx-background-color: red; -fx-padding: 20;" :
                "-fx-background-color: blue; -fx-padding: 20;");
        return vbox;
    }

    private void handleBoardClick(double x, double y) {
        int size = game.getSize();
        double cellWidth = boardWidth / size;
        double cellHeight = boardHeight / size;

        int col = (int) (x / cellWidth);
        int row = (int) (y / cellHeight);

        char letter = game.getCurrentPlayerLetter(currentLetterP1, currentLetterP2);
        if (game.placeLetter(row, col, letter)) {
            double centerX = col * cellWidth + cellWidth / 2;
            double centerY = row * cellHeight + cellHeight / 2;

            Text t = new Text(centerX - 7, centerY + 7, String.valueOf(letter));
            t.setFont(Font.font(24));
            t.setFill(Color.BLACK);
            letters.add(t);
            boardPane.getChildren().add(t);

            if (isRecording) {
                System.out.println("Placed " + letter + " at (" + row + "," + col + ")");
            }
        }
    }

    private void drawGrid() {
        double rowHeight = boardHeight / game.getSize();
        double colWidth = boardWidth / game.getSize();

        for (int i = 1; i < game.getSize(); i++) {
            Line h = new Line(0, i * rowHeight, boardWidth, i * rowHeight);
            h.setStroke(Color.LIGHTGRAY);
            Line v = new Line(i * colWidth, 0, i * colWidth, boardHeight);
            v.setStroke(Color.LIGHTGRAY);
            boardPane.getChildren().addAll(h, v);
        }
    }

    private void resetBoard() {
        game.resetGame();
        boardPane.getChildren().removeAll(letters);
        letters.clear();
        System.out.println("Board reset. Game Mode: " + (isSimpleMode ? "Simple" : "General"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
