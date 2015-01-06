package com.magda.movies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KBestHandler {

	private double[][] comparisonResults;
	private int k = 1;
	private int[] kBestMovieIds;
	private int[] kBestMovieEvals;
	private double[] kBestDistances;
	private int fileEnding;
	
	KBestHandler(double[][] comparisonResults, int k) {
		this.comparisonResults = comparisonResults;
		this.k = k;
		this.kBestMovieIds = new int[k];
		this.kBestMovieEvals = new int[k];
		this.kBestDistances = new double[k];
		fileEnding = k==1 ? 1 : k==3 ? 2 : k==5 ? 3 : 0;
		
		try (BufferedReader brTask = new BufferedReader(new FileReader("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\task.csv")))
		{
			BufferedReader brTrain = new BufferedReader(new FileReader("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\train.csv")); 
			FileWriter fOutput = new FileWriter("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\result"+fileEnding+".csv"); 
			
			//read training set from file to ArrayList
			String currentTrainLine;
			ArrayList<Evaluation> trainSet = new ArrayList<Evaluation>();
			while ((currentTrainLine = brTrain.readLine()) != null) {
				String[] fieldsTrain = currentTrainLine.split(";");
				trainSet.add(new Evaluation(Integer.valueOf(fieldsTrain[1]),Integer.valueOf(fieldsTrain[2]),Integer.valueOf(fieldsTrain[3])));
			}
			
			
			//spreading List - one List of evaluations per user
			HashMap<Integer, ArrayList<Evaluation>> processedMap = new HashMap<Integer, ArrayList<Evaluation>>(); //mapping: userId -> list of evaluations
			int rememberUserId = -1;
			ArrayList<Evaluation> userSet = new ArrayList<Evaluation>();
			for(int i=0; i<trainSet.size(); i++) {
				Evaluation e = trainSet.get(i);
				int userId = e.getUserId();
				if(userId == rememberUserId || rememberUserId == -1) {
					userSet.add(e);
				} else {
					processedMap.put(rememberUserId, userSet);
					userSet = new ArrayList<Evaluation>();
					userSet.add(e);
				}
				rememberUserId = userId;
			}
			processedMap.put(rememberUserId, userSet); //puts the last one
//			System.out.print(" "+processedMap.get(8).get(0).getEvaluation());
//			System.out.print(" "+processedMap.get(8).get(1).getEvaluation());
			
			
			//read from task file and write to a new file with predicted result
			String currentTaskLine;
			while ((currentTaskLine = brTask.readLine()) != null) {
				int predictedResult = 0;
				String[] fieldsTask = currentTaskLine.split(";");
				int userId = Integer.valueOf(fieldsTask[1]);
				int movieId = Integer.valueOf(fieldsTask[2]);
				
				predictedResult = predictResult(movieId, (ArrayList<Evaluation>) processedMap.get(userId));
				
				
				fOutput.write(fieldsTask[0]+";"+fieldsTask[1]+";"+fieldsTask[2]+";"+predictedResult+"\n");
			}
			
			fOutput.close();
			brTrain.close();
			brTask.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int predictResult(int movieId, ArrayList<Evaluation> evals) {
		movieId--; //adjust to 0-199
		for(int i=0; i<evals.size(); i++) {
			Evaluation e = evals.get(i);
			int movie2Id = e.getMovieId()-1; //adjust to 0-199
			int movie2Eval = e.getEvaluation();
			if(i<k) {
				kBestMovieIds[i] = movie2Id;
				kBestMovieEvals[i] = movie2Eval;
				kBestDistances[i] = comparisonResults[movieId][movie2Id];
				continue;
			}
			
			//find worst movie index from k best...
			int worstFromBest = 0;
			for(int j=1; j<k; j++)
				if(kBestDistances[j]<kBestDistances[worstFromBest])
					worstFromBest = j;
			
			//...and if needed replace the worst by the new one
			if(comparisonResults[movieId][movie2Id]>kBestDistances[worstFromBest]) {
				kBestMovieIds[worstFromBest] = movie2Id;
				kBestMovieEvals[worstFromBest] = movie2Eval;
				kBestDistances[worstFromBest] = comparisonResults[movieId][movie2Id];
			}
			
			//System.out.println(movie2Id+" "+evalMovie2);
		}
		
		int sum = 0;
		for (int i=0; i<k; i++)
			sum += kBestMovieEvals[i];
		
		return (int) Math.round(1.0*sum/k);
	}
}
