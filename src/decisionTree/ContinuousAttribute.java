package decisionTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.magda.movies.Evaluation;

public class ContinuousAttribute extends Attribute {
	private double threshold;
	private boolean isDate;

	public ContinuousAttribute(int featureIndex, String name, boolean isDate) {
		super();
		this.featureIndex = featureIndex;

		this.name = name;
		this.isDate = isDate;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public boolean isDate() {
		return isDate;
	}

	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}

	@Override
	public String getName() {
		return super.getName() + Double.toString(threshold);
	}

	public ArrayList<Double> findThresholdsValues(
			ArrayList<Evaluation> evaluations, String[][] features) {
		ArrayList<Double> thresholds = new ArrayList<Double>();

		double[][] values = getContValues(evaluations, features);

		Arrays.sort(values, new Comparator<double[]>() {
			@Override
			public int compare(final double[] s1, final double[] s2) {
				if (s1[0] > s2[0])
					return 1; // tells Arrays.sort() that s1 comes after s2
				else if (s1[0] < s2[0])
					return -1; // tells Arrays.sort() that s1 comes before s2
				else {
					return 0;
				}
			}
		});
	
		for (int i = 1; i < values.length; i++) {
			//if(this.getFeatureIndex()==4)
			
			if (values[i][1] != values[i - 1][1]) {
				
				double threshold = (values[i][0] + values[i - 1][0]) / 2d;
				if(isDate){
					threshold = Math.round(threshold);
				}
				thresholds.add(threshold);
			}
		}
		return thresholds;
	}

	private double[][] getContValues(ArrayList<Evaluation> evaluations,
			String[][] features) {
		double[][] values = new double[evaluations.size()][2];
		int withoutMissing = 0;
		for (int i = 0; i < evaluations.size(); i++) {
			if (!features[evaluations.get(i).getMovieId()][this
							.getFeatureIndex()].equals("0")) {
				withoutMissing++;
				if (isDate) {
					values[i][0] = Double.valueOf(features[evaluations.get(i)
							.getMovieId()][featureIndex].split("-")[0]);
				} else {

					values[i][0] = Double.parseDouble(features[evaluations.get(
							i).getMovieId()][this.getFeatureIndex()]);
					
				}
				values[i][1] = (double) evaluations.get(i).getEvaluation();
			}
		}
		double[][] withoutMissingValues = new double[withoutMissing][2];
		int j=0;
		for(int i = 0; i < values.length; i++){
			if(values[i][0]!=0){
				withoutMissingValues[j][0]=values[i][0];
				withoutMissingValues[j][1]=values[i][1];
				
				j++;
				
			}
		}
		return withoutMissingValues;
	}

}
