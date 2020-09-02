import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
  private class Node<Item> {
    Item value;
    Node<Item> next;
    Node<Item> prev;
  }

  private Node<Item> head;
  private Node<Item> tail;
  private int count;

  // is the deque empty?
  public boolean isEmpty() {
    return count == 0;
  }

  // return the number of items on the deque
  public int size() {
    return count;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("item can not be null");
    }

    Node<Item> n = new Node<>();

    // if q is empty
    if (head == null) {
        head = n;
        tail = n;
    } 

    n.value = item;
    head.prev = n;
    n.prev = null;
    n.next = head;
    head = n;

    count++;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("item can not be null");
    }

    Node<Item> n = new Node<>();

    // if q is empty
    if (head == null) {
      head = n;
      tail = n;
    } 

    n.value = item;
    tail.next = n;
    n.next = null;
    n.prev = tail;
    tail = n;

    count++;
  }


  // remove and return the item from the front
  public Item removeFirst() {
    // if q is empty
    if (head == null) {
      throw new java.util.NoSuchElementException("can't remove item from empty queue");
    }

    Item result = head.value;

    // if single item in q
    if (head == tail) {
      head = null;
      tail = null;
    } else {
      head = head.next;
      head.prev.next = null;
      head.prev = null;
    }

    count--;
    return result;
  }

  // remove and return the item from the back
  public Item removeLast() {
    // if q is empty
    if (head == null) {
      throw new java.util.NoSuchElementException("can't remove item from empty queue");
    }

    Item result = tail.value;

    // if single item in q
    if (head == tail) {
      head = null;
      tail = null;
    } else {
      tail = tail.prev;
      tail.next.prev = null;
      tail.next = null;
    }

    count--;
    return result;
  }

  // iterator
  private class NodeIterator<Item> implements Iterator<Item> {
    // track positioning
    public Node<Item> current;

    private NodeIterator(Node<Item> head) {
      current = head;
    }

    // unimplemented
    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Item next() {
      // throw if there is no items to start with
      if (current == null) {
        throw new java.util.NoSuchElementException("last item. can't iterate");
      }

      Item result = current.value;
      //StdOut.println(result);

      current = current.next;
      return result;
    }

    // if there is next
    public boolean hasNext() {
      return current != null;
    }
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new NodeIterator<Item>(head);
  }

  // unit testing (required)
  public static void main(String[] args) {
    // create deque
    Deque<Integer> q = new Deque<>();

    //test if default empty
    assert q.isEmpty();
    assert q.size() == 0;

    // add ten items to the front with decrementing 9-0 so results in 0-9
    for (int i = 0; i < 10; i++) {
      q.addFirst(9 - i);
    }

    // test if empty
    assert !q.isEmpty();
    assert q.size() == 10;

    // add ten items to the back with incrementing order 10-19 so results in 0-19
    for (int i = 0; i < 10; i++) {
      q.addLast(10 + i);
    }

    // test if size 20
    assert q.size() == 20;

    // iterate through deque and check if value = index
    int count = 0;
    for (Integer i : q) {
      assert i == count;
      count++;
    }

    // remove first 5 items and test size
    for (int i = 0; i < 5; i++) {
      q.removeFirst();
    }

    // test size
    assert q.size() == 15;

    // remove last 5 items
    for (int i = 0; i < 5; i++) {
      q.removeLast();
    }

    // test size
    assert q.size() == 10;

    // test not empty
    assert !q.isEmpty();
  }
}