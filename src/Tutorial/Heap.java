package Tutorial;/* Generic Min/Max Binary Tutorial.Heap
 * for /r/javaexamples
 *
 */
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("unchecked")

/**
 * Based on code by /u/Philboyd_Studge
 */
public class Heap<T extends Comparable<T>> { /*
    HEAP RULES:
        1: - The root of each subtree must be the largest node in that subtree

                This is what bubble up and bubble down are for, AKA upheap downheap, etc.

                These methods will ensure that a given node is in the right spot and will move them
                accordingly.

        2: - The tree nodes must be added left to right, top to bottom


[ 73, 12, 190, 2, 20 ]

for each element of the array, the index of its children is: (don't zero index)

index.left = 2 * index

index.right = 2 * index + 1

index.parent = index / 2

                        big
        smol                            smol2
tiny            tiny2           tiny3           tiny4


V       V       V       V       V       V       V       V

        [big, smol, smol2, tiny, tiny2, tiny3, tiny4]


        1,      2,      3,   4,   5,     6,      7


Height                                                                  Number of items
Level 1 :                        big                                    1
Level 2 :        smol                            smol2                  2
Level 3 :tiny            tiny2           tiny3           tiny4          4
Level 4 :                                                               8
Level 5 :                                                               16
Level n :                                                               2^(n-1)

Height for size n:

log base 2 of n


*/
    private T[] heap;                                   //The array into which we smush our tree
    private static final int DEFAULT_CAPACITY = 10;     //This is how big the array starts, and how much it grows every time it needs to grow
    private int size;           //This is how much crap we put in our heap!
    private boolean min;        //If this is active, we put the smallest item in the top priority spot


    public Heap(T[] array, boolean min){
        heap = (T[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
        this.min = min;

        for(T item : array){
            add(item);
        }

    }

    public Heap(boolean min){
        heap = (T[]) new Comparable[DEFAULT_CAPACITY];
        size = 0;
        this.min = min;
    }

    /**
     * Best case: O(1)      Inherited from best case of bubbleUp() times its own complexity of O(1)
     * Worst case: O(log(n)) Inherited from worst case of bubbleUp() times its own complexity of O(1)
     *
     */
    public void add(T value)
    {
        if(this.size >= heap.length - 1){
            heap = this.resize();
        }

        //Take note that we added something so our size must increase
        size++;

        //Set the value of the last index of the heap to be our added one (not the last index of the array but the last index of our heap, the size variable)
        heap[size] = value;

        //Let the newly added member rise up to where they belong from the slums!
        bubbleUp();
    }

    /**
     * Best case: O(1)      Inherited from best case of bubbleDown() times its own complexity of O(1)
     * Worst case: O(log(n)) Inherited from worst case of bubbleDown(), times its own complexity of O(1)
     *
     */
    public T pop() {
        T output = peek();      //Documenting out our target, here we ar removing the top element from the heap

        /*
        We send our target to the slums, where we can take it out without any hassle, and let garbage collection handle the rest.

        We bring one from the slums up to the top to temporarily take the place of the one we eliminated so that there is no gap
        up there where gaps are hard to fill.
         */
        swap(1, length());      //We get to a base case by swapping the item we are removing out from where it was (the start) to the end of the the structure, so we can nuke it easy!

        heap[length()] = null;  //Kill it off after moving it to a secluded location, let garbage collection take away the body :)

        /*
        Change the records to show that the one we eliminated was not there.
         */
        size--;

        /*
         Take the double back down to the slums after everyone has forgotten about the elimination.
         */
        bubbleDown();           //Clean up the mess, get rid of the evidence! This one will bubble the one we displaced to replace the victim back down to where it belongs.


        return output;          //Delivering the target

    }

    public boolean isEmpty()
    {
        return size ==0;
    }

    public T peek()
    {
        return heap[1];     //Just show what is at the top of the array :)
    }

    public int length() {
        return size;
    }

    /**
     * O(N) :(
     */
    private T[] resize() {
        return Arrays.copyOf(heap, heap.length+DEFAULT_CAPACITY);
    }

    /**
     * This method starts from the last node added, then it checks it against all of its lineage (parents, parent.parents. .....) and makes sure it is in the right order.
     *
     * Since we go through the height of the tree, we go through log n items! WORST CASE: O(Log(n))
     *
     */
    private void bubbleUp() {
        //Start from the bottom
        int idx = size;
        if(min){    //The case of a min heap
            //Now we're here
            while(hasParent(idx) && parent(idx).compareTo(heap[idx]) > 0){  //If the parent exists and is bigger (or smaller in the case of a max heap)
                swap(idx, parentIndex(idx));                //Swap with the one above (bubble up)
                idx = parentIndex(idx);                     //Climb up the tree
            }
        }else{      //The case of a max heap, it's just like the min heap, but the > is now a <
            while(hasParent(idx) && parent(idx).compareTo(heap[idx]) < 0){  //If the parent exists and is smaller (max heap)
                swap(idx, parentIndex(idx));                //Swap with the one above (bubble up)
                idx = parentIndex(idx);                     //Climb up the tree
            }
        }
    }

    /**
     *   Worst case : O(log(n))     fully in the wrong place (new bottom / top item)
     *
     *   Best case : O(1)           already in its place
     */

    private void bubbleDown() {
        //Start from the top
        int idx = 1;
        if(min){    //The case of a min heap
            while(hasLeftChild(idx)){
                int smallestChild = leftIndex(idx);       //Guess the smaller is the left child (ARBITRARY)
                if(hasRightChild(idx) && heap[leftIndex(idx)].compareTo(heap[rightIndex(idx)]) > 0){
                    smallestChild = rightIndex(idx);      //If the right child is smaller than the left, it is smaller, correct the guess
                }
                if(heap[idx].compareTo(heap[smallestChild]) > 0){ swap(idx,smallestChild); }
                else break;
                idx = smallestChild;                        //Move onto the subtree
            }
        }else{      //The case of a max heap
            while(hasLeftChild(idx)){
                int smallestChild = leftIndex(idx);       //Guess the smaller is the left child (ARBITRARY)
                if(hasRightChild(idx) && heap[leftIndex(idx)].compareTo(heap[rightIndex(idx)]) < 0){
                    smallestChild = rightIndex(idx);      //If the right child is bigger than the left, it is bigger, correct the guess
                }
                if(heap[idx].compareTo(heap[smallestChild]) < 0){ swap(idx,smallestChild); }
                else break;
                idx = smallestChild;                        //Move onto the subtree
            }
        }
    }

    private boolean hasParent(int i) {
        return i > 1;
    }

    private int leftIndex(int i) {
        return 2 * i;
    }

    private int rightIndex(int i) {
        return 2 * i + 1;
    }

    private boolean hasLeftChild(int i) {
        return leftIndex(i) <= size;
    }

    private boolean hasRightChild(int i) {
        return rightIndex(i) <= size;
    }

    private int parentIndex(int i) {
        return i /2;
    }

    private T parent(int i) {
        return heap[parentIndex(i)];
    }

    private void swap(int index1, int index2) {
        T temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (T item : heap)
        {
            if (item != null) sb.append(item).append(", ");
        }
        return sb.toString();

    }

    public static void main(String args[]){
        Random rand = new Random();
        Integer[] nums = new Integer[20];
        for(int i = 0; i < nums.length; i++){
            nums[i] = rand.nextInt(100);
        }
        Heap<Integer> testSubject = new Heap<Integer>(nums,false);

        while(!testSubject.isEmpty()){
            System.out.println(testSubject.pop());
        }
    }

}