// Created by Matchy

public abstract class AbstractBST {
    protected static class Node implements Comparable<Node> {
        protected Node parent;
        protected Node left;
        protected Node right;
        protected final String key;
        protected int freq;
        protected int accessCount;
        protected int height;

        public Node(String key) {
            this.parent = null;
            this.left = null;
            this.right = null;
            this.key = key;
            this.freq = 1;
            this.accessCount = 0;
            this.height = 1;
        }

        public int level() {
            int level = 0;
            Node parent = this.parent;
            while (parent != null) {
                level++;
                parent = parent.parent;
            }
            return level;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return (this.key).compareTo(anotherNode.key);
        }

        @Override
        public String toString() {
            return "[" + this.key + ":" +
                         this.freq + ":" +
                         this.accessCount + "]";
        }
    }

    protected Node root;
    protected int size;

    public AbstractBST() {
        this.root = null;
        this.size = 0;
    }

    public abstract int size();

    /**
     * Inserts a key into the tree using the standard BST insertion algorithm.
     * If the same key exists already in the tree, increment its frequency
     * @param key the key to be inserted into the tree
     */
    public abstract void insert(String key);

    public abstract boolean find(String key);

    /**
     * @return the sum of frequency of all the keys in the tree
     */
    public abstract int sumFreq();

    /**
     * @return the sum of access count of all the keys in the tree
     */
    public abstract int sumProbes();

    /**
     * Returns the sum of weighted path lengths of the tree. The weighted path
     * length of a node is calculated by weight * (1 + level), where weight refers
     * to the frequency of its key
     * @return the sum of weighted path lengths of the tree
     */
    public abstract int sumWeightedPath();

    /**
     * Resets both the frequencies and access counts for all the keys in the tree
     * to zero
     */
    public abstract void resetCounters();

    /**
     * Converts the tree from a plain BST into a Nearly Optimal BST. The
     * frequencies of the trees are used as weights
     * An NOBST can be constructed recursively in O(n log n) time. First, the root node of an NOBST is
     * chosen so as to minimize the difference in the weight sums of the left and right subtrees. Then, the
     * root of each subtree is chosen in the same way recursively. (When a tie needs to be broken, make the
     * right subtree heavier.)
     */
    public abstract void nobst();

    /**
     * Converts the tree from a plain BST into a Nearly Optimal BST. The
     * frequencies of the trees are used as weights
     */
    public abstract void obst();

    /**
     * Prints the keys in the tree in the increasing order of their values.
     * Each key should appear on a separate line and in the format of
     * [key:frequency:access_count]
     */
    public abstract void print();

}
