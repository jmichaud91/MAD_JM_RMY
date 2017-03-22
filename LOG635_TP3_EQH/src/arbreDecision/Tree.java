package arbreDecision;

import java.util.Objects;

/**
 * Created by Yassine on 2017-03-20.
 */
public class Tree {
    private Objects value;
    private Tree LeftNode;
    private Tree rightNode;
    
    public Tree()
    {
    	
    }

    public Tree(Objects value, Tree leftNode, Tree rightNode) {
        this.value = value;
        LeftNode = leftNode;
        this.rightNode = rightNode;
    }


    public Objects getValue() {
        return value;
    }

    public void setValue(Objects value) {
        this.value = value;
    }

    public Tree getLeftNode() {
        return LeftNode;
    }

    public void setLeftNode(Tree leftNode) {
        LeftNode = leftNode;
    }

    public Tree getRightNode() {
        return rightNode;
    }

    public void setRightNode(Tree rightNode) {
        this.rightNode = rightNode;
    }
}
