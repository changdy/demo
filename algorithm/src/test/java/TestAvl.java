import tree.avl.AvlNode;
import tree.avl.AvlNodeOperate;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by Changdy on 2019/5/19.
 */
public class TestAvl {
    @Test
    public void test() {
        AvlNode avlNode = AvlNodeOperate.addValue(null, 35);
        List<Integer> integers = Arrays.asList(25, 40, 15, 30, 45, 10, 20, 5);
        for (Integer integer : integers) {
            avlNode = AvlNodeOperate.addValue(avlNode, integer);
        }
        System.out.println(avlNode);
    }

    @Test
    public void simpleTest() {
        AvlNode avlNode = AvlNodeOperate.addValue(null, 35);
        avlNode = AvlNodeOperate.addValue(avlNode, 10);
        System.out.println(avlNode);
    }

    @Test
    public void test2() {
        AvlNode avlNode = null;
        Random random = new SecureRandom();
        Set<Integer> set = new HashSet<>(50);
        for (int i = 0; i < 50; i++) {
            set.add(random.nextInt(99) + 1);
        }
        List<Integer> list = new ArrayList<>(100);
        list.addAll(set);
        Collections.shuffle(list);
        System.out.println(list.toString().replaceAll(" ", "").replaceAll("\\[|\\]", ""));
        for (Integer i : list) {
            System.out.println(i);
            avlNode = AvlNodeOperate.addValue(avlNode, i);
            System.out.println(avlNode);
        }
    }
}
