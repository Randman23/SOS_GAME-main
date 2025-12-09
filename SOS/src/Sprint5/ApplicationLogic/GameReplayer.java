package Sprint5.ApplicationLogic;

import Sprint5.GameLogic.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.List;

/**
 * Handles replaying recorded games with animation.
 */
public class GameReplayer {
    private Game game;
    private List<GameMoveRecord> moves;
    private int currentMoveIndex;
    private boolean playing;
    private double playbackSpeed; // seconds between moves
    private ReplayCallback callback;

    public GameReplayer() {
        this.currentMoveIndex = 0;
        this.playing = false;
        this.playbackSpeed = 1.0; // 1 second default
    }

    public void loadRecording(GameRecorder.GameRecording recording) {
        this.moves = recording.getMoves();
        this.currentMoveIndex = 0;

        // Create appropriate game instance
        int size = recording.getBoardSize();
        if (recording.getGameMode().equalsIgnoreCase("Simple")) {
            this.game = new SimpleGame(size);
        } else {
            this.game = new GeneralGame(size);
        }

        System.out.println("Replay loaded: " + moves.size() + " moves");
    }

    public void setCallback(ReplayCallback callback) {
        this.callback = callback;
    }

    public void setPlaybackSpeed(double seconds) {
        this.playbackSpeed = seconds;
    }

    public void play() {
        if (moves == null || moves.isEmpty()) {
            System.out.println("No moves to replay");
            return;
        }

        playing = true;
        playNextMove();
    }

    public void pause() {
        playing = false;
        System.out.println("Replay paused at move " + currentMoveIndex);
    }

    public void stop() {
        playing = false;
        currentMoveIndex = 0;
        game.reset();

        if (callback != null) {
            callback.onReplayUpdate();
        }

        System.out.println("Replay stopped and reset");
    }

    public void stepForward() {
        if (currentMoveIndex < moves.size()) {
            executeMove(moves.get(currentMoveIndex));
            currentMoveIndex++;

            if (callback != null) {
                callback.onReplayUpdate();
            }
        }
    }

    public void stepBackward() {
        if (currentMoveIndex > 0) {
            // Reset game and replay up to previous move
            game.reset();
            currentMoveIndex--;

            for (int i = 0; i < currentMoveIndex; i++) {
                executeMove(moves.get(i));
            }

            if (callback != null) {
                callback.onReplayUpdate();
            }
        }
    }

    private void playNextMove() {
        if (!playing || currentMoveIndex >= moves.size()) {
            if (currentMoveIndex >= moves.size()) {
                System.out.println("Replay completed");
                playing = false;

                if (callback != null) {
                    callback.onReplayComplete();
                }
            }
            return;
        }

        // Execute current move
        GameMoveRecord move = moves.get(currentMoveIndex);
        executeMove(move);
        currentMoveIndex++;

        // Notify callback
        if (callback != null) {
            callback.onReplayUpdate();
        }

        // Schedule next move
        PauseTransition pause = new PauseTransition(Duration.seconds(playbackSpeed));
        pause.setOnFinished(e -> playNextMove());
        pause.play();
    }

    private void executeMove(GameMoveRecord move) {
        Player player = game.getCurrentPlayer();
        player.setSelectedLetter(move.getLetter());
        game.makeMove(move.getRow(), move.getCol());

        System.out.println("Replayed move " + move.getMoveNumber() + ": " +
                move.getPlayerName() + " placed " + move.getLetter() +
                " at (" + move.getRow() + "," + move.getCol() + ")");
    }

    public Game getGame() {
        return game;
    }

    public boolean isPlaying() {
        return playing;
    }

    public int getCurrentMoveIndex() {
        return currentMoveIndex;
    }

    public int getTotalMoves() {
        return moves != null ? moves.size() : 0;
    }

    /**
     * Callback interface for replay events.
     */
    public interface ReplayCallback {
        void onReplayUpdate();
        void onReplayComplete();
    }
}