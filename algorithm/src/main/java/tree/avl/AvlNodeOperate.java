package tree.avl;

import java.util.Objects;

/**
 * Created by Changdy on 2019/5/19.
 */
public class AvlNodeOperate {
    public static AvlNode initNewTree(int value) {
        return new AvlNode(value);
    }

    public static AvlNode addValue(AvlNode rootNode, int value) {
        if (rootNode == null) {
            return initNewTree(value);
        }
        // 简单插入,
        AvlNode newNode = new AvlNode(value, rootNode);
        if (rootNode.getValue() > value && rootNode.getLeftDepth() == 0) {
            rootNode.setLeftChild(newNode);
            rootNode.setLeftDepth(1);
            return rootNode;
        } else if (rootNode.getValue() < value && rootNode.getRightDepth() == 0) {
            rootNode.setRightChild(newNode);
            rootNode.setRightDepth(1);
            return rootNode;
        }
        // 找到位置
        AvlNode parentNode = findParentNode(rootNode, null, value);
        newNode = new AvlNode(value, parentNode);
        if (parentNode.getValue() > value) {
            parentNode.setLeftChild(newNode);
            parentNode.setLeftDepth(1);
        } else {
            parentNode.setRightChild(newNode);
            parentNode.setRightDepth(1);
        }
        if (parentNode.getLeftDepth() == parentNode.getRightDepth()) {
            // 判断本次插入是否有实际影响,没有的话直接return
            return rootNode;
        }
        return depthIncrease(parentNode.getParentNode(), rootNode, Objects.equals(parentNode, parentNode.getParentNode().getLeftChild()));
    }

    private static AvlNode depthIncrease(AvlNode currentNode, AvlNode rootNode, boolean leftIncrease) {
        if (leftIncrease) {
            currentNode.setLeftDepth(currentNode.getLeftDepth() + 1);
        } else {
            currentNode.setRightDepth(currentNode.getRightDepth() + 1);
        }
        int balanceFactor = currentNode.getLeftDepth() - currentNode.getRightDepth();
        // 已经平衡就无需改变了
        AvlNode parentNode = currentNode.getParentNode();
        boolean currentNodeIsRoot = parentNode == null;
        if (balanceFactor == 0) {
            return rootNode;
        }
        if (Math.abs(balanceFactor) == 1) {
            if (currentNodeIsRoot) {
                return rootNode;
            }
            return depthIncrease(parentNode, rootNode, Objects.equals(currentNode, parentNode.getLeftChild()));
        }
        if (balanceFactor == 2) {
            return leftRotate(currentNode, parentNode, currentNodeIsRoot, rootNode);
        } else if (balanceFactor == -2) {
            return rightRotate(currentNode, parentNode, currentNodeIsRoot, rootNode);
        }
        throw new RuntimeException("平衡因子错了吧");
    }

    private static AvlNode leftRotate(AvlNode currentNode, AvlNode parentNode, boolean currentNodeIsRoot, AvlNode rootNode) {
        AvlNode leftChild = currentNode.getLeftChild();
        AvlNode grandSon = leftChild.getRightChild();
        if (leftChild.getLeftDepth() > leftChild.getRightDepth()) {
            // 左左旋转
            if (grandSon != null) {
                grandSon.setParentNode(currentNode);
            }
            // 经过ll旋转后,leftChild右边的树高会调整到和左边的树一样的高度
            leftChild.setRightDepth(leftChild.getLeftDepth());
            leftChild.setRightChild(currentNode);
            leftChild.setParentNode(parentNode);
            currentNode.setLeftDepth(currentNode.getRightDepth());
            currentNode.setLeftChild(grandSon);
            currentNode.setParentNode(leftChild);
            if (currentNodeIsRoot) {
                return leftChild;
            } else {
                leftChild.setParentNode(parentNode);
                nodeReplace(parentNode, currentNode, leftChild);
                return rootNode;
            }
        } else {
            AvlNode temp = grandSon.getLeftChild();
            // 步骤1
            leftChild.setParentNode(grandSon);
            leftChild.setRightChild(temp);
            if (temp != null) {
                leftChild.setRightDepth(Math.max(temp.getLeftDepth(), temp.getRightDepth()) + 1);
                temp.setParentNode(leftChild);
            } else {
                leftChild.setRightDepth(0);
            }
            // 步骤2
            if (!currentNodeIsRoot) {
                nodeReplace(parentNode, currentNode, grandSon);
            }
            grandSon.setParentNode(parentNode);
            // 步骤3
            grandSon.setLeftDepth(Math.max(leftChild.getLeftDepth(), leftChild.getRightDepth()) + 1);
            grandSon.setLeftChild(leftChild);
            // 步骤4
            currentNode.setParentNode(grandSon);
            temp = grandSon.getRightChild();
            currentNode.setLeftChild(temp);
            if (temp != null) {
                currentNode.setLeftDepth(Math.max(temp.getLeftDepth(), temp.getRightDepth()) + 1);
                temp.setParentNode(currentNode);
            } else {
                currentNode.setLeftDepth(0);
            }
            // 步骤5
            grandSon.setRightDepth(Math.max(currentNode.getLeftDepth(), currentNode.getRightDepth()) + 1);
            grandSon.setRightChild(currentNode);
            return currentNodeIsRoot ? grandSon : rootNode;
        }
    }


    private static AvlNode rightRotate(AvlNode currentNode, AvlNode parentNode, boolean currentNodeIsRoot, AvlNode rootNode) {
        AvlNode rightChild = currentNode.getRightChild();
        AvlNode grandSon = rightChild.getLeftChild();
        if (rightChild.getRightDepth() > rightChild.getLeftDepth()) {
            // 左左旋转
            if (grandSon != null) {
                grandSon.setParentNode(currentNode);
            }
            // 经过ll旋转后,rightChild右边的树高会调整到和左边的树一样的高度
            rightChild.setLeftDepth(rightChild.getRightDepth());
            rightChild.setLeftChild(currentNode);
            rightChild.setParentNode(parentNode);
            currentNode.setRightDepth(currentNode.getLeftDepth());
            currentNode.setRightChild(grandSon);
            currentNode.setParentNode(rightChild);
            if (currentNodeIsRoot) {
                return rightChild;
            } else {
                rightChild.setParentNode(parentNode);
                nodeReplace(parentNode, currentNode, rightChild);
                return rootNode;
            }
        } else {
            AvlNode temp = grandSon.getRightChild();
            // 步骤1
            rightChild.setParentNode(grandSon);
            rightChild.setLeftChild(temp);
            if (temp != null) {
                rightChild.setLeftDepth(Math.max(temp.getRightDepth(), temp.getLeftDepth()) + 1);
                temp.setParentNode(rightChild);
            } else {
                rightChild.setLeftDepth(0);
            }
            // 步骤2
            if (!currentNodeIsRoot) {
                nodeReplace(parentNode, currentNode, grandSon);
            }
            grandSon.setParentNode(parentNode);
            // 步骤3
            grandSon.setRightDepth(Math.max(rightChild.getRightDepth(), rightChild.getLeftDepth()) + 1);
            grandSon.setRightChild(rightChild);
            // 步骤4
            currentNode.setParentNode(grandSon);
            temp = grandSon.getLeftChild();
            currentNode.setRightChild(temp);
            if (temp != null) {
                currentNode.setRightDepth(Math.max(temp.getRightDepth(), temp.getLeftDepth()) + 1);
                temp.setParentNode(currentNode);
            } else {
                currentNode.setRightDepth(0);
            }
            // 步骤5
            grandSon.setLeftDepth(Math.max(currentNode.getRightDepth(), currentNode.getLeftDepth()) + 1);
            grandSon.setLeftChild(currentNode);
            return currentNodeIsRoot ? grandSon : rootNode;
        }
    }

    private static void nodeReplace(AvlNode parentNode, AvlNode oldNode, AvlNode newNode) {
        if (Objects.equals(parentNode.getLeftChild(), oldNode)) {
            parentNode.setLeftChild(newNode);
        } else {
            parentNode.setRightChild(newNode);
        }
    }


    public static AvlNode findParentNode(AvlNode current, AvlNode parent, int value) {
        if (current == null) {
            return parent;
        }
        int currentValue = current.getValue();
        if (currentValue == value) {
            throw new RuntimeException("发现重复");
        }
        return currentValue > value ? findParentNode(current.getLeftChild(), current, value) : findParentNode(current.getRightChild(), current, value);
    }
}
