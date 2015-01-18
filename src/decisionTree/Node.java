package decisionTree;

import java.util.ArrayList;

import com.magda.movies.Evaluation;

public class Node {
	private Node parent;
	public Node[] children;
	private double entropy;
	private Attribute testAttribute;
	
	private boolean isLeaf;
	private int predictedValue;
	public Node() {
		setEntropy(0.0);
		setParent(null);
		setChildren(null);
		
		isLeaf = false;
		//setTestAttribute();
	}

	public Attribute getTestAttribute() {
		return testAttribute;
	}

	public void setTestAttribute(Attribute testAttribute) {
		this.testAttribute = testAttribute;
	}



	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return parent;
	}


	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Node[] getChildren() {
		return children;
	}

	public int getPredictedValue() {
		return predictedValue;
	}

	public void setPredictedValue(int predictedValue) {
		this.predictedValue = predictedValue;
	}


}
