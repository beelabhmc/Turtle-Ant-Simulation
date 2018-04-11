package turtleants;

public class ArenaArray {
	private char[][] arena;
	private int boxSize;
	private int rows;
	private int cols;
	private int totalRows, totalCols;

	/**
	 * makes arena with specified number of rows and columns
	 */
	public ArenaArray(int size, int rows, int cols) {
		arena = new char[size * rows][size * cols];
		this.rows = rows;
		this.cols = cols;
		this.boxSize = size;
		this.totalRows = rows * size;
		this.totalCols = cols * size;

		// make everything empty for now
		for (int r = 0; r < totalRows; r++) {
			System.out.println("making Os");

			for (int c = 0; c < totalCols; c++) {
				arena[r][c] = 'O';
			}
		}

		// make vertical walls
		for (int r = totalRows; r >= 0; r--) {
			for (int c = cols; c >= 0; c--) {
				if (c == 0) {
					arena[r][c * boxSize] = 'X';
				} else {
					arena[r][c * boxSize - 1] = 'X';
				}
			}
		}

		// make horizontal walls
		for (int c = totalCols; c >= 0; c--) {
			for (int r = rows; r >= 0; r--) {
				if (r == 0) {
					arena[r * boxSize][c] = 'X';

				} else {
					arena[r * boxSize - 1][c] = 'X';
				}
			}
		}

	}

	/**
	 * make bridge between specified boxes
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
			rPos = box2Row * boxSize + boxSize / 2;
		} else {
			rPos = box2Row * boxSize;
		}

		if (box1Col == box2Col) {
			cPos = box2Col * boxSize + boxSize / 2;
		} else {
			cPos = box2Col * boxSize;
		}

		System.out.print(rPos);
		System.out.print(cPos);

		arena[rPos][cPos] = 'B';
	}

	/**
	 * make nest in specified box
	 */
	public void addNest(int boxRow, int boxCol) {
		int rPos = (boxRow * boxSize) + boxSize / 2;
		int cPos = (boxCol * boxSize) + boxSize / 2;

		arena[rPos][cPos] = 'N';
	}

	/**
	 * @return number of rows in the game
	 */
	public int totalRows() {
		return rows * boxSize;
	}

	/**
	 * @return number of columns in the game
	 */
	public int totalCols() {
		return cols * boxSize;
	}

	public char[][] getArray() {
		return arena;
	}
}
