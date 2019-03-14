package stepcondition;

import model.Cell;

/**
 * Interface designed to check validity and cost of a move (from the parent cell to its neighbour).
 */

public interface CellChecker {
    public boolean check(Cell neighbour, Cell parent, int finishType);
    public int computeStepCost(Cell neighbour,Cell parent);
}
