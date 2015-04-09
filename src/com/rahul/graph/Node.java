package com.rahul.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents each node in the graph
 * 
 * @author rahuldevan@gmail.com
 */
class Node {
    final String word;
    final Set<Node> neighbors;
    Node previous;
    int distance = -1;

    Node(String word) {
        this.word = word;
        neighbors = new HashSet<Node>();
    }

    synchronized void addNeighbor(Node node) {
        neighbors.add(node);
    }

}
