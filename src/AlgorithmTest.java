import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

public class AlgorithmTest {

	@Test
	public void simpleTest() {
		testAlgorithm(new DataModel("simple-train.txt"), new DataModel("simple-test.txt"));
	}
	
	@Test
	public void voteTest() {
		testAlgorithm(new DataModel("heart-train.txt"), new DataModel("heart-test.txt"));
	}
	
	@Test
	public void heartTest() {
		testAlgorithm(new DataModel("vote-train.txt"), new DataModel("vote-test.txt"));
	}

	private void testAlgorithm(DataModel trainingModel, DataModel testingModel) {
		int[] correctCount = new int[2];
		int[] totalCount = new int[2];
		for (int i = 0; i < testingModel.getNumDataVectors(); i++){
			int prediction = trainingModel.predictDataClass(testingModel.getDataVector(i).getDataVector());
			
			int actualValue = testingModel.getDataVector(i).getDataClass();
			
			if (prediction == actualValue)
				correctCount[prediction]++;
			
			totalCount[actualValue]++;
		}

		for (int i = 0; i <= 1; i++){
			System.out.println("Class:"+ i + " tested " + totalCount[i] + ", correctly classified " + correctCount[i]);
		}
		System.out.println("Overall: tested " + (totalCount[0]+totalCount[1]) + ", correctly classified " + (correctCount[0]+correctCount[1]));
		double accuracy = ((double)(correctCount[0]+correctCount[1]))/((double)(totalCount[0]+totalCount[1]));
		System.out.println("Accuracy = "+ accuracy);
		System.out.println();
	}



}
