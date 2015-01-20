package decisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.magda.movies.Evaluation;
import com.magda.movies.TAndVSetInitializor;

public class DecisionTreePredictor {
	private String[][] features;

	public DecisionTreePredictor(String[][] features, boolean featureSelection) {
		this.features = features;
		HashMap<Integer, ArrayList<Evaluation>> processedMap = new HashMap<Integer, ArrayList<Evaluation>>(); // mapping:
		HashMap<Integer, ArrayList<Attribute>> userAttributes = new HashMap<Integer, ArrayList<Attribute>>(); // ->

		prepareProcessMap(processedMap, DataReader.readTrainSet());
		prepareAttributes(userAttributes, processedMap);
		if (featureSelection) {
			decisionTreeWithFeatureSelection(processedMap, userAttributes);
		} else {
			decisionTreeWithoutFeatureSelection(processedMap, userAttributes);
		}

	}

	private void decisionTreeWithoutFeatureSelection(
			HashMap<Integer, ArrayList<Evaluation>> processedMap,
			HashMap<Integer, ArrayList<Attribute>> userAttributes) {
		HashMap<Integer, Node> treeMap = new HashMap<Integer, Node>();
		createAllTrees(userAttributes, treeMap, processedMap);
	
		predictAndSave(treeMap, "without");

	}

	public void decisionTreeWithFeatureSelection(
			HashMap<Integer, ArrayList<Evaluation>> processedMap,
			HashMap<Integer, ArrayList<Attribute>> userAttributes) {

		DataSpliter data = new DataSpliter(processedMap);
		HashMap<Integer, ArrayList<Evaluation>> train = data.getTrainMap();
		
		HashMap<Integer, ArrayList<Attribute>> selected = new HashMap<Integer, ArrayList<Attribute>> ();
		HashMap<Integer, ArrayList<Evaluation>> validation = data
				.getValidationMap();

		HashMap<Integer, Node> treeMap = new HashMap<Integer, Node>();

		for (Integer userId : train.keySet()) {
		
			
				ArrayList<Attribute> userAllAttr = userAttributes.get(userId);
				ArrayList<Attribute> selectedAttr = new ArrayList<Attribute>(userAllAttr);
				ArrayList<Attribute> selectedAttrTemp = new ArrayList<Attribute>(selectedAttr);

				double bestQ = 0;
				Node bestTree = null;
				while (selectedAttr.size()!=1) {
					
					double previousBestQ = bestQ;
					
					for (Attribute attribute : userAllAttr) {
						ArrayList<Attribute> testedAttr = new ArrayList<Attribute>(
								selectedAttr);
						testedAttr.remove(attribute);
						
						ArrayList<Attribute> attToTree = new ArrayList<Attribute>(testedAttr);
						Node node = createUserTree(attToTree,
								train.get(userId));
					
						//printTree(node);
						double Q = testData(validation.get(userId), node);
						
						
						if (Q > bestQ) {
							bestQ = Q;
							System.out.println(bestQ + " " + attribute.getName());
							bestTree = node;
							
							selectedAttrTemp = testedAttr;
							System.out.println("size " +testedAttr.size());
							//System.out.println("size " +selectedAttrTemp.size());
						}
					}
					
					selectedAttr = selectedAttrTemp;
					if (previousBestQ == bestQ) {
						break;
					}
					
					
				}
				Node node = createUserTree(selectedAttr,
						processedMap.get(userId));
				treeMap.put(userId, node);
				selected.put(userId, selectedAttr);
			
		}
	
		predictAndSave(treeMap, "with");
		
	}

	private double testData(ArrayList<Evaluation> validation, Node root) {
		double Q = 0;
		int correctGuesses = 0;
		for (Evaluation eval : validation) {

			int predictedResult = predictResult(root, eval.getMovieId());
			//System.out.println(predictedResult + " " + eval.getEvaluation());
			if (predictedResult == eval.getEvaluation())// (Math.abs(predictedResult-realScore)<=1)
				correctGuesses++;
		}
		Q = (double)correctGuesses / (double)validation.size();
		return Q;

	}

	private void predictAndSave(HashMap<Integer, Node> treeMap, String filename) {

		try (BufferedReader brTask = new BufferedReader(new FileReader(
				"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\task.csv"))) {

			FileWriter fOutput = new FileWriter(
					"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\"+filename+".csv");
			String currentTaskLine;
			while ((currentTaskLine = brTask.readLine()) != null) {
				int predictedResult = 0;
				String[] fieldsTask = currentTaskLine.split(";");
				int userId = Integer.valueOf(fieldsTask[1]);
				int movieId = Integer.valueOf(fieldsTask[2]);
				movieId--;
				
					predictedResult = predictResult(treeMap.get(userId),
							movieId);
					fOutput.write(fieldsTask[0] + ";" + fieldsTask[1] + ";"
							+ fieldsTask[2] + ";" + predictedResult + "\n");
				
			}

			fOutput.close();

			brTask.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void prepareProcessMap(
			HashMap<Integer, ArrayList<Evaluation>> processedMap,
			ArrayList<Evaluation> trainSet) {
		int rememberUserId = -1;
		ArrayList<Evaluation> userSet = new ArrayList<Evaluation>();
		for (int i = 0; i < trainSet.size(); i++) {
			Evaluation e = trainSet.get(i);
			int userId = e.getUserId();
			if (userId == rememberUserId || rememberUserId == -1) {
				userSet.add(e);
			} else {
				processedMap.put(rememberUserId, userSet);

				userSet = new ArrayList<Evaluation>();
				userSet.add(e);
			}
			rememberUserId = userId;
		}
		processedMap.put(rememberUserId, userSet);
	}

	private void prepareAttributes(
			HashMap<Integer, ArrayList<Attribute>> userAttributes,
			HashMap<Integer, ArrayList<Evaluation>> processedMap) {
		for (Integer userId : processedMap.keySet()) {

			ArrayList<Evaluation> userSet = processedMap.get(userId);
			ArrayList<Attribute> attributes = AttributeHandler
					.createUserAttribute(userSet, this.features);
			userAttributes.put(userId, attributes);
		}
	}

	private void createAllTrees(
			HashMap<Integer, ArrayList<Attribute>> userAttributes,
			HashMap<Integer, Node> treeMap,
			HashMap<Integer, ArrayList<Evaluation>> processedMap) {

		for (Integer userId : processedMap.keySet()) {
			ArrayList<Evaluation> userSet = processedMap.get(userId);
			ArrayList<Attribute> attributes = userAttributes.get(userId);
			Node root = createUserTree(attributes, userSet);
			treeMap.put(userId, root);
		}

	}

	private Node createUserTree(ArrayList<Attribute> attributes,
			ArrayList<Evaluation> userSet) {
		Node root = new DecisionTree(this.features).buildTree(userSet,
				attributes);
		return root;
	}

	private int predictResult(Node root, int movieId) {

		
		// result = root.getPredictedValue();
		while (!root.isLeaf()) {
			Attribute attribute = root.getTestAttribute();
		
			if (attribute instanceof BooleanAttribute) {

				if (features[movieId][attribute.getFeatureIndex()]
						.equals("true")) {
					root = root.children[0];

				} else if (features[movieId][attribute.getFeatureIndex()]
						.equals("false")) {
					root = root.children[1];

				}

			} else if (attribute instanceof CollectionAttribute) {
				if (features[movieId][attribute.getFeatureIndex()]
						.contains(((CollectionAttribute) attribute).getValue())) {
					root = root.children[0];

				} else {
					root = root.children[1];

				}

			} else if (attribute instanceof ContinuousAttribute) {
				double value;
				if (((ContinuousAttribute) attribute).isDate()) {
					value = Double.valueOf(features[movieId][attribute
							.getFeatureIndex()].split("-")[0]);
				} else {
					
					value = Double.parseDouble(features[movieId][attribute
							.getFeatureIndex()]);
				}
				if (value < ((ContinuousAttribute) attribute).getThreshold()) {
					root = root.children[0];

				} else {
					root = root.children[1];

				}
			}

		}
		return root.getPredictedValue();
	}

	private void printTree(Node root) {
		Queue<Node> currentLevel = new LinkedList<Node>();
		Queue<Node> nextLevel = new LinkedList<Node>();

		currentLevel.add(root);

		while (!currentLevel.isEmpty()) {
			Iterator<Node> iter = currentLevel.iterator();
			while (iter.hasNext()) {
				Node currentNode = iter.next();
				if (currentNode.getChildren() != null) {
					nextLevel.add(currentNode.getChildren()[0]);
				}
				if (currentNode.getChildren() != null) {
					nextLevel.add(currentNode.getChildren()[1]);
				}
				System.out
						.print((currentNode.getTestAttribute() != null ? currentNode
								.getTestAttribute().getName() : currentNode
								.getPredictedValue())
								+ " ||| ");
			}
			System.out.println();
			currentLevel = nextLevel;
			nextLevel = new LinkedList<Node>();

		}
	}

}
