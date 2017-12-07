/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package set;

/**
 *
 * @author Justin
 */
public class DisjointSets {

    private int[] array;

    /**
     *  Construct a disjoint sets object.
     *
     *  @param numElements the initial number of elements--also the initial
     *  number of disjoint sets, since every element is initially in its own set.
     **/
    public DisjointSets(int numElements) {
        array = new int [numElements];
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
    }

    
    public void union(int root1, int root2) {
        root1 = find(root1);
        root2 = find(root2);
        if(root1 == root2) {
            System.err.println("trying to union set with itself");
            return;
        }
        if (array[root2] < array[root1]) {       // root2 has larger tree
            array[root2] += array[root1];        // update # of items in root2's tree
            array[root1] = root2;                // make root2 new root
        } else {                                 // root1 has equal or larger tree
            array[root1] += array[root2];        // update # of items in root1's tree
            array[root2] = root1;                // make root1 new root
        }
    }


    public int find(int x) {
        if (array[x] < 0) {
            return x;                         // x is the root of the tree; return it
        } else {
            // Find out who the root is; compress path by making the root x's parent.
            array[x] = find(array[x]);
            return array[x];                                      
        }
    }

   
    public static void main(String[] args) {
        int NumElements = 128;
        int NumInSameSet = 16;

        DisjointSets s = new DisjointSets(NumElements);
        int set1, set2;

        for (int k = 1; k < NumInSameSet; k *= 2) {
            for (int j = 0; j + k < NumElements; j += 2 * k) {
                set1 = s.find(j);
                set2 = s.find(j + k);
                s.union(set1, set2);
            }
        }

        for (int i = 0; i < NumElements; i++) {
            System.out.print(s.find(i) + "*");
            if (i % NumInSameSet == NumInSameSet - 1) {
                System.out.println();
            }
        }
        System.out.println();
    }
}