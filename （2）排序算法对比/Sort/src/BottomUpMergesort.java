import edu.princeton.cs.algs4.StdOut;

public class BottomUpMergesort {

    // 主排序方法，接收一个数组并对其进行排序
    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length]; // 辅助数组，用于归并操作
        int N = a.length;

        // 从大小为1的子数组开始，逐步增加子数组的大小
        for (int sz = 1; sz < N; sz = sz + sz) {
            // 从数组的开始位置开始，逐步归并更大的子数组
            for (int lo = 0; lo < N - sz; lo += sz + sz) {
                int mid = Math.min(lo + sz - 1, N - 1);
                int hi = Math.min(lo + sz + sz - 1, N - 1);
                merge(a, aux, lo, mid, hi);
            }
        }
    }

    // 归并操作
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // 复制数组元素到辅助数组
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(aux[j], aux[i])) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
    }

    // 辅助方法，用于比较两个元素
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 测试时间
    public static double time(Comparable[] a) {
        StdOut.println("自底向上归并排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] A = a.clone();

            long StartTime = System.nanoTime();
            BottomUpMergesort.sort(A);
            long endTime = System.nanoTime();

            StdOut.println((endTime - StartTime) / 1000000.0);
        }
        StdOut.println(" ");
        return 0;
    }

    // 测试空间
    public static double memo(Comparable[] a) {
        StdOut.println("自底向上归并排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] B = a.clone();

            Runtime run = Runtime.getRuntime();
            run.gc();
            long startMemo = run.totalMemory() - run.freeMemory();
            BottomUpMergesort.sort(B);
            long endMemo = run.totalMemory() - run.freeMemory();

            StdOut.println((endMemo - startMemo) / 1024);
        }
        return 0;
    }

}