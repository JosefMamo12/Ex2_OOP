import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.classes.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphAlgorithmsTest {
    NodeData[] nodes;
    DirectedWeightedGraph g;
    private static Random _rand;

    public static void initSeed(long seed) {
        _rand = new Random(seed);
    }

    @BeforeEach
    void Initialization() {
        initSeed(0);
        DirectedWeightedGraph testingGraph = new DirectedWeightedGraph();
    }

    @Test
    void getGraph() {
    }

    @Test
    void copy() {
        DirectedWeightedGraph testingGraph = graphCreator(100000,500000);
        DirectedWeightedGraphAlgorithms graphAlgo = new DirectedWeightedGraphAlgorithms();
        graphAlgo.init(testingGraph);
        DirectedWeightedGraph copyGraph = graphAlgo.copy();
        assertEquals(testingGraph.getMC(), copyGraph.getMC());
        assertEquals(testingGraph.nodeSize(), copyGraph.nodeSize());
        assertEquals(testingGraph.edgeSize(), copyGraph.edgeSize());
        Iterator<NodeData> realGraphNodeItr = testingGraph.nodeIter();
        Iterator<NodeData> copyGraphNodeItr = copyGraph.nodeIter();
        while (realGraphNodeItr.hasNext() && copyGraphNodeItr.hasNext()) {
            NodeData realNode = realGraphNodeItr.next();
            NodeData copyNode = copyGraphNodeItr.next();
            assertEquals(realNode, copyNode);
        }
        assertEquals(testingGraph.nodeSize(), copyGraph.nodeSize());
        copyGraph.addNode(new NodeData(100000, new GeoLocation(1.2232, 1.2344, 0)));
        assertNotEquals(testingGraph.nodeSize(), copyGraph.nodeSize());
        copyGraph.removeNode(100000);
        Iterator<EdgeData> realGraphEdgeItr = testingGraph.edgeIter();
        Iterator<EdgeData> copyGraphEdgeItr = testingGraph.edgeIter();
        while (realGraphEdgeItr.hasNext() && copyGraphEdgeItr.hasNext()) {
            EdgeData realEdge = realGraphEdgeItr.next();
            EdgeData copyEdge = copyGraphEdgeItr.next();
            assertEquals(realEdge, copyEdge);
        }
        assertEquals(testingGraph.edgeSize(), copyGraph.edgeSize());
        copyGraph.connect(4, 9, 0);
        assertNotEquals(testingGraph.edgeSize(), copyGraph.edgeSize());
        copyGraph.removeEdge(4, 9);

        for (int i = 0; i < nodes.length; i++) {
            Iterator<EdgeData> itr1 = testingGraph.edgeIter(i);
            Iterator<EdgeData> itr2 = copyGraph.edgeIter(i);
            while (itr1.hasNext() && itr2.hasNext()) {
                EdgeData e1 = itr1.next();
                EdgeData e2 = itr2.next();
                assertEquals(e1, e2);
            }

        }
        assertNotEquals(testingGraph.getMC(), copyGraph.getMC());
    }

    @Test
    void isConnected() {
        DirectedWeightedGraph testingGraph = graphCreator(10);
        DirectedWeightedGraphAlgorithms graphAlgo = new DirectedWeightedGraphAlgorithms();
        graphAlgo.init(testingGraph);
        assertFalse(graphAlgo.isConnected());
        for (int i = 0; i < 10; i++) {
            testingGraph.connect(i, i + 1, 0);
            testingGraph.connect(i + 1, i, 0);
        }
        // This is the graph
        // 0 -> <- 1 -> <- 2 -> <- 3 -> <- 4 -> <- 5 -> <- 6-> <- 7 -> <- 8 -> <- 9
        boolean b = graphAlgo.isConnected();
        assertTrue(graphAlgo.isConnected());
        testingGraph.removeEdge(8,9);
        b = graphAlgo.isConnected();
        assertFalse(graphAlgo.isConnected());
        testingGraph.removeNode(8);
        testingGraph.removeNode(9);
        assertTrue(graphAlgo.isConnected());
    }

    @Test
    void shortestPathDist() {
    }

    @Test
    void shortestPath() {
    }

    @Test
    void center() {
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }

    @Test
    void tsp() {
    }

    /**
     * Bank of nodes;
     *
     * @param size
     */
    private void nodeCreator(int size) {
        nodes = new NodeData[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new NodeData(i, new GeoLocation(_rand.nextDouble() + 35, _rand.nextDouble() + 35, 0.0));
        }
    }

    /**
     * Graph creator without any edges!
     *
     * @param numOfNodes
     */
    private DirectedWeightedGraph graphCreator(int numOfNodes) {
        DirectedWeightedGraph gr = new DirectedWeightedGraph();
        nodeCreator(numOfNodes);
        for (NodeData node : this.nodes) {
            gr.addNode(node);
        }
        return gr;
    }

    /**
     * Graph creator with numOfEdge param which means the
     * count of the edges, this graph is contains.
     *
     * @param numOfNodes
     * @param numOfEdges
     */
    private DirectedWeightedGraph graphCreator(int numOfNodes, int numOfEdges) {
        DirectedWeightedGraph gr = new DirectedWeightedGraph();
        nodeCreator(numOfNodes);
        for (NodeData node : this.nodes) {
            gr.addNode(node);
        }
        while (gr.edgeSize() < numOfEdges) {
            int l = _rand.nextInt(numOfNodes);
            int r = _rand.nextInt(numOfEdges);
            double w = _rand.nextDouble() + 1;
            gr.connect(l, r, w);
        }
        return gr;
    }
}
