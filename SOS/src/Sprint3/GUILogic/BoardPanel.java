package Sprint3.GUILogic;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import Sprint3.GameLogic.*;

/**
 * Displays the game board grid.
 */
public class BoardPanel extends Pane {
    private static final double CELL_SIZE = 60;
    private final Game game;
    private Text[][] letterTexts;
    private ClickHandler clickHandler;

    public BoardPanel(Game game) {
        this.game = game;
        int size = game.getBoard().getSize();

        setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 2;");

        // Set both preferred and actual size
        double boardSize = size * CELL_SIZE;
        setPrefSize(boardSize, boardSize);
        setMinSize(boardSize, boardSize);
        setMaxSize(boardSize, boardSize);

        letterTexts = new Text[size][size];
        setupMouseHandler();
        redraw();
    }

    private void setupMouseHandler() {
        setOnMouseClicked(event -> {
            if (clickHandler != null) {
                int size = game.getBoard().getSize();
                int col = (int)(event.getX() / CELL_SIZE);
                int row = (int)(event.getY() / CELL_SIZE);

                if (row >= 0 && row < size && col >= 0 && col < size) {
                    clickHandler.onCellClick(row, col);
                }
            }
        });
    }

    public void redraw() {
        getChildren().clear();

        // Update size when redrawing
        int size = game.getBoard().getSize();
        double boardSize = size * CELL_SIZE;
        setPrefSize(boardSize, boardSize);
        setMinSize(boardSize, boardSize);
        setMaxSize(boardSize, boardSize);

        drawGrid();
        drawLetters();
        drawSOSLines();
    }

    private void drawGrid() {
        int size = game.getBoard().getSize();
        double boardSize = size * CELL_SIZE;

        for (int i = 0; i <= size; i++) {
            Line horizontal = new Line(0, i * CELL_SIZE, boardSize, i * CELL_SIZE);
            Line vertical = new Line(i * CELL_SIZE, 0, i * CELL_SIZE, boardSize);

            horizontal.setStroke(Color.GRAY);
            vertical.setStroke(Color.GRAY);

            getChildren().addAll(horizontal, vertical);
        }
    }

    private void drawLetters() {
        Board board = game.getBoard();
        int size = board.getSize();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Cell cell = board.getCell(row, col);
                if (!cell.isEmpty()) {
                    drawLetter(row, col, cell.getContent());
                }
            }
        }
    }

    private void drawLetter(int row, int col, char letter) {
        double centerX = col * CELL_SIZE + CELL_SIZE / 2;
        double centerY = row * CELL_SIZE + CELL_SIZE / 2;

        Text text = new Text(String.valueOf(letter));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        text.setFill(Color.BLACK);

        // Center the text properly
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();

        text.setX(centerX - textWidth / 2);
        text.setY(centerY + textHeight / 4);

        letterTexts[row][col] = text;
        getChildren().add(text);
    }

    private void drawSOSLines() {
        for (SOSSequence seq : game.getAllSequences()) {
            double x1 = seq.getStartCol() * CELL_SIZE + CELL_SIZE / 2;
            double y1 = seq.getStartRow() * CELL_SIZE + CELL_SIZE / 2;
            double x2 = seq.getEndCol() * CELL_SIZE + CELL_SIZE / 2;
            double y2 = seq.getEndRow() * CELL_SIZE + CELL_SIZE / 2;

            Line line = new Line(x1, y1, x2, y2);
            line.setStroke(seq.getPlayer().getColor());
            line.setStrokeWidth(3);

            getChildren().add(line);
        }
    }

    public void setClickHandler(ClickHandler handler) {
        this.clickHandler = handler;
    }

    public interface ClickHandler {
        void onCellClick(int row, int col);
    }
}