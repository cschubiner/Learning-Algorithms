import java.util.ArrayList;


public class DataVector {
	private ArrayList<Integer> dataVector = new ArrayList<Integer>();
	private int dataClass;
	
	public ArrayList<Integer> getDataVector() {
		return dataVector;
	}

	public int getDataClass() {
		return dataClass;
	}

	public DataVector(String line) {
		String[] splits = line.split(":");
		dataClass = Integer.parseInt(splits[1].trim());
		String[] splits2 = splits[0].split(" ");

		for (int i = 0; i < splits2.length; i++)
			dataVector.add(Integer.parseInt(splits2[i].trim()));
		
		//System.out.println("dataVector: "+dataVector + " - dataClass: "+dataClass);
	}
	
	@Override
	public String toString() {
		return "dataVector: "+dataVector + " - dataClass: "+dataClass;
	}
}
