package arbreDecision;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Yassine on 2017-03-20.
 */
public class TreeRoot {
    private Object LeftNode;
    private Object rightNode;
    private Object root;
    private int depth;
    private Object parentNode;
    private boolean isCompleted;
    private List<String[]> serializationList;
    
    public TreeRoot(TreeBranch root) {
        this.root = root;
        depth = 1;
    }
    
    public TreeRoot(String filePath) throws IOException
    {
    	serializationList = new ArrayList<>();
    	String line;
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(filePath));
			while ((line = reader.readLine()) != null) {

				String[] lineSplit = line.split(",",-1);
				serializationList.add(lineSplit);
					
				
			}
		} finally {
			reader.close();
		}
		deSerialize(root, null, false);
    }
    
    private void deSerialize(Object newNode, Object parentNode, boolean isRightBranch)
    {
    	String[] line = serializationList.get(0);
    	serializationList.remove(0);
    	if (!line[3].isEmpty())
    	{
    		
    		TreeLeaf leaf = new TreeLeaf(line[3]);
    		// If the root is a leaf, it end right there
    		if (parentNode == null)
    		{
    			newNode = leaf;
    			return;
    		}
    		TreeBranch parentbranch = (TreeBranch) parentNode;
    		if (isRightBranch)
    		{
    			parentbranch.setRightNode(leaf);
    		}
    		else
    		{
    			parentbranch.setLeftNode(leaf);
    		}
    		
    	}
    	else
    	{
    		
    		newNode = new TreeBranch(line[0],Double.parseDouble(line[2]),  new Symbol(line[1]));
    		if (root == null)
    		{
    			root = newNode;
    		}
    		else if (isRightBranch)
    		{
    			((TreeBranch)parentNode).setRightNode(newNode);
    		}
    		else
    		{
    			((TreeBranch)parentNode).setLeftNode(newNode);
    		}
    			
    		deSerialize(((TreeBranch)newNode).getLeftNode(), newNode, false);
    		deSerialize(((TreeBranch)newNode).getLeftNode(), newNode, true);
    	}
    }
    /**
     * Serialize the data. first elem is the attribute, second is the string representation of the symbol, third is the partition and fourth is the value.
     * Everything is separated by ","
     * The value is empty if it's not a leaf. If it is a leaf, the 3 other parameters are empty
     * @param fileName
     * @throws IOException 
     */
    public void serialize(String fileName) throws IOException
    {
    	serializationList = new ArrayList<>();
    	BufferedWriter buffWriter = null;
		FileWriter fileWritter = null;
    	serializeRec(root);
    	try
    	{
    		String content = "";
    		for (String[] s : serializationList)
    		{
    			
    			content = content + s[0] + "," + s[1] + "," + s[2] + "," + s[3] +  System.lineSeparator();
    		}

    		fileWritter = new FileWriter(fileName + ".treeModel");
    		buffWriter = new BufferedWriter(fileWritter);
    		buffWriter.write(content);
    	}
    	finally
    	{
    		buffWriter.close();
    		fileWritter.close();
    	}
    
    }
    
    public void serializeRec(Object node)
    {
    	if (node == null)
    	{
    		return;
    	}
    	String[] data = new String[4];
    	if (node instanceof TreeLeaf)
    	{
    		data[0] = "";
    		data[1] = "";
    		data[2] = "";
    		data[3] = ((TreeLeaf) node).getValue();
    		serializationList.add(data);
    		return;
    	}
    	
    	TreeBranch branch = (TreeBranch) node;
    	data[0] = branch.getConditionAttribute();
    	data[1] = branch.getConditionSymbol().getStringValue();
    	data[2] = branch.getConditionPartition().toString();
    	data[3] = "";
    	serializationList.add(data);
    	
    	serializeRec(((TreeBranch) node).getLeftNode());
    	serializeRec(((TreeBranch) node).getRightNode());
    }
    
    public boolean isCompleted()
    {
    	getNextBranchConditions(); // update the tree
    	return isCompleted;
    }
    
    public String classifyInstance(Map<String,Double> instance)
    {
    	Object node = root;
    	do
    	{
    		TreeBranch branch = (TreeBranch) node;
    		Symbol symbol = branch.getConditionSymbol();
    		String attribute = branch.getConditionAttribute();
    		double partition = branch.getConditionPartition();
			if (symbol.isGreaterThan())
			{
				if (instance.get(attribute) >= partition )
				{
					node = branch.getRightNode();
				}
				else
				{
					node = branch.getLeftNode();
				}
				
			}
			else 
			{
				if (instance.get(attribute) < partition )
				{
					node = branch.getRightNode();
				}
				else
				{
					node = branch.getLeftNode();
				}
			}
			
    	} while (!(node instanceof TreeLeaf));
    	
    	return ((TreeLeaf) node).getValue();
    }
    
    
   public Map<String,Map<Symbol,List<Double>>> getNextBranchConditions()
    {
	   
    	Map<String,Map<Symbol,List<Double>>> m = digDeeper(root, 0);
    	// If we get null, increase the depth of the search. If it is still null, then all nodes are fully developped.
    	if (m == null)
    	{
    		depth++;
    		m = digDeeper(root, 0);
    	}
    	if (m == null)
    	{
    		isCompleted = true;
    	}
    	return m;
    }
   
   public void setCurrentBranchConditions(TreeBranch branch)
   {
   	if (parentNode == null)
   		throw new RuntimeException("There is no node selected. Please use getNextBranchCondition() to get the node");
   	
   	TreeBranch currentBranch = (TreeBranch) parentNode;
   	if (currentBranch.getLeftNode() == null)
   	{
   		currentBranch.setLeftNode(branch);
   	}
   	else
   	{
   		currentBranch.setRightNode(branch);
   	}
   	
   	currentBranch = null;
   }
   /**
    * Get the first uninitialized branch at the dept indicated starting from the left.
    * @param node
    * @param depth
    * @return
    */
   private Map<String,Map<Symbol,List<Double>>> digDeeper(Object node, int depth)
   {
	   if (node == null && this.depth == depth)
	   {
		   return new HashMap<String,Map<Symbol,List<Double>>>();
	   }
	   
	   if (depth > this.depth)
	   {
		   return null;
	   }
	   if (node instanceof TreeLeaf)
	   {
		   return null;
	   }
	   TreeBranch branch = (TreeBranch) node;
	   boolean isRightBranch;
	   Map<String,Map<Symbol,List<Double>>> conditionSoFar = digDeeper(branch.getLeftNode(), depth+1);
	   if (conditionSoFar!= null)
	   {
		   if (conditionSoFar.isEmpty())
		   {
			   parentNode = branch;
		   }
		   isRightBranch = false;
		  
		 return addToConditionList(branch.getCondition(), conditionSoFar, isRightBranch);
	   }
	   
	   conditionSoFar = digDeeper(branch.getRightNode(), depth+1);
	   if (conditionSoFar!= null)
	   {
		   if (conditionSoFar.isEmpty())
		   {
			   parentNode = branch;
		   }
		   isRightBranch = true;
		 return addToConditionList(branch.getCondition(), conditionSoFar, isRightBranch);
	   }
	   return null;
	   
	   
	   
   }
   
   private Map<String,Map<Symbol,List<Double>>> addToConditionList(Object[] newCondition,  Map<String,Map<Symbol,List<Double>>> currentConditions, boolean isRightBranch)
   {
	   Map<Symbol,List<Double>> singleAttributeConditions = currentConditions.get((String) newCondition[0]);
	   if (singleAttributeConditions == null)
		   singleAttributeConditions = new HashMap<>();
	   Symbol symbol = (Symbol) newCondition[1];
	   symbol.setIsRightBranch(isRightBranch);
	   List<Double> singleSymbolConditions = singleAttributeConditions.get(symbol);
	   if (singleSymbolConditions == null)
		   singleSymbolConditions = new ArrayList<>();
	   
	   singleSymbolConditions.add((Double) newCondition[2]);
	   
	   singleAttributeConditions.put((Symbol) symbol, singleSymbolConditions);
	   currentConditions.put((String) newCondition[0], singleAttributeConditions);
	   
	   return currentConditions;
	   
	   
   }
   
   public void setCurrentLeaf(TreeRoot tree)
   {
   	
   }
   
   public void moveToNextLeaf()
   {
	  
   }
}
