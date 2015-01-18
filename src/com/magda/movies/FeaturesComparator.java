package com.magda.movies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FeaturesComparator {

	private int featuresNumber = 18;
	private int moviesNumber = 200;
	private int[] featuresWeights = {27,23,6,28,11,9,7,10,7,14,10,16,11,22,23,25,13,13};
	//private int[] featuresWeights = {1,1,1,1,1,1,1,1,4,1,1,1,1,4,1,4,5,1};
	private double[] weightsNormalized = new double[featuresNumber];
	
	private String[][] features = new String[moviesNumber][featuresNumber];


	private double[][] comparisonResults = new double[moviesNumber][moviesNumber];
	
	public double[][] getComparisonResults() {
		return comparisonResults;
	}

	FeaturesComparator(String filePath) {
		//prepareWeights();
		prepareFeatures(filePath);
		//prepareComparisonResults();
	}
	
	private void prepareWeights() {
		double sum = 0;
		for(int i=0; i<featuresWeights.length; i++)
			sum += featuresWeights[i];
		for(int i=0; i<featuresWeights.length; i++)
			weightsNormalized[i] = featuresWeights[i]/sum;
	}
	
	public void prepareWeights(int[] weightsControl) {
		double sum = 0;
		for(int i=0; i<featuresWeights.length; i++)
			sum += featuresWeights[i]*weightsControl[i];
		for(int i=0; i<featuresWeights.length; i++)// {
			weightsNormalized[i] = featuresWeights[i]*weightsControl[i]/sum;
			//System.out.print(" "+ weightsNormalized[i] +" ");
		//}
	}
	
	private void prepareFeatures(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
		{
			String sCurrentLine;
			int count = 0;
			
			while ((sCurrentLine = br.readLine()) != null) {
				String[] fields = sCurrentLine.split(";");
				features[count/featuresNumber][count%featuresNumber] = fields.length==4 ? fields[3] : "";
				
				//System.out.println(count/featuresNumber+"; "+count%featuresNumber+"; "+features[count/featuresNumber][count%featuresNumber]);
				count++;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void prepareComparisonResults() {
		for(int movie=0; movie<moviesNumber; movie++) {
			for(int movie2=movie; movie2<moviesNumber; movie2++) {
				if(movie == movie2) {
					comparisonResults[movie][movie2] = 1;
					continue;
				}
				double result = 0;
				if(weightsNormalized[0] != 0) result += booleanComparison(movie, movie2, 0); //1 adult
				if(weightsNormalized[1] != 0) result += booleanComparison(movie, movie2, 1); //2 collection
				if(weightsNormalized[2] != 0) result += minOverMax(movie, movie2, 2); //3 budget
				if(weightsNormalized[3] != 0) result += collectionsComparison(movie, movie2, 3); //4 genres
				if(weightsNormalized[4] != 0) result += minOverMax(movie, movie2, 4); //5 popularity
				if(weightsNormalized[5] != 0) result += collectionsComparison(movie, movie2, 5); //6 production
				if(weightsNormalized[6] != 0) result += collectionsComparison(movie, movie2, 6); //7 countries
				if(weightsNormalized[7] != 0) result += dateComparison(movie, movie2, 7); //8 release date
				if(weightsNormalized[8] != 0) result += minOverMax(movie, movie2, 8); //9 revenue
				if(weightsNormalized[9] != 0) result += minOverMax(movie, movie2, 9); //10 runtime
				if(weightsNormalized[10] != 0) result += collectionsComparison(movie, movie2, 10); //11 languages
				if(weightsNormalized[11] != 0) result += avVotesComparison(movie, movie2, 11); //12 average votes
				if(weightsNormalized[12] != 0) result += minOverMax(movie, movie2, 12); //13 votes count
				if(weightsNormalized[13] != 0) result += collectionsComparison(movie, movie2, 13); //14 cast
				if(weightsNormalized[14] != 0) result += collectionsComparison(movie, movie2, 14); //15 keywords
				if(weightsNormalized[15] != 0) result += collectionsComparison(movie, movie2, 15); //16 crew
				if(weightsNormalized[16] != 0) result += collectionsComparison(movie, movie2, 16); //17 lists
				if(weightsNormalized[17] != 0) result += collectionsComparison(movie, movie2, 17); //18 similar movies

				comparisonResults[movie][movie2] = result;
				comparisonResults[movie2][movie] = result;
				//System.out.println(movie+"; "+movie2+"; "+comparisonResults[movie][movie2]);
			}
		}
	}
	
	private double booleanComparison(int index1, int index2, int featureIndex) {
		if(features[index1][featureIndex].equals(features[index2][featureIndex]))
			return weightsNormalized[featureIndex];
		return 0;
	}
	
	private double minOverMax(int index1, int index2, int featureIndex) {
		double value1, value2, minValue, maxValue;
		try {
			value1 = Double.valueOf(features[index1][featureIndex]);
			value2 = Double.valueOf(features[index2][featureIndex]);
			minValue = Math.min(value1,value2);
			maxValue = Math.max(value1,value2);
			return maxValue!=0 ? minValue/maxValue*weightsNormalized[featureIndex] : weightsNormalized[featureIndex];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private double collectionsComparison(int index1, int index2, int featureIndex) {
		String[] elements1, elements2;
		elements1 = features[index1][featureIndex].split(", ");
		elements2 = features[index2][featureIndex].split(", ");
		int smallerTotal, common = 0;
		
		//remove duplicates
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> shorterList, longerList;
		for(int i=0; i<elements1.length; i++)
			if(!list1.contains(elements1[i]))
				list1.add(elements1[i]);
		for(int i=0; i<elements2.length; i++)
			if(!list2.contains(elements2[i]))
				list2.add(elements2[i]);
		
		if(list1.size() < list2.size()) {
			shorterList = list1;
			longerList = list2;
			smallerTotal = list1.size();
		} else {
			shorterList = list2;
			longerList = list1;
			smallerTotal = list2.size();
		}
		
		for(int i=0; i<shorterList.size(); i++)
			for(int j=0; j<longerList.size(); j++)
				if(shorterList.get(i).equals(longerList.get(j)))
					common++;
		
		return smallerTotal!=0 ? weightsNormalized[featureIndex]*common/smallerTotal : 0;
	}
	
	private double dateComparison(int index1, int index2, int featureIndex) {
		int value1, value2, minValue, maxValue;
		try {
			value1 = Integer.valueOf(features[index1][featureIndex].split("-")[0]);
			value2 = Integer.valueOf(features[index2][featureIndex].split("-")[0]);
			minValue = Math.min(value1,value2);
			maxValue = Math.max(value1,value2);
			if(minValue < 1895)
				return 0;
			return (1-1.0*(maxValue-minValue)/(2015-1895))*weightsNormalized[featureIndex];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private double avVotesComparison(int index1, int index2, int featureIndex) {
		double value1, value2, minValue, maxValue;
		try {
			value1 = Double.valueOf(features[index1][featureIndex]);
			value2 = Double.valueOf(features[index2][featureIndex]);
			minValue = Math.min(value1,value2);
			maxValue = Math.max(value1,value2);
			if(maxValue == 0)
				return weightsNormalized[featureIndex];
			return (1-(maxValue-minValue)/10)*weightsNormalized[featureIndex];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public String[][] getFeatures() {
		return features;
	}

	public void setFeatures(String[][] features) {
		this.features = features;
	}
}
