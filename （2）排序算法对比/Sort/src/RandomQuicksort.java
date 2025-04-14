import java.util.Random;
import edu.princeton.cs.algs4.StdOut;

public class RandomQuicksort {

    private static final Random random = new Random();

    // 主排序方法，接收一个数组并对其进行排序
    public static void sort(Comparable[] a) {
        shuffle(a); // 随机打乱数组
        sort(a, 0, a.length - 1); // 调用递归的私有排序方法
    }

    // 随机打乱数组
    private static void shuffle(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            int r = i + random.nextInt(a.length - i); // 随机选择一个元素
            exch(a, i, r); // 交换当前元素和随机选择的元素
        }
    }

    // 私有的递归排序方法
    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return; // 基本情况，子数组只有一个元素或为空
        }
        int j = partition(a, lo, hi); // 划分数组并返回基准元素的位置
        sort(a, lo, j - 1); // 递归排序左半部分
        sort(a, j + 1, hi); // 递归排序右半部分
    }

    // 划分操作
    private static int partition(Comparable[] a, int lo, int hi) {
        Comparable v = a[lo]; // 选择基准元素
        int i = lo, j = hi + 1;
        while (true) {
            while (less(a[++i], v)){
                if(i == hi) break;
            } // 从左向右找到第一个大于等于基准的元素

            while (less(v, a[--j])){
                if(j == lo) break;
            } // 从右向左找到第一个小于等于基准的元素

            if (i >= j) break; // 如果两个指针交叉，停止

            exch(a, i, j); // 交换两个元素
        }
        exch(a, lo, j); // 将基准元素放到正确的位置
        return j; // 返回基准元素的位置
    }

    // 辅助方法，用于比较两个元素
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 交换数组中的两个元素
    private static void exch(Comparable[] a, int i, int j) {
        Comparable swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // 测试时间
    public static double time(Comparable[] a) {
        StdOut.println("随机快速排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] A = a.clone();

            long StartTime = System.nanoTime();
            RandomQuicksort.sort(A);
            long endTime = System.nanoTime();

            StdOut.println((endTime - StartTime) / 1000000.0);
        }
        StdOut.println(" ");
        return 0;
    }

    // 测试空间
    public static double memo(Comparable[] a) {
        StdOut.println("随机快速排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] B = a.clone();

            Runtime run = Runtime.getRuntime();
            run.gc();
            long startMemo = run.totalMemory() - run.freeMemory();
            RandomQuicksort.sort(B);
            long endMemo = run.totalMemory() - run.freeMemory();

            StdOut.println((endMemo - startMemo) / 1024);
        }
        return 0;
    }
}