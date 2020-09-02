import java.util.LinkedList;

// Terniary Symbol Table
public class TST {
  private Node root;   // root of TST

  private static class Node {
    private char c;                        // character
    private Node left, mid, right;  // left, middle, and right subtries
    private boolean isWord;                     // is the node a valid word
  }

  public Walker newWalker() {
    return new Walker();
  }

  public class Walker {
    private final LinkedList<Node> stack = new LinkedList<>();

    // return top node
    public boolean walkCurrent() {
      return stack.peekFirst().isWord;
    }

    // returns true if the key is a child of the top node and puts child , else false;
    public boolean walkDown(String key) {
      Node cNode;

      if (stack.isEmpty()) {
        cNode = get(root, key, 0);
      } else {
        // node with value of key of node on top of stack
        cNode = get(stack.peekFirst().mid, key, 0);
      }

      
      // if top node has key as child, child add to stack and return true
      if (cNode != null) {
        stack.addFirst(cNode);
        return true;
      }

      return false;
    }

    public void walkUp() {
      if (stack.isEmpty()) {
        throw new IllegalArgumentException("empty stack");
      }
      stack.removeFirst();
    }
  }

  // is the word in dictionary
  public boolean get(String key) {
    if (key == null) {
      throw new IllegalArgumentException("calls get() with null argument");
    }
    if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
    Node x = get(root, key, 0);
    if (x == null) return false;
    return x.isWord;
  }

  private Node get(Node x, String key, int d) {
    if (x == null) return null;
    if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
    char c = key.charAt(d);
    if      (c < x.c)              return get(x.left,  key, d);
    else if (c > x.c)              return get(x.right, key, d);
    else if (d < key.length() - 1) return get(x.mid,   key, d+1);
    else                           return x;
  }

  public void put(String key) {
    if (key == null) {
      throw new IllegalArgumentException("calls put() with null key");
    }  // delete existing key
    root = put(root, key, 0);
  }

  private Node put(Node x, String key, int d) {
    char c = key.charAt(d);
    if (x == null) {
      x = new Node();
      x.c = c;
    }
    if      (c < x.c)               x.left  = put(x.left,  key, d);
    else if (c > x.c)               x.right = put(x.right, key, d);
    else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, d+1);
    else                            x.isWord   = true;
    return x;
  }
}