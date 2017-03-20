package mainPackage;

import form.mainForm;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainForm a = new mainForm();
		a.setVisible(true);
		
		String filepath ="Dataset.csv";
		
		DatasetContainer trainingData = null;
		try
		{
		 trainingData = new DatasetContainer(filepath);
		 
			// do stuff
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
