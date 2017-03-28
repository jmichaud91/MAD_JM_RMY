package arbreDecision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mainPackage.DatasetContainer;

public class TreeBuilder 
{
	List<String> attributes;
	Map<String,List<Double>> dataByAttributes; // Key is the attribute name and value is the values for all lines on this attribute
	Map<String,List<Double>> partitionPerAttributes; // Key is the attribute name, the value is the list of partitions for this attribute
	List<String> classes;
	
	public TreeBuilder()
	{
		
	}
	
	public Tree buildTree(DatasetContainer container)
	{
		attributes = container.getKeys();
		dataByAttributes = container.getdata();
		partitionPerAttributes = new HashMap<>();
		classes = container.getDistinctClasses();
		
		// Fill the orderedData attributes 
		/*for (Map<String,Double> map : data)
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
				attributeData.add(map.get(key));
				orderedDataByAttributes.put(key,attributeData);
			}
		}*/
		Map<String,List<Double>> orderedDataByAttributes = container.getdata();
		// sort the data for every attributes
		for (List<Double> ls : orderedDataByAttributes.values())
		{
			Collections.sort(ls);
		}
		
		// Create the partitions for every attributes
		for (String attribute : attributes)
		{
			List<Double> orderedData = orderedDataByAttributes.get(attribute);
			List<Double> partitions = new ArrayList<>();
			for (int i = 0; i < orderedData.size() -1; i++)
			{
				double result = (orderedData.get(i+1) + (orderedData.get(i+1) - orderedData.get(i))/2);
				if (result != 0)
				partitions.add(result);
			}
			partitionPerAttributes.put(attribute, partitions);
		}
		
		double bestEM = 1;
		String bestAttribute = "";
		double bestPartition = 0;
		for (String attribute : attributes)
		{
			List<Double> attributePartition = partitionPerAttributes.get(attribute);
			Map<String,List<Map<Symbol,List<Double>>>> limitationsPerAttribute = new HashMap<>();
			for (Double partition : attributePartition)
			{
				double em = calculateEM(attribute, partition, limitationsPerAttribute.get(attribute));
				if (em < bestEM)
				{
					bestEM = em;
					bestAttribute = attribute;
					bestPartition = partition;
				}
				System.out.println("em: " + em + " attribute: " + attribute + " Partition: " + partition);		
			}
		}
		
		System.out.println("Best em: " + bestEM + " best attribute: " + bestAttribute + " best Partition: " + bestPartition);
		//But: calculer la meilleure EM.
		// Pour cela, calculer H
		// Pour cela calculer: Pour chaque attribut, pour chaque partition, calculer l'entropie moyenne.
		// Pour calculer H pour une partition, on fait le # positif / le # total
		// TODO create the big Map that will calculate over and under for every partition as well as H and EM
		
		
		
		Tree tree = new Tree();
		return tree;
	}
	
	private double calculateEM(String attribute, double partition, List<Map<Symbol,List<Double>>> limitationsForAttribute)
	{
		List<Double> dataForAttribute;
		dataForAttribute = dataByAttributes.get(attribute);
		List<Double> classForAttribute = dataByAttributes.get(attribute);
		
		
		
				if (limitationsForAttribute != null)
				{
				for (Map<Symbol, List<Double>> map : limitationsForAttribute) 
				{
					for (Map.Entry<Symbol, List<Double>> entry : map.entrySet())
					{
						for (Double d : entry.getValue()) 
						{
							Iterator<Double> it = dataForAttribute.iterator();
							
							while (it.hasNext())
							{
								Double value = it.next();
								if (entry.getKey().hasEquality() && value == d)
								{
									it.remove();
								}
								
								else if (entry.getKey().isGreaterThan() && value > d)
								{
									it.remove();
								}
								else if (!entry.getKey().isGreaterThan() && value < d)
								{
									it.remove();
								}
							}
						}
					}
					
				}
				}
			
		
		
		//int countGreatherEqualThan = 0;
		int countTotalGreaterEqualThan = 0;
		
		//int countLowerThan = 0;
		int countTotalLowerThan = 0;
		Map<String,Double> countAbovePartitionPerClass = new HashMap();
		Map<String,Double> countBelowPartitionPerClass = new HashMap();
		for (String s : classes)
		{
			countAbovePartitionPerClass.put(s, 0d);
			countBelowPartitionPerClass.put(s, 0d);
		}
		// Calculate P+ and P-
			for (int i = 0; i < dataForAttribute.size(); i++)
			{
					if (dataForAttribute.get(i) >= partition)
					{
						countAbovePartitionPerClass.put(String.valueOf(classForAttribute.get(i)), classForAttribute.get(i) + 1);
						countTotalGreaterEqualThan++;
					}
					else
					{
						countBelowPartitionPerClass.put(String.valueOf(classForAttribute.get(i)), classForAttribute.get(i) + 1);
						countTotalLowerThan++;
					}
			}
		
		
		
		//double pPlus = countGreatherEqualThan/countTotalGreaterEqualThan;
		//double pMinus = countLowerThan/countTotalLowerThan;
			double pPlus;
			double pMinus;
			double entropyPlus = 0;
			double entropyMinus = 0;
			for (String s : classes)
			{
				pPlus = countAbovePartitionPerClass.get(s)/ countTotalGreaterEqualThan;
				pMinus = countBelowPartitionPerClass.get(s)/countTotalLowerThan;
				entropyPlus = entropyPlus - pPlus*log2(pPlus);
				entropyMinus = entropyMinus - pMinus*log2(pMinus);
			}
		double pPlusMaj = countTotalGreaterEqualThan/(countTotalGreaterEqualThan + countTotalLowerThan);
		double pPlusMinus = countTotalLowerThan/(countTotalGreaterEqualThan + countTotalLowerThan);
		//double entropyPlus = -pPlus*log2(pPlus) - (1 - pPlus)*log2(pPlus);
		//double entropyMinus = -pMinus*log2(pMinus) - (1 - pMinus)*log2(pMinus);
		double EM = pPlusMaj*entropyPlus + (1-pPlusMaj)*entropyMinus;
		return EM;
		
	}
	
	// This division is used to grt the result of a log2. See https://en.wikipedia.org/wiki/Logarithm#Change_of_base for details
	private double log2(double x)
	{
		return Math.log(x)/Math.log(2);
	}
	// Class for the symbol ">", "<", ">=" or "<="
	protected class Symbol
	{
		String symbol;
		public Symbol(String symbol)
		{
			this.symbol = symbol;
			
		}
		
		public boolean hasEquality()
		{
			return symbol.contains("=");
		}
		
		public boolean isGreaterThan()
		{
			return symbol.contains(">");
		}
	}
	
	private class BranchCalculator
	{
		private String attribute;
		private List<Double> partitions;
		
		
	}
}
