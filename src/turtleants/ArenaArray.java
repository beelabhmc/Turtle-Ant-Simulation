package turtleants;

/**
 * This creates a matrix of strings representing cells in the arena. The string
 * indicates the type of cell it is.
 * 
 * @author Joanna
 *
 */
public class ArenaArray {

	// string array to represent the arena
	private String[][] arena;

	// size (number of cells in) of each box
	private int boxSize;

	// total rows and columns of cells in arena
	private int totalRows, totalCols;

	/**
	 * Make arena with specified number of rows and columns
	 */
	public ArenaArray(int size, int rows, int cols) {
		this.totalRows = rows * size + rows + 1;
		this.totalCols = cols * size + cols + 1;
		arena = new String[totalRows + 1][totalCols + 1];
		this.boxSize = size;

		// make everything empty for now
		for (int r = 0; r <= totalRows; r++) {
			for (int c = 0; c <= totalCols; c++) {
				arena[r][c] = "O";
			}
		}

		// make vertical walls
		for (int r = 0; r <= totalRows; r++) {
			for (int c = 0; c <= cols; c++) {
				arena[r][c * boxSize + c] = "X";
			}
		}

		// make horizontal walls
		for (int c = 0; c <= totalCols; c++) {
			for (int r = 0; r <= rows; r++) {
				arena[r * boxSize + r][c] = "X";
			}
		}
	}

	/**
	 * Make bridge between specified boxes
	 * 
	 * pre: (box2Row >= box1Row) && (box2Col >= box1Col)
	 * 
	 * @param box1Row
	 * @param box1Col
	 * @param box2Row
	 * @param box2Col
	 */
	public void addBridge(int box1Row, int box1Col, int box2Row, int box2Col) {
		int rPos, cPos;

		if (box1Row == box2Row) {
			rPos = box2Row * boxSize + box2Row + boxSize / 2 + 1;
		} else {
			rPos = box2Row * boxSize + box2Row;
		}

		if (box1Col == box2Col) {
			cPos = box2Col * boxSize + box2Col + boxSize / 2 + 1;
		} else {
			cPos = box2Col * boxSize + box2Col;
		}

		arena[rPos][cPos] = "B";
	}

	/**
	 * Make nest in specified box
	 */
	public void addNest(int boxRow, int boxCol, String nestName) {
		int rPos = (boxRow * boxSize) + boxRow + boxSize / 2 + 1;
		int cPos = (boxCol * boxSize) + boxCol + boxSize / 2 + 1;

		arena[rPos][cPos] = "N" + nestName;
	}

	/**
	 * @return number of rows in the game
	 */
	public int totalRows() {
		return totalRows;
	}

	/**
	 * @return number of columns in the game
	 */
	public int totalCols() {
		return totalCols;
	}

	/**
	 * @return array to represent the arena
	 */
	public String[][] getArray() {
		return arena;
	}
}
