package Sprint2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SOSGame class.
 * These tests verify core functionality without GUI involvement.
 */
public class SOSGameTest {

    private SOSGame game;

    @BeforeEach
    public void setup() {
        game = new SOSGame(3);  // Default 3x3 board
    }

    @Test
    public void testInitialBoardIsEmpty() {
        char[][] board = game.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals('\0', board[i][j],
                        "Each cell should start empty");
            }
        }
    }

    @Test
    public void testPlaceLetterSuccess() {
        assertTrue(game.placeLetter(1, 1, 'S'),
                "Should be able to place S in an empty cell");
        assertEquals('S', game.getBoard()[1][1],
                "Board should contain S after placement");
    }

    @Test
    public void testCannotPlaceLetterInSameCell() {
        assertTrue(game.placeLetter(0, 0, 'S'));
        assertFalse(game.placeLetter(0, 0, 'O'),
                "Should not allow overwriting an existing cell");
        assertEquals('S', game.getBoard()[0][0]);
    }

    @Test
    public void testTurnSwitchesAfterValidMove() {
        boolean firstTurn = game.isPlayerOneTurn();
        game.placeLetter(1, 1, 'O');
        assertNotEquals(firstTurn, game.isPlayerOneTurn(),
                "Turn should switch after a valid move");
    }

    @Test
    public void testInvalidMoveDoesNotSwitchTurn() {
        game.placeLetter(0, 0, 'S');
        boolean turnBefore = game.isPlayerOneTurn();
        game.placeLetter(0, 0, 'O');  // invalid move
        assertEquals(turnBefore, game.isPlayerOneTurn(),
                "Invalid move should not switch turn");
    }

    @Test
    public void testResetClearsBoard() {
        game.placeLetter(0, 0, 'S');
        game.placeLetter(1, 1, 'O');
        game.resetGame();

        char[][] board = game.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals('\0', board[i][j],
                        "Reset should clear all cells");
            }
        }
    }

    @Test
    public void testBoardSizeSetting() {
        game = new SOSGame(5);
        assertEquals(5, game.getBoard().length);
        assertEquals(5, game.getBoard()[0].length);
    }

    @Test
    public void testPlayerOneStartsFirst() {
        assertTrue(game.isPlayerOneTurn(),
                "Player one should always start first");
    }
}
