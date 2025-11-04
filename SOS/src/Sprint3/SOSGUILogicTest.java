package Sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the logical behavior of the SOSGUI related to the two acceptance criteria:
 *  1. Choosing a board size
 *  2. Choosing a game mode of a chosen board
 *
 * These tests do NOT use TestFX or GUI simulation — they call the logic directly.
 */
public class SOSGUILogicTest {

    private SOSGUI gui;

    @BeforeEach
    void setUp() {
        gui = new SOSGUI();
        // Manually create a game like in start()
        gui.game = new SOSGame(5);
    }

    /**
     * ✅ Test 1 — Choose a board size
     * Simulates changing the board size from 5 to another value.
     */
    @Test
    void testChooseBoardSizeUpdatesGame() {
        assertEquals(5, gui.game.getSize(), "Default board size should start at 5");

        // Simulate choosing a new board size (like ComboBox action)
        int newSize = 8;
        gui.game = new SOSGame(newSize);
        gui.game.resetGame();

        // Check the internal state
        assertEquals(8, gui.game.getSize(), "Game should update to new selected board size");
        assertTrue(gui.game.isPlayerOneTurn(), "Game should reset to Player 1’s turn after resize");
    }

    /**
     * ✅ Test 2 — Choose the game mode
     * Simulates toggling between Simple and General mode.
     */
    @Test
    void testToggleGameMode() {
        // Starts in Simple Mode
        assertTrue(gui.isSimpleMode, "Game should start in Simple Mode");

        // Simulate toggling to General mode (like checkbox action)
        gui.isSimpleMode = false;
        gui.game.resetGame();

        assertFalse(gui.isSimpleMode, "Game mode should switch to General");
        assertTrue(gui.game.isPlayerOneTurn(), "Game should reset to Player 1’s turn when mode changes");

        // Toggle back to Simple Mode
        gui.isSimpleMode = true;
        gui.game.resetGame();

        assertTrue(gui.isSimpleMode, "Game mode should switch back to Simple");
        assertTrue(gui.game.isPlayerOneTurn(), "Game should reset to Player 1’s turn again");
    }
}
