package Sprint4.GUILogic;

import Sprint4.GameLogic.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Displays current turn and game status messages.
 */
public class StatusPanel extends VBox {
    private final Label turnLabel;
    private final Label statusLabel;

    public StatusPanel() {
        turnLabel = new Label("Current turn: Blue");
        turnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        statusLabel = new Label("");
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        statusLabel.setStyle("-fx-text-fill: green;");

        getChildren().addAll(turnLabel, statusLabel);
        setAlignment(Pos.CENTER);
        setSpacing(5);
        setPadding(new Insets(10));
    }

    public void updateTurn(Player player) {
        turnLabel.setText("Current turn: " + player.getName());
    }

    public void showGameOver(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    public void clearStatus() {
        statusLabel.setText("");
    }
}