package graph;

public class DisplayGraph {

  public DisplayGraph() {
  }

  public static void print(String mazeData, MazeGraph mazeGraph) {

    int walls = 4;
    int cells = mazeData.length() / walls;
    int n = (int) Math.sqrt(cells);
    int[][] matrix = new int[n][n];

//    System.out.println("----------metadata---------");
//    System.out.println(mazeData);
//    System.out.println("walls = " + walls);
//    System.out.println("cells = " + cells);
//    System.out.println("n = " + n);
//    System.out.println("--------end metadata-------");

    String rowChar = "+";
    String wallChar = "|";
    String space = " ";

    CellNode currentCell;

    System.out.println("\nThe maze:");

    // print an initial top row, the skip printing top rows to avoid duplicates between
    // the top row, and the previous bottom row
    for (int i = 0; i < n; i += n) {
      // print next row of north walls
      for (int j = 0; j < n; j++) {
        currentCell = mazeGraph.adjList.get(j + i).neighborArray.get(0);

        if (currentCell.hasNorthWall()) {
          System.out.print(rowChar + rowChar + rowChar);
        } else {
          System.out.print(rowChar + space + rowChar);
        }
      }
      System.out.println();
    }

    for (int i = 0; i < (cells - walls); i += n) {

      // print next row of side walls
      for (int j = 0; j < n; j++) {
        currentCell = mazeGraph.adjList.get(j + i).neighborArray.get(0);

        if (currentCell.hasEastWall() && // both walls
            currentCell.hasWestWall()) {

          System.out.print(wallChar + space + wallChar);

        } else if (!currentCell.hasEastWall() && // no east, no west
            !currentCell.hasWestWall()) {

          System.out.print(space+space+space);

        } else if (!currentCell.hasEastWall() && // no east, yes west
            currentCell.hasWestWall()) {

          System.out.print(wallChar + space+space);

        } else if (currentCell.hasEastWall() && // yes east, not west
            !currentCell.hasWestWall()) {

          System.out.print(space+space + wallChar);

        }
      }
      System.out.println();

      // print next row of south walls
      for (int j = 0; j < n; j++) {
        currentCell = mazeGraph.adjList.get(j + i).neighborArray.get(0);

        if (currentCell.hasSouthWall()) {
          System.out.print(rowChar + rowChar + rowChar);
        } else {
          System.out.print(rowChar + space + rowChar);
        }
      }
      System.out.println();

    }

  }

}