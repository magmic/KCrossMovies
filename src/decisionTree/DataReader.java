package decisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.magda.movies.Evaluation;

public class DataReader {
	public static ArrayList<Evaluation> readTrainSet() {
		ArrayList<Evaluation> trainSet = new ArrayList<Evaluation>();
		try (BufferedReader brTrain = new BufferedReader(new FileReader(
				"C:\\Users\\Marta\\Documents\\CSIT\\II\\PIIS\\train.csv"))) {

			// read training set from file to ArrayList
			String currentTrainLine;

			while ((currentTrainLine = brTrain.readLine()) != null) {
				String[] fieldsTrain = currentTrainLine.split(";");
				trainSet.add(new Evaluation(Integer.valueOf(fieldsTrain[1]),
						Integer.valueOf(fieldsTrain[2]), Integer
								.valueOf(fieldsTrain[3])));
			}
			brTrain.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return trainSet;
	}
}
