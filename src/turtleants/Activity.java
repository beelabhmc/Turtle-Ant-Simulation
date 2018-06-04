package turtleants;

/**
 * Stores the data for an activity (bridge crossing or nest enter).
 * 
 * @author Joanna
 *
 */
public class Activity {

	// ID of ant that performed the activity
	private int antID;

	// time the activity occurred
	private long time;

	// whether a nest was entered or if a bridge was crossed
	private boolean nestEntered, bridgeCrossed;

	// where the activity occurred
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
