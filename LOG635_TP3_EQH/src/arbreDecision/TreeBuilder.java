package arbreDecision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TreeBuilder 
{
	List<String> attributes;
	List<String> distinctClasses;
	TreeRoot tree;
	
	public TreeBuilder()
	{
	}
	
	private Map<String,List<Double>> createPartitions(Map<String,List<Double>> data)
	{
		Map<String,List<Double>> unProcessedData = clone(data);
		Map<String,List<Double>> partitionPerAttribute = new HashMap<>();
		// sort the data for every attributes
		for (List<Double> ls : unProcessedData.values())
		{
			Collections.sort(ls);
		}
		
		// Create the partitions for every attributes
		for (String attribute : attributes)
		{
			List<Double> orderedData = unProcessedData.get(attribute);
			List<Double> partitions = new ArrayList<>();
			for (int i = 0; i < orderedData.size() -1; i++)
			{
				double d1 = orderedData.get(i);
				double d2 = orderedData.get(i+1);		
				if (d1 != d2)
				{
					double result = (d1 + ((d2 - d1)/2));
					partitions.add(result);	
				}
				
			}
			partitionPerAttribute.put(attribute, partitions);
		}
		return partitionPerAttribute;
	}
	
	public TreeRoot buildTree(DatasetContainer container, double prunePercent)
	{
		attributes = container.getKeys();
		distinctClasses = container.getDistinctClasses();
		attributes.remove(container.getClassesKey());
		do
		{
			double bestEM = Integer.MAX_VALUE;
			String bestAttribute = "";
			double bestPartition = 0;
			int bestAttributePartitionIndex = 0;
			Map.Entry<String, Double> bestAboveBranchClassPercentage = null;
			Map.Entry<String, Double> bestBelowBranchClassPercentage = null;
			
			Map<String,Map<Symbol,List<Double>>> limitations;
			if (tree == null)
			{
				limitations = new HashMap<>();
			}
			else
			{
				limitations = tree.getNextBranchConditions();
			}
			
			List<String>classes = container.getClasses();
			Map<String,List<Double>> filteredDataPerAttribute = clone(container.getdata());
			filteredDataPerAttribute.remove(container.getClassesKey());
			filterDataByLimitation(filteredDataPerAttribute,classes, limitations);
			
			Map<String,List<Double>> filteredPartitionPerAttribute = createPartitions(filteredDataPerAttribute);
			filterPartitionByLimitation(filteredPartitionPerAttribute, limitations);

		for (String attribute : attributes)
		{
			List<Double> attributePartition = new ArrayList<>(filteredPartitionPerAttribute.get(attribute));
			for (int  i = 0; i < attributePartition.size(); i++)
			{
				Object[] emAndBranchMainComposition;

				emAndBranchMainComposition = calculateEM(attribute, attributePartition.get(i), filteredDataPerAttribute.get(attribute), classes);
				
				if ((double)emAndBranchMainComposition[0] <= bestEM)
				{
					bestEM = (double)emAndBranchMainComposition[0];
					bestAttribute = attribute;
					bestPartition = attributePartition.get(i);
					bestAttributePartitionIndex = i;
					bestAboveBranchClassPercentage = (Map.Entry<String, Double>) emAndBranchMainComposition[1];
					bestBelowBranchClassPercentage = (Map.Entry<String, Double>) emAndBranchMainComposition[2];
				}
				//System.out.println("em: " + emAndBranchMainComposition[0] + " attribute: " + attribute + " Partition: " + attributePartition.get(i));		
			}
		}
		filteredPartitionPerAttribute.get(bestAttribute).remove(bestAttributePartitionIndex);
		TreeBranch branch = new TreeBranch(bestAttribute, bestPartition, new Symbol(">="));
		
		
		if (bestBelowBranchClassPercentage.getValue() >= prunePercent)
		{
			branch.setLeftNode(new TreeLeaf(bestBelowBranchClassPercentage.getKey()));
		}
		if (bestAboveBranchClassPercentage.getValue() >= prunePercent)
		{
			branch.setRightNode(new TreeLeaf(bestAboveBranchClassPercentage.getKey()));
		}
		
		if (tree == null)
		{
			tree = new TreeRoot(branch);
		}
		else
		{
			tree.setCurrentBranchConditions(branch);	
		}
		
		//System.out.println("Best em: " + bestEM + " best attribute: " + bestAttribute + " best Partition: " + bestPartition);
		} while (!tree.isCompleted());

		return tree;
	}
	
	private Map<String,List<Double>> clone(Map<String,List<Double>> data)
	{
		Map<String,List<Double>> clone = new HashMap<>();
		for (Map.Entry<String, List<Double>> entry : data.entrySet())
		{
			clone.put(entry.getKey(), new ArrayList<Double>(entry.getValue()));
		}
		return clone;
	}
	
	private Map<String,List<Double>> filterPartitionByLimitation(Map<String,List<Double>> partitions, Map<String,Map<Symbol,List<Double>>> limitations)
	{
		Map<String, List<Double>> filteredPartitions = new HashMap<>();
		// Pour chaque limitations par attribut
		for (Map.Entry<String, Map<Symbol,List<Double>>> limitationPerAttribute : limitations.entrySet())
		{
			// Pour chaque limitation par symbole
			for (Map.Entry<Symbol, List<Double>> limitationPerSymbol : limitationPerAttribute.getValue().entrySet())
			{
				// Pour chaque limitation
				for (Double limitation : limitationPerSymbol.getValue())
				{
					if (partitions.get(limitationPerAttribute.getKey())!= null)
					{
						Iterator<Double> it = partitions.get(limitationPerAttribute.getKey()).iterator();
						while (it.hasNext())
						{
							Double partition = it.next();
							
							
							boolean deleteAllGreaterThan;
							if (limitationPerSymbol.getKey().isGreaterThan() && limitationPerSymbol.getKey().isRightBranch)
							{
								deleteAllGreaterThan = false;
							}
							else if (limitationPerSymbol.getKey().isGreaterThan() && !limitationPerSymbol.getKey().isRightBranch)
							{
								deleteAllGreaterThan = true;
							}
							else if (!limitationPerSymbol.getKey().isGreaterThan() && limitationPerSymbol.getKey().isRightBranch)
							{
								deleteAllGreaterThan = true;
							}
							else
							{
								deleteAllGreaterThan = false;
							}
							
							if ((deleteAllGreaterThan && partition > limitation) || (!deleteAllGreaterThan && partition < limitation))
							{
								it.remove();
							}	
						}
					}
					
				}
			}
		}	
		return partitions;
			
	}
	
	
	private Map<String,List<Double>> filterDataByLimitation(Map<String,List<Double>> data,List<String> classes, Map<String,Map<Symbol,List<Double>>> limitations)
	{
		// Pour chaque limitations par attribut
		for (Map.Entry<String, Map<Symbol,List<Double>>> limitationPerAttribute : limitations.entrySet())
		{
			// Pour chaque limitation par symbole
			for (Map.Entry<Symbol, List<Double>> limitationPerSymbol : limitationPerAttribute.getValue().entrySet())
			{
				// Pour chaque limitation
				for (Double limitation : limitationPerSymbol.getValue())
				{
					if (data.get(limitationPerAttribute.getKey())!= null)
					{
						List<Integer> removeAtIndex = new ArrayList<>();
						for (int i = 0; i < data.get(limitationPerAttribute.getKey()).size(); i++)
						{
							
							boolean deleteAllGreaterThan;
							if (limitationPerSymbol.getKey().isGreaterThan() && limitationPerSymbol.getKey().isRightBranch)
							{
								deleteAllGreaterThan = false;
							}
							else if (limitationPerSymbol.getKey().isGreaterThan() && !limitationPerSymbol.getKey().isRightBranch)
							{
								deleteAllGreaterThan = true;
							}
							else if (!limitationPerSymbol.getKey().isGreaterThan() && limitationPerSymbol.getKey().isRightBranch)
							{
								deleteAllGreaterThan = true;
							}
							else
							{
								deleteAllGreaterThan = false;
							}
							
							if ((deleteAllGreaterThan && data.get(limitationPerAttribute.getKey()).get(i) > limitation) || (!deleteAllGreaterThan && data.get(limitationPerAttribute.getKey()).get(i) < limitation))
							{
								removeAtIndex.add(i);
							}	
						}
						removeAtIndex.sort(Comparator.reverseOrder());
						for (Map.Entry<String,List<Double>> d : data.entrySet())
							{
								for (int i : removeAtIndex)
									{
										d.getValue().remove(i);
									}
							}
						for (int i : removeAtIndex)
						{
							classes.remove(i);
						}
					}
					
				}
			}
		}	
		return data;
			
	}
	/**
	 * 
	 * @param attribute
	 * @param partition
	 * @param limitationsForAttribute
	 * @return table with 3 element. 1st is the em, second is the percentDataClass for ">=" of the most numerous class. Third is the same but for "<"
	 */
	private Object[] calculateEM(String attribute, double partition, List<Double> dataForAttribute, List<String> classeForAttribute)
	{
		
		int countTotalGreaterEqualThan = 0;
		int countTotalLowerThan = 0;
		Map<String,Double> countAbovePartitionPerClass = new HashMap<>();
		Map<String,Double> countBelowPartitionPerClass = new HashMap<>();
		for (String s : distinctClasses)
		{
			countAbovePartitionPerClass.put(s, 0d);
			countBelowPartitionPerClass.put(s, 0d);
		}
		// Calculate P+ and P-
			for (int i = 0; i < dataForAttribute.size(); i++)
			{
					if (dataForAttribute.get(i) >= partition)
					{
						countAbovePartitionPerClass.put(String.valueOf(classeForAttribute.get(i)), countAbovePartitionPerClass.get(classeForAttribute.get(i)) + 1);
						countTotalGreaterEqualThan++;
					}
					else
					{
						countBelowPartitionPerClass.put(String.valueOf(classeForAttribute.get(i)), countBelowPartitionPerClass.get(classeForAttribute.get(i)) + 1);
						countTotalLowerThan++;
					}
			}
			
			double pPlus;
			double pMinus;
			double entropyPlus = 0;
			double entropyMinus = 0;
			for (String s : distinctClasses)
			{
				pPlus = countAbovePartitionPerClass.get(s)/ (double) countTotalGreaterEqualThan;
				pMinus = countBelowPartitionPerClass.get(s)/ (double) countTotalLowerThan;
				// Si Pplus ou Pminus == 0, il y a une simplification à 0 du terme de droite.
				if (pPlus != 0)
					entropyPlus = entropyPlus - pPlus*log2(pPlus);
				if (pMinus != 0)
					entropyMinus = entropyMinus - pMinus*log2(pMinus);
				
				
			}
		double pPlusMaj = countTotalGreaterEqualThan/ (double) (countTotalGreaterEqualThan + countTotalLowerThan);
		double em = pPlusMaj*entropyPlus + (1-pPlusMaj)*entropyMinus;
		Map.Entry<String, Double> mostNumerousEntryGreaterThan = getMostNumerousEntry(countAbovePartitionPerClass);
		mostNumerousEntryGreaterThan.setValue((mostNumerousEntryGreaterThan.getValue()/countTotalGreaterEqualThan)*100);
		
		Map.Entry<String, Double> mostNumerousEntryLowerThan = getMostNumerousEntry(countBelowPartitionPerClass);
		mostNumerousEntryLowerThan.setValue((mostNumerousEntryLowerThan.getValue()/countTotalLowerThan)*100);
		return new Object[]{em, mostNumerousEntryGreaterThan, mostNumerousEntryLowerThan};
		
	}
	
	private Map.Entry<String, Double> getMostNumerousEntry(Map<String,Double> map)
	{
		Map.Entry<String,Double> m = null;
		for (Map.Entry<String, Double> entry : map.entrySet())
		{
			if (m == null)
			{
				m = entry;
			}
			else
			{
				if (entry.getValue() > m.getValue())
				{
					m = entry;
				}
			}
		}
		return m;
	}
	
	protected class percentDataClass
	{
		String classe;
		double percentage;
		
		public percentDataClass(String classe, int amount, int totalAmount)
		{
			percentage = (amount/ (double) totalAmount)*100;
			this.classe = classe;
		}
		
		public String getClasse()
		{
			return classe;
		}
		
		public double getPercentage()
		{
			return percentage;
		}
	}
	
	// This division is used to grt the result of a log2. See https://en.wikipedia.org/wiki/Logarithm#Change_of_base for details
	private double log2(double x)
	{
		return Math.log(x)/Math.log(2);
	}

	
}
