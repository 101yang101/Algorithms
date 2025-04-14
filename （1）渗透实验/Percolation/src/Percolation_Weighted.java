import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Percolation_Weighted {
    private WeightedQuickUnionUF unionFind;
    private int gridSize;
    private boolean[] siteStatus;
    private int openCount;

    // 创建一个 n x n 的网格，所有格点初始为封闭状态
    public Percolation_Weighted(int n) {
        this.gridSize = n;
        this.siteStatus = new boolean[gridSize * gridSize + 2]; // 添加两个虚拟节点
        siteStatus[0] = true; // 初始化虚拟头部
        siteStatus[gridSize * gridSize + 1] = true; // 初始化虚拟尾部
        openCount = 0;
        unionFind = new WeightedQuickUnionUF(gridSize * gridSize + 2);
    }

    // 打开指定的格点（行，列），如果尚未打开
    public void open(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }

        if (isOpen(row, col)) {
            return; // 已经打开，直接返回
        }

        siteStatus[(row - 1) * gridSize + col] = true; // 打开此格点
        openCount++;

        // 与头部和尾部连接
        if (row == 1) {
            unionFind.union(0, (row - 1) * gridSize + col);
        }
        if (row == gridSize) {
            unionFind.union((row - 1) * gridSize + col, gridSize * gridSize + 1);
        }

        // 检查四个方向并进行合并
        int currentIndex = (row - 1) * gridSize + col;
        // 向左
        if (col > 1 && siteStatus[currentIndex - 1]) {
            unionFind.union(currentIndex, currentIndex - 1);
        }
        // 向右
        if (col < gridSize && siteStatus[currentIndex + 1]) {
            unionFind.union(currentIndex, currentIndex + 1);
        }
        // 向上
        if (row > 1 && siteStatus[currentIndex - gridSize]) {
            unionFind.union(currentIndex, currentIndex - gridSize);
        }
        // 向下
        if (row < gridSize && siteStatus[currentIndex + gridSize]) {
            unionFind.union(currentIndex, currentIndex + gridSize);
        }
    }

    // 检查指定的格点是否开放
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        return siteStatus[(row - 1) * gridSize + col];
    }

    // 检查系统是否渗透
    public boolean percolates() {
        return unionFind.connected(0, gridSize * gridSize + 1);
    }

    public int OpenCount() {
        return openCount;
    }

    public static void main(String[] args) {
        try {
            FileInputStream fileInput = new FileInputStream("input.txt");
            System.setIn(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int n = StdIn.readInt();
        Percolation_Weighted simulation = new Percolation_Weighted(n);
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            simulation.open(row, col);
        }
        System.out.println(simulation.percolates());
    }
}
