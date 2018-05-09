package turtleants;

public class NestOccupation {
	private int numAnts;
	private String nestName;
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
