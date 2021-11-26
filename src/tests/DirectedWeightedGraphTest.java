import org.junit.jupiter.api.*;
import src.classes.DirectedWeightedGraph;
import src.classes.EdgeData;
import src.classes.GeoLocation;
import src.classes.NodeData;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphTest {
    NodeData[] nodes;
    DirectedWeightedGraph graph;
    private static Random _rand;

    public static void initSeed(long seed) {
        _rand = new Random(seed);
    }

    @BeforeEach
    void Initialization() {
        initSeed(0);
        this.graph = new DirectedWeightedGraph();

    }

    @Test
    void addGetNode() {
        int size = 15, expected = 0, actual = 0;
        nodeCreator(size);
        for (NodeData node : nodes) {
            graph.addNode(node);
            assertEquals(expected++, graph.getNode(actual++).getKey());
        }
    }

    @Test
    void connect() {
        int size = 15, edgesNumber = 30;
        EdgeData expected = null;
        nodeCreator(size);
        for (NodeData node : nodes) {
            graph.addNode(node);
        }
        while (this.graph.edgeSize() < edgesNumber) {
            int l = _rand.nextInt(size);
            int r = _rand.nextInt(size);
            double w = _rand.nextDouble() + 1;
            graph.connect(l, r, w);
            EdgeData actual = graph.getEdge(l, r);
            if (l != r) expected = new EdgeData(l, r, w); else expected = null;
            assertEquals(expected, actual);
        }
    }

    @Test
    void nodeIter() {
    }

    @Test
    void edgeIter() {
    }

    @Test
    void testEdgeIter() {
    }

    @Test
    void removeNode() {
        graphCreator(5, 10);
        for (int i = 0; i < 5; i++) {
            this.graph.removeNode(i);
        }
        assertEquals(0, this.nodes.length);
    }

    @Test
    void removeEdge() {
        graphCreator(2, 1);
    }

    @Test
    void nodeSize() {
    }

    @Test
    void edgeSize() {
    }

    @Test
    void getMC() {
    }
/////////////////////////////////////////////////////////////////

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

    private void graphCreator(int numOfNodes, int numOfEdges) {
        nodeCreator(numOfNodes);
        int count = 0;
        for (NodeData node : this.nodes) {
            graph.addNode(node);
        }
        while (this.graph.edgeSize() < numOfEdges) {
            int l = _rand.nextInt(numOfNodes);
            int r = _rand.nextInt(numOfEdges);
            double w = _rand.nextDouble() + 1;
            graph.connect(l, r, w);
            count++;
        }
    }
}