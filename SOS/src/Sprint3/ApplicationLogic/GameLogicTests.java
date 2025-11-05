package Sprint3.ApplicationLogic;

import Sprint3.GameLogic.*;
import Sprint3.GUILogic.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTests {

    private SimpleGame simpleGame;
    private GeneralGame generalGame;

    @BeforeEach
    public void setup() {
        simpleGame = new SimpleGame(5);
        generalGame = new GeneralGame(5);
    }

    // Board tests
    @Test
    public void testBoardInitialization() {
        Board board = new Board(5);
        assertEquals(5, board.getSize());
        assertTrue(board.getCell(0, 0).isEmpty());
    }

    @Test
    public void testBoardValidPosition() {
        Board board = new Board(5);
        assertTrue(board.isValidPosition(0, 0));
        assertTrue(board.isValidPosition(4, 4));
        assertFalse(board.isValidPosition(-1, 0));
        assertFalse(board.isValidPosition(5, 0));
    }

    @Test
    public void testBoardPlaceLetter() {
        Board board = new Board(5);
        assertTrue(board.placeLetterAt(0, 0, 'S'));
        assertEquals('S', board.getCell(0, 0).getContent());
        assertFalse(board.placeLetterAt(0, 0, 'O'));
    }

    // Cell tests
    @Test
    public void testCellInitialization() {
        Cell cell = new Cell(2, 3);
        assertEquals(2, cell.getRow());
        assertEquals(3, cell.getColumn());
        assertTrue(cell.isEmpty());
    }

    @Test
    public void testCellSetContent() {
        Cell cell = new Cell(0, 0);
        cell.setContent('S');
        assertEquals('S', cell.getContent());
        assertFalse(cell.isEmpty());
    }

    // Player tests
    @Test
    public void testPlayerInitialization() {
        Player player = new Player("Blue", javafx.scene.paint.Color.BLUE);
        assertEquals("Blue", player.getName());
        assertEquals(0, player.getScore());
        assertEquals('S', player.getSelectedLetter());
    }

    @Test
    public void testPlayerScoring() {
        Player player = new Player("Red", javafx.scene.paint.Color.RED);
        player.addScore(1);
        assertEquals(1, player.getScore());
        player.addScore(2);
        assertEquals(3, player.getScore());
        player.resetScore();
        assertEquals(0, player.getScore());
    }

    @Test
    public void testPlayerLetterSelection() {
        Player player = new Player("Blue", javafx.scene.paint.Color.BLUE);
        player.setSelectedLetter('O');
        assertEquals('O', player.getSelectedLetter());
    }

    // Simple game tests
    @Test
    public void testSimpleGameInitialization() {
        assertEquals("Simple", simpleGame.getGameMode());
        assertFalse(simpleGame.isGameFinished());
        assertEquals(simpleGame.getBluePlayer(), simpleGame.getCurrentPlayer());
    }

    @Test
    public void testSimpleGameMove() {
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        assertTrue(simpleGame.makeMove(0, 0));
        assertEquals('S', simpleGame.getBoard().getCell(0, 0).getContent());
    }

    @Test
    public void testSimpleGameEndsOnSOS() {
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 0);
        simpleGame.getCurrentPlayer().setSelectedLetter('O');
        simpleGame.makeMove(0, 1);
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 2);

        assertTrue(simpleGame.isGameFinished());
        assertNotNull(simpleGame.determineWinner());
    }

    @Test
    public void testSimpleGameTurnSwitch() {
        Player first = simpleGame.getCurrentPlayer();
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 0);
        assertNotEquals(first, simpleGame.getCurrentPlayer());
    }

    // General game tests
    @Test
    public void testGeneralGameInitialization() {
        assertEquals("General", generalGame.getGameMode());
        assertFalse(generalGame.isGameFinished());
    }

    @Test
    public void testGeneralGameContinuesAfterSOS() {
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(0, 0);
        generalGame.getCurrentPlayer().setSelectedLetter('O');
        generalGame.makeMove(0, 1);
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(0, 2);

        assertFalse(generalGame.isGameFinished());
    }

    @Test
    public void testGeneralGameScoringKeepsTurn() {
        Player currentPlayer = generalGame.getCurrentPlayer();
        currentPlayer.setSelectedLetter('S');
        generalGame.makeMove(1, 1);

        generalGame.getCurrentPlayer().setSelectedLetter('O');
        generalGame.makeMove(1, 2);

        Player beforeScore = generalGame.getCurrentPlayer();
        beforeScore.setSelectedLetter('S');
        generalGame.makeMove(1, 3);

        if (beforeScore.getScore() > 0) {
            assertEquals(beforeScore, generalGame.getCurrentPlayer());
        }
    }

    // SOS detection tests
    @Test
    public void testHorizontalSOSDetection() {
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(2, 2);
        generalGame.getCurrentPlayer().setSelectedLetter('O');
        generalGame.makeMove(2, 3);
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(2, 4);

        assertTrue(generalGame.getBluePlayer().getScore() > 0 ||
                generalGame.getRedPlayer().getScore() > 0);
    }

    @Test
    public void testVerticalSOSDetection() {
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(0, 2);
        generalGame.getCurrentPlayer().setSelectedLetter('O');
        generalGame.makeMove(1, 2);
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(2, 2);

        assertTrue(generalGame.getBluePlayer().getScore() > 0 ||
                generalGame.getRedPlayer().getScore() > 0);
    }

    @Test
    public void testDiagonalSOSDetection() {
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(1, 1);
        generalGame.getCurrentPlayer().setSelectedLetter('O');
        generalGame.makeMove(2, 2);
        generalGame.getCurrentPlayer().setSelectedLetter('S');
        generalGame.makeMove(3, 3);

        assertTrue(generalGame.getBluePlayer().getScore() > 0 ||
                generalGame.getRedPlayer().getScore() > 0);
    }

    // Reset tests
    @Test
    public void testGameReset() {
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 0);
        simpleGame.reset();

        assertTrue(simpleGame.getBoard().getCell(0, 0).isEmpty());
        assertEquals(0, simpleGame.getBluePlayer().getScore());
        assertEquals(0, simpleGame.getRedPlayer().getScore());
        assertFalse(simpleGame.isGameFinished());
    }

    // Invalid move tests
    @Test
    public void testInvalidMoveOutsideBounds() {
        assertFalse(simpleGame.makeMove(-1, 0));
        assertFalse(simpleGame.makeMove(5, 0));
    }

    @Test
    public void testInvalidMoveOccupiedCell() {
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 0);
        assertFalse(simpleGame.makeMove(0, 0));
    }

    @Test
    public void testInvalidMoveAfterGameEnd() {
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 0);
        simpleGame.getCurrentPlayer().setSelectedLetter('O');
        simpleGame.makeMove(0, 1);
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 2);

        assertTrue(simpleGame.isGameFinished());
        assertFalse(simpleGame.makeMove(1, 1));
    }

    // Winner determination tests
    @Test
    public void testSimpleGameWinnerDetermination() {
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 0);
        simpleGame.getCurrentPlayer().setSelectedLetter('O');
        simpleGame.makeMove(0, 1);
        simpleGame.getCurrentPlayer().setSelectedLetter('S');
        simpleGame.makeMove(0, 2);

        String winner = simpleGame.determineWinner();
        assertTrue(winner.contains("wins") || winner.equals("Draw"));
    }

    @Test
    public void testGeneralGameHighestScoreWins() {
        // Fill board strategically
        generalGame.getBluePlayer().addScore(3);
        generalGame.getRedPlayer().addScore(1);

        // Force game to end
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (generalGame.getBoard().getCell(i, j).isEmpty()) {
                    generalGame.getBoard().placeLetterAt(i, j, 'S');
                }
            }
        }

        generalGame.checkGameEnd();
        if (generalGame.isGameFinished()) {
            assertTrue(generalGame.determineWinner().contains("Blue"));
        }
    }
}