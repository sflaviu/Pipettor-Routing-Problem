package stepcondition;

import model.Cell;

public class ShortestRouteChecker implements CellChecker {

    public boolean check(Cell neighbour, Cell parent, int finishType) {
        if (neighbour.getType() != -1)
            return true;
        return false;
    }
    public int computeStepCost (Cell neighbour, Cell parent)
    {
        return 1;
    }
}
