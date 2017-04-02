package arbreDecision;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import arbreDecision.TreeBuilder.Symbol;

/**
 * Created by Marc-André on 2017-03-20.
 */
public class TreeBranch {

	private Object LeftNode;
    private Object rightNode;
    private String conditionAttribute;
    private Double conditionPartition;
    private Symbol conditionSymbol;
    private String value;

    public TreeBranch(String conditionAttribute, Double conditionPartition, Symbol conditionSymbol) {
        this.conditionAttribute = conditionAttribute;
        this.conditionPartition = conditionPartition;
        this.conditionSymbol = conditionSymbol;
    }

    public Object getLeftNode() {
        return LeftNode;
    }

    public void setLeftNode(Object leftNode) {
        LeftNode = leftNode;
    }
    

    public Object getRightNode() {
        return rightNode;
    }

    public void setRightNode(Object rightNode) {
        this.rightNode = rightNode;
    }
    
    public String getConditionAttribute() {
		return conditionAttribute;
	}

	public Double getConditionPartition() {
		return conditionPartition;
	}

	public Symbol getConditionSymbol() {
		return conditionSymbol;
	}

	public String getValue() {
		return value;
	}
	
	public Object[] getCondition()
	{
		Object[] elements = new Object[3];
		elements[0] = conditionAttribute;
		elements[1] = conditionSymbol;
		elements[2] = conditionPartition;
		return elements;
	}
}
