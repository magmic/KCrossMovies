package decisionTree;

public class Attribute {
	protected int featureIndex;
	
	protected String name;

	protected int numOfBranches=2;
	protected String[] possibleOutcomes; 
	public Attribute() {
	}

	public Attribute(int featureIndex, String name) {
		super();
		this.featureIndex = featureIndex;
		this.numOfBranches=2;
		this.name = name;
	}

	public String[] getPossibleOutcomes() {
		return possibleOutcomes;
	}

	public void setPossibleOutcomes(String[] possibleOutcomes) {
		this.possibleOutcomes = possibleOutcomes;
	}

	


	public int getNumOfBranches() {
		return numOfBranches;
	}

	public void setNumOfBranches(int numOfBranches) {
		this.numOfBranches = numOfBranches;
	}
	public int getFeatureIndex() {
		return featureIndex;
	}

	public void setFeatureIndex(int featureIndex) {
		this.featureIndex = featureIndex;
	}

	
	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	

}
