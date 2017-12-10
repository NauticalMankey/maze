package graph;

public class CellNode {

    public static int id_counter = 0;

    private boolean visited;
    private boolean northWall;
    private boolean southWall;
    private boolean eastWall;
    private boolean westWall;
    private int id;
    public int parent = 0;

    public CellNode(boolean northWall, boolean southWall, boolean eastWall, boolean westWall) {
        visited = false;
        this.northWall = northWall;
        this.southWall = southWall;
        this.eastWall = eastWall;
        this.westWall = westWall;
        this.id = id_counter;
        id_counter++;
    }

    // Getters and Setters
    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public int getId() {
        return id;
    }
    public boolean hasNorthWall() {
        return northWall;
    }
    public void setNorthWall(boolean northWall) {
        this.northWall = northWall;
    }
    public boolean hasSouthWall() {
        return southWall;
    }
    public void setSouthWall(boolean southWall) {
        this.southWall = southWall;
    }
    public boolean hasEastWall() {
        return eastWall;
    }
    public void setEastWall(boolean eastWall) {
        this.eastWall = eastWall;
    }
    public boolean hasWestWall() {
        return westWall;
    }
    public void setWestWall(boolean westWall) {
        this.westWall = westWall;
    }
    public CellNode getNode() {
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }

}

