package Sprint4.GUILogic;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import Sprint4.GameLogic.*;

/**
 * Panel for player controls and score display.
 */
public class PlayerPanel extends VBox {
    private final Player player;
    private final RadioButton sButton;
    private final RadioButton oButton;
    private final Label scoreLabel;
    private final Label playerTypeLabel;

    public PlayerPanel(Player player) {
        this.player = player;

        Label nameLabel = new Label(player.getName() + " player");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        playerTypeLabel = new Label("Human");
        playerTypeLabel.setFont(Font.font("Arial", 11));
        playerTypeLabel.setStyle("-fx-text-fill: gray;");

        sButton = new RadioButton("S");
        oButton = new RadioButton("O");

        ToggleGroup group = new ToggleGroup();
        sButton.setToggleGroup(group);
        oButton.setToggleGroup(group);
        sButton.setSelected(true);

        setupLetterSelection();

        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        getChildren().addAll(nameLabel, playerTypeLabel, sButton, oButton, scoreLabel);
        setAlignment(Pos.TOP_CENTER);
        setSpacing(15);
        setPadding(new Insets(20));
        setPrefWidth(150);

        stylePanel();
        updatePlayerTypeDisplay();
    }

    private void setupLetterSelection() {
        sButton.setOnAction(e -> {
            if (player.isHuman()) {
                player.setSelectedLetter('S');
            }
        });
        oButton.setOnAction(e -> {
            if (player.isHuman()) {
                player.setSelectedLetter('O');
            }
        });
    }

    private void stylePanel() {
        String colorHex = String.format("#%02x%02x%02x",
                (int)(player.getColor().getRed() * 255),
                (int)(player.getColor().getGreen() * 255),
                (int)(player.getColor().getBlue() * 255));

        String lightColor;
        if (player.getName().equals("Blue")) {
            lightColor = "#ddeeff";
        } else {
            lightColor = "#ffdddd";
        }

        setStyle(String.format(
                "-fx-background-color: %s; -fx-border-color: %s; -fx-border-width: 2;",
                lightColor, colorHex));
    }

    public void updateScore() {
        scoreLabel.setText("Score: " + player.getScore());
    }

    public void resetSelection() {
        sButton.setSelected(true);
        player.setSelectedLetter('S');
    }

    public void updatePlayerTypeDisplay() {
        if (player.isComputer()) {
            playerTypeLabel.setText("Computer");
            sButton.setDisable(true);
            oButton.setDisable(true);
        } else {
            playerTypeLabel.setText("Human");
            sButton.setDisable(false);
            oButton.setDisable(false);
        }
    }
}