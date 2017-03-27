package mainPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetContainer {

	List<String> columnsName = new ArrayList<>();
	List<Map<String, Double>> lines;
	List<String> labels;

	// Take into account that the exit classes are on the end of the file
	public DatasetContainer(String filepath) throws IOException {
		columnsName = new ArrayList<>();
		lines = new ArrayList<>();
		labels = new ArrayList<>();
		readCSVDataFile(filepath);
	}
	// Just for test
	public DatasetContainer()
	{
		columnsName.add("testPlus");
		columnsName.add("testMoins");
		lines = new ArrayList<>();
		
		Map<String,Double> map = new HashMap<>();
		map.put("testPlus", 6d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testPlus", 7d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testPlus", 6d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testPlus", 3d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testPlus", 2d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testPlus", 4d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testPlus", 2d);
		lines.add(map);
		
		map = new HashMap<>();
		map.put("testMoins", 0.2);
		lines.add(map);
		map = new HashMap<>();
		map.put("testMoins", 0.3);
		lines.add(map);
		map = new HashMap<>();
		map.put("testMoins", 0.5);
		lines.add(map);
		map = new HashMap<>();
		map.put("testMoins", 0.65);
		lines.add(map);
		map = new HashMap<>();
		map.put("testMoins", 1d);
		lines.add(map);
		map = new HashMap<>();
		map.put("testMoins", 1.2);
		lines.add(map);
		map = new HashMap<>();
		map.put("testMoins", 1.7);
		lines.add(map);
	}

	public List<Map<String, String>> readCSVDataFile(String filePath) throws IOException {
		Map<String, String> lineMap = new HashMap<>();
		List<Map<String, String>> lines = new ArrayList<>();
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
					continue;
				}
				else
				{
					// Add the labels to the list of labels. By default, labels are located at the end of the file (TODO)
					if (!labels.contains(lineSplit[lineSplit.length-1]))
					{
						labels.add(lineSplit[lineSplit.length-1]);
					}
					for (int i = 0; i < lineSplit.length; i++) {
						lineMap.put(columnsName.get(i), lineSplit[i]);
					}
					lines.add(lineMap);
				}

				
				

			}
		} finally {
			reader.close();
		}
		return null;

	}
	
	public List<Map<String,Double>> getdata()
	{
		return lines;
	}
	public List<String> getKeys()
	{
		return columnsName;
	}
}
