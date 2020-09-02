import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class SeamCarver {
  private Picture pic;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null) {
      throw new IllegalArgumentException();
    }
    pic = new Picture(picture);
  }

  // current picture
  public Picture picture() {
    return new Picture(pic);
  }

  // width of current picture
  public int width() {
    return pic.width();
  }

  // height of current picture
  public int height() {
    return pic.height();
  }

  private double energy(Picture pic, int x, int y) {
    if (x < 0 || y < 0 || x >= pic.width() || y >= pic.height()) {
      throw new IllegalArgumentException("index out of bounds");
    }

    // corners and edges always return 1000
    if (x == 0 || y == 0 || x == pic.width() - 1 || y == pic.height() - 1) {
      return 1000;
    }

    // find the colors of four sides
    int up = pic.getRGB(x, y - 1);
    int down = pic.getRGB(x, y + 1);
    int left = pic.getRGB(x - 1, y);
    int right = pic.getRGB(x + 1, y);

    int rV = (up & 0xFF) - (down & 0xFF);
    int gV = ((up >> 8) & 0xFF) - ((down >> 8) & 0xFF);
    int bV = ((up >> 16) & 0xFF) - ((down >> 16) & 0xFF);

    int vDif = rV * rV + gV * gV + bV * bV;

    int rH = (left & 0xFF) - (right & 0xFF);
    int gH = ((left >> 8) & 0xFF) - ((right >> 8) & 0xFF);
    int bH = ((left >> 16) & 0xFF) - ((right >> 16) & 0xFF);

    int hDif = rH * rH + gH * gH + bH * bH;

    // sqrt of sum between Δx and Δy
    return Math.sqrt(vDif + hDif);
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    return energy(pic, x, y);
  }

  // reflect off x = y
  private Picture transpose(Picture pic) {
    Picture np = new Picture(pic.height(), pic.width());

    for (int r = 0; r < np.height(); r++) {
      for (int c = 0; c < np.width(); c++) {
        np.setRGB(c, r, pic.getRGB(r, c));
      }
    }

    return np;
  }
  // find seam, can choose between horiz/vert
  private int[] findSeam(Picture pic) {
    double[] prevRow = new double[pic.width()];  // cumulative energy to get to cell
    double[] cRow = new double[pic.width()];  // current row
    int[][] mat = new int[pic.height()][pic.width()];  // matrix of direction; each cell points to parent

    // put values of first row into prevRow
    for (int c = 0; c < pic.width(); c++) {
      prevRow[c] = energy(pic, c, 0);
    }

    // iterate through all other rows
    for (int r = 1; r < pic.height(); r++) {

      for (int c = 0; c < pic.width(); c++) {
        double min = prevRow[c];  // the distance to reach the cell, default up
        int dir = 0;  // the direction the cell is pointing to, default up

        // compare up-left
        if (c != 0 && prevRow[c - 1] < min) {
          min = prevRow[c - 1];  //  update min
          dir = -1; //  direction up left
        }

        // compare up-right
        if (c != pic.width() - 1 && prevRow[c + 1] < min) {
          min = prevRow[c + 1];  //  update min
          dir = 1; //  direction up right
        }

        mat[r][c] = dir;  // add direction to matrix
        cRow[c] = min + energy(pic, c, r);  // shortest distance to get to cell
      }

      // update prevRow before iterating further
      for (int i = 0; i < cRow.length; i++) {
        prevRow[i] = cRow[i];
      }
    }

    int minNode = -1;  // default at -1
    double minValue = Double.MAX_VALUE;  // default at max value 

    // iterate through bottom row
    for (int c = 0; c < pic.width(); c++) {
      // find min cell in last row
      if (prevRow[c] < minValue) {
        minValue = prevRow[c];  // update min value
        minNode = c;  // update min node
      }
    }

    int[] result = new int[pic.height()];  // result array
    int cell = minNode;  // shortest endpoint
    
    // iterate through the rows in reverse order
    for (int r = pic.height() - 1; r >= 0; r--) {
      result[r] = cell;  // append cell to result
      cell += mat[r][cell];  // update cell
    }

    return result;
  }


  private int[] findHorizontalSeam(Picture pic) {
    return findSeam(transpose(pic));
  }
  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    return findHorizontalSeam(pic);
  }

  private int[] findVerticalSeam(Picture pic) {
    return findSeam(pic);
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    return findVerticalSeam(pic);
  }

  private Picture removeHorizontalSeam(Picture pic, int[] seam) {
    if (seam == null || seam.length != pic.width() || pic.height() <= 1) {
      throw new IllegalArgumentException();
    }

    Picture np = new Picture(pic.width(), pic.height() - 1);
    int prev = seam[0];

    for (int c = 0; c < pic.width(); c++) { 
      if (seam[c] > prev + 1 || seam[c] < prev - 1) {
        throw new IllegalArgumentException("impossible seam");
      }

      prev = seam[c];

      for (int r = 0; r < pic.height(); r++) {

        if (seam[c] == r) {
          continue;
        }

        if (r > seam[c]) {
          np.set(c, r - 1, pic.get(c, r));
        } else {
          np.set(c, r, pic.get(c, r));
        }
      }
    }

    return np;
  }


  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    pic = removeHorizontalSeam(pic, seam);
  }

  private Picture removeVerticalSeam(Picture pic, int[] seam) {
    if (seam == null || seam.length != pic.height() || pic.width() <= 1) {
      throw new IllegalArgumentException();
    }

    Picture np = new Picture(pic.width() - 1, pic.height());
    int prev = seam[0];

    for (int r = 0; r < pic.height(); r++) { 
      if (seam[r] > prev + 1 || seam[r] < prev - 1) {
        throw new IllegalArgumentException("impossible seam");
      }

      prev = seam[r];

      for (int c = 0; c < pic.width(); c++) {

        if (seam[r] == c) {
          continue;
        }

        if (c > seam[r]) {
          np.set(c - 1, r, pic.get(c, r));
        } else {
          np.set(c, r, pic.get(c, r));
        }
      }
    }

    return np;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    pic = removeVerticalSeam(pic, seam);
  }

  public static void main(String[] args) {
    if (args.length > 0) {
      // picture_file (h|v):n (h|v):n ...
      // picture_file: is the name of file
      // (h|v):n: a letter "h" (horizontal), or "v" (vertical) followed by ":" and a number
      //   specifying how many vertical, or horizontal seam to cut. 
      // If there are more than one (h|v):n then the picture will be processed in the order
      // specified.

      // result will be shown. First the original picture is show. Use left and right arrow 
      // key to cycle through each stage
      Picture original = new Picture(args[0]);
      original.show();

      SeamCarver sc = new SeamCarver(original);
      for (int i = 1; i < args.length; i++) {
        if (args[i].charAt(1) != ':') {
          throw new IllegalArgumentException("argument " + args[i] + " malformed");
        }

        boolean isVertical = true;
        switch (args[i].charAt(0)) {
        case 'h':
          isVertical = false;
          break;
        case 'v':
          isVertical = true;
          break;
        default:
          throw new IllegalArgumentException("argument " + args[i] + " malformed");
        }

        int n = Integer.parseInt(args[i].substring(2));
        for (int j = 0; j < n; j++) {
          if (isVertical) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
          } else {
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
          }
        }

        Picture p = new Picture(sc.picture());
        p.show();
      }
    } else {
      testVSeam1();
    }
  }

  private static void testVSeam1() {
    Picture p = new Picture("./5x6.png");
    int[] vseam = {2, 2, 2, 3, 2, 1};
    int[] hseam = {3, 3, 2, 3, 2};

    SeamCarver carver = new SeamCarver(p);
    int[] vseamResult = carver.findVerticalSeam();
    int[] hseamResult = carver.findHorizontalSeam();

    assert Arrays.equals(vseamResult, vseam) : 
      "vseam incorrect. Expecting " + Arrays.toString(vseam) + ", got " + Arrays.toString(vseamResult);
    assert Arrays.equals(hseamResult, hseam) : 
      "hseam incorrect. Expecting " +  Arrays.toString(hseam) + ", got " +  Arrays.toString(hseamResult);
  }
}