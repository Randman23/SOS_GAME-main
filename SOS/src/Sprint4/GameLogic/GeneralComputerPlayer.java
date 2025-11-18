package Sprint4.GameLogic;

import Sprint4.GameLogic.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Computer player strategy for General game mode.
 * Tries to maximize SOS sequences created.
 */
public class GeneralComputerPlayer extends ComputerPlayer {

    @Override
    public Move makeMove(Game game, Player player) {
        List<Move> availableMoves = getAvailableMoves(game);

        if (availableMoves.isEmpty()) {
            return null;
        }

        // Strategy 1: Find move that creates most SOS sequences
        Move bestScoringMove = findBestScoringMove(game, player, availableMoves);
        if (bestScoringMove != null) {
            System.out.println("Computer making scoring move: " + bestScoringMove);
            return bestScoringMove;
        }

        // Strategy 2: Set up future SOS opportunities
        Move setupMove = findSetupMove(game, availableMoves);
        if (setupMove != null) {
            System.out.println("Computer setting up future SOS: " + setupMove);
            return setupMove;
        }

        // Strategy 3: Random move with variety
        List<Move> shuffledMoves = new ArrayList<>(availableMoves);
        java.util.Collections.shuffle(shuffledMoves, random);
        Move randomMove = shuffledMoves.get(0);
        System.out.println("Computer making random move: " + randomMove);
        return randomMove;
    }

    private Move findBestScoringMove(Game game, Player player, List<Move> moves) {
        Move bestMove = null;
        int bestScore = 0;

        // Check all possible moves
        for (Move move : moves) {
            int score = evaluateMove(game, move, player);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove; // Returns null if no scoring moves
    }

    private Move findSetupMove(Game game, List<Move> moves) {
        // Shuffle to add variety
        List<Move> shuffledMoves = new ArrayList<>(moves);
        java.util.Collections.shuffle(shuffledMoves, random);

        // Look for moves that create S-O or O-S patterns
        for (Move move : shuffledMoves) {
            if (createsPartialPattern(game, move)) {
                return move;
            }
        }
        return null;
    }

    private boolean createsPartialPattern(Game game, Move move) {
        Board board = game.getBoard();
        int row = move.getRow();
        int col = move.getCol();
        char letter = move.getLetter();

        // Check all 8 directions for complementary letters
        int[][] directions = {
                {-1, 0}, {-1, 1}, {0, 1}, {1, 1},
                {1, 0}, {1, -1}, {0, -1}, {-1, -1}
        };

        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];

            if (board.isValidPosition(adjRow, adjCol)) {
                Cell adjCell = board.getCell(adjRow, adjCol);

                // Check for S-O or O-S patterns
                if (letter == 'S' && adjCell.getContent() == 'O') {
                    return true;
                }
                if (letter == 'O' && adjCell.getContent() == 'S') {
                    return true;
                }
            }
        }

        return false;
    }
}