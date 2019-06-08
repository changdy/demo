package sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Changdy on 2019/6/8.
 */
public class SortTest {
    @Test
    public void test() {
        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(random.nextInt(100));
        }
        ShellSort.startSort(list, list.size() / 2);
        System.out.println(list);
    }
}

