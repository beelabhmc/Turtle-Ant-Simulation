package turtleants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is a arena made up of cells.
 * 
 * @author Joanna Chang
 */
public class Arena extends JPanel {

	// number of rows and cols, and size of each box and cell
	public static final int BOXSIZE = 10, ROWS = 4, COLS = 4, CELLSIZE = 10;

	public static final int EMPTY = 0, WALL = 1, BRIDGE = 2, NEST = 3;

	// array of cells that represents the arena
	private Cell[][] theArena;

	// number and list of ants in simulation
	private List<Ant> ants = new ArrayList<Ant>();
	private final int NUM_ANTS = 20;

	private boolean started = false, setup = false;

	private ArenaArray arenaArray;

	/**
	 * Constructor for a Maze. <br>
	 * 
	 * @param canvas
	 *            the canvas the maze is drawn on
	 * @param controller
	 *            the game controller
	 */
	public void simulate() {
		started = true;

		// create the arena
		theArena = new Cell[ROWS * BOXSIZE][COLS * BOXSIZE];

		makeArena();
		initArena();

//		for (int row = 0; row < ROWS * BOXSIZE; row++) {
//			for (int col = 0; col < COLS * BOXSIZE; col++) {
//				if (theArena[row][col].isWall()) {
//					Rectangle2D.Double c;
//					c = new Rectangle2D.Double(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
//				} else if (theArena[row][col].isNest()) {
//					Rectangle2D.Double c;
//					c = new Rectangle2D.Double(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
//				}
//			}
//		}
		repaint();
	}

	public void makeArena() {
		arenaArray = new ArenaArray(BOXSIZE, ROWS, COLS);
		arenaArray.addBridge(0, 0, 0, 1);
//		arenaArray.addBridge(0, 0, 1, 0);
//		arenaArray.addBridge(0, 1, 0, 2);
//		arenaArray.addBridge(0, 2, 0, 3);
//		arenaArray.addBridge(0, 2, 1, 2);
//		arenaArray.addBridge(0, 3, 1, 3);
//		arenaArray.addBridge(1, 0, 2, 0);
//		arenaArray.addBridge(1, 2, 1, 3);
//		arenaArray.addBridge(0, 2, 1, 3);
//		// arenaArray.addBridge(0,3,1,2);
//		arenaArray.addBridge(2, 0, 3, 0);
//		arenaArray.addBridge(2, 1, 3, 1);
//		arenaArray.addBridge(2, 2, 3, 2);
//		arenaArray.addBridge(3, 0, 3, 1);
//		arenaArray.addBridge(2, 1, 2, 2);
//		arenaArray.addBridge(3, 2, 3, 3);
		arenaArray.addNest(0, 2);
		arenaArray.addNest(0, 3);
		arenaArray.addNest(1, 2);
		arenaArray.addNest(1, 3);
		arenaArray.addNest(2, 0);
		arenaArray.addNest(3, 1);
		arenaArray.addNest(2, 2);
		arenaArray.addNest(3, 3);

	}

	private void initArena() {
		char[][] a = arenaArray.getArray();
		System.out.println("for the array" + a[0][0]);

		for (int row = 0; row < arenaArray.totalRows(); row++) {
			for (int col = 0; col < arenaArray.totalCols(); col++) {
				if (a[row][col] == 'O') {
					System.out.println("this is:" +row+" "+col);
					addCell(EMPTY, row, col);
				} else if (a[row][col] == 'X') {
					addCell(WALL, row, col);
				} else if (a[row][col] == 'B') {
					addCell(BRIDGE, row, col);
				} else if (a[row][col] == 'N') {
					addCell(NEST, row, col);
				}
			}
		}
		System.out.println("here it is" + theArena[0][1]);

	}

	/**
	 * Paint the wood chips and termites on the screen after any move.
	 */
	public void paint(Graphics g) {

		// paint only if the simulation has started
		if (started) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;

			if (!setup) {
				for (int row = 0; row < ROWS * BOXSIZE; row++) {
					for (int col = 0; col < COLS * BOXSIZE; col++) {
						System.out.println("running");
						System.out.println(row + " " + col);
						System.out.println(theArena[row][col]);
						if (theArena[row][col].isWall()) {
							Rectangle2D.Double c;
							c = new Rectangle2D.Double(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
							g2.setPaint(Color.BLACK);
							g2.fill(c);
						} else if (theArena[row][col].isNest()) {
							Rectangle2D.Double c;
							c = new Rectangle2D.Double(col * CELLSIZE, row * CELLSIZE, CELLSIZE, CELLSIZE);
							g2.setPaint(Color.BLUE);
							g2.fill(c);
						}
					}

		
				}
				setup = true;
			}
			// for (Ant a : ant) {
			// Rectangle2D.Double c;
			// c = new Rectangle2D.Double(t.getCol() * CELLSIZE, t.getRow() * CELLSIZE,
			// CELLSIZE, CELLSIZE);
			// g2.setPaint(Color.RED);
			// g2.fill(c);
			// }
		}
	}

	/**
	 * Add a cell to the arena.
	 * 
	 * @param type
	 *            type of the cell
	 * @param row
	 *            row the cell is in the arena
	 * @param col
	 *            column the cell is in the arena
	 */
	public void addCell(int type, int row, int col) {
		theArena[row][col] = new Cell(type, row, col);
	}

	/**
	 * Create frame and Arena panel in the frame.
	 * 
	 * @param s
	 */
	public static void main(String[] s) {
		JFrame f = new JFrame("Ant Simulation");
		Arena a = new Arena();
		f.add(a);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(CELLSIZE * ROWS * BOXSIZE, CELLSIZE * COLS * BOXSIZE);
		f.setVisible(true);
		a.simulate();
	}

}
