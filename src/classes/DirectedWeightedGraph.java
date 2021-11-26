package src.classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class DirectedWeightedGraph implements api.DirectedWeightedGraph {
    HashMap<Integer, NodeData> nodes;
    Vector<EdgeData> edges;
    HashMap<Integer, HashMap<Integer, EdgeData>> graph;
    int mc;

    public DirectedWeightedGraph(){
        this.nodes = new HashMap<>();
        this.edges = new Vector<>();
        this.graph = new HashMap<>();
        this.mc = 0;
    }

    @Override
    public NodeData getNode(int key) {
        if (!this.graph.containsKey(key))
            return this.nodes.get(key);
        return null;
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        if (graph.containsKey(src) && graph.get(src).containsKey(dest))
            return this.graph.get(src).get(dest);
        return null;
    }

    @Override
    public void addNode(NodeData n) {
        if (!this.nodes.containsKey(n.key)) {
            this.nodes.put(n.key,n);
            this.graph.put(n.key,new HashMap<>());
            this.mc++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (this.nodes.containsKey(src) && this.nodes.containsKey(dest) && !this.graph.get(src).containsKey(dest)) {
            EdgeData e = new EdgeData(src, dest, w);
            this.edges.add(e);
            this.mc++;
            this.graph.get(src).put(dest, e);
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return this.nodes.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return this.edges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return this.graph.get(node_id).values().iterator();
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
        return this.nodes.size();
    }

    @Override
    public int edgeSize() {
        return this.edges.size();
    }

    @Override
    public int getMC() {
        return this.mc;
    }

}
