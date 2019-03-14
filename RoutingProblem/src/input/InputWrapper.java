package input;

import model.Cell;
import model.Route;

public class InputWrapper {

    private int nrRows,nrCols;
    private Cell[][] area;
    private int nrRoutes;
    private Route[] routes;

    public InputWrapper(int nrRows, int nrCols, Cell[][] area, int nrRoutes, Route[] routes) {
        this.nrRows = nrRows;
        this.nrCols = nrCols;
        this.area = area;
        this.nrRoutes = nrRoutes;
        this.routes = routes;
    }

    public int getNrRows() {
        return nrRows;
    }

    public int getNrCols() {
        return nrCols;
    }

    public Cell[][] getArea() {
        return area;
    }

    public int getNrRoutes() {
        return nrRoutes;
    }

    public Route[] getRoutes() {
        return routes;
    }
}
