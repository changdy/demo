package tree.redblack;


import tree.base.BaseOperateUtil;

/**
 * Created by Changdy on 2019/5/23.
 */
public class RBNodeOperate {
    public static RBNode initNewTree(int value) {
        RBNode node = new RBNode(value);
        node.setNodeColor(ColorEnums.BLACK);
        return node;
    }

    public static RBNode addValue(RBNode rootNode, int value) {
        if (rootNode == null) {
            return initNewTree(value);
        }
        RBNode parentNode = BaseOperateUtil.findParentNode(rootNode, null, value);
        RBNode currentNode = new RBNode(value, parentNode);
        boolean addAtLeft = parentNode.getValue() > value;
        if (addAtLeft) {
            parentNode.setLeftChild(currentNode);
        } else {
            parentNode.setRightChild(currentNode);
        }
        if (parentNode.getNodeColor() == ColorEnums.BLACK) {
            return rootNode;
        } else {
            return balanceRBNode(rootNode, parentNode, currentNode);
        }
    }


    public static RBNode balanceRBNode(RBNode rootNode, RBNode parentNode, RBNode currentNode) {
        RBNode grandParent = parentNode.getParentNode();
        if (grandParent == null) {
            return rootNode;
        }
        RBNode uncle = parentNode.equals(grandParent.getLeftChild()) ? grandParent.getRightChild() : grandParent.getLeftChild();
        if (uncle != null) {
            uncle.setNodeColor(ColorEnums.BLACK);
            parentNode.setNodeColor(ColorEnums.BLACK);
            if (grandParent.getParentNode() != null) {
                grandParent.setNodeColor(ColorEnums.RED);
                if (grandParent.getParentNode().getNodeColor() == ColorEnums.RED) {
                    return balanceRBNode(rootNode, grandParent.getParentNode(), grandParent);
                }
            }
            return rootNode;
        }
        boolean currentIsLeft = currentNode.getValue() < parentNode.getValue();
        boolean parentIsLeft = parentNode.getValue() < grandParent.getValue();
        if (currentIsLeft == parentIsLeft) {
            return singleDirection(currentIsLeft, rootNode, parentNode, grandParent);
        } else {
            BaseOperateUtil.nodeReplace(grandParent, parentNode, currentNode);
            currentNode.setParentNode(grandParent);
            parentNode.setParentNode(currentNode);
            RBNode childNode;
            if (parentIsLeft) {
                childNode = currentNode.getLeftChild();
                parentNode.setRightChild(childNode);
                currentNode.setLeftChild(parentNode);
            } else {
                childNode = currentNode.getRightChild();
                parentNode.setLeftChild(childNode);
                currentNode.setRightChild(parentNode);
            }
            if (childNode != null) {
                childNode.setParentNode(parentNode);
            }
            return singleDirection(parentIsLeft, rootNode, currentNode, grandParent);
        }
    }

    private static RBNode singleDirection(boolean leftDirection, RBNode rootNode, RBNode parentNode, RBNode grandParent) {
        parentNode.setParentNode(grandParent.getParentNode());
        BaseOperateUtil.nodeReplace(grandParent.getParentNode(), grandParent, parentNode);
        grandParent.setNodeColor(ColorEnums.RED);
        grandParent.setParentNode(parentNode);
        parentNode.setNodeColor(ColorEnums.BLACK);
        RBNode cousinNode;
        if (leftDirection) {
            cousinNode = parentNode.getRightChild();
            grandParent.setLeftChild(cousinNode);
            parentNode.setRightChild(grandParent);
        } else {
            cousinNode = parentNode.getLeftChild();
            grandParent.setRightChild(cousinNode);
            parentNode.setLeftChild(grandParent);
        }
        if (cousinNode != null) {
            cousinNode.setParentNode(grandParent);
        }
        return rootNode == grandParent ? parentNode : rootNode;
    }
}
