package stepcondition;

import model.Cell;
import model.HeightRange;

public class HeightChecker implements CellChecker {
    public boolean check(Cell neighbour, Cell parent, int finishType)
    {
        if (neighbour.getType() != -1)
            return true;
        return false;
    }

    public int computeStepCost (Cell neighbour, Cell parent)
    {
        int heightCost;

        HeightRange parentRange=parent.getHr();
        int neighbourHeight=neighbour.getType();

        if(compatibleHeights(parentRange,neighbourHeight))
            heightCost=0;
        else
        {
            if(neighbourHeight<parentRange.getHeightLow())
                heightCost=parentRange.getHeightLow()-neighbourHeight;
            else
                heightCost=neighbourHeight-parentRange.getHeightHigh();
        }
        return 1+heightCost;
    }
    private boolean compatibleHeights(HeightRange parentRange,int neighbourHeight)
    {
        if(parentRange.getHeightLow() <= neighbourHeight && neighbourHeight <= parentRange.getHeightHigh() )
            return true;
        return false;
    }
}
