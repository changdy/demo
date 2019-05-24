package tree.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Changdy on 2019/5/23.
 */
@Getter
@Setter
public class BaseNode<T extends BaseNode> {
    private T leftChild;
    private T rightChild;
    @JSONField(serializeUsing = ParentNodeSerializer.class)
    private T parentNode;
    private int value;

    public BaseNode(int value) {
        this.value = value;
    }

    public BaseNode(int value, T parentNode) {
        this.value = value;
        this.parentNode = parentNode;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
