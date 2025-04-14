import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture Original_Picture; // 传入图片
    private Picture Final_Picture;
    private int width;
    private int height;

    private int[][] Picture_RGB; // 记录处理过程中图片各像素的RGB值
    private double[][] Picture_Energy; // 记录处理过程中图片各像素的能量值
    private int[] Vertical_path;
    private int[] Horizontal_path;

    public SeamCarver(Picture picture)
    {
        // 检测图片有效性
        if (picture == null) {
            throw new IllegalArgumentException("Picture cannot be null");
        }

        // 获取图片
        Original_Picture = picture;
        width = Original_Picture.width();
        height = Original_Picture.height();

        // 获取图片各像素的 RGB 信息，存储到 Picture_RGB 中
        Picture_RGB = new int[height][width];
        get_Picture_RGB();

        // 计算能量图
        Picture_Energy = new double[height][width];
        get_Picture_energy();

        Vertical_path = new int[height];
        Horizontal_path = new int[width];
    }

    // 获取当前图片
    public Picture picture()
    {
        Final_Picture = new Picture(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Final_Picture.setRGB(x, y, Picture_RGB[y][x]);
            }
        }

        return Final_Picture;
    }

    // 获取原图片各像素的 RGB 信息
    private void get_Picture_RGB()
    {
        init_array(Picture_RGB, height, width);
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                Picture_RGB[y][x] = Original_Picture.getRGB(x, y);
            }
        }
    }

    // 基于当前的 Picture_RGB 数组，计算能量图
    public void get_Picture_energy()
    {
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                Picture_Energy[i][j] = energy(j, i);
            }
        }
    }

    // 计算某点的 energy
    public double energy(int x, int y)
    {
        // 边界检测
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordinates out of range");
        }

        // 获取该像素点上下左右像素点的 RGB 值
        int left = Picture_RGB[y][(x - 1 + width) % width];
        int right = Picture_RGB[y][(x + 1) % width];
        int up = Picture_RGB[(y - 1 + height) % height][x];
        int down = Picture_RGB[(y + 1) % height][x];

        // 代入公式计算
        int dxR = getRed(left) - getRed(right);
        int dxG = getGreen(left) - getGreen(right);
        int dxB = getBlue(left) - getBlue(right);

        int dyR = getRed(up) - getRed(down);
        int dyG = getGreen(up) - getGreen(down);
        int dyB = getBlue(up) - getBlue(down);

        int gradX = dxR * dxR + dxG * dxG + dxB * dxB;
        int gradY = dyR * dyR + dyG * dyG + dyB * dyB;

        return Math.sqrt(gradX + gradY);
    }

    // 动态规划计算垂直最短能量路径
    public int[] findVerticalSeam()
    {
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // 初始化第一行的距离为各自能量
        for (int x = 0; x < width; x++) {
            distTo[0][x] = Picture_Energy[0][x];
        }

        // 初始化其他行距离为无穷远
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }

        // 动态规划填充distTo和edgeTo数组
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                Vertical_relax(x, y, x - 1, y + 1, distTo, edgeTo);
                Vertical_relax(x, y, x, y + 1, distTo, edgeTo);
                Vertical_relax(x, y, x + 1, y + 1, distTo, edgeTo);
            }
        }

        // 找到最后一行中能量最小的路径终点
        double minDist = Double.MAX_VALUE;
        int endPoint = -1;
        for (int x = 0; x < width; x++) {
            if (distTo[height - 1][x] < minDist) {
                minDist = distTo[height - 1][x];
                endPoint = x;
            }
        }

        // 回溯路径
        Vertical_path[height - 1] = endPoint;
        for (int y = height - 2; y >= 0; y--) {
            Vertical_path[y] = edgeTo[y + 1][Vertical_path[y + 1]];
        }

        return Vertical_path;
    }

    // 松弛边
    private void Vertical_relax(int xFrom, int yFrom, int xTo, int yTo, double[][] distTo, int[][] edgeTo)
    {
        if (xTo < 0 || xTo >= width || yTo < 0 || yTo >= height) {
            return;
        }

        if (distTo[yFrom][xFrom] + Picture_Energy[yTo][xTo] < distTo[yTo][xTo]) {
            distTo[yTo][xTo] = distTo[yFrom][xFrom] + Picture_Energy[yTo][xTo];
            edgeTo[yTo][xTo] = xFrom;
        }
    }

    // 动态规划计算最短水平能量路径
    public int[] findHorizontalSeam()
    {
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // 初始化第一列的距离为各自能量
        for (int y = 0; y < height; y++) {
            distTo[y][0] = Picture_Energy[y][0];
        }

        // 初始化其他列距离为无穷远
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                distTo[y][x] = Double.POSITIVE_INFINITY;
            }
        }

        // 动态规划填充distTo和edgeTo数组
        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height; y++) {
                Horizontal_relax(x, y, x + 1, y - 1, distTo, edgeTo); // 上右
                Horizontal_relax(x, y, x + 1, y, distTo, edgeTo);      // 正右
                Horizontal_relax(x, y, x + 1, y + 1, distTo, edgeTo);    // 下右
            }
        }

        // 找到最后一列中能量最小的路径终点
        double minDist = Double.MAX_VALUE;
        int endPoint = -1;
        for (int y = 0; y < height; y++) {
            if (distTo[y][width - 1] < minDist) {
                minDist = distTo[y][width - 1];
                endPoint = y;
            }
        }

        // 回溯路径
        Horizontal_path[width - 1] = endPoint;
        for (int x = width - 2; x >= 0; x--) {
            Horizontal_path[x] = edgeTo[Horizontal_path[x + 1]][x + 1];
        }

        return Horizontal_path;
    }

    // 松弛边
    private void Horizontal_relax(int xFrom, int yFrom, int xTo, int yTo, double[][] distTo, int[][] edgeTo)
    {
        if (xTo < 0 || xTo >= width || yTo < 0 || yTo >= height) {
            return;
        }

        if (distTo[yFrom][xFrom] + Picture_Energy[yTo][xTo] < distTo[yTo][xTo]) {
            distTo[yTo][xTo] = distTo[yFrom][xFrom] + Picture_Energy[yTo][xTo];
            edgeTo[yTo][xTo] = yFrom;
        }
    }

    // 移除垂直 seam
    public void removeVerticalSeam(int[] seam)
    {
        if (seam == null) {
            throw new IllegalArgumentException("Seam cannot be null");
        }

//        if (seam == null || seam.length != height) {
//            StdOut.printf("%d   %d\n", seam.length, height);
//            throw new IllegalArgumentException("Invalid seam length");
//        }

        if (width == 1) {
            throw new IllegalArgumentException("Width of the picture is 1, cannot remove vertical seam");
        }

        validateSeam(seam, false, height);

        // 根据 seam 更新 Picture_RGB 的值，可由 Picture_RGB 建图，即为新图
        for (int y = 0; y < height; y++) {
            for (int x = seam[y]; x < width - 1; x++) {
                Picture_RGB[y][x] = Picture_RGB[y][x + 1];
            }
        }

        // 更新宽度信息
        width--;

        // 更新能量图
        get_Picture_energy();
    }

    // 移除水平 seam
    public void removeHorizontalSeam(int[] seam)
    {
        if (seam == null) {
            throw new IllegalArgumentException("Seam cannot be null");
        }

//        if (seam == null || seam.length != width) {
//            throw new IllegalArgumentException("Invalid seam length");
//        }

        if (height == 1) {
            throw new IllegalArgumentException("Height of the picture is 1, cannot remove horizontal seam");
        }

        validateSeam(seam, true, width);

        // 根据 seam 更新 Picture_RGB 的值，可由 Picture_RGB 建图，即为新图
        for (int x = 0; x < width; x++) {
            for (int y = seam[x]; y < height - 1; y++) {
                Picture_RGB[y][x] = Picture_RGB[y + 1][x];
            }
        }

        // 更新高度信息
        height--;

        // 更新能量图
        get_Picture_energy();
    }

    public int width() {return this.width;}

    public int height() {return this.height;}

    // 验证 seam 数组中各元素信息是否有效
    private void validateSeam(int[] seam, boolean isHorizontal, int length)
    {
        for (int i = 0; i < length; i++) {
            if (isHorizontal) {
                if (seam[i] < 0 || seam[i] >= height) {
                    throw new IllegalArgumentException("Seam entry out of bounds");
                }
            } else {
                if (seam[i] < 0 || seam[i] >= width) {
                    throw new IllegalArgumentException("Seam entry out of bounds");
                }
            }

            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Two adjacent entries in seam differ by more than 1");
            }
        }
    }

    // 初始化数组元素全为 0
    private void init_array(int[][] arr, int row, int col)
    {
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < col; j++)
            {
                arr[i][j] = 0;
            }
        }
    }

    // 从 RGB 中获得 R 值
    private int getRed(int rgb) {return (rgb >> 16) & 0xFF;}

    // 从 RGB 中获得 G 值
    private int getGreen(int rgb) {return (rgb >> 8) & 0xFF;}

    // 从 RGB 中获得 B 值
    private int getBlue(int rgb) {return (rgb >> 0) & 0xFF;}
}