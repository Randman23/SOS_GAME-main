package Sprint0;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


import java.util.ArrayList;
import java.util.List;

public class sosgui extends Application {

    private final double boxWidth = 300;
    private final double boxHeight = 300;

    private final int divisions = 5;  // Grid Size, maybe make dynamic

    private List<Line> gridLines = new ArrayList<>();
    private List<Text> letters = new ArrayList<>();

    private char currentLetterP1 = 'S'; // Set a default letter for players one and 2
    private char currentLetterP2 = 'S';

    private boolean isPlayerOneTurn = true;

    @Override
    public void start(Stage stage) {
        // initialize the viewing pane for the board
        Pane boardPane = new Pane();
        boardPane.setPrefSize(boxWidth , boxHeight );
        boardPane.setStyle("-fx-background-color: gray;");

        //make the board
        Rectangle box = new Rectangle(0, 0, boxWidth, boxHeight);
        box.setFill(Color.WHITE); //set the baord as white
        box.setStroke(Color.BLACK);
        box.setStrokeWidth(2);
        boardPane.getChildren().add(box);

        //create the label for player one
        Label player1Label = new Label("Player 1");
        player1Label.setFont(Font.font(16));
        // make radio buttons for s and o
        RadioButton p1SButton = new RadioButton("S");
        RadioButton p1OButton = new RadioButton("O");

        //format the buttons to one group
        ToggleGroup p1Group = new ToggleGroup();
        p1SButton.setToggleGroup(p1Group);
        p1OButton.setToggleGroup(p1Group);

        // Set default selection to S
        p1SButton.setSelected(true);

        // Change current letter when selection changes
        p1SButton.setOnAction(e -> currentLetterP1 = 'S');
        p1OButton.setOnAction(e -> currentLetterP1 = 'O');

        //Create the visual box for the player one grouping
        VBox leftButtons = new VBox(15, player1Label, p1SButton, p1OButton);
        leftButtons.setAlignment(Pos.TOP_CENTER);
        leftButtons.setStyle("-fx-background-color: RED; -fx-padding: 50;");

        //Controls for player 2
        Label player2Label = new Label("Player 2");
        player2Label.setFont(Font.font(16));

        RadioButton p2SButton = new RadioButton("S");
        RadioButton p2OButton = new RadioButton("O");

        // Group Player 2 radio buttons together
        ToggleGroup p2Group = new ToggleGroup();
        p2SButton.setToggleGroup(p2Group);
        p2OButton.setToggleGroup(p2Group);

        // Set default selection to S
        p2SButton.setSelected(true);

        // Change current letter when selection changes
        p2SButton.setOnAction(e -> currentLetterP2 = 'S');
        p2OButton.setOnAction(e -> currentLetterP2 = 'O');

        // Reset button to clear board
        Button resetButton = new Button("Reset Board");
        resetButton.setPrefWidth(100);
        resetButton.setOnAction(e -> resetBoard(boardPane));


        //add the visual box for the player 2 label and buttns
        VBox rightButtons = new VBox(15, player2Label, p2SButton, p2OButton);
        rightButtons.setAlignment(Pos.TOP_CENTER);
        rightButtons.setStyle("-fx-background-color: BLUE; -fx-padding: 50;");


        //Create a check box to record the game
        CheckBox recordGame = new CheckBox("Record game");

        //Make the bottom layout having the recoded game, reset button
        HBox bottomLayout = new HBox(15, recordGame, resetButton);
        bottomLayout.setAlignment(Pos.BOTTOM_CENTER);
        bottomLayout.setStyle("-fx-padding: 15;");

        //Capture the mouse when it is on the board
        boardPane.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            // Only allow clicks inside the board
            if (mouseX >= 0 && mouseX <= boxWidth &&
                    mouseY >= 0 && mouseY <= boxHeight) {

                double cellWidth = boxWidth / divisions;
                double cellHeight = boxHeight / divisions;

                //Find the rows and collums that were clicked on
                int col = (int) (mouseX / cellWidth);
                int row = (int) (mouseY / cellHeight);

                // Center of the clicked cell
                double centerX = col * cellWidth + cellWidth / 2;
                double centerY = row * cellHeight + cellHeight / 2;

                // Prevent duplicate letters in the same cell
                for (Text letter : letters) {
                    if (Math.abs(letter.getX() - (centerX - 7)) < 1 &&
                            Math.abs(letter.getY() - (centerY + 7)) < 1) {
                        return;
                    }
                }

                // Select current letter based on player's turn
                char selectedLetter = isPlayerOneTurn ? currentLetterP1 : currentLetterP2;

                // Create and place the letter
                Text letter = new Text(centerX - 7, centerY + 7, String.valueOf(selectedLetter));
                letter.setFont(Font.font(24));
                letter.setFill(Color.BLUE);
                letters.add(letter);
                boardPane.getChildren().add(letter);

                // Switch turns
                isPlayerOneTurn = !isPlayerOneTurn;
            }
        });

        // Draw the grid inside the board
        drawGrid(boardPane);

        // create the layout of the board, left buttons, board in center, right buttons
        HBox centerLayout = new HBox(40,  leftButtons, boardPane, rightButtons);
        centerLayout.setAlignment(Pos.CENTER);


        // Create the pane for the GUI
        BorderPane root = new BorderPane();
        root.setCenter(centerLayout);
        root.setBottom(bottomLayout);

        //add the final scene
        Scene scene = new Scene(root, 700, 350);
        stage.setTitle("SOS Game Board");
        stage.setScene(scene);
        stage.show();
    }

    // draw the rows and collums on the grids
    private void drawGrid(Pane boardPane) {
        double rowHeight = boxHeight / divisions;
        double colWidth = boxWidth / divisions;

        // iteration for the horizontal grid lines
        for (int i = 1; i < divisions; i++) {
            double y = i * rowHeight;
            Line line = new Line(0, y, boxWidth, y);
            line.setStroke(Color.LIGHTGRAY);
            gridLines.add(line);
        }

        // iteration for the vertical grid lines
        for (int i = 1; i < divisions; i++) {
            double x = i * colWidth;
            Line line = new Line(x, 0, x, boxHeight);
            line.setStroke(Color.LIGHTGRAY);
            gridLines.add(line);
        }

        boardPane.getChildren().addAll(gridLines);
    }

    // clears all of the letters from the board
    private void resetBoard(Pane boardPane) {
        boardPane.getChildren().removeAll(letters);
        letters.clear();
        isPlayerOneTurn = true; // Reset turn to Player 1
    }

    public static void main(String[] args) {
        launch(args);
    }
}
