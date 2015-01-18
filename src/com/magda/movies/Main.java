package com.magda.movies;

import decisionTree.DecisionTreePredictor;

import java.util.ArrayList;

public class Main {

	public static void main(String args[]) {

		FeaturesComparator fc = new FeaturesComparator(
				"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\data.csv");
		// new KBestHandler(fc.getComparisonResults(), 1);
		// new KBestHandler(fc.getComparisonResults(), 3);
		// new KBestHandler(fc.getComparisonResults(), 5);
		DecisionTreePredictor tree = new DecisionTreePredictor(fc.getFeatures());

		// FeaturesComparator fc = new
		// FeaturesComparator("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\data.csv");
		// TAndVSetInitializor data = new TAndVSetInitializor();
		//
		// int[] K = {1,3,5};
		// //int k=1;
		// for(int k : K) {
		// ArrayList<Integer> chosenFeatures = new ArrayList<Integer>();
		// ArrayList<Integer> bestFeatures = new ArrayList<Integer>();
		// double bestQ = -1;
		// for(int subsetLen = 1; subsetLen <= 18; subsetLen++) {
		//
		// int bestNewElement = 1;
		// double bestNewElementQ = -1;
		// for(int newElement=1; newElement<=18; newElement++) {
		// if(chosenFeatures.contains(newElement)) continue;
		// chosenFeatures.add(newElement);
		//
		// int[] weightsControl = new int[18];
		// for(int i=0; i<weightsControl.length; i++) {
		// if(chosenFeatures.contains(i+1))
		// weightsControl[i] = 1;
		// else
		// weightsControl[i] = 0;
		// }
		//
		// fc.prepareWeights(weightsControl);
		// fc.prepareComparisonResults();
		// QEstimator qEstimator = new QEstimator(fc.getComparisonResults(), k,
		// data.getValidationSet(), data.getTrainMap());
		// double q = qEstimator.getQ();
		// //System.out.println("q: "+q);
		// if(q>bestNewElementQ) {
		// bestNewElement = newElement;
		// bestNewElementQ = q;
		// }
		//
		// chosenFeatures.remove((Integer) newElement);
		// }
		// chosenFeatures.add(bestNewElement);
		// if(bestNewElementQ>bestQ) {
		// bestQ = bestNewElementQ;
		// bestFeatures = (ArrayList<Integer>) chosenFeatures.clone();
		// }// else break;
		// //System.out.println("k: "+k+", sublen: "+subsetLen+", bestNewElementQ: "+bestNewElementQ
		// +", best Q: " + bestQ);
		//
		// }
		// System.out.println("k: "+k+", best Q: " + bestQ);
		// System.out.println("k="+k+": ");
		// for(int i=0;i<bestFeatures.size(); i++)
		// System.out.print(bestFeatures.get(i)+", ");
		// System.out.println();
		// int[] weightsControl = new int[18];
		// for(int i=0; i<weightsControl.length; i++) {
		// if(bestFeatures.contains(i+1))
		// weightsControl[i] = 1;
		// else
		// weightsControl[i] = 0;
		// }
		//
		// fc.prepareWeights(weightsControl);
		// fc.prepareComparisonResults();
		// new KBestHandler(fc.getComparisonResults(), k);
		// }

		// new KBestHandler(fc.getComparisonResults(), 1);
		// new KBestHandler(fc.getComparisonResults(), 3);
		// new KBestHandler(fc.getComparisonResults(), 5);

		System.out.println("done!");
	}

}
