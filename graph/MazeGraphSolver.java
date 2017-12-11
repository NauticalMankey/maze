package graph;


import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

public class MazeGraphSolver {


  public void MazeGraphSolver() {
  }

  private static void printResults(MazeGraph mazeGraph, ArrayList path, CellNode currentCell, String type) {

    // ArrayList for reversing the reverse path
    ArrayList<Integer> reversedForward = new ArrayList<>();

    // Print forward path.
    System.out.print("Rooms visited by " + type + ": ");
    for (int i = 0; i < path.size(); i++) {
      System.out.print(path.get(i) + " ");
    }
    System.out.println();

    // Print reverse path:
    System.out.print("This is the path (in reverse): ");
    int origin = 0;
    int parent = currentCell.getId();

    while (parent != origin) {
      System.out.print(parent + " ");
      reversedForward.add(parent); // add parent ID to reversedForward
      parent = currentCell.parent;
      currentCell = mazeGraph.adjList.get(parent).neighborArray.get(0);
    }
    reversedForward.add(parent); // add dangling parent ID to reversedForward

    System.out.println(parent + " ");
    System.out.println("This is the path.");

    /**
     * Section from printing the resultant path
     */
    // Reverse reversedForward
    Collections.reverse(reversedForward);

    // Get the sqrt of the "size" for n
    int n = (((int) Math.sqrt(mazeGraph.getMazeListSize() + 1)));

    String[][] matrix = new String[n][n];

    // Generate matrix
    int counter = 0; // int to count through cells
    for (int i = 0; i < mazeGraph.getMazeListSize(); i+= n) {
      for (int j = 0; j < n; j++) {
        if (reversedForward.contains(i+j)) {
          matrix[counter][j] = "X";
        } else {
          matrix[counter][j] = " ";
        }
      }
      counter++;
    }

    // Print solved path matrix
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        System.out.print(matrix[i][j]);
      }
      System.out.println();
    }

  }




  // reset all nodes to visited = false
  private static void resetVisited(MazeGraph mazeGraph) {
    for (int i = 0; i < mazeGraph.getMazeListSize(); i++){
      mazeGraph.adjList.get(i).neighborArray.get(0).setVisited(false);
    }
  }

  public static void solveWithBFS(MazeGraph mazeGraph) {

    // reset all nodes to visited = false
    resetVisited(mazeGraph);

    Queue<CellNode> queueLinkedList = new LinkedList<>();

    // Create ArrayList to hold path
    ArrayList<Integer> path = new ArrayList<>();

    // Establish int for last cell Number
    int lastCell = (mazeGraph.adjList.size() - 1);
    // Create ArrayList to hold path
    ArrayList<Integer> pathBFS = new ArrayList<>();
    // Enqueue cell 0 into queueArrayList
    queueLinkedList.offer(mazeGraph.adjList.get(0).neighborArray.get(0));
    // Mark cell 0 as visited
    mazeGraph.adjList.get(0).neighborArray.get(0).setVisited(true);

    int parentID = 0;
    // Establish currentCell
    CellNode currentCell = queueLinkedList.peek();
    currentCell.parent = parentID; // set initial parent to 0

    while (!queueLinkedList.isEmpty()) {
      // dequeue currentCell
      currentCell = queueLinkedList.poll();

      for (int i = 0; i < mazeGraph.adjList.get(currentCell.getId()).getNeighborArraySize(); i++) {

        CellNode neighborCell = mazeGraph.adjList.get(currentCell.getId()).neighborArray.get(i);

        if (!neighborCell.isVisited()) {
          neighborCell.parent = currentCell.getId();
          queueLinkedList.offer(neighborCell);
          neighborCell.setVisited(true);
        }

      }

      pathBFS.add(currentCell.getId());

      // Exit found !
      if (currentCell.getId() == lastCell) {
        printResults(mazeGraph, pathBFS, currentCell, "BFS");
        break;
      }

    }

  }







  public static void solveWithDFS(MazeGraph mazeGraph) {

    // reset all nodes to visited = false
    resetVisited(mazeGraph);

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
        printResults(mazeGraph, path, currentCell, "DFS");
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