package Tutorial;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * This data structures adds stuff to it in a way that keeps it in order,
 * or rather easy to get into order quickly. Good for homies with chaotic
 * energy who may want to spontaneously add and remove items from their
 * dataset but still need to be able to quickly get their s*** together
 * in case someone wants things in order.
 */
public class BinaryTree<E extends Comparable<E>> implements Iterable<E> {
    /**
     * The top of this binary tree
     */
    private TreeNode<E> root;
    /**
     * The size of this tree
     */
    private int size = 0;

    /**
     * Instantiates an empty binary tree
     */
    public BinaryTree(){

    }
    //best: No nodes present except smallest and largest, O(1)
    //worst; O(n) has to traverse a linked list like thing from root to largest
    public E largest(){
        if(root == null) return null;
        TreeNode<E> temp = root;
        while(temp.getLeft() != null){
            temp = temp.getLeft();
        }
        return temp.getData();
    }

    public E smallest(){
        if(root == null) return null;
        TreeNode<E> temp = root;
        while(temp.getRight() != null){
            temp = temp.getRight();
        }
        return temp.getData();
    }

    /**
     * Adds an item to the tree, it puts it in its place right away
     * @param data what is being added
     */
    public void add(E data){
        if(root == null){
            root = new TreeNode<>(data);
            size++;
        }else{
            add(data, root);
        }
    }

    private TreeNode<E> find(E data){
        findRecursiveCounter=0;
        TreeNode<E> found =  findRecursive(data, root);
        System.out.println("Tree size is " + size +" counter is " + findRecursiveCounter);
        return found;
    }
    int findRecursiveCounter = 0;

    private TreeNode<E> findRecursive(E data, TreeNode<E > current){
        findRecursiveCounter++;
        //^^^THIS IS USELESS AND HAS NOTHING TO DO WITH ANYTHING*^^^

        if(current == null) return null;
        if(current.getData().equals(data)) return current;
        if(data.compareTo(current.getData()) > 0){  //If its larger, it must be to the left!
            {
                return findRecursive(data, current.left);
            }
        }else{                                       //If its not larger, it must be to the right
            {
                return findRecursive(data, current.right);
            }
        }
    }

    //Worst case: O(N) where tree is completely unbalanced and we add the largest or smallest element (in the direction of balance)
    //Best case: O(1) where you add to one of the sides of the root that is null!
    private void add(E data, TreeNode<E> node){
        if(data.compareTo(node.getData()) > 0){     //Check if the node is larger (this tree follows lefty larger)
            if(node.getLeft() == null){             //Check if its the base case
                node.setLeft(new TreeNode<>(data)); //In the base case, adding is really easy, we literally add it
                size++;
            }else{
                add(data, node.getLeft());          //In the non-base case, we work towards the base case
            }
        }else{                                      //Check if the node is smaller (this tree follows lefty larger)
            if(node.getRight() == null){            //Check if its the base case
                node.setRight(new TreeNode<>(data));//In the base case, adding is really easy, we literally add it
                size++;
            }else{
                add(data, node.getRight());          //In the non-base case, we work towards the base case
            }
        }
    }

    /**
     * We return the contents of the tree in sort order!
     * @return a list of the items in the tree in order
     */
    public Stack<E> inOrder(){
        Stack<E> workingStack = new Stack<>();
        workingStack = inOrder(root, workingStack);
        return workingStack;
    }



    /**
     * We return the contents of the tree in reverse sort order!
     * @return a list of the items in the tree in reverse order
     */
    public Stack<E> reverseOrder(){
        Stack<E> workingStack = new Stack<>();
        return reverseOrder(root, workingStack);
    }


    private Stack<E> inOrder(TreeNode<E> temp, Stack<E> list){
        //Right center left because this tree is LEFTY LARGER
        //Would be left center right if it was Righty Mighty
        if(temp.getRight() != null){        //check if there is a smaller subtree to the right
            inOrder(temp.getRight(),list);  //Recurse thru the smaller subtree first
        }
        list.push(temp.data);               //BASE CASE: We hit the center || is a leaf
                                            //We output here at cetner

        if(temp.getLeft() != null){         //Check if there is a larger subtree to the left
            inOrder(temp.getLeft(),list);   //Recurse through larger subtree after
        }
        return list;                        //WOOT We are done! Return the output!
    }

    /**
     * Shh! We are being sneaky with our recursion and hiding it.
     * This is helping us with reverseOrder()
     * @return a list of the items in the tree in reverse order
     */
    private Stack<E> reverseOrder(TreeNode<E> temp, Stack<E> list){
        if(temp.getLeft() != null){
            reverseOrder(temp.getLeft(),list);
        }
        list.push(temp.data);
        if(temp.getRight() != null){
            reverseOrder(temp.getRight(),list);
        }
        return list;
    }

    /**
     * An iterator which traverses through our tree
     *
     * @return an Iterator which traverses our btree in order
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Stack<E> inOrder = reverseOrder();
            @Override
            public boolean hasNext() {

                return !inOrder.isEmpty();
            }

            @Override
            public E next() {
                return inOrder.pop();
            }
        };
    }

    public void delete(E data){
        delete(data, root);
    }

    private TreeNode<E> delete(E data, TreeNode<E> node){
        if(node == null){
            size--;//TODO: proper size tracking
            return node;    //Takes care of leaf deletion
        }

        if(data.compareTo(node.getData()) > 0){

            //Consider:
            //Delete returns NULL if the node passed is null!
            //Delete returns a subtree if it isn't null!
            //Therefore in the base case, this just sets the left to null
            //And in other cases, runs a recursive delete thru the left subtree!
            node.setLeft(delete(data, node.getLeft()));
        } else if(data.compareTo(node.getData()) < 0){
            //Same concept as above
            node.setRight(delete(data,node.getRight()));
        }else{
            if(node.getLeft() == null){
                return node.getRight();     //If we are replacing a node above, that is in the lines above (201 and 204)
                                            //We can replace the nodes directly by swapping this node in but only if it has
                                            //a child on one side
            }else if(node.getRight() == null){
                return node.getLeft();
            }

        //A risky step that messes up our tree, but makes removing easy
        node.setData(smallestInSubtree(node.getRight()));
        //We set the data in the current node to be the smallest from the subtree of biggest
            //
            //A step which fixes what we messed up
        node.setRight(delete(node.getData(), node.getRight()));
        //Here, we cheat the system and we remove using the base case!
            // We swap our node for one that meets the same requirements (bigger than one side, smaller than other)
            // We delete a leaf which now a duplicate!
        }

        return node;
    }

    private E smallestInSubtree(TreeNode<E> node){
        E minimum = node.getData();
        while(node.getLeft() != null){
            minimum = node.getLeft().getData();
            node = node.getLeft();
        }
        return  minimum;
    }


    /**
     * The node used for the binary tree
     * @param <E>
     */
    static class TreeNode<E>{
        private TreeNode<E> left;
        private TreeNode<E> right;
        private E data;

        public TreeNode(E data) {
            this.data = data;
        }

        public TreeNode<E> getLeft() {
            return left;
        }

        public void setLeft(TreeNode<E> left) {
            this.left = left;
        }

        public TreeNode<E> getRight() {
            return right;
        }

        public void setRight(TreeNode<E> right) {
            this.right = right;
        }

        public E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }
    }


    private static void testBinaryTree(){
        BinaryTree<PocketMonster> tree = new BinaryTree<>();
        ArrayList<PocketMonster> list = new ArrayList<>();
        for(int i = 0; i < 150; i++){
            PocketMonster pm = PocketMonster.createPocketMonster();
            tree.add(pm);
            list.add(pm);
        }
        /*list.sort(PocketMonster::compareTo);
        List<PocketMonster> treeTraversed = tree.inOrder();
        List<PocketMonster> treeReversed = tree.reverseOrder();
        if(treeTraversed.get(0) != treeReversed.get(treeReversed.size() - 1)) System.out.println("Reversed is not reversed!");
        if(treeTraversed.size() != list.size()) System.out.println("FAILED! Sizes do not match!");
        for(int i = 0; i < list.size(); i++){
           if(treeTraversed.get(i).compareTo(list.get(i)) != 0) {
               System.out.println("FAILED! Not in sort order at index " + i);
           }
            System.out.println(treeTraversed.get(i) + "        " + list.get(i));
        }*/

        TreeNode<PocketMonster> MiddleOfList = tree.find(list.get(list.size()/2));

    System.out.println(MiddleOfList);
        /*for(PocketMonster pm : tree){
            System.out.println(pm);
        }*/
    }

    //A better way to find average case:
    //Have a dataset that is long and random as heck
    //Take note of how many times you recurse for size of tree (with a random element)
    //Watch the numbers change as size ++
    //Find the ratio between size and number of steps and then plot
    //Average the above a lot from many runs

    public static void main(String args[]){
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.add(50);
        tree.add(30);
        tree.add(20);
        tree.add(40);
        tree.add(70);tree.add(60);
        tree.add(80);
        tree.add(55);

        tree.delete(50);

        tree.delete(80);
        tree.delete(30);

        testBinaryTree();
    }
}
