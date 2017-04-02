package arbreDecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import arbreDecision.TreeBuilder.Symbol;

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
    
    public TreeRoot(TreeBranch root) {
        this.root = root;
        depth = 1;
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
