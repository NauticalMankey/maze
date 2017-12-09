package graph;

import java.util.ArrayList;

public class NeighborArray {

    private int neighborArraySize = 0; // CellStack size
    public ArrayList<CellNode> neighborArray;

    public NeighborArray() { // Create an empty Array called neighborArray.
        neighborArray = new ArrayList<>();
    }

    public int getNeighborArraySize() {
        return neighborArraySize;
    }

    public void addCellNode(CellNode cellNodeToAdd) {
        neighborArray.add(cellNodeToAdd);
        neighborArraySize++;
    }

    public void addCellNode(boolean northWall, boolean southWall, boolean eastWall, boolean westWall) {
        neighborArray.add(new CellNode(northWall, southWall, eastWall, westWall));
        neighborArraySize++;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < neighborArray.size(); i++) {
            if (i == 0) {
                result += neighborArray.get(i).toString() + "-";
            } else {
                result += "-" + neighborArray.get(i).toString();
            }
        }
        result += "\n";
        return result;
    }

}
