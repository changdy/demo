package tree.redblack;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;
import tree.base.BaseNode;

import static tree.redblack.ColorEnums.RED;

/**
 * Created by Changdy on 2019/5/23.
 */
@Getter
@Setter
@JSONType(orders = {"value", "nodeColor", "leftChild", "rightChild"})
public class RBNode extends BaseNode<RBNode> {
    private ColorEnums nodeColor = RED;

    public RBNode(int value) {
        super(value);
    }

    public RBNode(int value, RBNode parentNode) {
        super(value, parentNode);
    }

}
