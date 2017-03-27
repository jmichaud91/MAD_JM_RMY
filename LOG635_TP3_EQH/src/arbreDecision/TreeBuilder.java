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
	Map<String,List<Double>> orderedDataByAttributes; // Key is the attribute name and value is the values for all lines on this attribute
	Map<String,List<Double>> partitionPerAttributes; // Key is the attribute name, the value is the list of partitions for this attribute
	
	public TreeBuilder()
	{
		
	}
	
	public Tree buildTree(DatasetContainer container)
	{
		attributes = container.getKeys();
		Map<String,List<Double>> data = container.getdata();
		orderedDataByAttributes = container.getdata();
		partitionPerAttributes = new HashMap<>();
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
				double em = calculateEM(attribute, partition, limitationsPerAttribute);
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
	
	private double calculateEM(String attribute, double partition, Map<String,List<Map<Symbol,List<Double>>>> limitationsPerAttribute)
	{
		Map<String,List<Double>> orderedDataByAttributesClone = new HashMap<>();
		for (Map.Entry<String, List<Double>> entry : orderedDataByAttributes.entrySet())
		{
			orderedDataByAttributesClone.put(entry.getKey(), entry.getValue());
		}
		
		// Grande multi-boucle qui MAJ la liste de données selon les limitations
		for (String attr : limitationsPerAttribute.keySet()) // Divise en attributs
		{
			for (Map<Symbol,List<Double>> constraints : limitationsPerAttribute.get(attr)) // divise en contraintes
			{
				for (Map.Entry<Symbol, List<Double>> entry : constraints.entrySet()) // divise en symbole/contrainte
				{
					for (Double d : entry.getValue()) // Pour chaque contrainte
					{
						Iterator<Double> it = orderedDataByAttributesClone.get(attr).iterator();
						
						while (it.hasNext()) // boucle sur les vrai valeurs
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
		
		int countGreatherEqualThan = 0;
		int countTotalGreaterEqualThan = 0;
		
		int countLowerThan = 0;
		int countTotalLowerThan = 0;
		// Calculate P+ and P-
		for (Map.Entry<String, List<Double>> entry : orderedDataByAttributesClone.entrySet())
		{
			for (Double d : entry.getValue())
			{
				if (entry.getKey().equals(attribute))
				{
					if (d >= partition)
					{
						countGreatherEqualThan++;
					}
					else
					{
						countLowerThan++;
					}
				}
				
				
					if (d >= partition)
					{
						countTotalGreaterEqualThan++;
					}
					else
					{
						countTotalLowerThan++;
					}
			}
		}
		
		
		double pPlus = countGreatherEqualThan/countTotalGreaterEqualThan;
		double pMinus = countLowerThan/countTotalLowerThan;
		double pPlusMaj = countTotalGreaterEqualThan/(countTotalGreaterEqualThan + countTotalLowerThan);
		double pPlusMinus = countTotalLowerThan/(countTotalGreaterEqualThan + countTotalLowerThan);
		double entropyPlus = -pPlus*log2(pPlus) - (1 - pPlus)*log2(pPlus);
		double entropyMinus = -pMinus*log2(pMinus) - (1 - pMinus)*log2(pMinus);
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
