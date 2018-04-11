package turtleants;

/**
 * Class representing a cell.
 * 
 * @author Joanna Chang
 * @version 11/20/17
 */

public class Cell {

	private int row, col;
	private int type;
	private boolean visited;
	private int timePassed = 0;
	private final int PHEROMONE_STRENGTH = 10;

	/**
	 * Constructor for a Cell. <br>
	 */
	public Cell(int type, int row, int col) {
		// remember these variables
		this.type = type;
		this.row = row;
		this.col = col;
	}

	/**
	 * Decide if the cell is a wall.
	 */
	public boolean isWall() {
		return type == Arena.WALL;
	}

	/**
	 * Decide if the cell is a bridge.
	 */
	public boolean isBridge() {
		return type == Arena.BRIDGE;
	}

	/**
	 * Decide if the cell is empty.
	 */
	public boolean isEmpty() {
		return type == Arena.EMPTY;
	}

	/**
	 * Decide if the cell is a nest.
	 */
	public boolean isNest() {
		return type == Arena.NEST;
	}

	/**
	 * Determine if the cell has been visited.
	 */
	public boolean visited() {
		return visited;
	}

	/**
	 * Note that the cell has been visited.
	 */
	synchronized public void visit() {
		visited = true;
	}

	/**
	 * Determine time that has passed since the last visit.
	 */
	synchronized public void addTime() {
		timePassed++;
		if (timePassed > PHEROMONE_STRENGTH) {
			visited = false;
			timePassed = 0;
		}
	}
}