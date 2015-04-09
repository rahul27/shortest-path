package com.rahul.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class creates a graph that connects each node to a neighbor such that
 * the word contained in the two nodes only differ only by one letter.
 * 
 * This allows the user to run a Breadth First Search Algorithm on the graph to
 * find the shortest distance between two nodes.
 * 
 * @author rahuldevan@gmail.com
 *
 */
public class Graph {
    private static final int DEFAULT_WORD_LENGTH = 5;

    private ConcurrentHashMap<String, Node> nodes = new ConcurrentHashMap<String, Node>();
    private ExecutorService executors;
    private Queue<String> dictionary;
    private int threads;

    private Graph() {
        threads = Runtime.getRuntime().availableProcessors();
        executors = Executors.newFixedThreadPool(threads);
    }

    private class WorkerThread implements Runnable {

        @Override
        public void run() {
            String word;
            while ((word = dictionary.poll()) != null) {
                // remove the whitespaces and convert to lower case
                word = word.trim().toLowerCase();
                // create the node if it does not exist
                if (word.length() == DEFAULT_WORD_LENGTH && nodes.get(word) == null) {
                    Node newNode = new Node(word);
                    nodes.put(word, newNode);
                    for (String graphWord : nodes.keySet()) {
                        checkNeighbor(graphWord, word);
                    }
                }
            }
        }

        // Check if two strings differ only by one letter
        private void checkNeighbor(String s1, String s2) {
            boolean mismatchFound = false;

            for (int i = 0; i < DEFAULT_WORD_LENGTH; ++i) {
                if (s1.charAt(i) != s2.charAt(i)) {
                    if (mismatchFound == false) {
                        mismatchFound = true;
                    } else {
                        return;
                    }
                }
            }
            if (mismatchFound) {
                Node n1 = nodes.get(s1);
                Node n2 = nodes.get(s2);
                n1.addNeighbor(n2);
                n2.addNeighbor(n1);
            }
        }
    }

    private void addDictionary(List<String> dictionary) {
        this.dictionary = new ConcurrentLinkedQueue<String>(dictionary);

        // Use threads to create the graph
        for (int i = 0; i < threads; i++) {
            executors.submit(new WorkerThread());
        }
        executors.shutdown();
        try {
            executors.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<String> getPath(String sourceString, String destinationString) {
        LinkedList<String> returnList = new LinkedList<String>();
        Node sourceNode = nodes.get(sourceString);
        Node destinationNode = nodes.get(destinationString);

        // Check if the words exist in the dictionary
        if (sourceNode != null && destinationNode != null) {

            sourceNode.distance = 0;
            Queue<Node> producerQueue = new LinkedList<Node>();
            producerQueue.add(sourceNode);

            // Run BFS on the graph to find the shortest path to the
            // destination node, marking each node with the shortest
            // distance from the source node using the "distance" and
            // "previous" node values
            while (producerQueue.isEmpty() == false) {
                Node parent = producerQueue.poll();
                for (Node child : parent.neighbors) {
                    if (child.distance == -1 || child.distance > parent.distance + 1) {
                        child.distance = parent.distance + 1;
                        child.previous = parent;
                        producerQueue.add(child);
                    }
                }
            }

            // Now, try using the previous links to find a path to the
            // source from the destination
            while (destinationNode != null) {
                returnList.addFirst(destinationNode.word);
                destinationNode = destinationNode.previous;
            }

            // Check if the returned path is valid
            if (returnList.size() <= 2 || returnList.getFirst().equals(sourceString) == false
                    && returnList.getLast().equals(destinationString) == false) {
                returnList.clear();
            }
        }
        return returnList;
    }

    /**
     * This function returns the shortest path between two input words in a
     * graph such that only one character is changed at a time.
     * 
     * All the intermediate words in the path will belong to the words in the
     * provided dictionary.
     * 
     * @param source
     *            The 5 letter source word
     * @param destination
     *            The 5 letter destination word
     * @param dictionary
     *            The 5 letter dictionary
     * @return if a valid path exists between source and destination it returns
     *         a list containing that path else it returns an empty list
     */
    public static List<String> findPath(String source, String destination, List<String> dictionary) {
        List<String> returnList = new ArrayList<String>();

        // Validate the input for null values
        if (source != null && destination != null && dictionary != null
                && dictionary.isEmpty() == false) {

            // Remove whitespaces from the input strings and convert to lower
            // case
            String sourceString = source.trim().toLowerCase();
            String destinationString = destination.trim().toLowerCase();

            // Check for inconsistent input
            if (sourceString.equals(destinationString) == false
                    && sourceString.length() == DEFAULT_WORD_LENGTH
                    && destinationString.length() == DEFAULT_WORD_LENGTH) {
                Graph graph = new Graph();
                graph.addDictionary(dictionary);
                returnList = graph.getPath(sourceString, destinationString);
                graph.clear();
            }
        }
        return returnList;
    }

    private void clear() {
        // clear the graph data
        nodes.clear();
        executors.shutdownNow();
        if (this.dictionary != null) {
            this.dictionary.clear();
        }
    }

}