package src.classes;

import org.w3c.dom.Node;

import java.util.*;


public class DirectedWeightedGraph implements api.DirectedWeightedGraph {
    HashMap<Integer, NodeData> nodes;
    Vector<EdgeData> edges;
    HashMap<Integer, HashMap<Integer, EdgeData>> graph;
    int mc;


    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges, graph, mc);
    }

    public DirectedWeightedGraph(){
        this.nodes = new HashMap<>();
        this.edges = new Vector<>();
        this.graph = new HashMap<>();
        this.mc = 0;
    }

    @Override
    public NodeData getNode(int key) {
        if (this.nodes.containsKey(key))
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
        if (!this.nodes.containsKey(n.getKey())) {
            this.nodes.put(n.getKey(),n);
            this.graph.put(n.getKey(),new HashMap<>());
            this.mc++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (src != dest && this.nodes.containsKey(src) && this.nodes.containsKey(dest) && !this.graph.get(src).containsKey(dest)) {
            EdgeData e = new EdgeData(src, dest, w);
            this.edges.add(e);
            this.graph.get(src).put(dest, e);
            this.mc++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedWeightedGraph that = (DirectedWeightedGraph) o;
        if (nodes.size() != that.nodeSize() && edges.size() != that.edgeSize() && graph.size() != that.graph.size() && mc != that.mc) return false;
        for (int i = 0; i <nodeSize() ; i++) {
            if (!nodes.get(i).equals(that.nodes.get(i)))
                return false;
        }
        for (int i = 0; i < edgeSize() ; i++) {
            if(!edges.get(i).equals(that.edges.get(i)))
                return false;
        }
        Iterator<Map.Entry<Integer, HashMap<Integer, EdgeData>>> itr1 = graph.entrySet().iterator();
        Iterator<Map.Entry<Integer, HashMap<Integer, EdgeData>>> itr2 = that.graph.entrySet().iterator();
        while(itr1.hasNext() || itr2.hasNext()){
            Map.Entry<Integer,HashMap<Integer,EdgeData>> e1 = itr1.next();
            Map.Entry<Integer,HashMap<Integer,EdgeData>> e2 = itr2.next();
            int key1 = e1.getKey();
            int key2 = e2.getKey();
            if (key1 != key2) return false;
            Iterator<EdgeData> eItr1 = edgeIter(key1);
            Iterator<EdgeData> eItr2 = edgeIter(key2);
            while(eItr1.hasNext() || eItr2.hasNext()){
                if(!eItr1.next().equals(eItr2.next()))return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return this.nodes.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
       return edges.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return this.graph.get(node_id).values().iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        if(this.nodes.containsKey(key)){
            Iterator<EdgeData> it = edgeIter(key);
            while (it.hasNext()){
                EdgeData e = it.next();
                it.remove();
                removeEdge(e.getSrc(),e.getDest());
                if(this.nodes.containsKey(e.getDest()) && this.graph.get(e.getDest()).containsKey(e.getSrc()))
                    removeEdge(e.getDest(),e.getSrc());
            }
            NodeData node = this.getNode(key);
            this.nodes.remove(key);
            this.graph.remove(key);
            mc++;
            return node;
        }
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        EdgeData e = null;
        if(src != dest && this.nodes.containsKey(src) && this.nodes.containsKey(dest) && this.graph.get(src).containsKey(dest)) {
            e = this.graph.get(src).get(dest);
            this.edges.remove(e);
            this.graph.get(src).remove(dest);
            mc++;
        }
        return e;
    }

    @Override
    public int nodeSize() {
        return this.nodes.size();
    }

    @Override
    public int edgeSize() {
            return edges.size();

        }

    @Override
    public int getMC() {
        return this.mc;
    }

}
