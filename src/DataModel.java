import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataModel {

	private ArrayList<DataVector> dataArray = new ArrayList<DataVector>(); 

	public DataModel(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
		{
			br.readLine();br.readLine();
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				dataArray.add(new DataVector(sCurrentLine));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumDataVectors(){
		return dataArray.size();
	}

	public DataVector getDataVector(int index){
		return dataArray.get(index);
	}

	private int getZeroesOrOnesForXGivenY(int zeroOrOne, int x, int y){
		int count = 0;
		for (int i = 0; i < dataArray.size(); i++){
			DataVector dv = dataArray.get(i);
			if (dv.getDataClass() == y)
				if (dv.getDataVector().get(x) == zeroOrOne)
					count++;

		}
		return count;
	}
	
	double calculateGradientPart(double[] betas, ArrayList<Integer> vector){
		double z = 0;
		for (int j = 0; j < vector.size(); j++){
			z += betas[j]*vector.get(j);
		}
		return 1.0/(1+Math.exp(-z));
	}

	
	double calculateGradient(double[] betas, ArrayList<Integer> vector, int y){
		return (double)y - calculateGradientPart(betas, vector);
	}
	
	double[] betas = null;
	
	public int predictDataClass(ArrayList<Integer> vector2){
		if (m == null) m = vector2.size()+1;
		
		ArrayList<Integer> vector = new ArrayList<Integer>();
		vector.add(1);
		for (int i = 0; i < vector2.size(); i++)
			vector.add(vector2.get(i));
		
		if (betas == null){
			betas = calculateBetas();
		}
		return (calculateGradientPart(betas, vector) > .5 ? 1 : 0);
	}

	Integer m;
	final int epochs = 10000;
	final double learning_rate = 0.0001;

	private double[] calculateBetas() {
		double[] betas = new double[m];
		double[] gradient = new double[m];
		
		for (int i = 0; i < epochs; i++) {
			for (int j = 0; j < m; j++) 
				gradient[j] = 0;
			// Compute "batch" gradient vector
			
			for (DataVector dv : dataArray){
				// Add contribution to gradient for each data point
				
				for (int j = 0; j < m; j++) {
					gradient[j]  += (double)dv.getDataVectorWithOne().get(j)*calculateGradient(betas, dv.getDataVectorWithOne(), dv.getDataClass());
				}
			}
			
			// Update all j. Note learning rate h is pre-set constant
			for (int j = 0; j < m; j++) {
				betas[j] += learning_rate * gradient[j];
			}
		}
		return betas;
	}

	public int predictDataClassMLE(ArrayList<Integer> vector){
		final boolean laPlace = false;
		
		ArrayList<double[][]> matrices = new ArrayList<double[][]>();

		for (int i = 0; i < vector.size(); i++){
			double[][] counts = new double[2][2];
			int total = 0;
			for (int y = 0; y < counts.length; y++){
				for(int x = 0; x < counts[0].length; x++){
					counts[y][x] = getZeroesOrOnesForXGivenY(x, i, y);
					total+= counts[y][x];
				}
			}

			for (int y = 0; y < counts.length; y++){
				for(int x = 0; x < counts[0].length; x++){
					if (laPlace){
						counts[y][x]++;
						counts[y][x] *= 1.0/(double)(total+counts.length*counts[0].length);
					}						
					else counts[y][x] *= 1.0/(double)total;
				}
			}

			matrices.add(counts);
		}

		double[] yCounts = new double[2];
		double[][] counts = matrices.get(0);
		for (int y = 0; y < counts.length; y++){
			for(int x = 0; x < counts[0].length; x++){
				yCounts[y] += counts[y][x];
			}
		}

		double prob0 = probabilityOfY(vector, matrices, 0, yCounts);
		double prob1 = probabilityOfY(vector, matrices, 1, yCounts);
		return (prob0 > prob1 ? 0 : 1);
	}

	private double probabilityOfY(ArrayList<Integer> vector,
			ArrayList<double[][]> matrices, int y, double[] yCounts) {
		double probability = matrices.get(0)[y][vector.get(0)];
		for (int i = 1; i < matrices.size(); i++)
			probability *= matrices.get(i)[y][vector.get(i)];
		
		probability *= 1.0/Math.pow(yCounts[y], vector.size()-1);
		return probability;
	}
}
