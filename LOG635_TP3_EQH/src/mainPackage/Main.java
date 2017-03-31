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
			 Map<String,List<Double>> dataTest = trainingData.getdata();
			 for (String s : trainingData.getColumnNames())
			 {
				 System.out.print(s);
			 }
			 System.out.println();
			 for (int i = 0; i < 10; i++)
			 {
				 
			 for (Map.Entry<String, List<Double>> entry : dataTest.entrySet())
			 {
				 System.out.print(entry.getValue().get(i) + "     ");
			 }
			 System.out.println();
			 }
			 
			 builder.buildTree(trainingData);
		 
			// do stuff
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
