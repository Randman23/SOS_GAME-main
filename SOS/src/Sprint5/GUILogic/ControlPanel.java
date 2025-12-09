package Sprint5.GUILogic;

import Sprint5.GameLogic.PlayerType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

/**
 * Control panel for game mode, board size, and recording/replay options.
 */
public class ControlPanel extends HBox {
    private final RadioButton simpleButton;
    private final RadioButton generalButton;
    private final ComboBox<Integer> sizeBox;
    private final Button newGameButton;
    private final Button replayButton;
    private final Button startButton;
    private final Button stopButton;
    private final CheckBox recordCheckBox;

    // Player type controls
    private final RadioButton blueHumanButton;
    private final RadioButton blueComputerButton;
    private final RadioButton redHumanButton;
    private final RadioButton redComputerButton;

    public ControlPanel() {
        Label titleLabel = new Label("SOS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Game mode selection
        simpleButton = new RadioButton("Simple game");
        generalButton = new RadioButton("General game");

        ToggleGroup modeGroup = new ToggleGroup();
        simpleButton.setToggleGroup(modeGroup);
        generalButton.setToggleGroup(modeGroup);
        simpleButton.setSelected(true);

        VBox modeBox = new VBox(5, simpleButton, generalButton);

        // Board size selection
        Label sizeLabel = new Label("Board size");
        sizeBox = new ComboBox<>();
        for (int i = 3; i <= 10; i++) {
            sizeBox.getItems().add(i);
        }
        sizeBox.setValue(5);

        VBox sizeSelection = new VBox(5, sizeLabel, sizeBox);
        sizeSelection.setAlignment(Pos.CENTER_LEFT);

        // Blue player type selection
        Label bluePlayerLabel = new Label("Blue player");
        bluePlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        blueHumanButton = new RadioButton("Human");
        blueComputerButton = new RadioButton("Computer");

        ToggleGroup blueGroup = new ToggleGroup();
        blueHumanButton.setToggleGroup(blueGroup);
        blueComputerButton.setToggleGroup(blueGroup);
        blueHumanButton.setSelected(true);

        VBox bluePlayerBox = new VBox(5, bluePlayerLabel, blueHumanButton, blueComputerButton);
        bluePlayerBox.setStyle("-fx-padding: 5; -fx-border-color: lightblue; -fx-border-width: 1;");

        // Red player type selection
        Label redPlayerLabel = new Label("Red player");
        redPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        redHumanButton = new RadioButton("Human");
        redComputerButton = new RadioButton("Computer");

        ToggleGroup redGroup = new ToggleGroup();
        redHumanButton.setToggleGroup(redGroup);
        redComputerButton.setToggleGroup(redGroup);
        redHumanButton.setSelected(true);

        VBox redPlayerBox = new VBox(5, redPlayerLabel, redHumanButton, redComputerButton);
        redPlayerBox.setStyle("-fx-padding: 5; -fx-border-color: lightcoral; -fx-border-width: 1;");

        // Recording checkbox
        recordCheckBox = new CheckBox("Record game");
        recordCheckBox.setFont(Font.font("Arial", 12));

        // Game control buttons
        newGameButton = new Button("New game");
        newGameButton.setStyle("-fx-font-weight: bold;");
        newGameButton.setPrefWidth(100);

        replayButton = new Button("Replay");
        replayButton.setStyle("-fx-font-weight: bold;");
        replayButton.setPrefWidth(100);

        // Replay control buttons
        startButton = new Button("▶ Start");
        startButton.setStyle("-fx-font-weight: bold; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setPrefWidth(100);
        startButton.setDisable(true); // Disabled until replay is loaded

        stopButton = new Button("⬛ Stop");
        stopButton.setStyle("-fx-font-weight: bold; -fx-background-color: #f44336; -fx-text-fill: white;");
        stopButton.setPrefWidth(100);
        stopButton.setDisable(true); // Disabled until replay is loaded

        VBox gameControlsBox = new VBox(8, recordCheckBox, newGameButton, replayButton);
        gameControlsBox.setAlignment(Pos.CENTER_LEFT);

        VBox replayControlsBox = new VBox(8, startButton, stopButton);
        replayControlsBox.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(titleLabel, modeBox, sizeSelection,
                bluePlayerBox, redPlayerBox, gameControlsBox, replayControlsBox);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        setPadding(new Insets(10));
    }

    public boolean isSimpleMode() {
        return simpleButton.isSelected();
    }

    public int getSelectedSize() {
        return sizeBox.getValue();
    }

    public boolean isBluePlayerHuman() {
        return blueHumanButton.isSelected();
    }

    public boolean isRedPlayerHuman() {
        return redHumanButton.isSelected();
    }

    public PlayerType getBluePlayerType() {
        return blueHumanButton.isSelected() ? PlayerType.HUMAN : PlayerType.COMPUTER;
    }

    public PlayerType getRedPlayerType() {
        return redHumanButton.isSelected() ? PlayerType.HUMAN : PlayerType.COMPUTER;
    }

    public boolean isRecordingEnabled() {
        return recordCheckBox.isSelected();
    }

    public void enableReplayControls(boolean enable) {
        startButton.setDisable(!enable);
        stopButton.setDisable(!enable);
    }

    public void setStartButtonText(String text) {
        startButton.setText(text);
    }

    public void setNewGameHandler(Runnable handler) {
        newGameButton.setOnAction(e -> handler.run());
    }

    public void setReplayHandler(Runnable handler) {
        replayButton.setOnAction(e -> handler.run());
    }

    public void setStartHandler(Runnable handler) {
        startButton.setOnAction(e -> handler.run());
    }

    public void setStopHandler(Runnable handler) {
        stopButton.setOnAction(e -> handler.run());
    }

    public void setModeChangeHandler(Runnable handler) {
        simpleButton.setOnAction(e -> handler.run());
        generalButton.setOnAction(e -> handler.run());
    }

    public void setSizeChangeHandler(Runnable handler) {
        sizeBox.setOnAction(e -> handler.run());
    }

    public void setPlayerTypeChangeHandler(Runnable handler) {
        blueHumanButton.setOnAction(e -> handler.run());
        blueComputerButton.setOnAction(e -> handler.run());
        redHumanButton.setOnAction(e -> handler.run());
        redComputerButton.setOnAction(e -> handler.run());
    }

    public void setRecordingChangeHandler(Consumer<Boolean> handler) {
        recordCheckBox.setOnAction(e -> handler.accept(recordCheckBox.isSelected()));
    }
}