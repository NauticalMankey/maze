/**
 * Rat-in-a-maze
 * Data Structures Project
 * CIS 256 Data Structures: Java Fall 2017
 * Professor Dr. Kamran Eftekhari
 * 12/15/2017
 * 
 * @author Justin Buhay
 * @author Justin Evans
 * @author Farbod Ghiasi
 * @author Bryan Woods
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import graph.DisplayGraph;
import graph.MazeGraph;
import graph.MazeGraphSolver;
import java.util.Random;

public class Maze {

  public static final int NORTH = 0;
  public static final int SOUTH = 1;
  public static final int EAST = 2;
  public static final int WEST = 3;

  public static int N;
  public static int n;
  public static int[] b;

  // Vars for use with the code pertaining to reading maze data from a file, and processing it
  public static int mazeSize; // "size" of maze, meaning either width or height
  public static String mazeData; // String of 1's and 0's representing the maze
  public static int cellWalls = 4; // Number of walls in each cell

  public static void main(String[] args) {

    if (args.length != 0) {
      // readFile
      File filename = new File(args[0]);
      try {
        readFile(filename);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      int numCells = mazeData.length() / cellWalls; // establish # of cells in maze
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

    } else {
      Scanner sc = new Scanner(System.in);
      System.out.print("Enter size of Maze: ");

      try {
        n = Integer.parseInt(sc.nextLine());
      }
      catch (NumberFormatException e) {
        System.out.println("Argument to simulation is not an number.");
      }

      N = n * n;
      b = new int[N];
      for (int i = 0; i < N; i++) {
        b[i] = -1;
      }

      generateRandomMaze(n);

    }

  }

  public static void generateRandomMaze(int n) {
    N = n * n;
    int[][] a = new int[N][4];
    // Close everything for now.
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < 4; j++) {
        a[i][j] = 1;
      }
    }

    // Leave north wall of start room open.
    a[0][NORTH] = 0;

    // Leave south wall of goal room open
    a[n * n - 1][SOUTH] = 0;

    // Close every north wall of first row (leaving the start room out)
    for (int i = 1; i < n; i++) {
      a[i][NORTH] = 1;
    }

    // Close every south wall of last row (leaving the goal room out)
    for (int i = N - n; i < N - 1; i++) {
      a[i][SOUTH] = 1;
    }

    // Close every west wall of first column
    for (int i = 0; i < N; i++) {
      if (i % n == 0) {
        a[i][WEST] = 1;
      }
    }

    // Close every east wall of last column
    for (int i = 0; i < N; i++) {
      if ((i + 1) % n == 0) {
        a[i][EAST] = 1;
      }
    }

    // Choose a random room
    Random room = new Random();
    Random side = new Random();
    while (find(0) != find(N - 1)) {
      int m = room.nextInt(N);

      // case for when m is the start room
      if (m == 0) {
        int s = side.nextInt(2);
        if (s == 0 && find(0) != find(n)) {

          a[0][EAST] = 0;
          a[1][WEST] = 0;
          union(0, 1);
        } else if (s == 1 && find(0) != find(1)) {
          a[0][SOUTH] = 0;
          a[n][NORTH] = 0;
          union(0, n);
        }
        // case for when m is the top right corner room
      } else if (m == n - 1) {
        int s = side.nextInt(2);
        if (s == 0 && find(n - 1) != find(n - 2)) {
          a[n - 1][SOUTH] = 0;
          a[(n - 1) + n][NORTH] = 0;
          union(n - 1, n + (n - 1));
        } else {
          a[m][WEST] = 0;
          a[m - 1][EAST] = 0;
          union(m, m - 1);
        }
        // case for when m is the bottom left corner room
      } else if (m == N - n) {
        int s = side.nextInt(2);
        if (s == 0) {
          a[N - n][NORTH] = 0;
          a[(N - n) - n][SOUTH] = 0;
          union(N - n, (N - n) - n);
        } else {
          a[N - n][EAST] = 0;
          a[(N - n) + 1][WEST] = 0;
          union(N - n, (N - n) + 1);
        }
        // case for when m is the goal room
      } else if (m == N - 1) {
        int s = side.nextInt(2);
        if (s == 0 && find(N - 1) != find(N - 2)) {
          a[N - 1][NORTH] = 0;
          a[(N - 1) - n][SOUTH] = 0;
          union(N - 1, (N - 1) - n);
        } else if (s == 1 && find(N - 1) != find((N - 1) - n)) {
          a[N - 1][WEST] = 0;
          a[N - 2][EAST] = 0;
          union(N - 1, N - 2);
        }
        // case for when m is the top row but not the top right corner room or start
        // room
      } else if (m >= 1 && m < n - 1) {
        int s = side.nextInt(3);
        if (s == 0) {
          a[m][EAST] = 0;
          a[m + 1][WEST] = 0;
          union(m, m + 1);
        } else if (s == 1) {
          a[m][SOUTH] = 0;
          a[m + n][NORTH] = 0;
          union(m, m + n);
        } else {
          a[m][WEST] = 0;
          a[m - 1][EAST] = 0;
          union(m, m - 1);
        }
        // case for when m is the first column except goal room
      } else if (m % n == 0 && m > 1 && m < (N - n)) {
        int s = side.nextInt(3);
        if (s == 0) {
          a[m][NORTH] = 0;
          a[m - n][SOUTH] = 0;
          union(m, m - n);
        } else if (s == 1) {
          a[m][SOUTH] = 0;
          a[m + n][NORTH] = 0;
          union(m, m + n);
        } else {
          a[m][EAST] = 0;
          a[m + 1][WEST] = 0;
          union(m, m + 1);
        }
        // case for when m is the last column of except the corner rooms
      } else if ((m + 1) % n == 0 && m > n && m < (N - 1)) {
        int s = side.nextInt(3);
        if (s == 0) {
          a[m][NORTH] = 0;
          a[m - n][SOUTH] = 0;
          union(m, m - n);
        } else if (s == 1) {
          a[m][SOUTH] = 0;
          a[m + n][NORTH] = 0;
          union(m, m + n);
        } else {
          a[m][WEST] = 0;
          a[m - 1][EAST] = 0;
          union(m, m - 1);
        }
        // case for when m is the bottom row besides corners
      } else if (m < N - 1 && m > N - n) {
        int s = side.nextInt(3);
        if (s == 0) {
          a[m][NORTH] = 0;
          a[m - n][SOUTH] = 0;
          union(m, m - n);
        } else if (s == 1) {
          a[m][EAST] = 0;
          a[m + 1][WEST] = 0;
          union(m, m + 1);
        } else {
          a[m][WEST] = 0;
          a[m - 1][EAST] = 0;
          union(m, m - 1);
        }
        // any middle room
      } else {
        int s = side.nextInt(4);
        if (s == 0) {
          a[m][NORTH] = 0;
          a[m - n][SOUTH] = 0;
          union(m, m - n);
        } else if (s == 1) {
          a[m][SOUTH] = 0;
          a[m + n][NORTH] = 0;
          union(m, m + n);
        } else if (s == 2) {
          a[m][EAST] = 0;
          a[m + 1][WEST] = 0;
          union(m, m + 1);
        } else {
          a[m][WEST] = 0;
          a[m - 1][EAST] = 0;
          union(m, m - 1);
        }
      }

    } // end while

    String maze = "";
    for (int i = 0; i < N; i++) {
      //System.out.println();
      for (int j = 0; j < 4; j++) {
        maze += a[i][j];
        //System.out.print(" " + a[i][j]);

      }
    }
    System.out.println("\n");
    int numCells = maze.length() / 4;
    MazeGraph mazeGraph = new MazeGraph(numCells);
    mazeGraph.populateMazeGraph(maze);

    // Displaying results
    DisplayGraph.print(maze, mazeGraph);
    // run the solver(s)
    System.out.println("");
    MazeGraphSolver.solveWithBFS(mazeGraph);
    System.out.println();
    MazeGraphSolver.solveWithDFS(mazeGraph);
    System.out.println("");
    System.out.println("\n");

  } // End of generate maze

  // find method, returns the root of the disjoint set
  public static int find(int x) {
    if (b[x] < 0) {
      return x;
    } else {
      b[x] = find(b[x]);
      return b[x];
    }
  }

  // union method conjoins the two rooms into the same set
  public static void union(int room1, int room2) {

    int parentRoom1 = find(room1);
    int parentRoom2 = find(room2);

    if (parentRoom1 == parentRoom2) {
      return;
    }

    if (b[parentRoom2] < b[parentRoom1]) {
      b[parentRoom2] += b[parentRoom1];
      b[parentRoom1] = parentRoom2;
    } else {
      b[parentRoom1] += b[parentRoom2];
      b[parentRoom2] = parentRoom1;
    }

  }

  /**
   * readFile() takes in a String, which is the name of a file and reads the data from
   * that file into mazeSize and mazeData
   *
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
    mazeData = fileContent.substring(1).replaceAll("\\s+", "");
    mazeSize = Integer.parseInt(fileContent.substring(0, 1));
  }

}
