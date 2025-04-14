/* *****************************************************************************
 *  Compilation:  javac-algs4 ResizeDemo.java
 *  Execution:    java-algs4 ResizeDemo input.png columnsToRemove rowsToRemove
 *  Dependencies: SeamCarver.java
 *
 *
 *  Read image from file specified as command line argument. Use SeamCarver
 *  to remove number of rows and columns specified as command line arguments.
 *  Display the image and print time elapsed.
 *
 *  % java-algs4 ResizeDemo HJoceanSmall.png 150 0
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo {
    // Print progress bar
    private static void printProgressBar(int targetSize, int currentProgress, int totalSteps) {
        int progressBarWidth = 100;
        int completedBars = (int) ((currentProgress * progressBarWidth) / totalSteps);
        StringBuilder progressBar = new StringBuilder();

        for (int i = 0; i < progressBarWidth; i++) {
            if (i < completedBars) {
                progressBar.append("=");
            } else {
                progressBar.append(" ");
            }
        }

        StdOut.print("\r[" + progressBar.toString() + "] " + (int) (((double) currentProgress / totalSteps) * 100) + "%");
//        StdOut.flush();
    }

    public static void main() {
        Picture picture = new Picture("C:\\Users\\林梓阳\\Desktop\\Seam_Carving实验\\seam测试图片和例程\\bob-sedgewick.png");
        int width_to = 1800;
        int height_to = 900;

        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        SeamCarver sc = new SeamCarver(picture);

        Stopwatch sw = new Stopwatch();

        StdOut.printf("正在压缩高度：\n");
        for (int i = picture.height(); i > height_to; i--) {
            int[] horizontalSeam = sc.findHorizontalSeam();
            sc.removeHorizontalSeam(horizontalSeam);
            //StdOut.println(i);
            printProgressBar(height_to, picture.height() - i + 1, picture.height() - height_to);
        }

        StdOut.printf("\n正在压缩宽度：\n");
        for (int i = picture.width(); i > width_to; i--) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
            //StdOut.println(i);
            printProgressBar(width_to, picture.width() - i + 1, picture.width() - width_to);
        }

        StdOut.println();

        StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        picture.show();
        sc.picture().show();
        sc.picture().save("C:\\Users\\林梓阳\\Desktop\\output.jpg");
    }
}