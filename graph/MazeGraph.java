package graph;

/**
 * Class MazeGraph -Creates new adjacency matrix with data passed to it.
 *
 * First initialize a new graph:
 *
 *       MazeGraph mazeGraph = new MazeGraph(int);
 *
 * Populate the graph with data:
 *
 *      mazeGraph.populateMazeGraph(String);
 *      (Where String is the maze's cells in 0 and 1's format.
 *
 *
 */

import java.util.ArrayList;
import java.util.List;

public class MazeGraph {

    private int mazeListSize = 0;
    private int cellWalls = 4;
    public List<NeighborArray> adjList;

    public MazeGraph(int cells) {

        adjList = new ArrayList<NeighborArray>();

        for (int i = 0; i < cells; i++) {
            adjList.add(new NeighborArray());
            mazeListSize++;
        }

    }

    public int getMazeListSize() {
        return mazeListSize;
    }

    public MazeGraph populateMazeGraph(String mazeData) {

        int totalSize = mazeData.length(); // total length of string
        int cells = totalSize/cellWalls; // number of cells
        int cellWalls = 4; // number of cell walls
        int width = (int) Math.sqrt(cells); // with of maze
        int height = (int) Math.sqrt(cells); // heigh of maze

        // This section creates all the new "cell" nodes and adds them to the appropriate array
        boolean[] cellArray = new boolean[4];
        int cellCounter = 0;
        NeighborArray tempNeighborArray;

        for (int i = 0; i < mazeData.length(); i += cellWalls) {
            for (int j = 0; j < cellWalls; j++) {
                // convert int from input to boolean: 0=false 1=true
                if (Integer.parseInt(mazeData.substring((i + j), (i + j + 1))) == 0) {
                    cellArray[j] = false;
                } else {
                    cellArray[j] = true;
                }
            }

            tempNeighborArray = this.adjList.get(cellCounter);
            tempNeighborArray.addCellNode(cellArray[0], cellArray[1], cellArray[2], cellArray[3]);

            cellCounter++;
        }

        // This section adds references to all "linked" nodes through open doors to appropriate stacks
        for (int i = 0; i < cells; i++) {

            //Establish, "this", northNode, southNode, eastNode, and westNode
            CellNode thisCell = this.adjList.get(i).neighborArray.get(0).getNode(); // this
            CellNode northCell = null;
            CellNode southCell = null;
            CellNode eastCell = null;
            CellNode westCell = null;

            // No North wall, add reference to northCell, unless "this" is first cell
            if ((!this.adjList.get(i).neighborArray.get(0).hasNorthWall()) &&
                this.adjList.get(i) != this.adjList.get(0)) {

                northCell = this.adjList.get(i - width).neighborArray.get(0).getNode();
                this.adjList.get(i).addCellNode(northCell);

            }
            // No South, add reference to southCell, unless "this" is last cell
            if ((!this.adjList.get(i).neighborArray.get(0).hasSouthWall()) &&
                this.adjList.get(i) != this.adjList.get(this.adjList.size() - 1)) {


                southCell = this.adjList.get(i + width).neighborArray.get(0).getNode();
                this.adjList.get(i).addCellNode(southCell);

            }
            // No East wall, add reference to eastWalla
            if (!this.adjList.get(i).neighborArray.get(0).hasEastWall()) {

                eastCell = this.adjList.get(i + 1).neighborArray.get(0).getNode();
                this.adjList.get(i).addCellNode(eastCell);

            }
            // No West wall
            if (!this.adjList.get(i).neighborArray.get(0).hasWestWall()) {

                westCell = this.adjList.get(i - 1).neighborArray.get(0).getNode();
                this.adjList.get(i).addCellNode(westCell);
            }

        }

        return this;

    }

    @Override
    public String toString() {
        String result = "";
        // create a toString() representation of adjList
        for (int i = 0; i < adjList.size(); i++) {
            result+= adjList.get(i).toString() ;

        }
        return result;
    }

}
