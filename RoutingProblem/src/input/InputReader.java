package input;

import model.Cell;
import model.Route;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class InputReader {

    public InputWrapper read(String fileName) {

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String[] line = bufferedReader.readLine().split(" ");
            int rows = Integer.parseInt(line[0]);
            int cols= Integer.parseInt(line[1]);

            Cell[][] area= new Cell[rows][cols];

            for (int i = 0; i < rows; i++) {

                line = bufferedReader.readLine().split(" ");

                for(int j=0; j<cols; j++) {
                    area[i][j]=new Cell(Integer.parseInt(line[j]),i,j);
                }
            }

            line = bufferedReader.readLine().split(" ");

            int nrRoutes=Integer.parseInt(line[0]);

            Route[] routes=new Route[nrRoutes];
            for (int i = 0; i < nrRoutes; i++) {
                line = bufferedReader.readLine().split(" ");

                int index=Integer.parseInt(line[0]);
                int startRow=Integer.parseInt(line[1]);
                int startCol=Integer.parseInt(line[2]);
                int endRow=Integer.parseInt(line[3]);
                int endCol=Integer.parseInt(line[4]);

                Route r=new Route(index,startRow,startCol,endRow, endCol);
                routes[i]=r;
            }
            bufferedReader.close();

            return new InputWrapper(rows,cols,area,nrRoutes,routes);
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
            return null;
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            return null;
        }


    }


}
