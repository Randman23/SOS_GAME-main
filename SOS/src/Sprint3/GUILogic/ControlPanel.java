package Sprint3.GUILogic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;
import java.awt.*;

/**
 * Control panel for game mode and board size selection.
 */
public class ControlPanel extends HBox {
    private final RadioButton simpleButton;
    private final RadioButton generalButton;
    private final ComboBox<Integer> sizeBox;
    private final Button newGameButton;
    private final CheckBox recordButton;

    public ControlPanel() {
        Label titleLabel = new Label("SOS");
        titleLabel.setFont(Font.font("TimesNewRoman", FontWeight.BOLD, 24));

        simpleButton = new RadioButton("Simple game");
        generalButton = new RadioButton("General game");
        recordButton = new CheckBox("Record");

        ToggleGroup modeGroup = new ToggleGroup();
        simpleButton.setToggleGroup(modeGroup);
        generalButton.setToggleGroup(modeGroup);
        simpleButton.setSelected(true);

        HBox modeBox = new HBox(10, simpleButton, generalButton);
        modeBox.setAlignment(Pos.CENTER_LEFT);

        Label sizeLabel = new Label("Board size");
        sizeBox = new ComboBox<>();
        for (int i = 3; i <= 10; i++) {
            sizeBox.getItems().add(i);
        }
        sizeBox.setValue(5);

        HBox sizeSelection = new HBox(10, sizeLabel, sizeBox,  recordButton);
        sizeSelection.setAlignment(Pos.CENTER_LEFT);

        newGameButton = new Button("New game");

        getChildren().addAll(titleLabel, modeBox, sizeSelection, newGameButton);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(30);
        setPadding(new Insets(10));
    }

    public boolean isSimpleMode() {
        return simpleButton.isSelected();
    }

    public int getSelectedSize() {
        return sizeBox.getValue();
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
}