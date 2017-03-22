package arbreDecision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mainPackage.DatasetContainer;

public class TreeBuilder 
{
	List<String> attributes;
	Map<String,List<Double>> orderedDataByAttributes; // Key is the attribute name and value is the values for all lines on this attribute
	Map<String,List<String>> partitionPerAttributes; // Key is the attribute name, the value is the list of partitions for this attribute
	List<Map<String,String>> resultsPerPartition;
	
	public TreeBuilder()
	{
		
	}
	
	public Tree buildTree(DatasetContainer container)
	{
		attributes = container.getKeys();
		List<Map<String,String>> data = container.getdata();
		orderedDataByAttributes = new HashMap<>();
		// Fill the orderedData attributes 
		for (Map<String,String> map : data)
		{
			for (String key : attributes)
			{
				List<Double> attributeData = orderedDataByAttributes.get(key);
				if (attributeData == null)
				{
					attributeData = new ArrayList<>();
				}
				if (map.get(key) == null)
				{
					// filter the null out
					continue;
				}
				attributeData.add(Double.parseDouble(map.get(key)));
				orderedDataByAttributes.put(key,attributeData);
			}
		}
		// sort the data for every attributes
		for (List<Double> ls : orderedDataByAttributes.values())
		{
			Collections.sort(ls);
		}
		
		// Create the partitions for every attributes
		for (String attribute : attributes)
		{
			List<Double> orderedData = orderedDataByAttributes.get(attribute);
			for (int i = 0; i < orderedData.size() -1; i++)
			{
				List<Double> partitions = new ArrayList<>();
				partitions.add((orderedData.get(i+1) - orderedData.get(i))/2);
			}
		}
		
		// TODO create the big Map that will calculate over and under for every partition as well as H and EM
		
		
		
		Tree tree = new Tree();
		return tree;
	}
}
