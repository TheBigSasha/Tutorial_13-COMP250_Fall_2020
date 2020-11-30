package Tutorial;

import java.util.Random;

class TreapNode<E extends Comparable<E>>
{
    E data;
    int priority;
    TreapNode<E> left;
    TreapNode<E> right;

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public TreapNode<E> getLeft() {
        return left;
    }

    public void setLeft(TreapNode<E> left) {
        this.left = left;
    }

    public TreapNode<E> getRight() {
        return right;
    }

    public void setRight(TreapNode<E> right) {
        this.right = right;
    }

    public TreapNode(E data) {
        this.data = data;
        this.priority = new Random().nextInt(100);
    }
}

class Treap<E extends Comparable<E>>
{
    private TreapNode<E> root;


    public TreapNode<E> rotateLeft(TreapNode<E> root){
        return null;
    }


    public TreapNode<E> rotateRight(TreapNode<E> root) {
        return null;
    }

    public TreapNode<E> add(TreapNode<E> root, E data) {
        return root;
    }

    public void delete(E key){
        root = delete(root, key);
    }

    public void add(E key){
        root = add(root, key);
    }

    public TreapNode<E> delete(TreapNode<E> root, E key)
    {
        return null;
    }

    public static void main(String[] args)
    {
        // Treap keys
        int[] keys = { 5, 2, 1, 4, 9, 8, 10 };

        // construct a Treap
        Treap<Integer> tr = new Treap<>();
        for (int key: keys)
            tr.add(key);

        System.out.println("Constructed Treap:\n\n");

        System.out.println("\nDeleting node 1:\n\n");
        tr.delete(1);

        System.out.println("\nDeleting node 5:\n\n");
        tr.delete(5);

        System.out.println("\nDeleting node 9:\n\n");
        tr.delete(9);
    }
}