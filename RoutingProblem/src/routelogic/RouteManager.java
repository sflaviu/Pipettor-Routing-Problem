package routelogic;

import stepcondition.CellChecker;
import stepcondition.HeightChecker;
import stepcondition.NoContaminationChecker;
import stepcondition.ShortestRouteChecker;
import model.Cell;
import model.Route;

import java.util.List;

/**
 *  The RouteManager class has information about all the routes and the map. Chooses what kind of path to be computed.
 */
public class RouteManager {

    private Route[] routes;
    private Cell[][] area;

    public RouteManager(Route[] routes, Cell[][] area)
    {
        this.routes=routes;
        this.area=area;
    }
    public List<String> solve(int pathNr, String conditions)
    {
        List<String> path;
        Route r=routes[pathNr];

        RouteGenerator rg=new RouteGenerator(r,area);

        CellChecker currentCheck;

        switch (conditions)
        {
            case "A":
                currentCheck = new NoContaminationChecker();
                path = rg.findSimpleWay(r.getIndex(), currentCheck);
                break;

            case "B":
                currentCheck = new ShortestRouteChecker();
                path = rg.findSimpleWay(r.getIndex(), currentCheck);
                break;

            case "C":
                currentCheck = new HeightChecker();
                path = rg.findHeightedWay(r.getIndex(), currentCheck);
                break;

            case "D":
                currentCheck = new HeightChecker();
                path = rg.findAcceleratedWay(r.getIndex(), currentCheck);
                break;

            default:
                currentCheck = new HeightChecker();
                path = rg.findAcceleratedWay(r.getIndex(), currentCheck);
                break;

        }

        return path;
    }

}
