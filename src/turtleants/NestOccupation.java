package turtleants;

/** 
 * Stores the data for nest occupation
 * @author Joanna
 *
 */
public class NestOccupation {
	
	//number of ants in the nest
	private int numAnts;
	
	//name of the nest
	private String nestName;
	
	//number of simulations that occurred
	private int numSimulation;
	
	
	private boolean isNeg;

	public NestOccupation(String nestName, int numAnts, int numSimulation, boolean isNeg) {
		this.numAnts = numAnts;
		this.nestName = nestName;
		this.numSimulation = numSimulation;
		this.isNeg = isNeg;
	}

	public int getAnts() {
		return numAnts;
	}

	public String getName() {
		return nestName;
	}

	public int getSimulation() {
		return numSimulation;
	}

	public boolean getIsNeg() {
		return isNeg;
	}
}
