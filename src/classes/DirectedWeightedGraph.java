package src.classes;

import java.util.Iterator;


public class DirectedWeightedGraph implements api.DirectedWeightedGraph {
    @Override
    public NodeData getNode(int key) {
        return null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return null;
    }

    @Override
    public void connect(int src, int dest, double w) {

    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return null;
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
    }

    @Override
    public void addNode(NodeData n) {

    }
}
