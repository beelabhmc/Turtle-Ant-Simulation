package turtleants;

/**
 * Class representing a cell.
 * 
 * @author Joanna Chang
 * @version 11/20/17
 */

public class Cell {

	// row and column the cell is in
	private int row, col;

	// type of cell: empty, wall, bridge, or nest
	private int type;

	// if the cell has been visited by a nest
	private boolean visited;

	// keep track of pheromone in cell
	static final int PHEROMONE_STRENGTH = 20; // 20 usually, change to 0 if no pheromones
	private int pheromone = 0;

	// keep track of number of ants in cell
	private int numAnts = 0;

	// name of the nest if applicable
	private String nestName;
	// private boolean hasAnt = false;

	/**
	 * Constructor for a Cell. <br>
	 */
	public Cell(int type, int row, int col, String name) {
		// remember these variables
		this.type = type;
		this.row = row;
		this.col = col;
		this.nestName = name;
	}

	/**
	 * Return cell type
	 */
	synchronized public int type() {
		return type;
	}

	/**
	 * Decide if the cell is a wall.
	 */
	synchronized public boolean isWall() {
		return type == Arena.WALL;
	}

	/**
	 * Decide if the cell is a bridge.
	 */
	synchronized public boolean isBridge() {
		return type == Arena.BRIDGE;
	}

	/**
	 * Decide if the cell is empty.
	 */
	synchronized public boolean isEmpty() {
		return type == Arena.EMPTY;
	}

	/**
	 * Decide if the cell is a nest.
	 */
	synchronized public boolean isNest() {
		return type == Arena.NEST;
	}

	/**
	 * Get name of nest if applicable
	 * 
	 * @return name of nest
	 */
	synchronized public String getNestName() {
		return nestName;
	}

	// synchronized public void addAnt() {
	// hasAnt = true;
	// System.out.println(numAnts);
	//
	// numAnts++;
	// System.out.println("adding");
	// System.out.println(numAnts);
	// }
	//
	// synchronized public void removeAnt() {
	// System.out.println(numAnts);
	// System.out.println(hasAnt);
	// numAnts--;
	// System.out.println("removing");
	// System.out.println(numAnts);
	//
	//
	// }

	synchronized public int getNumAnts() {
		return numAnts;
	}

	// for testing code, delete
	synchronized public boolean isNegative() {
		return numAnts < 0;
	}

	/**
	 * Determine if the cell has been visited.
	 */
	synchronized public boolean visited() {
		return visited;
	}

	/**
	 * Reset the cell
	 */
	synchronized public void reset() {
		visited = false;
	}

	/**
	 * Note that the cell has been visited.
	 */
	synchronized public void visit() {
		pheromone = Math.min(pheromone + PHEROMONE_STRENGTH, 7500);
		visited = true;
	}

	/**
	 * Determine time that has passed since the last visit.
	 */
	synchronized public void addTime() {
		pheromone = Math.max(pheromone - PHEROMONE_STRENGTH - PHEROMONE_STRENGTH * 2, 0);
		if (pheromone <= 0) {
			visited = false;
		}
	}

	synchronized public int getPheromone() {
		return pheromone;
	}
}