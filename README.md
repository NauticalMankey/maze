"# maze" 

Run the program in one of two ways:

  java Maze

-or-

  java Maze maze.txt

The first method contains no arguments, the second method
contains an argument, in this case "maze.txt", which is the
name of a file containing data for a pre-built maze. 

***Note
added maze2.txt, so the program could also be run:
  java Maze maze2.txt

==================================================

The additonal files in the graph package can be invoked
by passing it a String of maze data, where maze data is
0's, and 1's which define a maze, like the examples
maze.txt and maze2.txt

Each 4 characters define a cell's walls in the order
north, south, east, west. 0 = no wall, 1 = a wall to
the respective side.

Examples:

  0110 would define a cell:
    -no wall on the north,
    -wall on the south, 
    -wall on the east, 
    -no wall on the west.

THERE IS A REQUIREMENT THAT THE MAZE BE SQAURE. SAME HEIGHT AND WIDTH

Use the following method to run the maze through the grapher/solver.

  mazeData = <MAZE STRING>

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


