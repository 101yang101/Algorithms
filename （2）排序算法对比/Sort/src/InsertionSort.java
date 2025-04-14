import edu.princeton.cs.algs4.StdOut;

public class InsertionSort {

    // 插入排序的主要方法
    public static void sort(Comparable[] a) {
        int N = a.length;
        for (int i = 1; i < N; i++) {
            Comparable v = a[i];
            int j = i;
            while (j > 0 && less(v, a[j - 1])) {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = v;
        }
    }

    // 辅助方法，用于比较元素 v 和 w
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 测试时间
    public static double time(Comparable[] a) {
        StdOut.println("插入排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] A = a.clone();

//            for (int j = 0; j < a.length; j++) {
//                System.out.print(A[j] + " "); // 打印原数组a的每个元素
//            }
//            System.out.print(" ---- ");

            long StartTime = System.nanoTime();
            InsertionSort.sort(A);
            long endTime = System.nanoTime();

//            for (int j = 0; j < a.length; j++) {
//                System.out.print(A[j] + " "); // 打印原数组a的每个元素
//            }

            StdOut.println((endTime - StartTime) / 1000000.0);
        }
        StdOut.println(" ");
        return 0;
    }

    // 测试空间
    public static double memo(Comparable[] a) {
        StdOut.println("插入排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] B = a.clone();

            Runtime run = Runtime.getRuntime();
            run.gc();
            long startMemo = run.totalMemory() - run.freeMemory();
            InsertionSort.sort(B);
            long endMemo = run.totalMemory() - run.freeMemory();

            StdOut.println((endMemo - startMemo) / 1024);
        }
        return 0;
    }
}