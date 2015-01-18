package decisionTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.magda.movies.Evaluation;

public class DecisionTree {
	static Integer positive = 1;
	static Integer negative = 0;
	private String[][] features;

	public DecisionTree(String[][] features) {
		this.features = features;
	}

	public Node buildTree(ArrayList<Evaluation> evaluations,
			ArrayList<Attribute> attributes) {
		int bestAttributeIndex = -1;
		double bestGain = 0;

		ArrayList<Evaluation> positiveEvaluation = new ArrayList<Evaluation>();
		ArrayList<Evaluation> negativeEvaluation = new ArrayList<Evaluation>();

		Node root = new Node();
		int predictedValue = checkExamples(evaluations);
		if (predictedValue != -1) {
			root.setLeaf(true);
			root.setPredictedValue(predictedValue);
			return root;
		}

		if (attributes.size() == 0) {
			root.setLeaf(true);
			root.setPredictedValue(mostCommonValue(evaluations));
			return root;
		}

		root.setEntropy(Entropy.calculateEntropy(evaluations));
		// System.out.println("rentropy" + root.getEntropy()+" "
		// +attributes.size());
		for (int i = 0; i < attributes.size(); i++) {

			HashMap<Integer, ArrayList<Evaluation>> map = splitDataSet(
					attributes.get(i), evaluations, root);
			double gain = calculateGain(map, root);
			
			if (gain > bestGain) {

				positiveEvaluation = map.get(positive);
				negativeEvaluation = map.get(negative);
				bestAttributeIndex = i;
				bestGain = gain;
			}

		}

		if (bestAttributeIndex == -1) {

			root.setLeaf(true);
			root.setPredictedValue(mostCommonValue(evaluations));
			return root;
		}
		Attribute bestAttribute = attributes.get(bestAttributeIndex);
//		System.out.println("best "
//				+ attributes.get(bestAttributeIndex).getName() + " " + bestGain);
//		
		root.setTestAttribute(bestAttribute);

		Node[] children = new Node[bestAttribute.getNumOfBranches()];
		root.setChildren(children);
		for (int i = 0; i < children.length; i++) {

			ArrayList<Evaluation> subset;
			if (i == 0) {
				subset = positiveEvaluation;
			} else {
				subset = negativeEvaluation;
			}
			if (subset.size() == 0) {

				children[i] = new Node();
				children[i].setLeaf(true);
				children[i].setPredictedValue(mostCommonValue(evaluations));
			} else {
				// attributes.remove(bestAttributeIndex);
				if (attributes.contains(bestAttribute)) {
					attributes.remove(bestAttribute);
				}

				 children[i] = buildTree(subset, attributes);

			}
		}
		return root;

	}

	private double calculateGain(HashMap<Integer, ArrayList<Evaluation>> map,
			Node root) {
		double[] entropies = new double[2];
		int[] setSizes = new int[2];

		entropies[0] = Entropy.calculateEntropy(map.get(positive));

		setSizes[0] = map.get(positive).size();
		entropies[1] = Entropy.calculateEntropy(map.get(negative));
		setSizes[1] = map.get(negative).size();

		double gain = Entropy.calculateInformationGain(root.getEntropy(),
				entropies, setSizes);
		return gain;

	}

	private HashMap<Integer, ArrayList<Evaluation>> splitDataSet(
			Attribute attribute, ArrayList<Evaluation> evaluations, Node root) {

		HashMap<Integer, ArrayList<Evaluation>> map = new HashMap<Integer, ArrayList<Evaluation>>();

		if (attribute instanceof BooleanAttribute) {

			booleanSplit(attribute, evaluations, map);

		} else if (attribute instanceof CollectionAttribute) {
			collectionSplit(attribute, evaluations, map);

		} else if (attribute instanceof ContinuousAttribute) {

			continuousSplit(attribute, evaluations, map, root);
		}

		return map;
	}

	private void booleanSplit(Attribute attribute,
			ArrayList<Evaluation> evaluations,
			HashMap<Integer, ArrayList<Evaluation>> map) {

		ArrayList<Evaluation> positives = new ArrayList<Evaluation>();
		ArrayList<Evaluation> negatives = new ArrayList<Evaluation>();
		for (Evaluation evaluation : evaluations) {
			if (features[evaluation.getMovieId()][attribute.getFeatureIndex()]
					.equals("true")) {
				positives.add(evaluation);

			} else if (features[evaluation.getMovieId()][attribute
					.getFeatureIndex()].equals("false")) {
				negatives.add(evaluation);

			}
		}

		map.put(positive, positives);
		map.put(negative, negatives);

	}

	private void collectionSplit(Attribute attribute,
			ArrayList<Evaluation> evaluations,
			HashMap<Integer, ArrayList<Evaluation>> map) {

		ArrayList<Evaluation> positives = new ArrayList<Evaluation>();
		ArrayList<Evaluation> negatives = new ArrayList<Evaluation>();
		
		for (Evaluation evaluation : evaluations) {
		
				
			if (features[evaluation.getMovieId()][attribute.getFeatureIndex()]
					.contains(((CollectionAttribute) attribute).getValue())) {
				positives.add(evaluation);

			} else {
				negatives.add(evaluation);

			}

		}

		map.put(positive, positives);
		map.put(negative, negatives);
	}

	private void continuousSplit(Attribute attribute,
			ArrayList<Evaluation> evaluations,
			HashMap<Integer, ArrayList<Evaluation>> map, Node root) {

		ArrayList<Evaluation> positives = new ArrayList<Evaluation>();
		ArrayList<Evaluation> negatives = new ArrayList<Evaluation>();
		ArrayList<Evaluation> positiveEvaluation = new ArrayList<Evaluation>();
		ArrayList<Evaluation> negativeEvaluation = new ArrayList<Evaluation>();

		double bestGain = 0;
		ArrayList<Double> thresholds = ((ContinuousAttribute) attribute)
				.findThresholdsValues(evaluations, features);

		for (Double threshold : thresholds) {

			map.clear();
			positives.clear();
			negatives.clear();
			for (Evaluation evaluation : evaluations) {
				double value;
				if (!features[evaluation.getMovieId()][attribute
						.getFeatureIndex()].equals("0")) {
					if (((ContinuousAttribute) attribute).isDate()) {
						value = Double
								.valueOf(features[evaluation.getMovieId()][attribute
										.getFeatureIndex()].split("-")[0]);
					} else {
						value = Double.parseDouble(features[evaluation
								.getMovieId()][attribute.getFeatureIndex()]);
					}
					if (value < threshold) {
						positives.add(evaluation);
					} else {
						negatives.add(evaluation);

					}
				}

			}
			map.put(positive, positives);
			map.put(negative, negatives);
			double gain = calculateGain(map, root);
			if (bestGain < gain) {
				bestGain = gain;
				positiveEvaluation.clear();
				negativeEvaluation.clear();
				positiveEvaluation.addAll(positives);
				negativeEvaluation.addAll(negatives);

				((ContinuousAttribute) attribute).setThreshold(threshold);
			}

		}
		map.put(positive, positiveEvaluation);
		map.put(negative, negativeEvaluation);

	}

	private int checkExamples(ArrayList<Evaluation> evaluations) {
		Evaluation firstEvaluation = evaluations.get(0);
		int returnValue = firstEvaluation.getEvaluation();

		for (Evaluation evaluation : evaluations) {

			if (evaluation.getEvaluation() != firstEvaluation.getEvaluation()) {
				returnValue = -1;
				break;
			}
		}
		return returnValue;
	}

	private int mostCommonValue(ArrayList<Evaluation> evaluations) {
		int mostCommon = 0;
		int maxValue = -1;
		HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
		for (Evaluation singleEvaluation : evaluations) {
			int count = values.containsKey(singleEvaluation.getEvaluation()) ? values
					.get(singleEvaluation.getEvaluation()) : 0;
			count++;
			values.put(singleEvaluation.getEvaluation(), count);
			if (maxValue < count) {
				maxValue = count;
				mostCommon = singleEvaluation.getEvaluation();
			}
		}
		return mostCommon;
	}

}
