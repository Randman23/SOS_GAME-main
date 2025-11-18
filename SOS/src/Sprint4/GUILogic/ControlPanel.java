package Sprint4.GUILogic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import Sprint4.GameLogic.*;
/**
 * Control panel for game mode and board size selection.
 */
public class ControlPanel extends HBox {
    private final RadioButton simpleButton;
    private final RadioButton generalButton;
    private final ComboBox<Integer> sizeBox;
    private final Button newGameButton;

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

        // New game button
        newGameButton = new Button("New game");
        newGameButton.setStyle("-fx-font-weight: bold;");

        getChildren().addAll(titleLabel, modeBox, sizeSelection,
                bluePlayerBox, redPlayerBox, newGameButton);
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

    public void setNewGameHandler(Runnable handler) {
        newGameButton.setOnAction(e -> handler.run());
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
}