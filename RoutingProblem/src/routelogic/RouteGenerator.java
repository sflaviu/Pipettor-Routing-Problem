package routelogic;

import stepcondition.CellChecker;
import heuristic.HeuristicCalculator;
import model.Cell;
import model.HeightRange;
import model.Pipettor;
import model.Route;

import java.util.*;

/**
 * Implementation of the A* algorithm for finding the best suited path. Contains multiple methods for different conditions.
 *
 *   In order to solve the problem taking the height map into consideration, I have used a HeightRange object, that keeps
 *  track of all the possible heights the pipettor might be in a certain cell. As we compute the path, we check if the
 *  height of the cell that we wish to go to fits in the HeightRange we currently lie in. If not, some additional steps
 *  will be required, and the cost to get there will be higher (see the HeightChecker's ComputeStepCost method).After this,
 *  the appropriate heights will be assigned starting from our destination to the start of the path, restricting the range
 *  according to the height of our destination.
 *
 *   The Acceleration/Deceleration problem is solved by computing a simpler path and compressing it, whenever the accelerating
 *  is a valid possibility. Due to the way the shortest path problem and height map problem are solved, there are not many
 *  conditions that need to be checked. The accelerated decisions are computed separately for the XZ and Y axis, and are
 *  afterwards combined. I have considered that the pipettor is allowed to choose not to accelerate at any point (for
 *  example, the move sequance  "L" "LL" "LL" "LL" "L" is a correct one) but it must always decelerate to a stable
 *  position before changing direction.
 *
 */

public class RouteGenerator {

    private Route r;
    private Pipettor p;
    private Cell[][] area;
    private TreeSet<Cell> openList;

    public RouteGenerator(Route route,Cell[][] cells)
    {
        r=route;
        p=new Pipettor(r.getStartRow(),r.getStartCol(),cells[r.getStartRow()][r.getStartCol()].getType());
        area=cells;
        openList=new TreeSet<Cell>();
    }

    /**
     * Method that computes the way based on the conditions it receives through the checker.
     * @param routeNr - current route number
     * @param cc - conditions holder
     * @return - the route
     */
    private List<String> findWay(int routeNr, CellChecker cc)
    {
        int startRow,startCol;

        startRow=r.getStartRow();
        startCol=r.getStartCol();

        Cell startCell=area[startRow][startCol];
        startCell.setVisited(routeNr);
        startCell.setGCost(0);
        startCell.initializeHr();

        openList.add(area[startRow][startCol]);

        while (!noMovesPossible() && !endCondition()) {
            Cell nextStep = openList.first();
            openList.remove(nextStep);
            check4Neighbours(nextStep.getRow(), nextStep.getCol(), routeNr, cc);

            p.setRow(nextStep.getRow());
            p.setCol(nextStep.getCol());

            nextStep.setInClosed(routeNr);
        }

        if(!endCondition()) {
            System.out.println("Could not find path !");
            return null;
        }
        else {
            List<String> way = new ArrayList<String>();

            Cell start = area[r.getStartRow()][r.getStartCol()];
            Cell step=area[r.getEndRow()][r.getEndCol()];

            while(step!=start) {
                way.add(step.getMove());
                step = step.getParent();
            }
            return way;
        }
    }

    public List<String> findSimpleWay(int routeNr, CellChecker cc)
    {
        List<String> rez=findWay(routeNr,cc);
        Collections.reverse(rez);
        return rez;
    }

    public List<String> findHeightedWay(int routeNr,CellChecker c)
    {
        List<String> ls=findWay(routeNr,c);

        int currentRow=r.getEndRow();
        int currentCol=r.getEndCol();
        int currentHeight=area[currentRow][currentCol].getType();

        List<String> reversedList= new ArrayList<>();

        for(int i=0;i<ls.size();i++)
        {
            String t=ls.get(i);

            int nextRow= interpretCommandRow(currentRow,t);
            int nextCol= interpretCommandCol(currentCol,t);

            HeightRange nextHeightRange=area[nextRow][nextCol].getHr();

            int heightIncrement=0;
            String yDirection="";
            boolean addLast=true;

            if (nextHeightRange.getHeightLow() > currentHeight)
            {
                heightIncrement=1;
                yDirection="Z";
            }

            if (nextHeightRange.getHeightHigh() < currentHeight) {
                heightIncrement=-1;
                yDirection="P";
                reversedList.add(t);
                addLast=false;
            }

            while(!nextHeightRange.contains(currentHeight)) {
                currentHeight=currentHeight+heightIncrement;
                reversedList.add(yDirection);
            }
            if(addLast)
                reversedList.add(t);

            currentCol = nextCol;
            currentRow = nextRow;
        }
        List<String> correctList= new ArrayList<>();

        for(int i=reversedList.size()-1;i>=0; i--)
        {
            String rl=reversedList.get(i);
            if((rl.equals("Z") ||rl.equals("P")) && i>0 && !rl.equals(reversedList.get(i-1)) ) {
                correctList.add(rl.concat(reversedList.get(--i)));
            }
            else
                correctList.add(rl);
        }
        return correctList;
    }

    public List<String> findAcceleratedWay(int routeNr,CellChecker c) {

        List<String> ls = findHeightedWay(routeNr,c);

        int previousXZAxisAcceleration=1;
        List<String> acceleratedXZWay=new ArrayList<String>();

        String previousXZAxisMove = getXZMove(ls.get(0));
        for(int i=1;i<ls.size();i++)
        {
            if(!previousXZAxisMove.equals("") ) {
                if(ls.get(i).contains(previousXZAxisMove))
                    previousXZAxisAcceleration++;
                else
                {
                    addAcceleratedWay(previousXZAxisMove,previousXZAxisAcceleration,acceleratedXZWay);
                    previousXZAxisAcceleration=1;
                    previousXZAxisMove=getXZMove(ls.get(i));
                }
            }
            else {
                previousXZAxisMove = getXZMove(ls.get(i));
                acceleratedXZWay.add("");
            }

        }
        addAcceleratedWay(previousXZAxisMove,previousXZAxisAcceleration,acceleratedXZWay);

        int previousYAxisAcceleration=1;
        List<String> acceleratedYWay=new ArrayList<String>();


        String previousYAxisMove = getYMove(ls.get(0));

        for(int i=1;i<ls.size();i++)
        {
            if(!previousYAxisMove.equals("") ) {
                if(ls.get(i).contains(previousYAxisMove))
                    previousYAxisAcceleration++;
                else
                {
                    addAcceleratedWay(previousYAxisMove,previousYAxisAcceleration,acceleratedYWay);
                    previousYAxisAcceleration=1;
                    previousYAxisMove=getYMove(ls.get(i));
                }
            }
            else {
                previousYAxisMove = getYMove(ls.get(i));
                acceleratedYWay.add("");
            }

        }
        addAcceleratedWay(previousYAxisMove,previousYAxisAcceleration,acceleratedYWay);

        List<String> acceleratedWay=new ArrayList<String>();
        for(int i=0;i<ls.size();i++)
        {
            String combinedElement=acceleratedYWay.get(i).concat(acceleratedXZWay.get(i));
            if(!combinedElement.equals(""))
                acceleratedWay.add(combinedElement);
        }
        return acceleratedWay;
    }

    private void check4Neighbours(int row, int col,int route,CellChecker cc)
    {
        if(row >0)
            checkNeighbour(row-1,col,row,col,route,"U",cc);
        if(row<area.length-1)
            checkNeighbour(row+1,col,row,col,route,"D",cc);
        if(col >0)
            checkNeighbour(row,col-1,row,col,route,"L",cc);
        if(col<area[0].length-1)
            checkNeighbour(row,col+1,row, col,route,"R",cc);
    }


    private void checkNeighbour(int neighbourRow, int neighborCol, int parentRow, int parentCol,int route,String move, CellChecker cc)
    {
        Cell neighbour=area[neighbourRow][neighborCol];
        Cell parent=area[parentRow][parentCol];

        if(cc.check(neighbour,parent,area[r.getEndRow()][r.getEndCol()].getType()))
        {
            int neighbourNewGCost = parent.getGCost() + cc.computeStepCost(neighbour,parent);

            if (neighbour.isVisited(route)) {
                int neighbourOldGCost = neighbour.getGCost();

                if (openList.contains(neighbour)) {
                    if (neighbourOldGCost > neighbourNewGCost) {
                        openList.remove(neighbour);
                        updateNeighbour(neighbour,parent,neighbourNewGCost,move);
                        openList.add(neighbour);
                    }
                } else if (neighbour.getInClosed() == route) {
                    if (neighbourOldGCost > neighbourNewGCost) {
                        updateNeighbour(neighbour,parent,neighbourNewGCost,move);;
                        openList.add(neighbour);
                    }
                }
            } else {
                neighbour.setVisited(route);

                int hCost= HeuristicCalculator.manhattanDistance(neighbourRow,neighborCol,r.getEndRow(),r.getEndCol());
                neighbour.setHCost(hCost);

                updateNeighbour(neighbour,parent,neighbourNewGCost,move);
                openList.add(neighbour);
            }
        }
    }

    private void updateNeighbour(Cell neighbour,Cell parent,int gCost,String move)
    {
        neighbour.updateCost(gCost);
        neighbour.setParent(parent);
        neighbour.setMove(move);
        neighbour.updateHr(parent.getHr());
    }

    private String getXZMove(String t)
    {
        if(t.contains("D"))
            return "D";
        if(t.contains("U"))
            return "U";
        if(t.contains("L"))
            return "L";
        if(t.contains("R"))
            return "R";
        return "";
    }

    private String getYMove(String t)
    {
        if(t.contains("P"))
            return "P";
        if(t.contains("Z"))
            return "Z";
        return "";
    }

    private void addAcceleratedWay(String previousMove,int previousAcceleration,List<String> acceleratedWay)
    {

        if(previousAcceleration<=3)
            for(int j=0;j<previousAcceleration;j++)
                acceleratedWay.add(previousMove);
        else {
            int length = (int) Math.sqrt((double) previousAcceleration);
            int rest = previousAcceleration - length * length;

            int i;

            int countMoves = 2*length-1;
            for (i = 1; i <= length; i++)
                acceleratedWay.add(multiplyString(previousMove, i));

            while (rest >= length) {
                acceleratedWay.add(multiplyString(previousMove, length));
                rest = rest - length;
                countMoves++;
            }

            i -= 2;
            while (i > 0) {
                acceleratedWay.add(multiplyString(previousMove, i));
                while (rest >= i) {
                    acceleratedWay.add(multiplyString(previousMove, i));
                    rest = rest - i;
                    countMoves++;
                }
                i--;
            }

            for (int j = countMoves; j < previousAcceleration; j++)
                acceleratedWay.add("");
        }
    }

    private boolean endCondition()
    {
        if(p.getRow()==r.getEndRow() && p.getCol()==r.getEndCol())
            return true;
        return false;
    }

    private boolean noMovesPossible()
    {
        if(openList.isEmpty())
            return true;
        return false;
    }

    private String multiplyString(String s,int repetitions)
    {
        String rez="";
        for(int i=0;i<repetitions;i++)
            rez=rez.concat(s);
        return rez;
    }
    private int interpretCommandRow(int row,String t)
    {
        if(t.charAt(0)=='D')
            return row-t.length();
        if(t.charAt(0)=='U')
            return row+t.length();
        return row;
    }
    private int interpretCommandCol(int col,String t)
    {
        if(t.charAt(0)=='L')
            return col+t.length();
        if(t.charAt(0)=='R')
            return col-t.length();
        return col;
    }


}
