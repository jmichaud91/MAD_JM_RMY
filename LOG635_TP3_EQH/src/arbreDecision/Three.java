package arbreDecision;

import java.util.Objects;

/**
 * Created by Yassine on 2017-03-20.
 */
public class Three {
    private Objects value;
    private Three LeftNode;
    private Three rightNode;

    public Three(Objects value, Three leftNode, Three rightNode) {
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

    public Three getLeftNode() {
        return LeftNode;
    }

    public void setLeftNode(Three leftNode) {
        LeftNode = leftNode;
    }

    public Three getRightNode() {
        return rightNode;
    }

    public void setRightNode(Three rightNode) {
        this.rightNode = rightNode;
    }
}
