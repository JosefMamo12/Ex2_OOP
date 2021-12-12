package classes;

import java.util.*;

/**
 * This class repr
 */
public class DirectedWeightedGraph implements api.DirectedWeightedGraph {
    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashSet<Integer>> edgeIn;
    private HashMap<Integer, HashMap<Integer, EdgeData>> graph;
    private int mc;
    private int edgeSize;

    public HashMap<Integer, NodeData> getNodes() {
        return nodes;
    }

    public HashMap<Integer, HashMap<Integer, EdgeData>> getGraph() {
        return graph;
    }

    public static final String ERROR = "ConcurrentModificationException: Changed the structure of the data structure while iterating!";

    @Override
    public int hashCode() {
        return Objects.hash(nodes, graph, mc);
    }

    public DirectedWeightedGraph() {
        this.nodes = new HashMap<>();
        this.graph = new HashMap<>();
        this.edgeIn = new HashMap<>();
        this.mc = 0;
        this.edgeSize = 0;
    }

    /**
     * Return  NodeData object by the integar value input
     * return null if not exist.
     * @param key - the node_id
     * @return
     */
    @Override
    public NodeData getNode(int key) {
        if (this.nodes.containsKey(key))
            return this.nodes.get(key);
        return null;
    }

    /**
     * Return EdgeData object by giving src value and dest
     * retun null if not exist.
     * @param src
     * @param dest
     * @return EdgeData
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        if (graph.containsKey(src) && graph.get(src).containsKey(dest))
            return this.graph.get(src).get(dest);
        return null;
    }

    /**
     * Add new node to the graph,
     * Igonore if the key already exist.
     * @param n
     */
    @Override
    public void addNode(NodeData n) {
        if (!this.graph.containsKey(n.getKey())) {
            this.nodes.put(n.getKey(), n);
            this.graph.put(n.getKey(), new HashMap<>());
            this.edgeIn.put(n.getKey(), new HashSet<>());
            this.mc++;
        }
    }

    /**
     * Connect between source and dest and edding as EdgeData object to the graph.
     * Override if the src and dest is already exist at graph.
     * Throw exceptions when one of the varibels is forbiden.
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */

    @Override
    public void connect(int src, int dest, double w) {
        if (src != dest && this.graph.containsKey(src) && this.graph.containsKey(dest) && w > 0) {
            if (graph.get(src).containsKey(dest)) {
                removeEdge(src, dest);
            }

            EdgeData e = new EdgeData(src, dest, w);
            this.graph.get(src).put(dest, e);
            this.edgeIn.get(dest).add(src);
            this.mc++;
            this.edgeSize++;
        } else {
            throw new IllegalArgumentException("ERROR: illegal argument src,dest,w");
        }
    }

    /**
     * Check equal between two graph
     * Created speacialy to test DirectedWeightedGraphAlgorithm copy function.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedWeightedGraph that = (DirectedWeightedGraph) o;
        if (nodes.size() != that.nodeSize() && graph.size() != that.graph.size())
            return false;
        for (int i = 0; i < nodeSize(); i++) {
            if (!nodes.get(i).equals(that.nodes.get(i)))
                return false;
        }
        Iterator<Map.Entry<Integer, HashMap<Integer, EdgeData>>> itr1 = graph.entrySet().iterator();
        Iterator<Map.Entry<Integer, HashMap<Integer, EdgeData>>> itr2 = that.graph.entrySet().iterator();
        while (itr1.hasNext() || itr2.hasNext()) {
            Map.Entry<Integer, HashMap<Integer, EdgeData>> e1 = itr1.next();
            Map.Entry<Integer, HashMap<Integer, EdgeData>> e2 = itr2.next();
            int key1 = e1.getKey();
            int key2 = e2.getKey();
            if (key1 != key2) return false;
            Iterator<EdgeData> eItr1 = edgeIter(key1);
            Iterator<EdgeData> eItr2 = edgeIter(key2);
            while (eItr1.hasNext() || eItr2.hasNext()) {
                if (!eItr1.next().equals(eItr2.next())) return false;
            }
        }
        return true;
    }

    /**
     * Iterating over all varibles of the data structure that contains all the nodes of the graph
     * Throw exception when trying to change the graph while iterating.
     * @return
     */
    @Override
    public Iterator<NodeData> nodeIter() {
        return new Iterator<NodeData>() {
            final int iteratorMc = mc;
            final Iterator<NodeData> itr = nodes.values().iterator();

            @Override
            public boolean hasNext() {
                return itr.hasNext();
            }

            @Override
            public NodeData next() {
                if (itr.hasNext() && iteratorMc == mc)
                    return itr.next();
                throw new ConcurrentModificationException(ERROR);
            }
        };
    }

    /**
     * Iterate over all the edges of the graph
     * using nodeIter() function and edgeIter(int node_id) to save using to another data structure.
     * will throw exception because the use of the related iterators.
     * @return
     */
    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<EdgeData>() {
            Iterator<NodeData> it = nodeIter();
            Iterator<EdgeData> edItr;
            boolean flagForHasNext = false;
            int Itrmc = getMC();

            @Override
            public boolean hasNext() {
                while (true) {
                    if (it.hasNext() && !flagForHasNext) {
                        flagForHasNext = true;
                        edItr = edgeIter(it.next().getKey());
                        if(edItr.hasNext() == false && it.hasNext()) {
                            edItr = edgeIter(it.next().getKey());
                        }
                    } else if (flagForHasNext) {
                        if (!edItr.hasNext())
                            flagForHasNext = false;
                        else
                            return edItr.hasNext();
                    } else {
                        return false;
                    }
                }
            }

            @Override
            public EdgeData next() {
                if (!edItr.hasNext() && Itrmc != getMC())
                    throw new ConcurrentModificationException(ERROR);
                return edItr.next();
            }
        };
    }

    /**
     * Iterating over edges of given node.
     * Throw exception whn trying to changing somthing in graph while iterating.
     * @param node_id
     * @return
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new Iterator<EdgeData>() {
            Iterator<EdgeData> it = graph.get(node_id).values().iterator();
            int itrMc = getMC();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public EdgeData next() {
                if (mc == itrMc) {
                    return it.next();
                }
                throw new ConcurrentModificationException(ERROR);
            }
        };

    }
    /**
     * Remove of node by given key remove all the edges that out of that node,
     * remove all the nodes thats get in to this node by using EdgeIn data structure.
     * Returning null if the key is not exist.
     * @param key
     * @return
     */
    @Override
    public NodeData removeNode(int key) {
        if (this.nodes.containsKey(key)) {
            Iterator<EdgeData> it = graph.get(key).values().iterator();
            while (it.hasNext()) {
                EdgeData e = it.next();
                it.remove();
                edgeIn.get(e.getDest()).remove(e.getSrc());
                mc++;
                edgeSize--;
                if (graph.get(e.getDest()).containsKey(e.getSrc()))
                    removeEdge(e.getDest(), e.getSrc());

            }
            Iterator<Integer> edgeInItr = edgeIn.get(key).iterator();
            while (edgeInItr.hasNext()) {
                Integer keyToDelete = edgeInItr.next();
                edgeInItr.remove();
                removeEdge(keyToDelete, key);

            }
            NodeData node = this.getNode(key);
            this.nodes.remove(key);
            this.graph.remove(key);
            this.edgeIn.remove(key);
            mc++;
            return node;
        }
        return null;
    }

    /**
     * Remove edge by given source and destination.
     * Return null if the edge is not exist.
     * @param src
     * @param dest
     * @return
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        EdgeData e = null;
        if (src != dest && this.graph.containsKey(src) && this.graph.containsKey(dest) && this.graph.get(src).containsKey(dest)) {
            e = this.graph.get(src).get(dest);
            this.graph.get(src).remove(dest);
            edgeIn.get(e.getDest()).remove(e.getSrc());
            edgeSize--;
            mc++;
        }
        return e;
    }

    /**
     * Return true if and only if the node contains in the graph if exist
     * @param node
     * @return
     */
    public boolean contains(int node) {
        if (nodeSize() > 0)
            return this.graph.containsKey(node);
        return false;
    }

    /**
     * Return the amount of node in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return this.graph.size();
    }

    /**
     * Return the amount of edges in the graph.
     * @return
     */
    @Override
    public int edgeSize() {
        return edgeSize;
    }

    /**
     * Return every change in the graph for example remove node, add node, remove edge.
     * @return
     */
    @Override
    public int getMC() {
        return this.mc;
    }

}
