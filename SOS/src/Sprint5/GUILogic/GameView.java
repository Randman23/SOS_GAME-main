package Sprint5.GUILogic;

import Sprint5.GameLogic.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Main view that assembles all GUI components.
 */
public class GameView extends BorderPane {
    private Game game;
    private BoardPanel boardPanel;
    private PlayerPanel bluePanel;
    private PlayerPanel redPanel;
    private ControlPanel controlPanel;
    private StatusPanel statusPanel;
    private BoardPanel.ClickHandler clickHandler;
    private HBox centerSection;

    public GameView(Game initialGame) {
        this.game = initialGame;
        buildUI();
    }

    private void buildUI() {
        controlPanel = new ControlPanel();
        statusPanel = new StatusPanel();
        boardPanel = new BoardPanel(game);

        bluePanel = new PlayerPanel(game.getBluePlayer());
        redPanel = new PlayerPanel(game.getRedPlayer());

        centerSection = new HBox(20);
        centerSection.getChildren().addAll(bluePanel, boardPanel, redPanel);
        centerSection.setAlignment(Pos.CENTER);
        centerSection.setPadding(new Insets(20));

        HBox.setHgrow(boardPanel, Priority.NEVER);
        HBox.setHgrow(bluePanel, Priority.NEVER);
        HBox.setHgrow(redPanel, Priority.NEVER);

        setTop(controlPanel);
        setCenter(centerSection);
        setBottom(statusPanel);
        setPadding(new Insets(15));

        updateTurn();
    }

    public void setGame(Game newGame) {
        this.game = newGame;

        centerSection.getChildren().remove(boardPanel);

        boardPanel = new BoardPanel(game);
        HBox.setHgrow(boardPanel, Priority.NEVER);

        if (clickHandler != null) {
            boardPanel.setClickHandler(clickHandler);
        }

        centerSection.getChildren().add(1, boardPanel);

        centerSection.getChildren().remove(bluePanel);
        centerSection.getChildren().remove(redPanel);

        bluePanel = new PlayerPanel(game.getBluePlayer());
        redPanel = new PlayerPanel(game.getRedPlayer());

        HBox.setHgrow(bluePanel, Priority.NEVER);
        HBox.setHgrow(redPanel, Priority.NEVER);

        centerSection.getChildren().add(0, bluePanel);
        centerSection.getChildren().add(redPanel);
    }

    public void updatePlayerScores() {
        bluePanel.updateScore();
        redPanel.updateScore();
    }

    public void updateTurn() {
        statusPanel.updateTurn(game.getCurrentPlayer());
    }

    public void updatePlayerPanels() {
        bluePanel.updatePlayerTypeDisplay();
        redPanel.updatePlayerTypeDisplay();
    }

    public void resetView() {
        boardPanel.redraw();
        bluePanel.updateScore();
        redPanel.updateScore();
        bluePanel.resetSelection();
        redPanel.resetSelection();
        bluePanel.updatePlayerTypeDisplay();
        redPanel.updatePlayerTypeDisplay();
        statusPanel.clearStatus();
        statusPanel.updateTurn(game.getCurrentPlayer());
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    public void setClickHandler(BoardPanel.ClickHandler handler) {
        this.clickHandler = handler;
        boardPanel.setClickHandler(handler);
    }
}