package com.magda.movies;

public class Evaluation {

	private int userId;
	private int movieId;
	private int evaluationValue;

	public Evaluation(int userId, int movieId, int evaluationValue) {
		this.userId = userId;
		this.movieId = movieId;
		this.evaluationValue = evaluationValue;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMovieId() {
		return movieId-1;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getEvaluation() {
		return evaluationValue;
	}

	public void setEvaluation(int evaluationValue) {
		this.evaluationValue = evaluationValue;
	}
}
