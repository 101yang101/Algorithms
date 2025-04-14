import edu.princeton.cs.algs4.StdOut;

public class TopDownMergesort {

    // 主排序方法，接收一个数组并对其进行排序
    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length]; // 辅助数组，用于归并操作
        sort(a, aux, 0, a.length - 1); // 调用递归的私有排序方法
    }

    // 私有的递归排序方法
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) {
            return; // 基本情况，子数组只有一个元素或为空
        }
        int mid = lo + (hi - lo) / 2; // 计算中点，将数组分成两半
        sort(a, aux, lo, mid); // 递归排序左半部分
        sort(a, aux, mid + 1, hi); // 递归排序右半部分
        merge(a, aux, lo, mid, hi); // 归并两个已排序的半部分
    }

    // 归并操作
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k]; // 将原数组的元素复制到辅助数组
        }
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++]; // 左半部分用完，取右半部分的元素
            } else if (j > hi) {
                a[k] = aux[i++]; // 右半部分用完，取左半部分的元素
            } else if (less(aux[j], aux[i])) {
                a[k] = aux[j++]; // 右半部分的元素较小，取右半部分的元素
            } else {
                a[k] = aux[i++]; // 左半部分的元素较小或相等，取左半部分的元素
            }
        }
    }

    // 辅助方法，用于比较两个元素
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 测试时间
    public static double time(Comparable[] a) {
        StdOut.println("自顶向下归并排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] A = a.clone();

            long StartTime = System.nanoTime();
            TopDownMergesort.sort(A);
            long endTime = System.nanoTime();

            StdOut.println((endTime - StartTime) / 1000000.0);
        }
        StdOut.println(" ");
        return 0;
    }

    // 测试空间
    public static double memo(Comparable[] a) {
        StdOut.println("自顶向下归并排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] B = a.clone();

            Runtime run = Runtime.getRuntime();
            run.gc();
            long startMemo = run.totalMemory() - run.freeMemory();
            TopDownMergesort.sort(B);
            long endMemo = run.totalMemory() - run.freeMemory();

            StdOut.println((endMemo - startMemo) / 1024);
        }
        return 0;
    }
}