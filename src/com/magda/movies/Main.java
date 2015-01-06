package com.magda.movies;

public class Main {

	public static void main(String args[]) {
		
		FeaturesComparator fc = new FeaturesComparator("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\data.csv");
		new KBestHandler(fc.getComparisonResults(), 1);
		new KBestHandler(fc.getComparisonResults(), 3);
		new KBestHandler(fc.getComparisonResults(), 5);
		
		System.out.println("done!");
	}
	
}
