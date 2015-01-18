package decisionTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.magda.movies.Evaluation;

public class DecisionTreePredictor {
	private String[][] features;

	private int fileEnding;

	public DecisionTreePredictor(String[][] features) {
		this.features = features;
		try (BufferedReader brTask = new BufferedReader(new FileReader(
				"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\task.csv"))) {
			BufferedReader brTrain = new BufferedReader(new FileReader(
					"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\train.csv"));
			FileWriter fOutput = new FileWriter(
					"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\resulttree3.csv");

			// read training set from file to ArrayList
			String currentTrainLine;
			ArrayList<Evaluation> trainSet = new ArrayList<Evaluation>();
			while ((currentTrainLine = brTrain.readLine()) != null) {
				String[] fieldsTrain = currentTrainLine.split(";");
				trainSet.add(new Evaluation(Integer.valueOf(fieldsTrain[1]),
						Integer.valueOf(fieldsTrain[2]), Integer
								.valueOf(fieldsTrain[3])));
			}

			// spreading List - one List of evaluations per user
			HashMap<Integer, ArrayList<Evaluation>> processedMap = new HashMap<Integer, ArrayList<Evaluation>>(); // mapping:
																													// userId
																													// ->
																													// list
																													// of
			HashMap<Integer, Node> treeMap = new HashMap<Integer, Node>(); // mapping:
																			// evaluations
			int rememberUserId = -1;
			ArrayList<Evaluation> userSet = new ArrayList<Evaluation>();
			for (int i = 0; i < trainSet.size(); i++) {
				Evaluation e = trainSet.get(i);
				int userId = e.getUserId();
				if (userId == rememberUserId || rememberUserId == -1) {
					userSet.add(e);
				} else {
					processedMap.put(rememberUserId, userSet);
					ArrayList<Attribute> attributes = AttributeHandler
							.createUserAttribute(userSet, this.features);
					Node root = new DecisionTree(this.features).buildTree(
							userSet, attributes);
					treeMap.put(rememberUserId, root);
					userSet = new ArrayList<Evaluation>();
					userSet.add(e);
				}
				rememberUserId = userId;
			}
			processedMap.put(rememberUserId, userSet);
			System.out.println(processedMap.size());
			ArrayList<Attribute> attributes = AttributeHandler
					.createUserAttribute(userSet, this.features);

			// for(Attribute attribute: attributes){
			// System.out.println(attribute.name + " " +
			// attribute.featureIndex);
			// }
			Node root = new DecisionTree(this.features).buildTree(userSet,
					attributes);
			treeMap.put(rememberUserId, root);


			// read from task file and write to a new file with predicted result
			String currentTaskLine;
			while ((currentTaskLine = brTask.readLine()) != null) {
				int predictedResult = 0;
				String[] fieldsTask = currentTaskLine.split(";");
				int userId = Integer.valueOf(fieldsTask[1]);
				int movieId = Integer.valueOf(fieldsTask[2]);

				predictedResult = predictResult(treeMap.get(userId),movieId);

				fOutput.write(fieldsTask[0] + ";" + fieldsTask[1] + ";"
						+ fieldsTask[2] + ";" + predictedResult + "\n");
			}

			fOutput.close();
			brTrain.close();
			brTask.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int predictResult(Node root, int movieId) {

		movieId--;
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
	
	private void printTree(Node root){
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
					 System.out.print((currentNode.getTestAttribute()!=null?currentNode.getTestAttribute().getName()
					 : currentNode.getPredictedValue()) + " ||| ");
					 }
					 System.out.println();
					 currentLevel = nextLevel;
					 nextLevel = new LinkedList<Node>();
					
					 }
	}

}
