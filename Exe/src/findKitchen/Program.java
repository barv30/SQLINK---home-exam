package findKitchen;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Program {
    final static int INF = 99999;
    private int[][] dist;
    private int[][] adjMatrix;
    ArrayList<Vertex> vertexList;
    private int amountOfEmployees;


    public Program()
    {
        vertexList = new ArrayList<>();
    }

    public void findLocationOfKitchen(String fileName) throws Exception {
        Path pathFile = Paths.get(fileName);
        readFromFileAndCreateMatrix(pathFile);
        createAdjMatrix();
        buildMatrixOfShorterPathToAllVertexs();
        Point resLocation = checkPathsToKitchenAndFindLocation();
        if (resLocation.x != INF && resLocation.y != INF)
        {
            System.out.println("The best location to put the kitchen is:[" + resLocation.x +","+resLocation.y+"]");
        }

        else
        {
            System.out.println("There is no place to put the kitchen.");
        }

    }

    /**
     * create array with all the optional locations to place the kitchen ,and checks where the best location
     * if we found location - we return the location. else - we return "Error flag" : point with [INF,INF]
     */
    private Point checkPathsToKitchenAndFindLocation()
    {
        Point res = new Point();
        HashMap<Integer,SpaceLocationDetails> arrOptionalLocations = new HashMap<>();
        initArrLocation(arrOptionalLocations);
        Vertex currentVertex;
        for(int i=0 ; i<vertexList.size(); i++)
        {
            currentVertex = vertexList.get(i);
            if(currentVertex.getType() == 'E')
            {
                for(int j =0 ; j< vertexList.size(); j++) // add distance to all neighbours and accessible
                {
                    if((dist[currentVertex.getNumber()][j] != INF) &&( dist[currentVertex.getNumber()][j] != 0) && vertexList.get(j).getType() !='E')
                    {
                        arrOptionalLocations.get(j).setSumOfDistance(arrOptionalLocations.get(j).getSumOfDistance() + dist[currentVertex.getNumber()][j]);
                        arrOptionalLocations.get(j).setAccessible(arrOptionalLocations.get(j).getAccessible() +1 );
                    }
                }
            }
        }

        int minSumOfDistance = INF;
        int indexOfBestLocation = -1;

        for(Integer key:arrOptionalLocations.keySet())
        {
            if (arrOptionalLocations.get(key).getSumOfDistance() <= minSumOfDistance)
            {
                // check accessible from all employees
                if (arrOptionalLocations.get(key).getAccessible() == amountOfEmployees)
                {
                    minSumOfDistance = arrOptionalLocations.get(key).getSumOfDistance();
                    indexOfBestLocation = key;
                }
            }
        }

        if (indexOfBestLocation != -1) // if we found place
        {
            res.x = vertexList.get(arrOptionalLocations.get(indexOfBestLocation).getNumber()).getLocation().x;
            res.y = vertexList.get(arrOptionalLocations.get(indexOfBestLocation).getNumber()).getLocation().y;
        }

        else // there is no place to the kitchen
        {
            res.x = INF;
            res.y = INF;
        }

        return res;
    }


    /**
     * init the array locations. key = number of vertex in list.
     */
    private void initArrLocation(HashMap<Integer, SpaceLocationDetails> arrOptionalLocations) {

        Vertex currentVertex;
        for(int i=0 ; i< vertexList.size() ; i++)
        {
            currentVertex = vertexList.get(i);
            if(currentVertex.getType() == ' ')
            {
                arrOptionalLocations.put(currentVertex.getNumber(),new SpaceLocationDetails(currentVertex.getNumber()));
            }
        }
    }


    /**
     * Floyd Warshall Algorithm - create matrix with all shorter path from all vertex to all vertex.
     */
    private void buildMatrixOfShorterPathToAllVertexs() {
        int vertex = vertexList.size();

        dist = new int[vertex][vertex];
        int i, j, k;

        for (i = 0; i < vertex; i++)
            for (j = 0; j < vertex; j++)
                if(adjMatrix[i][j] == 1 || i == j) {
                    dist[i][j] = adjMatrix[i][j];
                }
                else {
                    dist[i][j] = INF;
                }

        for (k = 0; k < vertex; k++) {
            for (i = 0; i < vertex; i++) {

                for (j = 0; j < vertex; j++) {

                    if (dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
    }

    /**
     * create create adjacency matrix - if we have the edge i,j - 1
     *                                  else, 0
     */
    private void createAdjMatrix()
    {
        int vertex = vertexList.size();
        adjMatrix = new int [vertex][vertex];
        for(int i=0 ; i< vertexList.size();i++)
        {
            adjMatrix[vertexList.get(i).getNumber()][vertexList.get(i).getNumber()]=0;
            addAgdes(vertexList.get(i));
        }
    }

    /**
     * add all adges from vertex v
     *
     */
    private void addAgdes(Vertex vertex)
    {
        Vertex currentVertex;
      for(int i=0; i<vertexList.size();i++)
      {
          currentVertex = vertexList.get(i);
          if(vertex.getNumber() == currentVertex.getNumber())
              continue;
          if(checkIfTheVertexIsNeighbour(vertex.getLocation(),currentVertex.getLocation()))
          {
              adjMatrix[vertex.getNumber()][currentVertex.getNumber()] = 1;
          }
          else
          {
              adjMatrix[vertex.getNumber()][currentVertex.getNumber()] = 0;
          }
      }

    }

    /**
     * check is vertex is neighbour of other vertex
     *
     */
    private boolean checkIfTheVertexIsNeighbour(Point locationCurrent, Point locationOther)
    {
        if(locationCurrent.x ==locationOther.x && ((locationCurrent.y)+1 ==locationOther.y || (locationCurrent.y)-1 ==locationOther.y))
            return true;
        else if (locationCurrent.y ==locationOther.y && ((locationCurrent.x)+1 ==locationOther.x || (locationCurrent.x)-1 ==locationOther.x))
            return true;
        return false;
    }

    /**
     * read from file and fill data in vertex list
     * @throws Exception if there is a problem with the file
     */
    private void readFromFileAndCreateMatrix(Path filePath) throws Exception {
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Error: file not found");
        }

        File file = new File(filePath.toString());
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file);
            char current;
            int number=0;
            int indexRow=0,indexCol=0;
            while (fis.available() > 0) {
                current = (char) fis.read();
                if(current =='\n')
                {
                    indexRow++;
                    indexCol=0;
                }
                if ( current == 'E' || current == ' ' || current == 'W')
                {
                        if(current != 'W') {
                            vertexList.add(new Vertex(current, indexRow, indexCol, number));
                            number++;
                            if (current == 'E') {
                                amountOfEmployees++;
                            }
                        }
                    indexCol++;

                }
            }

    }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } finally {
            fis.close();

        }
    }
}
