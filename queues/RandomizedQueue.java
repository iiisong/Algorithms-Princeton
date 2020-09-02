import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private static int initSize = 10;

  private Item data[];
  private int count = 0;


  // construct an empty randomized queue
  public RandomizedQueue() {
    this.data = (Item[]) new Object[initSize];
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return count == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return count;
  }

  // add the item
  public void enqueue(Item item) {
    // throw when empty
    if (item == null) {
      throw new IllegalArgumentException();
    }

    data[count] = item;
    count++;

    // expand queue if full
    if (count >= data.length) {
      expand();
    }
  }

  // remove and return a random item
  public Item dequeue() {
    // throw when empty
    if (count == 0) {
      throw new java.util.NoSuchElementException("randomized queue is empty");
    }

    int index = StdRandom.uniform(count);
    Item result = data[index];

    data[index] = data[count - 1];
    data[count - 1] = null;
    count--;

    //shrink queue if existing occupy less than 1/4
    if (count <= data.length * 0.25) {
      shrink();
    }

    return result;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    if (count == 0) {
      throw new java.util.NoSuchElementException("randomized queue is empty");
    }
    return data[StdRandom.uniform(count)];
  }


  private class RandomIterator implements Iterator<Item>{
    private int pos = 0;
    private int[] order;
    private RandomizedQueue<Item> queue;
    private int qsize;

    public RandomIterator(RandomizedQueue<Item> queue) {
      this.queue = queue;
      qsize = queue.size();
      order = StdRandom.permutation(qsize);
    }

    public Item next() {
      if (pos > qsize - 1) {
        throw new java.util.NoSuchElementException("no more items to return");
      }

      Item result = queue.data[order[pos]];
      pos++;

      return result;
    }

    public boolean hasNext() {
      return pos <= qsize - 1;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private void expand() {
    Item temp[] = (Item[]) new Object[data.length * 2];

    // copy data to temp
    for (int i = 0; i < count; i++) {
      temp[i] = data[i];
      data[i] = null;
    }

    data = temp;
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomIterator(this);
  }

  private void shrink() {
    Item temp[] = (Item[]) new Object[data.length / 2];

    // copy data to temp
    for (int i = 0; i < count; i++) {
      temp[i] = data[i];
      data[i] = null;
    }

    data = temp;
  }
  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<Integer> q = new RandomizedQueue<>();

    // test if default empty
    assert q.isEmpty();
    assert q.size() == 0;

    // enqueue 9 items; q = {0 - 8}
    for (int i = 0; i < 9; i++) {
      q.enqueue(i);
    }

    // test size of q
    assert !q.isEmpty();
    assert q.size() == 9;

    // enqueue 6 itemsto hit max; q = {0 - 14}
    for (int i = 0; i < 6; i++) {
      q.enqueue(9 + i);
    }

    // iterate through q
    int count = 0;
    for (Integer i : q) {
      count++;
    }

    // test if max doubled

    // dequeue 5 times
    for (int i = 0; i < 5; i++) {
      q.dequeue();
    }

    // test if max didn't change

    // deque 7 items
    for (int i = 0; i < 7; i++) {
      q.dequeue();
    }

    // test if max halved
    
    // test if 3 items left
    assert q.size() == 3;

    // sample 3 items, could repeat
    for (int i = 0; i < 3; i++) {
      q.sample();
    }

    // test if size remains same
    assert q.size() == 3;
  }
}