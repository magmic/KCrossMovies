package com.magda.movies;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * not used - useful for splitting evaluations.csv to task.csv and train.csv files
 */
public class FileProcessing {

	FileProcessing() {
		try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\evaluations.csv")))
		{
			FileWriter fwTrain = new FileWriter("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\train.csv"); 
			FileWriter fwTask = new FileWriter("C:\\Users\\Okruszon\\Desktop\\Programming of Intelligent Information Systems\\Part II, Task 3\\task.csv"); 
			
			String sCurrentLine;
			String fields[];
			
			while ((sCurrentLine = br.readLine()) != null) {
				fields = sCurrentLine.split(";");
				System.out.println(fields[3] + " " + fields.length);
				if(!fields[3].equals("NULL"))
					fwTrain.write(sCurrentLine+"\r\n");
				else
					fwTask.write(sCurrentLine+"\r\n");
			}
			fwTrain.close();
			fwTask.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
