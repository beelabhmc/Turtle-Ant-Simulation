package turtleants;

public class Activity {

	private int antID;
	private long time;
	private boolean nestEntered, bridgeCrossed;
	private int row, col;

	public Activity(long time, int antID, boolean nestEntered, boolean bridgeCrossed, int row, int col) {
		this.time = time;
		this.antID = antID;
		this.nestEntered = nestEntered;
		this.bridgeCrossed = bridgeCrossed;
		this.row = row;
		this.col = col;
	}

	public int getID() {
		return antID;
	}

	public long getTime() {
		return time;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean getNestEntered() {
		return nestEntered;
	}

	public boolean getBridgeCrossed() {
		return bridgeCrossed;
	}

}
