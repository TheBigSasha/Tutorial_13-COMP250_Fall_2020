package Tutorial;

import com.sun.source.tree.BinaryTree;

import java.util.Arrays;
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
        if(data instanceof PocketMonster){
            this.priority = ((PocketMonster) data).getDamage(); //THIS IS LIKE IN A3 WHERE PRIORITY IS A FIELD OF THE OBJECT
        }
        this.priority = new Random().nextInt();                 //THIS IS LIKE A NORMAL TREAP, WHERE PRIORITY IS RANDOM
    }
}

class Treap<E extends Comparable<E>>
{
    private TreapNode<E> root;


    public TreapNode<E> rotateLeft(TreapNode<E> root){
        TreapNode<E> Right = root.right;
        TreapNode<E> Left = root.right.left;

        Right.left = root;  //Make the reference of the old right's left (new root's left) to be the old root
        root.right = Left;  //We make the right of the old root's right be the left of the new root

        this.root = Right;      //Root is new root
        return Right;
    }


    public TreapNode<E> rotateRight(TreapNode<E> root) {

        //In this situation, we are one level of recursion higher, and root is acting kinda like parent
        //Why? This is less confusing and there are less pointers to stuff~!

        //When we rotate, we swap parent for child, and reroot. So we can swap arent with left, in this case
        TreapNode<E> left = root.left;
        TreapNode<E> right = root.left.right;

        left.right = root;
        root.left = right;

        this.root = left;
        return left;
    }

    public TreapNode<E> add(TreapNode<E> root, E data) {

        if(root == null){
            return new TreapNode<>(data);
        }

        //BST Check, lefty larger or lefty smaller depending
        if(data.compareTo(root.data) < 0){
            root.left = add(root.left, data);

            //THE TREAP PART, THE FUN PART, THIS IS WHERE THE FUN BEGINS
            if(root.left != null && root.left.priority > root.getPriority()){   //In A3, priority is defined as one of the fields of the dog, that is, how long its been in the shelter
                root = rotateRight(root);
            }
        }else{
            root.right = add(root.right, data);

            if(root.right != null && root.right.priority > root.getPriority()){
                root = rotateLeft(root);
            }
        }

        return root;
    }

    public void delete(E key){
        root = delete(root, key);
    }

    public void add(E key){
        root = add(root, key);
    }
    //This is a helper method because:
    //It helps in the delete method which does not return anything
    //It is not designed for the user to ever see it!
    private TreapNode<E> delete(TreapNode<E> root, E key) {
        if(root == null){           //BASE CASE, EASY PEASY LEMON SQUEEZEY
            return null;
        }

        //Here we search for what we are removing
        if(key.compareTo(root.data) < 0){          //NAVIGATE TO THE ONE WE ARE TRYING TO DELETE
            root.left = delete(root.left,key);
        } else if(key.compareTo(root.data) > 0){    //also navigate but the other direction
            root.right = delete(root.right,key);
        }

        //Here we have found the thing to remove:
        else{
            //Base case, easy to kill when they have no children who will miss them
            if(root.left == null && root.right == null){
                root = null;
                //in this case, the node has no kids and no one loves it, so you can easily kill it
            }

            //Here, we have 2 kids so we rotate crap until 2 kids are not had!
            //We do not need to get to the bottom of the tree or to a leaf,
            //Just somewhere where it has just 1 kid
            else if(root.left != null && root.right != null){
                //The node has a loving family, and you are a monster who wants to kill it, so you
                //have a lot of work to do

                if(root.left.priority < root.right.getPriority()){
                    root = rotateLeft(root);        //Here we rotate so the heap stays a heap and so that our node is easy to remove

                    //Rotating it down to a place where it is easier to delete, and swapping it with its lowest prioirty child

                    root.left = delete(root.left, key);
                }else{
                    root = rotateRight(root);

                    //Still the same as above, rotating it down to the place where it is ez to kill, but the other child has priority.

                    root.right = delete(root.right,key);
                }
            }

            else{
                //Just one child is present
                TreapNode<E> child;
                if(root.left != null) {
                        //if the left child is present, swap with its left
                     child = root.left;
                }else{
                    //if the right child is present, swap with right
                    child = root.right;
                }

                //Swap the node and its child, but swap I mean replace
                root = child;
            }
        }
        return root;
    }

    public static void main(String[] args)
    {
        // Treap keys
        int[] keys = { 5, 2, 1, 4, 9, 8, 10, 11, 12, 13, 14, 15, -4, 124, -444 };

        Arrays.sort(keys);

        // construct a Treap
        Treap<Integer> tr = new Treap<>();
        Tutorial.BinaryTree<Integer> tree = new Tutorial.BinaryTree<>();
        for (int key: keys) {
            tr.add(key);
            tree.add(key);
        }


        tr.delete(1);

        tr.delete(5);

        tr.delete(9);

        tr.delete(keys[7]);

        tr.delete(keys[6]);

        tr.delete(keys[5]);
    }
}