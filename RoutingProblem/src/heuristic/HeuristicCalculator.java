package heuristic;

public class HeuristicCalculator {

    public static int manhattanDistance(int startRow,int startCol,int endRow,int endCol)
    {
        return Math.abs(endRow-startRow)+Math.abs(endCol-startCol);

    }

}
