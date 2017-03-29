package mainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetContainer {

	List<String> columnsName;
	Map<String, List<Double>> columns;
	List<String> classes;

	// Take into account that the exit classes are on the end of the file
	public DatasetContainer(String filepath) throws IOException {
		columnsName = new ArrayList<>();
		columns = new HashMap<>();
		classes = new ArrayList<>();
		readCSVDataFile(filepath);
	}
	
	public DatasetContainer(Map<String,List<Double>> data, List<String> columnName)
	{
		columns = new HashMap<>();
		classes = new ArrayList<>();
		
		this.columnsName = new ArrayList<>(columnName);
		for (Map.Entry<String,List<Double>> entry : data.entrySet())
		{
			if (entry.getKey().equals(columnsName.get(1)))
			{
				for (Double d : entry.getValue())
				{
					classes.add(String.valueOf(d));
				}
			}
			columns.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		
		
	}
	// Just for test
	public DatasetContainer()
	{
		columnsName.add("testPlus");
		columnsName.add("testMoins");
		columns = new HashMap();
		
//		Map<String,List<Double>> map = new HashMap<>();
//		List<Double> elems = new ArrayList();
//		elems.add(e)
//		map.put("testPlus", 6d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testPlus", 7d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testPlus", 6d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testPlus", 3d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testPlus", 2d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testPlus", 4d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testPlus", 2d);
//		columns.add(map);
//		
//		map = new HashMap<>();
//		map.put("testMoins", 0.2);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testMoins", 0.3);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testMoins", 0.5);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testMoins", 0.65);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testMoins", 1d);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testMoins", 1.2);
//		columns.add(map);
//		map = new HashMap<>();
//		map.put("testMoins", 1.7);
//		columns.add(map);
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
					firstLine = false;
				}
				else
				{
					//if (!classes.contains(lineSplit[2]))
					//{
						classes.add(lineSplit[2]);
				//	}
					for (int i = 0; i < lineSplit.length; i++) {
						List<Double> l = columns.get(columnsName.get(i));
						if (l == null)
						{
							l = new ArrayList<>();
						}
						try
						{
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
		Map<String,List<Double>> clone = new HashMap<>();
		
		for (Map.Entry<String,List<Double>> entry : columns.entrySet())
		{
			clone.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return clone;
	}
	public List<String> getKeys()
	{
		return columnsName;
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
}
