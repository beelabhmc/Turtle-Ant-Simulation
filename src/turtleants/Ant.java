package turtleants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ant extends Thread {

	// current row and column of ant
	private int row, col;

	// speed of the ant
	private int speed;

	private int id;
	private long startTime;

	private List<Activity> activityList;

	// Random number generator to determine how ant wanders
	private Random motionGen;

	// World containing all of the cells to be displayed
	private Cell world[][];

	// Class responsible for writing paint method for repainting the screen
	private Arena arena;

	// if the ant is in a nest and the chance that it will stay in the nest
	private boolean inNest = false;
	private int chanceInNest;
	private int chanceFollowPher, chanceMoveOn; // chanceFollowPher>chanceMoveOn

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
	 * @param chanceInNest
	 *            chance that it will remain in a nest
	 */
	public Ant(int antID, long startTime, List<Activity> activityList, int row, int col, Cell[][] world, Arena arena,
			int speed, int chanceInNest, int chanceFollowPher, int chanceMoveOn) {
		this.id = antID;
		this.startTime = startTime;
		this.activityList = activityList;
		this.world = world;
		this.row = row;
		this.col = col;
		this.arena = arena;
		this.speed = speed;
		this.chanceInNest = chanceInNest;
		this.chanceFollowPher = chanceFollowPher;
		this.chanceMoveOn = chanceMoveOn;

		// random generator to determine ant's movement
		motionGen = new Random();

	}

	/**
	 * move one cell over in a random direction from current position and pause. Can
	 * move up, down, left, right, in diagonal directions or stay in the same spot.
	 */
	private void move() {

		int chanceOut = 100;
		boolean findMove, stayInPlace;

		if (inNest) {
			// determine the chance that the ant will come out of a nest
			chanceOut = motionGen.nextInt(100);
		}

		// move if the ant is not in a nest or has the chance to come out of a nest
		if (!inNest || chanceOut > chanceInNest) {
			int tempCol, tempRow;
			int colDir, rowDir;

			do {
				findMove = true;
				stayInPlace = true;

				// randomly choose to move right, left, or stay in place
				colDir = motionGen.nextInt(3) - 1;
				tempCol = col + colDir;

				// randomly chooose to move up, down, or stay in place
				rowDir = motionGen.nextInt(3) - 1;
				tempRow = row + rowDir;

				// note if the ant stays in place
				if (!(rowDir == 0 && colDir == 0)) {
					stayInPlace = false;
				}

				// there's a greater chance of moving into a cell with pheromones
				if ((world[tempRow][tempCol].visited() && !stayInPlace && motionGen.nextInt(100) < chanceFollowPher)
						|| motionGen.nextInt(100) < chanceMoveOn) {
					findMove = false;
				}
				// don't move into a wall
			} while (world[tempRow][tempCol].isWall() || findMove);

			// if you've made a move
			if (!stayInPlace) {

				// note if ant moves into/out of a nest
				if (world[tempRow][tempCol].isNest()) {
					inNest = true;
					activityList.add(
							new Activity(System.currentTimeMillis() - startTime, id, true, false, tempRow, tempCol));
				} else {
					inNest = false;
				}

				// note if ant moves onto a bridge
				if (world[tempRow][tempCol].isBridge()) {
					activityList.add(
							new Activity(System.currentTimeMillis() - startTime, id, false, true, tempRow, tempCol));
				}
			}

			col = tempCol;
			row = tempRow;

			world[row][col].visit();
		}

		// update the ant on the screen
		arena.repaint();
		try

		{
			sleep(speed);
		} catch (InterruptedException exc) {
			System.out.println("sleep interrupted!");
		}
	}

	/**
	 * Run method for Ant.
	 */
	public void run() {

		while (!stop) {
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

	public void stopAnt() {
		stop = true;
	}
}