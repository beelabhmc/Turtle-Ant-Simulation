package turtleants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant extends Thread {

	// current row and column of ant
	private int row, col;

	// speed of the ant
	private int speed;

	// ID of ant
	private int id;

	// keep track of activities during simulation
	private List<Activity> activityList;

	// Random number generator to determine how ant wanders
	private Random motionGen;

	// World containing all of the cells to be displayed
	private Cell world[][];

	// Class responsible for writing paint method for repainting the screen
	private Arena arena;

	// if the ant has moved
	private boolean stayInPlace = true;

	// row and column directions
	private static final int[] ROW_DIR = new int[] { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
	private static final int[] COL_DIR = new int[] { -1, 0, 1, -1, 0, 1, -1, 0, 1 };

	// keep track of previous and current cell ant is in
	private Cell prevCell, currCell;

	// chance that ant will leave a nest without pheromones
	private static final int CHANCE_LEAVE = 5000; // should be 5000 or 1/2

	// base chance that ant will make a a move
	private static final int CHANCE_MOVE = 5000; // should be 5000 or 1/2

	// amount of pheromone that cuts chance of leaving a nest in half
	private static final int PHEROMONE_FACTOR = 600;

	// if the ant stops moving
	private boolean stop = false;

	/**
	 * Create an ant
	 * 
	 * @param row
	 *            row ant is in
	 * @param col
	 *            col ant is in
	 * @param world
	 *            array of cells where the ant is in
	 * @param arena
	 *            class responsible for paint method
	 * @param speed
	 *            how fast the ant is
	 */
	public Ant(int antID, List<Activity> activityList, int row, int col, Cell[][] world, Arena arena, int speed) {
		this.id = antID;
		this.activityList = activityList;
		this.world = world;
		this.row = row;
		this.col = col;
		this.arena = arena;
		this.speed = speed;

		prevCell = world[row][col];
		currCell = world[row][col];

		// random generator to determine ant's movement
		motionGen = new Random();

	}

	/**
	 * Move one cell over in a random direction from current position and pause. Can
	 * move up, down, left, right, in diagonal directions or stay in the same spot.
	 * 
	 * Probability of moving is affected by pheromones in a nest
	 */
	private void move() {

		int chanceOut = 10000;

		if (currCell.isNest()) {
			// determine the chance that the ant will come out of a nest
			chanceOut = motionGen.nextInt(10000);
		}

		// move if the ant is not in a nest or has the chance to come out of a nest
		if (!currCell.isNest()
				|| chanceOut < CHANCE_LEAVE / (Math.pow(2, currCell.getPheromone() / PHEROMONE_FACTOR))) {
			makeNextMove();
		}

		// // if you've made a move
		// if (!stayInPlace) {
		//
		// // note if ant moves into/out of a nest
		// if (prevCell.isNest() && !currCell.isNest()) {
		// // currCell.addAnt();
		//
		// // activityList.add(
		// // new Activity(System.currentTimeMillis() - startTime, id, true, false,
		// // row, col));
		//
		// } else if (!prevCell.isNest() && currCell.isNest()) {
		// // currCell.removeAnt();
		//
		// } else if (prevCell.isBridge() && !currCell.isBridge()) {
		// activityList.add(new Activity(arena.getNumRounds(), id, false, true, row,
		// col));
		// }
		// }
		// }

		currCell.visit(); // ASK: should this be here or only if ant has moved?: should pher increase if
							// ant stays in place?

		// update the ant on the screen
		arena.repaint();
		try {
			sleep(speed);
		} catch (InterruptedException exc) {
			System.out.println("sleep interrupted!");
		}
	}

	synchronized public void makeNextMove() {

		// weight (probability of moving to) of each cell
		int[] cellWeights = new int[9];

		// find weight of each possible cell to move into
		int i = 0;

		System.out.println("row: " + row + ", " + "col: " + col);

		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				Cell tempCell = world[row + r][col + c];

				if (!tempCell.isWall()) {
					if (i == 0) {
						cellWeights[i] = CHANCE_MOVE + tempCell.getPheromone();
					}
					// weight of staying in place is not affected by pheromones
					else if (i == 4) {
						cellWeights[i] = cellWeights[i - 1] + CHANCE_MOVE;
					} else {
						cellWeights[i] = cellWeights[i - 1] + CHANCE_MOVE + tempCell.getPheromone();
					}
				}

				// don't move into a wall
				else {
					if (i == 0) {
						cellWeights[i] = 0;
					} else {
						cellWeights[i] = cellWeights[i - 1] + 0;
					}
				}
				i++;
			}
		}

		// randomly generate number and find move that corresponds with number
		int num = motionGen.nextInt(cellWeights[8]);
		int move = binarySearch(cellWeights, 0, 8, num);

		prevCell = world[row][col];

		if (move == 4) {
			stayInPlace = true;
		} else {
			col = col + COL_DIR[move];
			row = row + ROW_DIR[move];

			currCell = world[row][col];
			stayInPlace = false;
		}
	}

	// find the leftmost mid that target < weights[mid]; eg.target=10,weight[mid]=12
	private static int binarySearch(int[] weights, int start, int end, int target) {
		while (start < end) {
			int mid = start + (end - start) / 2;
			if (target <= weights[mid]) {
				end = mid;
			} else {
				start = mid + 1;
			}
		}
		return start;
	}

	/**
	 * Run method for Ant.
	 */
	public void run() {
		while (!stop) {
			move();
		}
	}

	public void restart() {
		stop = false;
	}

	/**
	 * Changes the speed at which the ant moves
	 */
	public void changeSpeed(int s) {
		speed = s;
	}

	/**
	 * @return row where ant is currently
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return column where ant is currently
	 */
	public int getCol() {
		return col;
	}

	public void stopAnt() {
		stop = true;
	}

	public int getID() {
		return id;
	}

	public void setRow(int r) {
		row = r;
	}

	public void setCol(int c) {
		col = c;
	}
}