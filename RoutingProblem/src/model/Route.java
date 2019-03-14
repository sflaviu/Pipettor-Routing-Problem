package model;

public class Route {

    private int index;
    private int startRow,startCol;
    private int endRow, endCol;

    public Route(int index, int startRow, int startCol, int endRow, int endCol) {
        this.index = index;
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public int getIndex() {
        return index;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }
}
