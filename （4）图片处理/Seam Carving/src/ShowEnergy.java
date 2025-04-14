/* *****************************************************************************
 *  Compilation:  javac-algs4 ShowEnergy.java
 *  Execution:    java-algs4 ShowEnergy input.png
 *  Dependencies: SeamCarver.java SCUtility.java
 *
 *  Read image from file specified as command-line argument. Show original
 *  image (useful only if image is large enough).
 *
 *  % java-algs4 ShowEnergy HJoceanSmall.png
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class ShowEnergy {

    public static void main() {
        String filename = "C:\\Users\\林梓阳\\Desktop\\Seam_Carving实验\\seam测试图片和例程\\chameleon.png";

        Picture picture = new Picture(filename);
        StdOut.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }

}