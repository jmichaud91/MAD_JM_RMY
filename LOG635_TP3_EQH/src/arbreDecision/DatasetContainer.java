package arbreDecision;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.NullCipher;

public class DatasetContainer {

	List<String> columnsName;
	Map<String, List<Double>> columns;
	List<String> classes;
	String classesKey;

	// Take into account that the exit classes are on the end of the file
	public DatasetContainer(String filepath) throws IOException {
		columnsName = new ArrayList<>();
		columns = new LinkedHashMap<>();
		classes = new ArrayList<>();
		readCSVDataFile(filepath);
	}
	
	public DatasetContainer(Map<String,List<Double>> data)
	{
		columns = new HashMap<>();
		classes = new ArrayList<>();
		columnsName = new ArrayList<>();
		
		for (Map.Entry<String,List<Double>> entry : data.entrySet())
		{
			columnsName.add(entry.getKey());
			// If we are at the second line set the classes
			if (columnsName.size() ==2 )
			{
				classesKey = entry.getKey();
				for (Double d : entry.getValue())
				{
					classes.add(String.valueOf(d));
				}
			}
			columns.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		
		
	}
	

	public List<Map<String, String>> readCSVDataFile(String filePath) throws IOException {
		String line;
		BufferedReader reader = null;
		boolean firstLine = true;
		try {

			reader = new BufferedReader(new FileReader(filePath));
			while ((line = reader.readLine()) != null) {

				String[] lineSplit = line.split(",");
				if (firstLine) {
					for (String s : lineSplit) {
						columnsName.add(s);
					}
					classesKey = lineSplit[1];
					firstLine = false;
				}
				else
				{
					//if (!classes.contains(lineSplit[2]))
					//{
						classes.add(lineSplit[1]);
				//	}
					for (int i = 0; i < lineSplit.length; i++) {
						List<Double> l = columns.get(columnsName.get(i));
						if (l == null)
						{
							l = new ArrayList<>();
						}
						try
						{
							if (lineSplit[i].contains("\""))
							{
								lineSplit[i] = lineSplit[i].replace("\"", "");
							}
							l.add(Double.parseDouble(lineSplit[i]));	
						} catch (Exception e )
						{
							l.add(null);
						}
						
						columns.put(columnsName.get(i), l);
					}
					
				}
			}
		} finally {
			reader.close();
		}
		return null;

	}
	
	public Map<String,List<Double>> getdata()
	{
		Map<String,List<Double>> clone = new LinkedHashMap<>();
		
		for (Map.Entry<String,List<Double>> entry : columns.entrySet())
		{
			clone.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return clone;
	}
	public List<String> getKeys()
	{
		return new ArrayList<>(columnsName);
	}
	
	public List<String> getDistinctClasses()
	{
		List<String> distinctClasses = new ArrayList<>();
		for (String s : classes)
		{
			if (!distinctClasses.contains(s))
			{
				distinctClasses.add(s);
			}
		}
		return distinctClasses;
	}
	
	public List<String> getClasses()
	{
		return new ArrayList<>(classes);
	}
	
	public List<String> getColumnNames()
	{
		return new ArrayList<>(columnsName);
	}
	
	public String getClassesKey()
	{
		return classesKey;
	}
}
