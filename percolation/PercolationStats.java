import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double ZCONSTANT = 1.96;
  private final int trials;
  private final double mean;
  private final double stddev;

  /** perform independent trials on an n-by-n grid.
  **/
  public PercolationStats(int n, int trials) {
    if (n < 1) {
      throw new IllegalArgumentException("n should be positive. Received " + n + " instead.");
    }
    if (trials < 1) {
      throw new IllegalArgumentException("trials should be positive. Received " + trials + " instead.");
    }
    this.trials = trials;

    double[] results = new double[trials];

    for (int i = 0; i < trials; i++) {
      results[i] = runTrial(n);
    }

    this.mean = StdStats.mean(results);
    this.stddev = StdStats.stddev(results);
  }

  // sample mean of percolation threshold
  public double mean() {
    return mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean - ZCONSTANT * stddev / Math.sqrt(trials);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean + ZCONSTANT * stddev / Math.sqrt(trials);
  }
  
  /** runs Percolation of size n^2 trial times.
  **/
  private double runTrial(int n) {
    Percolation perco = new Percolation(n);

    int[] seq = StdRandom.permutation(n * n);
    
    for (int i = 0; i < seq.length; i++) {
      perco.open((seq[i] / n) + 1, (seq[i] % n) + 1);

      if (perco.percolates()) {
        return (double) (i + 1) / (n * n);
      }
    }
    
    return 0;
  }

  /** Command line testing suite. 
  * @param args num_matrix num_trials
  */
  public static void main(String[] args) {
    PercolationStats p = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    StdOut.println(String.format("%-23s = %.15f", "mean", p.mean()));
    StdOut.println(String.format("%-23s = %.15f", "stddev", p.stddev()));
    StdOut.println(String.format("%-23s = [%.15f, %.15f]", "95% confidence interval", 
                                p.confidenceLo(), p.confidenceHi()));
    StdOut.println(String.format("%-23s = %f", "Interval delta", 
                                p.confidenceHi() - p.confidenceLo()));
  }
   
}