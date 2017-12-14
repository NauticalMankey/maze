import java.util.*;
import set.*;
import graph.*;
import java.io.File;
import java.io.IOException;

/**
 *  The Maze class represents a maze in a rectangular grid.  There is exactly
 *  one path between any two points.
 **/

public class Maze {

  // Horizontal and vertical dimensions of the maze.
  protected int horiz;
  protected int vert;
  // Horizontal and vertical interior walls; each is true if the wall exists.
  protected boolean[][] hWalls;
  protected boolean[][] vWalls;

  // Object for generting random numbers.
  private static Random random;

  // Constants used in depth-first search (which checks for cycles in the
  // maze).
  private static final int STARTHERE = 0;
  private static final int FROMLEFT = 1;
  private static final int FROMRIGHT = 2;
  private static final int FROMABOVE = 3;
  private static final int FROMBELOW = 4;

  // Vars for use with the code pertaining to reading maze data from a file, and processing it
  public static int mazeSize; // "size" of maze, meaning either width or height
  public static String mazeData; // String of 1's and 0's representing the maze
  public static int cellWalls = 4; // Number of walls in each cell
 
  public Maze(int horizontalSize, int verticalSize) {
    int i, j;

    horiz = horizontalSize;
    vert = verticalSize;
    if ((horiz < 1) || (vert < 1) || ((horiz == 1) && (vert == 1))) {
      return;                                    // There are no interior walls
    }

    // Create all of the horizontal interior walls.  Initially, every
    // horizontal wall exists; they will be removed later by the maze
    // generation algorithm.
    if (vert > 1) {
      hWalls = new boolean[horiz][vert - 1];
      for (j = 0; j < vert - 1; j++) {
        for (i = 0; i < horiz; i++) {
          hWalls[i][j] = true;
        }
      }
    }
    // Create all of the vertical interior walls.
    if (horiz > 1) {
      vWalls = new boolean[horiz - 1][vert];
      for (i = 0; i < horiz - 1; i++) {
        for (j = 0; j < vert; j++) {
          vWalls[i][j] = true;
        }
      }
    }



 
    // Data structure for holding maze cell
    DisjointSets cells = new DisjointSets(horiz*vert);
    int num_horiz_walls = horiz*(vert-1);
    int num_vert_walls = vert*(horiz-1);
    // array to hold total number of walls
    int[] walls= new int[num_horiz_walls+num_vert_walls];
    for(int k=0;k<walls.length;k++) {
        walls[k] = k;
    }
    // scramble the wall sequence
    int tmp, pos;
    for(int k=walls.length-1; k>0;k--) {
        pos = randInt(k);
        tmp = walls[pos];
        walls[pos] = walls[k];
        walls[k] = tmp;
    }
    // creating maze
    int cella_x, cella_y, cellb_x, cellb_y;
    int cella_idx, cellb_idx;
    int root1, root2;
    int walltype; // 0 for horizontal, 1 for vertical
    for(int k=0; k<walls.length; k++) {
        tmp = walls[k];
        // this is horizontal walls
        if(tmp <num_horiz_walls) {
            cella_x = tmp%horiz;
            cella_y = tmp/horiz;
            cellb_x = cella_x;
            cellb_y = cella_y+1;
            walltype = 0;
            assert(horizontalWall(cella_x, cella_y)==true);
        } else { // vertical walls
            tmp -= num_horiz_walls;
            cella_x = tmp%(horiz-1);
            cella_y = tmp/(horiz-1);
            cellb_x = cella_x+1;
            cellb_y = cella_y;
            walltype = 1;
            assert(verticalWall(cella_x,cella_y) == true);
        }
        cella_idx = cella_y*horiz+cella_x;
        cellb_idx = cellb_y*horiz+cellb_x;
        // check if they belong to the same set
        root1 =cells.find(cella_idx);
        root2 =cells.find(cellb_idx);
        if(root1!=root2) {
            cells.union(root1,root2);
            // mark the wall as false
            if(walltype==0) {
                hWalls[cella_x][cella_y] = false;
            } else {
                vWalls[cella_x][cella_y] = false;
            }
        }
    }

  }

  /**
   *  toString() returns a string representation of the maze.
   **/
  public String toString() {
    int i, j;
    String s = "  ";

    // Print the top exterior wall.
    for (i = 0; i < horiz-1; i++) {
      s = s + "--";
    }
    s = s + "-\n|";

    // Print the maze interior.
    for (j = 0; j < vert; j++) {
      // Print a row of cells and vertical walls.
      for (i = 0; i < horiz - 1; i++) {
        if (vWalls[i][j]) {
          s = s + " |";
        } else {
          s = s + "  ";
        }
      }
      s = s + " |\n+";
      if (j < vert - 1) {
        // Print a row of horizontal walls
        for (i = 0; i < horiz; i++) {
          if (hWalls[i][j]) {
            s = s + "-+";
          } else {
            s = s + " +";
          }
        }
        s = s + "\n|";
      }
    }

    // Print the bottom exterior wall with outlet 
    for (i = 0; i < horiz-1; i++) {
      s = s + "--";
    }
    return s + "  \n";
  }

  
  public boolean horizontalWall(int x, int y) {
    if ((x < 0) || (y < 0) || (x > horiz - 1) || (y > vert - 2)) {
      return true;
    }
    return hWalls[x][y];
  }

  public boolean verticalWall(int x, int y) {
    if ((x < 0) || (y < 0) || (x > horiz - 2) || (y > vert - 1)) {
      return true;
    }
    return vWalls[x][y];
  }

  
  private static int randInt(int choices) {
    if (random == null) {       
      random = new Random();       // Create a "Random" object with random number
    }
    int r = random.nextInt() % choices;      // From 1 - choices to choices - 1
    if (r < 0) {
      r = -r;                                          // From 0 to choices - 1
    }
    return r;
  }

 
  
 // depthFirstSearch() does a depth-first traversal of the maze, marking each
 
  protected boolean depthFirstSearch(int x, int y, int fromWhere,
                                     boolean[][] cellVisited) {
    boolean cycleDetected = false;
    cellVisited[x][y] = true;

    // Visit the cell to the right?
    if ((fromWhere != FROMRIGHT) && !verticalWall(x, y)) {
      if (cellVisited[x + 1][y]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x + 1, y, FROMLEFT, cellVisited) ||
                        cycleDetected;
      }
    }

    // Visit the cell below?
    if ((fromWhere != FROMBELOW) && !horizontalWall(x, y)) {
      if (cellVisited[x][y + 1]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x, y + 1, FROMABOVE, cellVisited) ||
                        cycleDetected;
      }
    }

    // Visit the cell to the left?
    if ((fromWhere != FROMLEFT) && !verticalWall(x - 1, y)) {
      if (cellVisited[x - 1][y]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x - 1, y, FROMRIGHT, cellVisited) ||
                        cycleDetected;
      }
    }

    // Visit the cell above?
    if ((fromWhere != FROMABOVE) && !horizontalWall(x, y - 1)) {
      if (cellVisited[x][y - 1]) {
        cycleDetected = true;
      } else {
        cycleDetected = depthFirstSearch(x, y - 1, FROMBELOW, cellVisited) ||
                        cycleDetected;
      }
    }

    return cycleDetected;
  }

  /**
   * readFile() takes in a String, which is the name of a file and reads the data from
   * that file into mazeSize and mazeData
   * @param filename - Name of file
   * @throws IOException - Catches IOExceptions
   */
  // readFile sets mazeText and mazeSize static variables from "filename" passed.
  public static void readFile(File filename) throws IOException {
    Scanner sc = new Scanner(filename);
    StringBuilder fileContent = new StringBuilder();
    while (sc.hasNext()) {
      fileContent.append(sc.nextLine());
    }
    mazeData = fileContent.substring(1).replaceAll("\\s+","");
    mazeSize = Integer.parseInt(fileContent.substring(0,1));
  }

  
  public static void main(String[] args) {
    
    // Breaking the project into two main parts here.
    // 1) If filename argument is passed, read file and take action.
    // 2) Else, run program in its original state.

    // Read data file from the first argument on the command line passed.
    if (args.length != 0) {
      // read in a file
      File filename = new File(args[0]);
      try {
        readFile(filename);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
        
      // todo - remove this section
      // Commenting out metadata for now
      // Just a test to make sure vars get update with data from file.
		   // System.out.println("---------------------Print metadata during testing.---------------------");
		   // System.out.println("mazeSize = " + mazeSize);
		   // System.out.println("mazeData = " + mazeData);
		   // System.out.println("numWalls = " + numCells);
		   // System.out.println("\nAdjacency List:");
		   // System.out.println("---------------\n");
		   // System.out.print(mazeGraph); // print the graph via its toString()
		   // System.out.println();
		   // System.out.println("---------------------End metadata-------------");

      int numCells = mazeData.length()/cellWalls; // establish # of cells in maze
      MazeGraph mazeGraph = new MazeGraph(numCells); // initialize a new graph with number of cells
      mazeGraph.populateMazeGraph(mazeData); // populate the graph with the data in 0's and 1's format

      // Displaying results
      DisplayGraph.print(mazeData, mazeGraph);
      // run the solver(s)
      System.out.println("");
      MazeGraphSolver.solveWithBFS(mazeGraph);
      System.out.println();
      MazeGraphSolver.solveWithDFS(mazeGraph);
      System.out.println("");
      // End displaying results

    } else { // Original untouched program here.

      Scanner input= new Scanner(System.in);
      System.out.println("please enter the horizontal size of maze");
      int x = input.nextInt();
      System.out.println("please enter the verticle size of maze");
      int y = input.nextInt();

      /**
       *  Read the input parameters.
       * 
       */

      if (args.length > 0) {
        try {
          x = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
          System.out.println("First argument to Simulation is not an number.");
        }
      }

      if (args.length > 1) {
        try {
          y = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
          System.out.println("Second argument to Simulation is not an number.");
        }
      }

      Maze maze = new Maze(x, y);
      System.out.print(maze);
    }

  }

}
