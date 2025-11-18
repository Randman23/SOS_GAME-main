package Sprint4.GameLogic;

import Sprint4.GameLogic.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Computer player strategy for Simple game mode.
 * Tries to create SOS to win, or blocks opponent from winning.
 */
public class SimpleComputerPlayer extends ComputerPlayer {

    @Override
    public Move makeMove(Game game, Player player) {
        List<Move> availableMoves = getAvailableMoves(game);

        if (availableMoves.isEmpty()) {
            return null;
        }

        // Strategy 1: Try to win by creating an SOS
        Move winningMove = findWinningMove(game, player, availableMoves);
        if (winningMove != null) {
            System.out.println("Computer found winning move: " + winningMove);
            return winningMove;
        }

        // Strategy 2: Block opponent from winning
        Player opponent = (player == game.getBluePlayer()) ?
                game.getRedPlayer() : game.getBluePlayer();
        Move blockingMove = findWinningMove(game, opponent, availableMoves);
        if (blockingMove != null) {
            System.out.println("Computer blocking opponent: " + blockingMove);
            return blockingMove;
        }

        // Strategy 3: Make a safe move that doesn't set up opponent
        Move safeMove = findSafeMove(game, availableMoves, opponent);
        if (safeMove != null) {
            System.out.println("Computer making safe move: " + safeMove);
            return safeMove;
        }

        // Strategy 4: Random move (choose randomly between S and O)
        Move randomMove = availableMoves.get(random.nextInt(availableMoves.size()));
        System.out.println("Computer making random move: " + randomMove);
        return randomMove;
    }

    private Move findWinningMove(Game game, Player player, List<Move> moves) {
        // Try both S and O for each empty cell
        for (Move move : moves) {
            if (evaluateMove(game, move, player) > 0) {
                return move;
            }
        }
        return null;
    }

    private Move findSafeMove(Game game, List<Move> moves, Player opponent) {
        // Shuffle to avoid always picking same "safe" spot
        List<Move> shuffledMoves = new ArrayList<>(moves);
        java.util.Collections.shuffle(shuffledMoves, random);

        for (Move move : shuffledMoves) {
            // Check if this move would allow opponent to win next turn
            if (!createsOpportunityForOpponent(game, move, opponent)) {
                return move;
            }
        }
        return null; // No safe moves available
    }

    private boolean createsOpportunityForOpponent(Game game, Move move, Player opponent) {
        // Temporarily place the move
        Cell cell = game.getBoard().getCell(move.getRow(), move.getCol());
        char originalContent = cell.getContent();
        cell.setContent(move.getLetter());

        // Check if opponent can win on next move
        List<Move> opponentMoves = getAvailableMoves(game);
        boolean createsOpportunity = false;

        for (Move oppMove : opponentMoves) {
            if (evaluateMove(game, oppMove, opponent) > 0) {
                createsOpportunity = true;
                break;
            }
        }

        // Restore original state
        cell.setContent(originalContent);

        return createsOpportunity;
    }
}