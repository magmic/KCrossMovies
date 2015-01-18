package com.magda.movies;

import java.util.ArrayList;
import java.util.HashMap;

public class QEstimator {

	private double[][] comparisonResults;
	private int k = 1;
	private int[] kBestMovieIds;
	private int[] kBestMovieEvals;
	private double[] kBestDistances;
	private double Q;
	//private int fileEnding;
	
	QEstimator(double[][] comparisonResults, int k, ArrayList<Evaluation> validationSet, HashMap<Integer, ArrayList<Evaluation>> trainMap) {
		this.comparisonResults = comparisonResults;
		this.k = k;
		this.kBestMovieIds = new int[k];
		this.kBestMovieEvals = new int[k];
		this.kBestDistances = new double[k];
		//fileEnding = k==1 ? 1 : k==3 ? 2 : k==5 ? 3 : 0;
		
		int correctGuesses = 0;
		for(int i=0; i<validationSet.size(); i++) {
			Evaluation e = validationSet.get(i);
			int userId = e.getUserId();
			int movieId = e.getMovieId();
			int realScore = e.getEvaluation();
			
			int predictedResult = predictResult(movieId, (ArrayList<Evaluation>) trainMap.get(userId));
			
			if(predictedResult==realScore)//(Math.abs(predictedResult-realScore)<=1)
				correctGuesses++;
		}
		Q = 100.0*correctGuesses/validationSet.size();
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
		for (int i=0; i<k; i++) {
			sum += kBestMovieEvals[i];
			//System.out.println(movieId+1 + " similar to " + (kBestMovieIds[i]+1) + ", distance "+kBestDistances[i]);
		}
		
		return (int) Math.round(1.0*sum/k);
	}
	
	public double getQ() {
		return Q;
	}

}
