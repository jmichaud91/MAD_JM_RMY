package mainPackage;

import java.util.List;
import java.util.Map;

import arbreDecision.TreeBuilder;
import form.mainForm;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//mainForm a = new mainForm();
		//a.setVisible(true);
		
		String filepath ="Dataset.csv";
		String filepathTest = "ForTest_ArbreDecisionAttrNum.txt";
		
		DatasetContainer trainingData = null;
		try
		{
		 trainingData = new DatasetContainer(filepathTest);
			 TreeBuilder builder = new TreeBuilder();
			 Map<String,List<Double>> data = trainingData.getdata();
			 data.remove("Positionx");
			 List<String> columnNames = trainingData.getColumnNames();
			 columnNames.remove(0);
			 
			 builder.buildTree(new DatasetContainer(data, columnNames));
		 
			// do stuff
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
