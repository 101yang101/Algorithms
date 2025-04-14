import edu.princeton.cs.algs4.StdOut;

public class QuicksortWith3WayPartition {

    // 3-way partitioning
    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int lt = lo, i = lo + 1, gt = hi;
        Comparable v = a[lo];
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) {
                exch(a, lt++, i++);
            } else if (cmp > 0) {
                exch(a, i, gt--);
            } else {
                i++;
            }
        }
        for (i = lo; i < lt; i++) {
            a[i] = v;
        }
        sort(a, lo, lt - 1);
        sort(a, gt + 1, hi);
    }

    //交换数组中的两个元素
    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // 测试时间
    public static double time(Comparable[] a) {
        StdOut.println("Dijkstra 3-路划分快速排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] A = a.clone();

            long StartTime = System.nanoTime();
            QuicksortWith3WayPartition.sort(A, 0, A.length - 1);
            long endTime = System.nanoTime();

            StdOut.println((endTime - StartTime) / 1000000.0);
        }
        StdOut.println(" ");
        return 0;
    }

    // 测试空间
    public static double memo(Comparable[] a) {
        StdOut.println("Dijkstra 3-路划分快速排序:");
        for (int i = 0; i < 10; i++) {
            Comparable[] B = a.clone();

            Runtime run = Runtime.getRuntime();
            run.gc();
            long startMemo = run.totalMemory() - run.freeMemory();
            QuicksortWith3WayPartition.sort(B, 0, B.length - 1);
            long endMemo = run.totalMemory() - run.freeMemory();

            StdOut.println((endMemo - startMemo) / 1024);
        }
        return 0;
    }
}