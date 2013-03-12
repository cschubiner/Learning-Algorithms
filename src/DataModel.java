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
	
	public int predictDataClass(ArrayList<Integer> vector){
		return 1;
	}
}
