import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Scanner;

public class Map {
    // 读取文件中节点信息
    public static void createPoint(In in, Node[] Nodes, int num_of_v) {
        for (int i = 0; i < num_of_v; i++) {
            int n = in.readInt();
            int x = in.readInt();
            int y = in.readInt();
            Nodes[n] = new Node(x, y);
        }
    }

    // 读取文件中边的信息，并根据节点信息创建图
    public static void createGraph(In in, EdgeWeightedGraph Graph, int num_of_e, Node[] Nodes) {
        for (int i = 0; i < num_of_e; i++) {
            int n1 = in.readInt();
            int n2 = in.readInt();
            double weight = Nodes[n1].Dist(Nodes[n2]);
            Graph.addEdge(new Edge(n1, n2, weight));
        }
    }



    public static void main(String[] args) {
        In in = new In("E:\\课程任务\\各课程实验\\算法课实验\\（3）地图路由\\dijk\\usamap.txt");

        // 读入节点数和边数
        int num_of_v = in.readInt();
        int num_of_e = in.readInt();

        // 存储节点信息（编号、坐标）
        Node[] Nodes = new Node[num_of_v];
        createPoint(in, Nodes, num_of_v);

        // 读取边的信息，构建图
        EdgeWeightedGraph Graph = new EdgeWeightedGraph(num_of_v);
        createGraph(in, Graph, num_of_e, Nodes);

        Scanner in2 = new Scanner(System.in);
        int s = in2.nextInt();
        int d = in2.nextInt();

        // 未优化
        long StartTime0 = System.nanoTime();
        DijkstraUndirectedSP_0 Dijk_0 = new DijkstraUndirectedSP_0(Graph, Nodes, s, d);
        long endTime0 = System.nanoTime();
        StdOut.println("（ DijkstraUndirectedSP_0 ）查询时间占用:" + (endTime0 - StartTime0) / 1000000.0 + " ms");

        // 优化1
        long StartTime1 = System.nanoTime();
        DijkstraUndirectedSP_1 Dijk_1 = new DijkstraUndirectedSP_1(Graph, Nodes, s, d);
        long endTime1 = System.nanoTime();
        StdOut.println("（ DijkstraUndirectedSP_1 ）查询时间占用:" + (endTime1 - StartTime1) / 1000000.0 + " ms");

        // 优化2
        long StartTime2 = System.nanoTime();
        DijkstraUndirectedSP_2 Dijk_2 = new DijkstraUndirectedSP_2(Graph, Nodes, s, d);
        long endTime2 = System.nanoTime();
        StdOut.println("（ DijkstraUndirectedSP_2 ）查询时间占用:" + (endTime2 - StartTime2) / 1000000.0 + " ms");

        // 优化3
        long StartTime3 = System.nanoTime();
        DijkstraUndirectedSP_3 Dijk_3 = new DijkstraUndirectedSP_3(Graph, Nodes, s, d);
        long endTime3 = System.nanoTime();
        StdOut.println("（ DijkstraUndirectedSP_3 ）查询时间占用:" + (endTime3 - StartTime3) / 1000000.0 + " ms");

        // 打印路径
        Dijk_3.printPathTo();

        // 记录数据
        StdOut.println((endTime0 - StartTime0) / 1000000.0);
        StdOut.println((endTime1 - StartTime1) / 1000000.0);
        StdOut.println((endTime2 - StartTime2) / 1000000.0);
        StdOut.println((endTime3 - StartTime3) / 1000000.0);
    }
}

// 定义节点类
class Node{
    private double x;
    private double y;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // 计算节点之间距离
    public double Dist(Node n) {// 计算距离
        double delta_x = this.x - n.x;
        double delta_y = this.y - n.y;
        return Math.sqrt(delta_x * delta_x + delta_y * delta_y);
    }
}