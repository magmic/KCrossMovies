package com.magda.movies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TAndVSetInitializor {

	ArrayList<Evaluation> trainSet = new ArrayList<Evaluation>();
	ArrayList<Evaluation> validationSet = new ArrayList<Evaluation>();
	HashMap<Integer, ArrayList<Evaluation>> trainMap = new HashMap<Integer, ArrayList<Evaluation>>(); //mapping: userId -> list of evaluations
	HashMap<Integer, ArrayList<Evaluation>> validationMap = new HashMap<Integer, ArrayList<Evaluation>>(); //mapping: userId -> list of evaluations
	//validationMap probaly not needed
	
	public TAndVSetInitializor() {
//		this.comparisonResults = comparisonResults;
//		this.k = k;
//		this.kBestMovieIds = new int[k];
//		this.kBestMovieEvals = new int[k];
//		this.kBestDistances = new double[k];
//		fileEnding = k==1 ? 1 : k==3 ? 2 : k==5 ? 3 : 0;
		
		try
		{
			BufferedReader brTrain = new BufferedReader(new FileReader("C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\train.csv")); 
			//FileWriter fOutput = new FileWriter("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 4\\result"+fileEnding+".csv"); 
			
			//read training set from file to ArrayList
			String currentTrainLine;
			while ((currentTrainLine = brTrain.readLine()) != null) {
				String[] fieldsTrain = currentTrainLine.split(";");
				if(Math.random()>0.3)
					trainSet.add(new Evaluation(Integer.valueOf(fieldsTrain[1]),Integer.valueOf(fieldsTrain[2]),Integer.valueOf(fieldsTrain[3])));
				else
					validationSet.add(new Evaluation(Integer.valueOf(fieldsTrain[1]),Integer.valueOf(fieldsTrain[2]),Integer.valueOf(fieldsTrain[3])));
			}
			
			
			//spreading List - one List of evaluations per user
			int rememberUserId = -1;
			ArrayList<Evaluation> userSet = new ArrayList<Evaluation>();
			for(int i=0; i<trainSet.size(); i++) {
				Evaluation e = trainSet.get(i);
				int userId = e.getUserId();
				if(userId == rememberUserId || rememberUserId == -1) {
					userSet.add(e);
				} else {
					trainMap.put(rememberUserId, userSet);
					userSet = new ArrayList<Evaluation>();
					userSet.add(e);
				}
				rememberUserId = userId;
			}
			trainMap.put(rememberUserId, userSet); //puts the last one
			
			//spreading List - one List of evaluations per user
			rememberUserId = -1;
			userSet = new ArrayList<Evaluation>();
			for(int i=0; i<validationSet.size(); i++) {
				Evaluation e = validationSet.get(i);
				int userId = e.getUserId();
				if(userId == rememberUserId || rememberUserId == -1) {
					userSet.add(e);
				} else {
					validationMap.put(rememberUserId, userSet);
					userSet = new ArrayList<Evaluation>();
					userSet.add(e);
				}
				rememberUserId = userId;
			}
			validationMap.put(rememberUserId, userSet); //puts the last one
			
//			System.out.print(" "+processedMap.get(8).get(0).getEvaluation());
//			System.out.print(" "+processedMap.get(8).get(1).getEvaluation());
			
			
			//read from task file and write to a new file with predicted result
//			String currentTaskLine;
//			while ((currentTaskLine = brTask.readLine()) != null) {
//				int predictedResult = 0;
//				String[] fieldsTask = currentTaskLine.split(";");
//				int userId = Integer.valueOf(fieldsTask[1]);
//				int movieId = Integer.valueOf(fieldsTask[2]);
//				
//				predictedResult = predictResult(movieId, (ArrayList<Evaluation>) processedMap.get(userId));
//				
//				fOutput.write(fieldsTask[0]+";"+fieldsTask[1]+";"+fieldsTask[2]+";"+predictedResult+"\n");
//			}
			
			//fOutput.close();
			brTrain.close();
			//brTask.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	public ArrayList<Evaluation> getValidationSet() {
		return validationSet;
	}

	public HashMap<Integer, ArrayList<Evaluation>> getTrainMap() {
		return trainMap;
	}
}
