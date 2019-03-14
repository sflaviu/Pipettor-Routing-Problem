package model;

/**
 *  The Cell class has information about the height of a cell, it's position, the costs associated to reach it in a specific route and
 * its parent in a route.
 *
 *   The visited and inClosed integer fields correspond to the route that is being computed (if a cell's visited field is set to x,
 *  that means that route x has considered that cell at a certain point).
 *
 *   It implements comparable in order to order cells based on their approximate cost to reach the target.
 */
public class Cell implements Comparable<Cell>{


    private int type;

    private int visited;
    private int inClosed;


    private int gCost;
    private int hCost;
    private int fCost;

    private int row;
    private int col;

    private String move;
    private Cell parent;

    private HeightRange hr;

    public Cell(int type,int row,int col)
    {
        this.type=type;
        this.visited=-1;

        this.row=row;
        this.col=col;
        initializeHr();

    }
    @Override
    public int compareTo(Cell o) {

        if(o==this)
            return 0;
        if(this.getFCost()>o.getFCost())
            return 1;
        return -1;

    }

    public void updateCost(int newGCost)
    {
        this.gCost=newGCost;
        this.fCost=this.hCost+newGCost;
    }
    public int getInClosed() {
        return inClosed;
    }

    public void setInClosed(int inClosed) {
        this.inClosed = inClosed;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int cost) {
        this.gCost = cost;
    }


    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int cost) {
        this.fCost = cost;
    }
    public boolean isVisited(int route) {
        return visited==route;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }

    public int getType() {
        return type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public HeightRange getHr() {
        return hr;
    }

    public void initializeHr() {
        this.hr=new HeightRange(type,type);
    }
    public void updateHr(HeightRange hr) {

        int newLow=hr.getHeightLow()-1;
        int newHigh=hr.getHeightHigh()+1;
        if(type>newLow)
            newLow=type;
        if(newHigh<type)
            newHigh=type;
        this.hr.setHeightLow(newLow);
        this.hr.setHeightHigh(newHigh);
    }
}
