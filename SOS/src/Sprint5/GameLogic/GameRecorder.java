package Sprint5.GameLogic;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles recording and saving game moves to files.
 */
public class GameRecorder {
    private List<GameMoveRecord> moves;
    private String gameMode;
    private int boardSize;
    private String bluePlayerType;
    private String redPlayerType;
    private boolean recording;

    public GameRecorder() {
        this.moves = new ArrayList<>();
        this.recording = false;
    }

    public void startRecording(String gameMode, int boardSize,
                               String bluePlayerType, String redPlayerType) {
        this.gameMode = gameMode;
        this.boardSize = boardSize;
        this.bluePlayerType = bluePlayerType;
        this.redPlayerType = redPlayerType;
        this.moves.clear();
        this.recording = true;
        System.out.println("Recording started: " + gameMode + " game, " + boardSize + "x" + boardSize);
    }

    public void stopRecording() {
        this.recording = false;
        System.out.println("Recording stopped. Total moves: " + moves.size());
    }

    public void recordMove(int row, int col, char letter, String playerName) {
        if (!recording) {
            return;
        }

        int moveNumber = moves.size() + 1;
        GameMoveRecord move = new GameMoveRecord(moveNumber, row, col, letter, playerName);
        moves.add(move);
        System.out.println("Recorded move " + moveNumber + ": " + move);
    }

    public boolean isRecording() {
        return recording;
    }

    public List<GameMoveRecord> getMoves() {
        return new ArrayList<>(moves);
    }

    /**
     * Save the recorded game to a file.
     */
    public String saveToFile(String directory) throws IOException {
        if (moves.isEmpty()) {
            throw new IllegalStateException("No moves to save");
        }

        // Create filename with timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = gameMode + "_" + boardSize + "x" + boardSize + "_" + timestamp + ".txt";

        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write("GAME_MODE=" + gameMode);
            writer.newLine();
            writer.write("BOARD_SIZE=" + boardSize);
            writer.newLine();
            writer.write("BLUE_PLAYER=" + bluePlayerType);
            writer.newLine();
            writer.write("RED_PLAYER=" + redPlayerType);
            writer.newLine();
            writer.write("MOVES=" + moves.size());
            writer.newLine();
            writer.newLine();

            // Write moves header
            writer.write("MoveNumber,Row,Col,Letter,PlayerName");
            writer.newLine();

            // Write all moves
            for (GameMoveRecord move : moves) {
                writer.write(move.toString());
                writer.newLine();
            }
        }

        System.out.println("Game saved to: " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    /**
     * Load a game from a file.
     */
    public static GameRecording loadFromFile(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filepath);
        }

        GameRecording recording = new GameRecording();
        List<GameMoveRecord> loadedMoves = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean readingMoves = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Parse header
                if (line.startsWith("GAME_MODE=")) {
                    recording.gameMode = line.substring(10);
                } else if (line.startsWith("BOARD_SIZE=")) {
                    recording.boardSize = Integer.parseInt(line.substring(11));
                } else if (line.startsWith("BLUE_PLAYER=")) {
                    recording.bluePlayerType = line.substring(12);
                } else if (line.startsWith("RED_PLAYER=")) {
                    recording.redPlayerType = line.substring(11);
                } else if (line.startsWith("MOVES=")) {
                    // Skip this line, we'll count moves from actual data
                } else if (line.startsWith("MoveNumber,")) {
                    // Header line for moves
                    readingMoves = true;
                } else if (readingMoves) {
                    // Parse move
                    try {
                        GameMoveRecord move = GameMoveRecord.fromString(line);
                        loadedMoves.add(move);
                    } catch (Exception e) {
                        System.err.println("Error parsing move: " + line);
                        e.printStackTrace();
                    }
                }
            }
        }

        recording.moves = loadedMoves;
        System.out.println("Loaded game: " + recording.gameMode + ", " +
                recording.boardSize + "x" + recording.boardSize +
                ", " + loadedMoves.size() + " moves");

        return recording;
    }

    /**
     * Inner class to hold loaded game data.
     */
    public static class GameRecording {
        private String gameMode;
        private int boardSize;
        private String bluePlayerType;
        private String redPlayerType;
        private List<GameMoveRecord> moves;

        public String getGameMode() {
            return gameMode;
        }

        public int getBoardSize() {
            return boardSize;
        }

        public String getBluePlayerType() {
            return bluePlayerType;
        }

        public String getRedPlayerType() {
            return redPlayerType;
        }

        public List<GameMoveRecord> getMoves() {
            return moves;
        }
    }
}