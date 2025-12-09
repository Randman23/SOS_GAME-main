package Sprint5.GameLogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Detects SOS sequences on the game board.
 */
public class SOSDetector {
    private final Board board;
    private static final int[][] DIRECTIONS = {
            {-1, 0}, {-1, 1}, {0, 1}, {1, 1},
            {1, 0}, {1, -1}, {0, -1}, {-1, -1}
    };

    public SOSDetector(Board board) {
        this.board = board;
    }

    public List<SOSSequence> findSequencesForMove(int row, int col, Player player) {
        Set<String> uniqueSequences = new HashSet<>();
        List<SOSSequence> sequences = new ArrayList<>();
        Cell cell = board.getCell(row, col);

        if (cell == null) {
            return sequences;
        }

        char letter = cell.getContent();

        if (letter == 'S') {
            addUniqueSequences(checkAsStartingS(row, col, player), uniqueSequences, sequences);
            addUniqueSequences(checkAsEndingS(row, col, player), uniqueSequences, sequences);
        } else if (letter == 'O') {
            addUniqueSequences(checkAsMiddleO(row, col, player), uniqueSequences, sequences);
        }

        return sequences;
    }

    private void addUniqueSequences(List<SOSSequence> newSequences,
                                    Set<String> uniqueKeys,
                                    List<SOSSequence> resultList) {
        for (SOSSequence seq : newSequences) {
            // Create a unique key for this sequence using normalized coordinates
            // Always store with smaller coordinates first to avoid duplicates
            int minRow = Math.min(seq.getStartRow(), seq.getEndRow());
            int maxRow = Math.max(seq.getStartRow(), seq.getEndRow());
            int minCol = Math.min(seq.getStartCol(), seq.getEndCol());
            int maxCol = Math.max(seq.getStartCol(), seq.getEndCol());

            String key = minRow + "," + minCol + "-" + maxRow + "," + maxCol;

            if (!uniqueKeys.contains(key)) {
                uniqueKeys.add(key);
                resultList.add(seq);
            }
        }
    }

    private List<SOSSequence> checkAsStartingS(int row, int col, Player player) {
        List<SOSSequence> found = new ArrayList<>();

        // Check if this S is the START of an SOS sequence
        for (int[] dir : DIRECTIONS) {
            int midRow = row + dir[0];
            int midCol = col + dir[1];
            int endRow = row + 2 * dir[0];
            int endCol = col + 2 * dir[1];

            if (checkPattern(row, col, midRow, midCol, endRow, endCol, 'S', 'O', 'S')) {
                found.add(new SOSSequence(row, col, endRow, endCol, player));
            }
        }

        return found;
    }

    private List<SOSSequence> checkAsEndingS(int row, int col, Player player) {
        List<SOSSequence> found = new ArrayList<>();

        // Check if this S is the END of an SOS sequence
        for (int[] dir : DIRECTIONS) {
            int midRow = row - dir[0];
            int midCol = col - dir[1];
            int startRow = row - 2 * dir[0];
            int startCol = col - 2 * dir[1];

            if (checkPattern(startRow, startCol, midRow, midCol, row, col, 'S', 'O', 'S')) {
                found.add(new SOSSequence(startRow, startCol, row, col, player));
            }
        }

        return found;
    }

    private List<SOSSequence> checkAsMiddleO(int row, int col, Player player) {
        List<SOSSequence> found = new ArrayList<>();

        // Check if this O is the MIDDLE of SOS sequences
        for (int[] dir : DIRECTIONS) {
            int startRow = row - dir[0];
            int startCol = col - dir[1];
            int endRow = row + dir[0];
            int endCol = col + dir[1];

            if (checkPattern(startRow, startCol, row, col, endRow, endCol, 'S', 'O', 'S')) {
                found.add(new SOSSequence(startRow, startCol, endRow, endCol, player));
            }
        }

        return found;
    }

    private boolean checkPattern(int r1, int c1, int r2, int c2, int r3, int c3,
                                 char expected1, char expected2, char expected3) {
        Cell cell1 = board.getCell(r1, c1);
        Cell cell2 = board.getCell(r2, c2);
        Cell cell3 = board.getCell(r3, c3);

        if (cell1 == null || cell2 == null || cell3 == null) {
            return false;
        }

        return cell1.getContent() == expected1 &&
                cell2.getContent() == expected2 &&
                cell3.getContent() == expected3;
    }
}