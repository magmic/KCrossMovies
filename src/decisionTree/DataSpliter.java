package decisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.magda.movies.Evaluation;

public class DataSpliter {
	HashMap<Integer, ArrayList<Evaluation>> trainMap = new HashMap<Integer, ArrayList<Evaluation>>(); //mapping: userId -> list of evaluations
	HashMap<Integer, ArrayList<Evaluation>> validationMap = new HashMap<Integer, ArrayList<Evaluation>>(); //mapping: userId -> list of evaluations
	
	public DataSpliter(HashMap<Integer, ArrayList<Evaluation>> allData){
		for(Integer userId : allData.keySet()){
			ArrayList<Evaluation> dataSet = allData.get(userId);
			ArrayList<Evaluation> train =new ArrayList<Evaluation>(dataSet);
			ArrayList<Evaluation> val =new ArrayList<Evaluation>();
			int validationSize = (int)Math.round(0.2*(double)train.size());
			Random rand = new Random();
			
			while(val.size()!=validationSize){
				int pos =rand.nextInt(train.size());
				val.add(train.get(pos));
				train.remove(pos);	
			}
			trainMap.put(userId,  train);
			validationMap.put(userId, val);
		
		}
		
	}
	public HashMap<Integer, ArrayList<Evaluation>> getTrainMap() {
		return trainMap;
	}
	public HashMap<Integer, ArrayList<Evaluation>> getValidationMap() {
		return validationMap;
	}
	
}
