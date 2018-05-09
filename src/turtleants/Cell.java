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
	static final int PHEROMONE_STRENGTH = 20; //20 usually
	private int pheromone = 0;
	private int numAnts = 0;
	private String nestName;
	//private boolean hasAnt = false;

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
	 * Return cell type
	 */
	synchronized public int type() {
		return type;
	}

	/**
	 * Decide if the cell is a nest.
	 */
	synchronized public boolean isNest() {
		return type == Arena.NEST;
	}

	synchronized public String getNestName() {
		return nestName;
	}

//	synchronized public void addAnt() {
//		hasAnt = true;
//		System.out.println(numAnts);
//
//		numAnts++;
//		System.out.println("adding");
//		System.out.println(numAnts);
//	}
//
//	synchronized public void removeAnt() {
//		System.out.println(numAnts);
//		System.out.println(hasAnt);
//		numAnts--;
//		System.out.println("removing");
//		System.out.println(numAnts);
//
//		
//	}

	synchronized public int getNumAnts() {
		return numAnts;
	}

	synchronized public boolean isNegative() {
		return numAnts < 0;
	}

	/**
	 * Determine if the cell has been visited.
	 */
	synchronized public boolean visited() {
		pheromone = Math.min(pheromone + PHEROMONE_STRENGTH, 7500);
		return visited;
	}

	synchronized public void reset() {
		visited = false;
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
		pheromone = Math.max(pheromone - PHEROMONE_STRENGTH - PHEROMONE_STRENGTH * 2, 0);
		if (pheromone <= 0) {
			visited = false;
		}
	}

	synchronized public int getPheromone() {
		return pheromone;
	}
}