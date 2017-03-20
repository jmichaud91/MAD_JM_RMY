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
	List<Map<String, String>> lines;

	public DatasetContainer(String filepath) throws IOException {
		columnsName = new ArrayList<>();
		lines = new ArrayList<>();
		readCSVDataFile(filepath);
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

				for (int i = 0; i < lineSplit.length; i++) {
					lineMap.put(columnsName.get(i), lineSplit[i]);
				}
				lines.add(lineMap);

			}
		} finally {
			reader.close();
		}
		return null;

	}
	
	public List<Map<String,String>> getdata()
	{
		return lines;
	}
	public List<String> getKeys()
	{
		return columnsName;
	}
}
