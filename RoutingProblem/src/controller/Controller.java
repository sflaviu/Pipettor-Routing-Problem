package controller;

import input.InputReader;
import input.InputWrapper;
import model.Cell;
import view.RoutingGUI;
import view.ColorCellRenderer;
import routelogic.RouteManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A singleton controller class.
 */

public class Controller {

    private InputReader ir;
    private InputWrapper iw;
    private RouteManager rm;
    private RoutingGUI gui;

    private int row;
    private int col;
    private int height;
    private int currentIndex=0;
    private int pathNr=-1;

    private Timer timer;

    private static Controller c;

    private Controller(){}

    public static Controller getController()
    {
        if(c==null)
            c=new Controller();
        return c;
    }

    /**
     * Method that initializes the user interface with the read data.
     *
     * @param filename -file from which the input is read
     */
    public void start(String filename)
    {
        ir=new InputReader();
        iw=ir.read(filename);

        Cell[][] area=iw.getArea();

        rm=new RouteManager(iw.getRoutes(),area);

        int height=iw.getNrRows();
        int width=iw.getNrCols();

        Integer[][] tableData=getTableData(area,height,width);
        Integer[] columnHeader=getColumnHeader(width);

        ColorCellRenderer cl=new ColorCellRenderer();

        gui=new RoutingGUI(tableData,columnHeader,cl);

        BtnActionListener al=new BtnActionListener();
        gui.setStartBtnListener(al);

        gui.setVisible(true);

    }

    /**
     * A nested action listener class, designed for the start button.
     */
    private class BtnActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("btnStart")) {

                if(pathNr<iw.getNrRoutes()-1) {
                    clearMap();
                    pathNr++;

                    gui.updateRouteLabel(iw.getRoutes()[pathNr].getIndex());

                    int startRow=iw.getRoutes()[pathNr].getStartRow();
                    int startCol=iw.getRoutes()[pathNr].getStartCol();

                    gui.updateRouteStartLabel(startRow, startCol);

                    height=iw.getArea()[startRow][startCol].getType();

                    gui.updateRouteEndLabel(iw.getRoutes()[pathNr].getEndRow(), iw.getRoutes()[pathNr].getEndCol());

                    String condition=gui.getSelectedAction();
                    List<String> path = rm.solve(pathNr,condition);

                    System.out.println(iw.getRoutes()[pathNr].getIndex() + "\n" + path);
                    showPath(path, pathNr);
                }
                else
                    JOptionPane.showMessageDialog(null, "No more routes to show !");
            }
        }
    }

    /**
     *  Method that displays a route, based on the computed moves (uses an action listener that triggers on a timer).
     * @param a - a list of moves
     * @param routeNr - current route number
     */
    private void showPath(List<String> a, int routeNr)
    {
        gui.redrawCell(row,col,iw.getArea()[row][col].getType());

        row=iw.getRoutes()[routeNr].getStartRow();
        col=iw.getRoutes()[routeNr].getStartCol();

        height=iw.getArea()[row][col].getHr().getHeightLow();

        gui.redrawCell(iw.getRoutes()[routeNr].getEndRow(),iw.getRoutes()[routeNr].getEndCol(),-2);

        currentIndex=0;

        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if(currentIndex<a.size()) {
                    String s=a.get(currentIndex);
                    int prevRow=row;
                    int prevCol=col;

                    interpretString(s);
                    updateTable(prevRow,prevCol);
                    if(currentIndex==0)
                        gui.redrawCell(prevRow,prevCol,-2);

                    currentIndex++;
                }
                else {
                    timer.stop();
                    currentIndex = 0;
                    gui.endableStartButton();
                }
            }
        };
        timer=new Timer(500, taskPerformer);
        timer.start();
        gui.dislabeStartButton();

    }


    private void interpretString(String s)
    {
        for(int i=0;i<s.length();i++)
        {
            switch(s.charAt(i)) {
                case 'Z':
                    height--;
                    break;
                case 'P':
                    height++;
                    break;
                case 'D':
                    row++;
                    break;
                case 'U':
                    row--;
                    break;
                case 'L':
                    col--;
                    break;
                case 'R':
                    col++;
                    break;
            }

        }
    }
    private void updateTable(int prevRow,int prevCol)
    {
        Cell[][] tableMap=iw.getArea();
        gui.redrawCell(prevRow,prevCol,tableMap[prevRow][prevCol].getType());
        gui.redrawCell(row,col,-3-height);
    }

    private Integer[][] getTableData(Cell[][] tableMap,int height,int width)
    {
        Integer[][] tableData=new Integer[height][width];
        for(int i=0;i<height;i++)
            for(int j=0;j<width;j++)
                tableData[i][j]=(tableMap[i][j].getType());
        return tableData;
    }
    private Integer[] getColumnHeader(int width) {
        Integer[] columnData = new Integer[width];
        for (int j = 0; j < width; j++)
            columnData[j] = j;
        return columnData;
    }
    private void clearMap() {
        if(pathNr>=0)
        {
            gui.redrawCell(iw.getRoutes()[pathNr].getStartRow(),iw.getRoutes()[pathNr].getStartCol(),iw.getArea()[iw.getRoutes()[pathNr].getStartRow()][iw.getRoutes()[pathNr].getStartCol()].getType());
            gui.redrawCell(iw.getRoutes()[pathNr].getEndRow(),iw.getRoutes()[pathNr].getEndCol(),iw.getArea()[iw.getRoutes()[pathNr].getEndRow()][iw.getRoutes()[pathNr].getEndCol()].getType());

        }
    }
}
