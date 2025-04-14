import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats_NoWeighted {
    private int gridLength;
    private double average;//平均值
    private double variance;//方差
    private double lowerConfidence;//置信区间下限
    private double upperConfidence;//置信区间上限
    private double[] results;

    // 在 n x n 网格上执行独立实验
    public PercolationStats_NoWeighted(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        gridLength = n;

        if (gridLength == 1)
        {
            average = 1;
            variance = Double.NaN;
            lowerConfidence = Double.NaN;
            upperConfidence = Double.NaN;
        }
        else
        {
            results = new double[trials];
            for (int i = 0; i < trials; i++) {
                Percolation_NoWeighted simulation = new Percolation_NoWeighted(n);
                while (!simulation.percolates()) {
                    int row = StdRandom.uniform(n) + 1;
                    int col = StdRandom.uniform(n) + 1;
                    simulation.open(row, col);
                }
                results[i] = (double) simulation.OpenCount() / (n * n);
            }
            average = StdStats.mean(results);
            variance = StdStats.stddev(results);
            lowerConfidence = average - (1.96 * variance) / Math.sqrt(trials);
            upperConfidence = average + (1.96 * variance) / Math.sqrt(trials);
        }
    }

    public double mean()
    {
        return average;
    }

    public double stddev()
    {
        return variance;
    }

    public double confidenceLo()
    {
        return lowerConfidence;
    }

    public double confidenceHi()
    {
        return upperConfidence;
    }


    public static void main(String[] args) {
        int n = 320;
        int trials = 1000;

        long startTime = System.nanoTime(); // 开始时间
        PercolationStats_NoWeighted statistics = new PercolationStats_NoWeighted(n, trials);
        long endTime = System.nanoTime(); // 结束时间

        System.out.println("样本均值mean:  " + statistics.mean());
        System.out.println("标准差stddev:  " + statistics.stddev());
        System.out.println("置信区间confidence Low:  " + statistics.confidenceLo());
        System.out.println("置信水平confidence High:  " + statistics.confidenceHi());
        System.out.println("运行时间: " + (endTime-startTime) + " 纳秒");
    }
}
