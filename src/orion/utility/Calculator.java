package orion.utility;

import java.util.List;

public class Calculator {
	
	public static double calculateMean(List<Double> dataList) {
		
		double mean = 0.0;
		double sum = 0.0;
		
		for(double ltpRatio : dataList) {
			sum +=ltpRatio;
		}
		mean = sum/dataList.size();
		
		return mean;
	}
	
	public static double calculateStandardDeviation(List<Double> dataList) {
		
		double sumOfSquareOfDistanceFromMean = 0.0;
		double mean = calculateMean(dataList);
		
		for(double priceRatio:dataList) {
			sumOfSquareOfDistanceFromMean += Math.pow(priceRatio-mean, 2);
		}
		
		double standardDeviation = Math.sqrt((sumOfSquareOfDistanceFromMean/(dataList.size()-1)));
		
		return standardDeviation;
	}

	public static double calculateStandardDeviation(List<Double> dataList, double mean) {
		
		double sumOfSquareOfDistanceFromMean = 0.0;
		
		for(double priceRatio:dataList) {
			sumOfSquareOfDistanceFromMean += Math.pow(priceRatio-mean, 2);
		}
		
		double standardDeviation = Math.sqrt((sumOfSquareOfDistanceFromMean/(dataList.size()-1)));
		
		return standardDeviation;
	}
}
