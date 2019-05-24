import org.junit.Test;
import tree.avl.AvlNode;
import tree.avl.AvlNodeOperate;
import tree.redblack.RBNode;
import tree.redblack.RBNodeOperate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Changdy on 2019/5/19.
 */
public class TestAvl {
    @Test
    public void testAVLTree() {
        List<Integer> integers = Arrays.asList(44, 71, 93, 85, 23, 25, 89, 17, 97, 10, 43, 91);
        AvlNode avlNode = null;
        for (int i = 0; i < integers.size(); i++) {
            avlNode = AvlNodeOperate.addValue(avlNode, integers.get(i));
        }
        System.out.println(avlNode);
    }

    @Test
    public void testRBTree() {
        List<Integer> integers = Arrays.asList(44, 71, 93, 85, 23, 25, 89, 17, 97, 10, 43, 91);
        RBNode rbNode = null;
        for (int i = 0; i < integers.size(); i++) {
            rbNode = RBNodeOperate.addValue(rbNode, integers.get(i));
        }
        System.out.println(rbNode);
    }
}