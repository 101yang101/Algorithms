import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class SortCompare {
    public static void main(String[] args) {
        StdOut.println("请输入待排序数量N：");
        int N = StdIn.readInt();
        Comparable array[] = new Comparable[N];

        for (int i = 0; i < N; i++) {
            array[i] = StdRandom.uniform(10000);
        }

        // 测试时间
        InsertionSort.time(array);
        TopDownMergesort.time(array);
        BottomUpMergesort.time(array);
        RandomQuicksort.time(array);
        QuicksortWith3WayPartition.time(array);

        InsertionSort.memo(array);
        TopDownMergesort.memo(array);
        BottomUpMergesort.memo(array);
        RandomQuicksort.memo(array);
        QuicksortWith3WayPartition.memo(array);
    }
}