package turtleants;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This is a arena made up of cells. The cells can be empty, bridges, walls, or
 * nests. Ants can travel over empty and bridge cells, as well as go into nests.
 * 
 * @author Joanna Chang
 */
public class Arena extends JPanel {

	// number of rows and cols, and size of each cell and box (# cells in each box)
	public static final int BOXSIZE = 7, // should be odd number
			ROWS = 4, COLS = 4, CELLSIZE = 10;

	// integers to describe the type of cell
	public static final int EMPTY = 0, WALL = 1, BRIDGE = 2, NEST = 3;

	// array of cells that represents the arena
	private Cell[][] theArena;

	// array of characters to represent the arena
	private ArenaArray arenaArray;

	private List<Cell> theNests = new ArrayList<Cell>();

	// number and list of ants in simulation
	public List<Ant> ANTS = new ArrayList<Ant>();
	private List<Activity> activity = new ArrayList<Activity>();
	private List<NestOccupation> nestOccupation = new ArrayList<NestOccupation>();
	private final int NUM_ANTS = 100;

	// if the simulation has started
	private boolean started = false;
	private int numRounds = 0;
	private int numSimulations = 0;

	// speed of ants and chance that they will stay in a nest,
	// or move on to the next cell
	private final int SPEED = 0;
	private final int CHANCE_IN = 6000, CHANCE_MOVE_ON = 5000;

	private static final int RUNTIME = 2; // in minutes
	private long startTime;

	private static final int TOTAL_ROUNDS = 3000;
	private static final int TOTAL_SIMULATIONS = 50;

	private final String FILE_NAME = "Summer-Pheromone-100Ants.xls";

	/**
	 * Start the simulation. Make the arena and initialize it. Add ants inside.
	 */
	public void simulate() {
		started = true;

		// create the arena and initialize it
		theArena = new Cell[ROWS * BOXSIZE * ROWS + 2][COLS * BOXSIZE + COLS + 2];
		makeArena();
		initArena();

		startTime = System.currentTimeMillis();

		for (int i = 0; i < NUM_ANTS; i++) {
			Ant a = new Ant(i, startTime, activity, BOXSIZE / 2, BOXSIZE / 2, theArena, this, SPEED, CHANCE_IN,
					CHANCE_MOVE_ON);
			ANTS.add(a);
			a.start();
		}

		repaint();
	}

	/**
	 * make the array for the arena. add bridges and nests
	 */
	public void makeArena() {
		arenaArray = new ArenaArray(BOXSIZE, ROWS, COLS);

		// add bridges to the arena
		arenaArray.addBridge(0, 0, 0, 1);
		arenaArray.addBridge(0, 0, 1, 0);
		arenaArray.addBridge(0, 1, 0, 2);
		arenaArray.addBridge(0, 2, 0, 3);
		arenaArray.addBridge(0, 2, 1, 2);
		arenaArray.addBridge(0, 3, 1, 3);
		arenaArray.addBridge(1, 0, 2, 0);
		arenaArray.addBridge(1, 2, 1, 3);
		arenaArray.addBridge(0, 2, 1, 3);
		// arenaArray.addBridge(0,3,1,2);
		arenaArray.addBridge(2, 0, 3, 0);
		arenaArray.addBridge(2, 1, 3, 1);
		arenaArray.addBridge(2, 2, 3, 2);
		arenaArray.addBridge(3, 0, 3, 1);
		arenaArray.addBridge(2, 1, 2, 2);
		arenaArray.addBridge(3, 2, 3, 3); //Summer only

		// add nests to the arena

		// Summer R Nests
		arenaArray.addNest(0, 2, "R1"); // Summer only
		arenaArray.addNest(0, 3, "R2");
		arenaArray.addNest(1, 2, "R3");
		arenaArray.addNest(1, 3, "R4");

		// Summer D nests
		arenaArray.addNest(2, 0, "D1");
		arenaArray.addNest(3, 1, "D2");
		arenaArray.addNest(2, 2, "D3");
		arenaArray.addNest(3, 3, "D4");

//		// Fall R nests
//		arenaArray.addNest(0, 3, "R1");
//		arenaArray.addNest(1, 2, "R2");
//		arenaArray.addNest(1, 3, "R3");
//
//		// Fall D nests
//		arenaArray.addNest(3, 0, "D1");
//		arenaArray.addNest(2, 1, "D2");
//		arenaArray.addNest(3, 2, "D3");

		// arenaArray.addNest(2, 0);
		// arenaArray.addNest(3, 1);
		// arenaArray.addNest(2, 2);
		// arenaArray.addNest(3, 3);

	}

	/**
	 * initialize the arena based on the arena array. add cells, specifying their
	 * types.
	 */
	private void initArena() {
		String[][] a = arenaArray.getArray();

		for (int row = 0; row <= arenaArray.totalRows(); row++) {
			for (int col = 0; col <= arenaArray.totalCols(); col++) {
				if (a[row][col] == "O") {
					addCell(EMPTY, row, col, null);
				} else if (a[row][col] == "X") {
					addCell(WALL, row, col, null);
				} else if (a[row][col] == "B") {
					addCell(BRIDGE, row, col, null);
				} else if (a[row][col].charAt(0) == 'N') {
					addCell(NEST, row, col, a[row][col].substring(1));
					theNests.add(theArena[row][col]);
				}
			}
		}
	}

	/**
	 * Paint the arena and ants on the screen after any move.
	 */
	public void paint(Graphics g) {

		// paint only if the simulation has started
		if (started) {

			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;

			for (int row = 0; row <= arenaArray.totalRows(); row++) {
				for (int col = 0; col <= arenaArray.totalCols(); col++) {
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
					if (theArena[row][col].visited()) {
						theArena[row][col].addTime();
						// if (theArena[row][col].visited()) {
						// Rectangle2D.Double c;
						// c = new Rectangle2D.Double(col * CELLSIZE, row * CELLSIZE, CELLSIZE,
						// CELLSIZE);
						//
						// if (theArena[row][col].isNest()) {
						// g2.setPaint(Color.GREEN);
						// } else {
						// g2.setPaint(Color.BLUE);
						// }
						// g2.fill(c);
						// }
					}
				}
			}

			// for (Ant a : ANTS) {
			// Rectangle2D.Double c;
			// c = new Rectangle2D.Double(a.getCol() * CELLSIZE, a.getRow() * CELLSIZE,
			// CELLSIZE, CELLSIZE);
			// g2.setPaint(Color.RED);
			// g2.fill(c);
			// }

			if (numRounds >= TOTAL_ROUNDS) {
				for (Ant a : ANTS) {
					a.stopAnt();
				}

				for (Cell nest : theNests) {
					int numAnts = 0;
					for (Ant a : ANTS) {
						int r = a.getRow();
						int c = a.getCol();
						if (theArena[r][c] == nest) {
							numAnts++;
						}
					}
					nestOccupation
							.add(new NestOccupation(nest.getNestName(), numAnts, numSimulations, nest.isNegative()));
				}

				System.out.println("Simulation: " + numSimulations);
				numSimulations++;

				if (numSimulations <= TOTAL_SIMULATIONS) {
					reset();
				} else {
					makeExcelNests(FILE_NAME);
				}
				numRounds = 0;

			}

			numRounds++;
		}

		// if(System.currentTimeMillis()-startTime > RUNTIME*60*1000) {
		// for (Ant a : ants) {
		// a.stopAnt();
		// }
		// makeExcel(activity, FILE_NAME);
		// }

	}

	public void reset() {
		ANTS = new ArrayList<Ant>();
		for (int i = 0; i < NUM_ANTS; i++) {
			Ant a = new Ant(i, startTime, activity, BOXSIZE / 2, BOXSIZE / 2, theArena, this, SPEED, CHANCE_IN,
					CHANCE_MOVE_ON);
			ANTS.add(a);
			a.start();
		}
		for (int row = 0; row <= arenaArray.totalRows(); row++) {
			for (int col = 0; col <= arenaArray.totalCols(); col++) {
				theArena[row][col].reset();
			}
		}
		repaint();
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
	public void addCell(int type, int row, int col, String name) {
		theArena[row][col] = new Cell(type, row, col, name);
	}

	public void makeExcelActivity(List<Activity> activityList, String excelFilePath) {
		Workbook workbook = new HSSFWorkbook();
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet();
		createHeaderRow(sheet);

		int rowCount = 0;
		for (Activity a : activityList) {
			Row row = sheet.createRow(++rowCount);
			writeActivity(a, row);
		}

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
			workbook.write(outputStream);
		} catch (Exception IOException) {
			System.out.println("File not found");
		}
	}

	public void makeExcelNests(String excelFilePath) {
		Workbook workbook = new HSSFWorkbook();
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet();
		createNestHeaderRow(sheet);

		int rowCount = 0;
		for (NestOccupation n : nestOccupation) {
			Row row = sheet.createRow(++rowCount);
			writeNestOccupation(n, row);
		}

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
			workbook.write(outputStream);
		} catch (Exception IOException) {
			System.out.println("File not found");
		}
	}

	private void writeNestOccupation(NestOccupation n, Row row) {
		org.apache.poi.ss.usermodel.Cell cell = row.createCell(1);
		cell.setCellValue(n.getName());

		cell = row.createCell(2);
		cell.setCellValue(n.getAnts());

		cell = row.createCell(3);
		cell.setCellValue("" + n.getName().charAt(0));

		cell = row.createCell(4);
		cell.setCellValue(Cell.PHEROMONE_STRENGTH);

		cell = row.createCell(5);
		cell.setCellValue(n.getSimulation());

		cell = row.createCell(6);
		cell.setCellValue(n.getIsNeg());
	}

	private void writeActivity(Activity a, Row row) {
		org.apache.poi.ss.usermodel.Cell cell = row.createCell(1);
		cell.setCellValue(a.getTime());

		cell = row.createCell(2);
		cell.setCellValue(a.getID());

		cell = row.createCell(3);
		cell.setCellValue(a.getNestEntered());

		cell = row.createCell(4);
		cell.setCellValue(a.getBridgeCrossed());

		cell = row.createCell(5);
		cell.setCellValue(a.getRow());

		cell = row.createCell(6);
		cell.setCellValue(a.getCol());
	}

	private void createNestHeaderRow(org.apache.poi.ss.usermodel.Sheet sheet) {

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		cellStyle.setFont(font);

		Row row = sheet.createRow(0);
		org.apache.poi.ss.usermodel.Cell cellNest = row.createCell(1);

		cellNest.setCellStyle(cellStyle);
		cellNest.setCellValue("Nest");

		org.apache.poi.ss.usermodel.Cell cellNumAnts = row.createCell(2);
		cellNumAnts.setCellStyle(cellStyle);
		cellNumAnts.setCellValue("Number of Ants ");

		org.apache.poi.ss.usermodel.Cell cellNestType = row.createCell(3);
		cellNestType.setCellStyle(cellStyle);
		cellNestType.setCellValue("Nest Type");

		org.apache.poi.ss.usermodel.Cell cellPheromone = row.createCell(4);
		cellPheromone.setCellStyle(cellStyle);
		cellPheromone.setCellValue("Pheromone Strength");

		org.apache.poi.ss.usermodel.Cell cellSimulation = row.createCell(5);
		cellSimulation.setCellStyle(cellStyle);
		cellSimulation.setCellValue("Simulation");
	}

	private void createHeaderRow(org.apache.poi.ss.usermodel.Sheet sheet) {

		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font = sheet.getWorkbook().createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		cellStyle.setFont(font);

		Row row = sheet.createRow(0);
		org.apache.poi.ss.usermodel.Cell cellTime = row.createCell(1);

		cellTime.setCellStyle(cellStyle);
		cellTime.setCellValue("Time");

		org.apache.poi.ss.usermodel.Cell cellAuthor = row.createCell(2);
		cellAuthor.setCellStyle(cellStyle);
		cellAuthor.setCellValue("Ant ID");

		org.apache.poi.ss.usermodel.Cell cellPrice = row.createCell(3);
		cellPrice.setCellStyle(cellStyle);
		cellPrice.setCellValue("Nest Entered?");

		org.apache.poi.ss.usermodel.Cell cellBridge = row.createCell(4);
		cellBridge.setCellStyle(cellStyle);
		cellBridge.setCellValue("Bridge Crossed?");

		org.apache.poi.ss.usermodel.Cell cellRow = row.createCell(5);
		cellRow.setCellStyle(cellStyle);
		cellRow.setCellValue("Row");

		org.apache.poi.ss.usermodel.Cell cellCol = row.createCell(6);
		cellCol.setCellStyle(cellStyle);
		cellCol.setCellValue("Col");
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
		f.setSize(CELLSIZE * ROWS * BOXSIZE + 350, CELLSIZE * COLS * BOXSIZE + 350);
		f.setVisible(true);
		a.simulate();
	}

}
