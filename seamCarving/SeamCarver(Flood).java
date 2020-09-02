import edu.princeton.cs.algs4.Picture;
import java.util.PriorityQueue;

public class SeamCarver {
   Picture pic;


   // create a seam carver object based on the given picture
   public SeamCarver(Picture picture) {
      pic = picture;
   }

   // current picture
   public Picture picture() {
      return pic;
   }

   // width of current picture
   public int width() {
      return pic.width();
   }

   // height of current picture
   public int height() {
      return pic.height();
   }

   // basic class to with node and associated value for pq
   private class Entry implements Comparable {
      private final int node;
      private final int weight;
      private final int prev;

      public Entry(int row, int col, int weight, Entry prev) {
         this.row = row;
         this.col = col;
         this.weight = weight;
         this.prev = prev;
      }

      // returns the row
      public int row() {
         return row;
      }

      public int col() {
         return col
      }

      // returns the "value" the cumulative weight
      public int weight() {
         return weight;
      }

      // returns the pointer to the prev node
      public Entry prev() {
         return prev;
      }

      // compare by weigth
      public int compareTo(Entry other) {
         return this.weight().compareTo(other.weight());
      }
   }

   // helper function to find the difference in two colors: Δred^2 + Δgreen^2 + Δblue^2
   private colorDifference(Color c1, Color c2) {
      return Math.pow(c1.getRed() - c2.getRed(), 2) + 
             Math.pow(c1.getGreen() - c2.getGreen(), 2) +
             Math.pow(c1.getBlue() - c2.getBlue(), 2);
   }

   // energy of pixel at column x and row y
   public double energy(int x, int y) {
      // corners and edges always return 1000
      if (x == 0 || y == 0 || x == pic.width() - 1 || y == pic.height() - 1) {
         return 1000;
      }

      // find the colors of four sides
      Color up = pic.get(x, y - 1);
      Color down = pic.get(x, y + 1);
      Color left = pic.get(x - 1, y);
      Color right = pic.get(x + 1, y);

      // sqrt of sum between Δx and Δy
      return Math.sqrt(colorDifference(left, right) + colorDifference(up, down));
   }

   private nodeEnergy(int node, boolean isHoriz) {
      int x = isHoriz ? node / pic.height : node % pic.width;
      int y = isHoriz ? node % pic.height : node / pic.width;
      return energy(x, y);
   }

   // find seam, can choose between horiz/vert
   private int[] findSeam(boolean isHoriz) {
      int majorAxis = isHoriz ? pic.width() : pic.height(); // axis parralel to the path
      int minorAxis = isHoriz ? pic.height() : pic.width(); // axis perpendicular to the path


      PriorityQueue<Entry> pq = new PriorityQueue<>();

      // preset
      for (int i = 0; i < majorAxis; i++) {
         pq.add(new Entry(i, nodeEnergy(i, isHoriz), null));
      }

      while (!pq.isEmpty()) {
         Entry item = pq.poll;

         if (item.node() / majorAxis >= majorAxis) {
            return item;
         }

         if (item.node() >= minorAxis * majorAxis) {
            StdOut.println("impossible");
         }

         pq.add(new Entry(item.node() + minorAxis, item.weight() + nodeEnergy(i, isHoriz), item));

         // right/down
         if (item.node() + 1 % minorAxis != 0) {
            pq.add(new Entry(item.node() + minorAxis - 1, item.weight() + nodeEnergy(i, isHoriz), item));
         }

         // left/up
         if (item.node() % minorAxis != 0) {
            pq.add(new Entry(item.node() + minorAxis + 1, item.weight() + nodeEnergy(i, isHoriz), item));
         }
      }

      return -1;
   }

   // sequence of indices for horizontal seam
   public int[] findHorizontalSeam() {

   }

   // sequence of indices for vertical seam
   public int[] findVerticalSeam() {

   }

   // remove horizontal seam from current picture
   public void removeHorizontalSeam(int[] seam) {

   }

   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {

   }

   //  unit testing (optional)
   public static void main(String[] args) {

   }
}