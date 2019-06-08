package sort;

import java.util.List;

public class ShellSort {

    public static void startSort(List<Integer> list, int interval) {
        int endIndex = list.size() - interval;
        for (int i = 0; i < endIndex; i++) {
            compare(i, i + interval, interval, list);
        }
        if (interval != 1) {
            startSort(list, interval / 2);
        }
    }

    private static void checkFormer(int index, int interval, List<Integer> list) {
        int formerIndex = index - interval;
        if (formerIndex >= 0) {
            compare(formerIndex, index, interval, list);
        }
    }

    private static void compare(int left, int right, int interval, List<Integer> list) {
        Integer leftValue = list.get(left);
        if (leftValue > list.get(right)) {
            list.set(left, list.get(right));
            list.set(right, leftValue);
            checkFormer(left, interval, list);
        }
    }
}