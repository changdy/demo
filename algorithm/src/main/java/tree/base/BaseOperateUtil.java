package tree.base;

import java.util.Objects;

/**
 * Created by Changdy on 2019/5/23.
 */
public class BaseOperateUtil {
    public static <T extends BaseNode> T findParentNode(T current, T parent, int value) {
        if (current == null) {
            return parent;
        }
        int currentValue = current.getValue();
        if (currentValue == value) {
            throw new RuntimeException("发现重复");
        }
        return (currentValue > value ? findParentNode((T) current.getLeftChild(), current, value) : findParentNode((T) current.getRightChild(), current, value));
    }

    public static <T extends BaseNode> void nodeReplace(T parentNode, T oldNode, T newNode) {
        if (parentNode == null) {
            return;
        }
        if (Objects.equals(parentNode.getLeftChild(), oldNode)) {
            parentNode.setLeftChild(newNode);
        } else {
            parentNode.setRightChild(newNode);
        }
    }
}
