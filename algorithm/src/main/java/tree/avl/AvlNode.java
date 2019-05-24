package tree.avl;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;
import tree.base.BaseNode;

/**
 * Created by Changdy on 2019/5/18.
 */
@Getter
@Setter
@JSONType(orders = {"value", "leftDepth", "leftChild", "rightDepth", "rightChild"})
public class AvlNode extends BaseNode<AvlNode> {
    private int leftDepth;
    private int rightDepth;

    public AvlNode(int value) {
        super(value);
    }

    public AvlNode(int value, AvlNode parentNode) {
        super(value, parentNode);
    }
}
