package com.magda.movies;

import decisionTree.DecisionTreePredictor;

public class Main {

	public static void main(String args[]) {
		
		FeaturesComparator fc = new FeaturesComparator("C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\data.csv");
//		new KBestHandler(fc.getComparisonResults(), 1);
//		new KBestHandler(fc.getComparisonResults(), 3);
//		new KBestHandler(fc.getComparisonResults(), 5);
		DecisionTreePredictor tree= new DecisionTreePredictor(fc.getFeatures());
		
		System.out.println("done!");
	}
	
}
