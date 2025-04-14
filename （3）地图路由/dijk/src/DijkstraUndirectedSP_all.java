import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.IndexMultiwayMinPQ;

// 全部未改动版本
public class DijkstraUndirectedSP_all {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    //private indexPQ<Double> pq;;    // priority queue of vertices
    private IndexMultiwayMinPQ<Double> pq;
    private EdgeWeightedGraph G;
    private Node[] Nodes;
    int s;
    int d;

    public DijkstraUndirectedSP_all(EdgeWeightedGraph G, Node[] Nodes, int s, int d) {
        this.G = G;
        this.Nodes = Nodes;
        this.s = s;
        this.d = d;

        // 检测边的权重有效
        for (Edge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        // 开辟空间
        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        // 建立优先队列
        // 优化3：优化优先队列
        //pq = new indexPQ<>(G.V());
        pq = new IndexMultiwayMinPQ<Double>(G.V(), 2);

        // 初始化源点到其他点之间的距离为无穷
        for (int v = 0; v < G.V(); v++)
        {
            if (pq.contains(v)) {
                pq.delete(v);
            }
            if (edgeTo[v] != null) {
                edgeTo[v] = null;
            }

            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        // 插入源点
        pq.insert(s, distTo[s]);

        // 计算最短路径
        hasPathTo();

        // 打印最短路径
        //printPathTo();
    }

    // relax edge e and update pq if changed
    private void relax(Edge e, int v) {
        int w = e.other(v);
        //StdOut.println(w);
        // 优化2：A*算法
        double weight = distTo[v] + e.weight() + Nodes[v].Dist(Nodes[d]) - Nodes[w].Dist(Nodes[d]);//优化2进行优化
        //double weight = distTo[v] + e.weight();
        if (distTo[w] > weight) {
            distTo[w] = weight;
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }

    // 计算并判断最短路径
    public boolean hasPathTo() {
        if(distTo[d] < Double.POSITIVE_INFINITY)
        {
            return true;
        }

        long num = 0;
        while (!pq.isEmpty()) {
            int x = pq.delMin();
            //StdOut.printf("%d\n", x);

            num++;
            // 优化1：减少检查点的数量
            if (x == d) {
                //StdOut.printf("%d\n", num);
                //StdOut.printf("1\n");
                return true;
            }

            for (Edge e : G.adj(x))
            {
                relax(e, x);
            }
        }
        //StdOut.printf("%d\n", num);
        //StdOut.printf("2\n");
        return distTo[d] < Double.POSITIVE_INFINITY;
    }

    public Iterable<Edge> pathTo() {
        if (!hasPathTo()) return null;
        Stack<Edge> path = new Stack<Edge>();
        int x = d;
        for (Edge e = edgeTo[d]; e != null; e = edgeTo[x]) {
            path.push(e);
            x = e.other(x);
        }
        return path;
    }

    // 打印路径
    public void printPathTo() {
        if (!hasPathTo()) {
            StdOut.printf("没有从（ %d ）到（ %d ）的路径 ", s, d);
            return;
        }

        StdOut.printf("从（ %d ）到（ %d ）的最短路径：\n", s, d);
        int last_to = s;
        double sum = 0.0;
        for (Edge e : pathTo()) {
            int from = e.either();
            int to = e.other(from);
            double weight = e.weight();
            sum += weight;
            if(to == last_to)
            {
                StdOut.printf("从 %d 到 %d, 路程: %.2f\n", to, from, weight);
                last_to = from;
            }
            else
            {
                StdOut.printf("从 %d 到 %d, 路程: %.2f\n", from, to, weight);
                last_to = to;
            }
        }
        StdOut.printf("总路程: %.2f\n", sum);
    }
}