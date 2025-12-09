package Sprint5.GameLogic;

import javafx.scene.paint.Color;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String name;
    private final Color color;
    private int score;
    private char selectedLetter;
    private PlayerType playerType;
    private ComputerPlayer computerStrategy;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.score = 0;
        this.selectedLetter = 'S';
        this.playerType = PlayerType.HUMAN;
        this.computerStrategy = null;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public void resetScore() {
        score = 0;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char letter) {
        if (letter == 'S' || letter == 'O') {
            this.selectedLetter = letter;
        }
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerType type) {
        this.playerType = type;
    }

    public boolean isComputer() {
        return playerType == PlayerType.COMPUTER;
    }

    public boolean isHuman() {
        return playerType == PlayerType.HUMAN;
    }

    public ComputerPlayer getComputerStrategy() {
        return computerStrategy;
    }

    public void setComputerStrategy(ComputerPlayer strategy) {
        this.computerStrategy = strategy;
    }
}