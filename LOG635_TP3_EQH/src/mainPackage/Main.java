package mainPackage;

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
			 trainingData.getdata();
			 builder.buildTree(trainingData);
		 
			// do stuff
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
