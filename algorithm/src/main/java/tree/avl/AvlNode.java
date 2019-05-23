package tree.avl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Changdy on 2019/5/18.
 */
@Getter
@Setter
@JSONType(orders = {"value", "leftDepth", "leftChild", "rightDepth", "rightChild"})
public class AvlNode {
    private AvlNode leftChild;
    private AvlNode rightChild;
    @JSONField(serializeUsing = ParentNodeSerializer.class)
    private AvlNode parentNode;
    private int leftDepth;
    private int rightDepth;
    private int value;

    public AvlNode(int value) {
        this.value = value;
    }

    public AvlNode(int value, AvlNode parentNode) {
        this.value = value;
        this.parentNode = parentNode;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
