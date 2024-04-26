package wordle;

import project20280.interfaces.Entry;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;

import java.util.List;
import java.util.Map;

import static wordle.Wordle.letterFreqMap;

public class Huffman {
    public static void huffmanTree(List<String> dictionary) {


        HeapPriorityQueue<Integer, LinkedBinaryTree<String>> q = new HeapPriorityQueue<>();

        // Fill the heap with values from the frequency map
        for (Map.Entry<String, Integer> entry : letterFreqMap.entrySet()) {
            LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
            t.addRoot(entry.getKey());
            q.insert(entry.getValue(), t);
        }


        // Build the Huffman tree
        while (q.size() > 1) {
            Entry<Integer, LinkedBinaryTree<String>> e1 = q.removeMin();
            Entry<Integer, LinkedBinaryTree<String>> e2 = q.removeMin();

            LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
            t.addRoot(e1.getValue().root().getElement().concat(e2.getValue().root().getElement()));

            t.attach(t.root(), e1.getValue(), e2.getValue());

            q.insert(e1.getKey() + e2.getKey(), t);


        }


        System.out.println(q.min().getValue().toBinaryTreeString());
    }
}
