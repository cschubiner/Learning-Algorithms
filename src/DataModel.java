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

	public int predictDataClass(ArrayList<Integer> vector){
		final boolean laPlace = true;
		
		ArrayList<double[][]> matrices = new ArrayList<double[][]>();

		for (int i = 0; i < vector.size(); i++){
			double[][] counts = new double[2][vector.size()];
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

			System.out.println("counts:" + counts[0][0] + ", "+counts[0][1]);
			System.out.println("counts:" + counts[1][0] + ", "+counts[1][1]);
			System.out.println();
			matrices.add(counts);
		}

		double[] yCounts = new double[2];
		double[][] counts = matrices.get(0);
		for (int y = 0; y < counts.length; y++){
			for(int x = 0; x < counts[0].length; x++){
				yCounts[y] += counts[y][x];
			}
		}

		//	totalCounts[0] = countsForOne[0] + countsForZero[0];
		//totalCounts[1] = countsForOne[1] + countsForZero[1];
		//System.out.println("yCounts:" + yCounts[0] + ", "+yCounts[1]);

		double prob0 = probabilityOfY(vector, matrices, 0, yCounts);
		double prob1 = probabilityOfY(vector, matrices, 1, yCounts);
	//	System.out.println("prob 0: " + prob0);
		//System.out.println("prob 1: " + prob1);
		
		
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
