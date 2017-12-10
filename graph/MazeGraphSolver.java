package graph;

import java.util.ArrayList;
import java.util.Stack;

public class MazeGraphSolver {


  public void MazeGraphSolver() {
  }

  public static void solveWithBFS(MazeGraph mazeGraph) {

  }

  public static void solveWithDFS(MazeGraph mazeGraph) {

    Stack<CellNode> stackArrayList = new Stack<>();

    // Establish int for last cell Number
    int lastCell = (mazeGraph.adjList.size() -1);

    // Create ArrayList to hold path
    ArrayList<Integer> path = new ArrayList<>();

    // Pop cell 0 into stackArrayList
    stackArrayList.push(mazeGraph.adjList.get(0).neighborArray.get(0));
    // Mark cell 0 as visited
    stackArrayList.push(mazeGraph.adjList.get(0).neighborArray.get(0)).setVisited(true);

    while (!stackArrayList.empty()) { // While stackArrayList not empty

      CellNode currentCell = stackArrayList.pop(); // Pop element and store it as currentCell

      path.add(currentCell.getId()); // add currentCell to path

      // If currentCell = (N-1), then break from the while-loop and print the path found
      if (currentCell.getId() == mazeGraph.adjList.get(lastCell).neighborArray.get(0).getId() ) {

        // Print forward path.
        System.out.print("Rooms visited by DFS: ");
        for (int i = 0; i < path.size(); i++) {
          System.out.print(path.get(i) + " ");
        }
        System.out.println();

        // Print reverse path:
        System.out.println("This is the path (in reverse):");
        int origin = 0;
        int parent = currentCell.getId();
        while (parent != origin) {
          //System.out.println(currentCell.getId());
          System.out.print(parent + " ");
          parent = currentCell.parent;
          currentCell = mazeGraph.adjList.get(parent).neighborArray.get(0);
        }
        System.out.println(parent + " ");
        System.out.println("\n");

        break;
      }

      NeighborArray nextRow = mazeGraph.adjList.get(currentCell.getId());

      // Push the reachable cells from this cell into stackArrayList
      for (int i = 0; i < nextRow.getNeighborArraySize(); i++) {

        if (!nextRow.neighborArray.get(i).isVisited()) {

          stackArrayList.push(nextRow.neighborArray.get(i));
          nextRow.neighborArray.get(i).setVisited(true);
          nextRow.neighborArray.get(i).parent = currentCell.getId();

        }

      }

    }

  }

}
