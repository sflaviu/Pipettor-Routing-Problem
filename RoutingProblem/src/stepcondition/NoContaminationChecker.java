package stepcondition;

import model.Cell;

public class NoContaminationChecker implements CellChecker {

    public boolean check(Cell neighbour, Cell parent, int finishType)
    {
        if(neighbour.getType()==0 || neighbour.getType()==finishType)
            return true;
        return false;
    }

    public int computeStepCost (Cell neighbour, Cell parent)
    {
        return 1;
    }
}
