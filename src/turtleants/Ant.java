package turtleants;

import java.util.Random;

public class Ant extends Thread {
	// current row and column of ant
	private int row, col;

	// speed of the ant
	private int speed;

	// Random number generatore to determine how ant wanders
	private Random motionGen;

	// World containing all of the cells to be displayed
	private Cell world[][];

	// Class responsible for writing paint method for repainting the screen
	private Arena arena;

	private boolean inNest = false;
	private int chanceInNest;

	/**
	 * Construct an ant
	 * 
	 * @param row
	 *            row where ant starts
	 * @param col
	 *            column where ant starts
	 * @param world
	 *            world of cells where arena is
	 * @param arena
	 *            object responsible for repainting canvas
	 * @param speed
	 *            speed of the ant
	 */
	public Ant(int row, int col, Cell[][] world, Arena arena, int speed, int chanceInNest) {
		this.world = world;
		this.row = row;
		this.col = col;
		this.arena = arena;
		this.speed = speed;
		this.chanceInNest = chanceInNest;
		motionGen = new Random();

	}

	/**
	 * move one cell over in a random direction from current position and pause Can
	 * move up, down, left, right, in diagonal directions or stay in the same spot.
	 */
	private void move() {

		int chanceOut = 100;

		if (inNest) {
			// determine the chance that the ant will come out of a nest
			chanceOut = motionGen.nextInt(100);
		}

		if (!inNest || chanceOut < chanceInNest) {
			int tempCol, tempRow;
			int colDir, rowDir;

			do {
				// randomly choose to move right, left, or stay in place
				colDir = motionGen.nextInt(3) - 1;
				tempCol = col + colDir;

				// randomly chooose to move up, down, or stay in place
				rowDir = motionGen.nextInt(3) - 1;
				tempRow = row + rowDir;
			
			//don't move into a wall
			} while (world[tempRow][tempCol].isWall());
			
			
			//note if ant moves into a nest
			if (world[tempRow][tempCol].isNest()) {
				inNest = true;
			}

			col = tempCol;
			row = tempRow;
		}

		// update the ant on the screen
		arena.repaint();
		try {
			sleep(speed);
		} catch (InterruptedException exc) {
			System.out.println("sleep interrupted!");
		}
	}

	/**
	 * Run method for Ant.
	 * 
	 * The termite moves randomly until it finds a wooden chip. It drops the chip in
	 * an empty space if it encounters another chip.
	 */
	public void run() {

		while (true) {
			move();
		}
	}

	/**
	 * Changes the speed at which the ant moves
	 * 
	 * @param s
	 *            speed of the ant
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

}