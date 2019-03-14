package model;

public class Pipettor {
    private int row;
    private int col;
    private int height;


    public Pipettor(int row, int col,int height) {
        this.row = row;

        this.col = col;
        this.height=height;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
