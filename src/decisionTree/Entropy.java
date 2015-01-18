package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.magda.movies.Evaluation;

public class Entropy {
	private static double log(double x, int base) {
		return (double) (Math.log(x) / Math.log(base));
	}

	public static double calculateEntropy(ArrayList<Evaluation> set) {
		HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
		for (Evaluation singleEvaluation : set) {

			int count = values.containsKey(singleEvaluation.getEvaluation()) ? values
					.get(singleEvaluation.getEvaluation()) : 0;
			// System.out.println("count " + count);
			values.put(singleEvaluation.getEvaluation(), count + 1);

		}

		double entropy = 0.0;
		for (Integer entry : values.values()) {

			double probability = (double) entry / (double) set.size();

			entropy += -probability * log(probability, 2);

		}

		return entropy;

	}

	public static double calculateInformationGain(double rootEntropy,
			double[] subEntropies, int[] setSizes) {

		double gain = rootEntropy;

		int data = setSizes[0] + setSizes[1];
		if (data == 0) {
			return 0;
		}
		for (int i = 0; i < subEntropies.length; i++) {

			gain -= (double) setSizes[i] / ((double) data) * subEntropies[i];
		}

		return gain;

	}

	public static double calculateSplitInformation(int[] setSizes, int data) {
		double splitInformation = 0;
		for (int i = 0; i < setSizes.length; i++) {
			splitInformation -= ((double) setSizes[i] / (double) data)
					* log(((double) setSizes[i] / (double) data), 2);
		}
		return splitInformation;
	}

	public static double calculateGainRatio(double gain, double splitInformation) {
		return gain / splitInformation;
	}

}
