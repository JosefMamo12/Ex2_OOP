package test;

import classes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.*;

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
        g = new DirectedWeightedGraph();
    }

    @Test
    void copy() {
        DirectedWeightedGraph testingGraph = graphCreator(10, 20);
        DirectedWeightedGraphAlgorithms graphAlgo = new DirectedWeightedGraphAlgorithms();
        graphAlgo.init(testingGraph);
        DirectedWeightedGraph copyGraph = graphAlgo.copy();
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
        copyGraph.addNode(new classes.NodeData(100000, new GeoLocation(1.2232, 1.2344, 0)));
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
        copyGraph.connect(4, 9, 1);
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
        for (int i = 0; i < 9; i++) {
            testingGraph.connect(i, i + 1, 1);
            testingGraph.connect(i + 1, i, 1);
        }
        // This is the graph
        // 0 -> <- 1 -> <- 2 -> <- 3 -> <- 4 -> <- 5 -> <- 6-> <- 7 -> <- 8 -> <- 9
        boolean b = graphAlgo.isConnected();
        assertTrue(graphAlgo.isConnected());
        testingGraph.removeEdge(8, 9);
        b = graphAlgo.isConnected();
        assertFalse(graphAlgo.isConnected());
        testingGraph.removeNode(8);
        testingGraph.removeNode(9);
        b = graphAlgo.isConnected();
        assertTrue(graphAlgo.isConnected());

    }

    @Test
    void isConnectedBigGraph() {
        this.graphCreator1(10000, 200000);
        DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithms();
        dwga.init(this.g);
        System.out.println(dwga.isConnected());

    }
    @Test
    void ShortestPathBigGraphTest (){
        this.graphCreator1(1000000,20000000);
        DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithms();
        dwga.init(this.g);
       printPath(dwga.shortestPath(12,5000));
    }
    @Test
    void centerBigGraphs() {
        this.graphCreator1(1000, 20000);
        DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithms();
        dwga.init(this.g);
        dwga.center();
    }
    @Test
    void tspBigGraphs(){
        this.graphCreator1(100000, 2000000);
        DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithms();
        dwga.init(this.g);
        List<NodeData> lst = new ArrayList<>();
        lst.add(new NodeData(nodes[1]));
        lst.add(new NodeData(nodes[150]));
        lst.add(new NodeData(nodes[870]));
        printPath(dwga.tsp(lst));

    }

    @Test
    void shortestPathDist() {
        g = graphCreator(6);
        g.connect(0, 1, 3);
        g.connect(1, 2, 2);
        g.connect(2, 3, 1);
        g.connect(3, 5, 1);
        g.connect(1, 5, 8);
        g.connect(5, 1, 8);
        g.connect(1, 4, 1);
        g.connect(4, 0, 3);
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        assertEquals(7, dwa.shortestPathDist(0, 5));
        g.removeEdge(0, 1);
        assertEquals(-1, dwa.shortestPathDist(0, 5));

    }

    @Test
    void shortestPath() {
        DirectedWeightedGraph g1 = graphCreator(13);
        g1.connect(0, 1, 3);
        g1.connect(1, 2, 2);
        g1.connect(2, 3, 1);
        g1.connect(3, 5, 1);
        g1.connect(1, 5, 8);
        g1.connect(5, 1, 8);
        g1.connect(5, 10, 6);
        g1.connect(0, 10, 1);
        g1.connect(10, 12, 10);
        g1.connect(12, 6, 2);
        g1.connect(6, 9, 12);
        g1.connect(9, 6, 12);
        g1.connect(3, 9, 20);
        g1.connect(11, 9, 10);
        g1.connect(12, 11, 5);
        g1.connect(11, 8, 3);
        g1.connect(8, 3, 4);
        DirectedWeightedGraphAlgorithms dwa1 = new DirectedWeightedGraphAlgorithms();
        dwa1.init(g1);
        String actual = "";
        String excpected = "->1->2->3->5->10->12";
        List<NodeData> lst = dwa1.shortestPath(1, 12);
        for (NodeData nodeData : lst) {
            actual += "->" + nodeData.getKey();
        }
        g1.removeNode(5);
        assertEquals(null,dwa1.shortestPath(1,12));
        assertEquals(excpected, actual);
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        dwa.load("data/G1.json");
        actual = "";
        excpected = "->0->1->2->6->7->8->9";
        List<NodeData> lnd = dwa.shortestPath(0, 9);
        for (NodeData nodeData : lnd) {
            actual += "->" + nodeData.getKey();
        }
        assertEquals(actual, excpected);
//        assertEquals(d, 7);
    }

    @Test
    void center() {
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        dwa.load("data/1000Nodes.json");
        assertEquals(362, dwa.center().getKey());
        DirectedWeightedGraph dwg = graphCreator(4);
        dwg.connect(1, 0, 1);
        dwg.connect(0, 1, 1);
        dwg.connect(1, 3, 1);
        dwg.connect(3, 2, 1);
        dwg.connect(2, 0, 1);
        dwa.init(dwg);
        assertEquals(1, dwa.center().getKey());
    }

    @Test
    void save() {
        DirectedWeightedGraph fG = new DirectedWeightedGraph();
        DirectedWeightedGraph tG = new DirectedWeightedGraph();
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        DirectedWeightedGraphAlgorithms tdwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(fG);
        tdwa.init(tG);
        dwa.load("data/G1.json");
        DirectedWeightedGraph loadedGraphBeforeSave = dwa.copy();
        dwa.save("data/G1Copy.json");
        tdwa.load("data/G1Copy.json");
        DirectedWeightedGraph loadedGraphAfterSave = tdwa.copy();
        assertEquals(loadedGraphAfterSave, loadedGraphBeforeSave);
    }

    @Test
    void load() {
        DirectedWeightedGraphAlgorithms dwa = new DirectedWeightedGraphAlgorithms();
        dwa.init(g);
        dwa.load("data/TestJsonGraph");
        DirectedWeightedGraph compareToJson = new DirectedWeightedGraph();
        NodeData nd = new NodeData(0, new GeoLocation(35.19589389346247, 32.10152879327731, 0.0));
        NodeData nd1 = new NodeData(1, new GeoLocation(35.20319591121872, 32.10318254621849, 0.0));
        NodeData nd2 = new NodeData(2, new GeoLocation(35.20752617756255, 32.1025646605042, 0.0));
        compareToJson.addNode(nd);
        compareToJson.addNode(nd1);
        compareToJson.addNode(nd2);
        compareToJson.connect(0, 2, 1.3118716362419698);
        compareToJson.connect(0, 1, 1.232037506070033);
        compareToJson.connect(1, 0, 1.8635670623870366);
        assertEquals(g, compareToJson);
    }

    @Test
    /**
     * Tsp check for not connected graph which the relative vertics are being connected
     * When 0 is not part of the path meaning impossible to get to 0 node
     */
    void tspNotConnectedGraph() {
        DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithms();
        dwga.init(g);
        dwga.load("data/notCon.json");
        NodeData d1 = new NodeData(g.getNode(5)), d2 = new NodeData(g.getNode(2)), d3 = new NodeData(g.getNode(4));
        ArrayList<NodeData> cities = new ArrayList<>();
        cities.add(d2);
        cities.add(d1);
        cities.add(d3);
        String EXPECTED= "->2->3->4->5";
        String ACTUAL = stringPathBuilder(dwga.tsp(cities));
        assertEquals(EXPECTED,ACTUAL);
        cities.clear();
        d1 = new NodeData(g.getNode(0));
        cities.add(d1); // adding the 0 node.
        cities.add(d2);
        cities.add(d3);
        dwga.tsp(cities);
        ACTUAL = stringPathBuilder(dwga.tsp(cities));
        EXPECTED = null;
        assertEquals(ACTUAL,EXPECTED);

    }
    @Test
    void tspConnectedGraph(){
        DirectedWeightedGraphAlgorithms dwga = new DirectedWeightedGraphAlgorithms();
        dwga.init(g);
        assertEquals(null,dwga.tsp(null)); // null list
        dwga.load("data/subGraph.json");
        NodeData d1 = new NodeData(g.getNode(0)), d2 = new NodeData(g.getNode(2)), d3 = new NodeData(g.getNode(4));
        ArrayList<NodeData> al = new ArrayList<>();
        al.add(d1);
        al.add(d2);
        al.add(d3);
        String EXPECTED= "->0->1->2->3->4";
        String ACTUAL = stringPathBuilder(dwga.tsp(al));
        assertEquals(EXPECTED,ACTUAL);
    }

    /**
     * Bank of nodes;
     *
     * @param size
     */
    private void nodeCreator(int size) {
        nodes = new NodeData[size];
        for (int i = 0; i < size; i++) {
            nodes[i] = new classes.NodeData(i, new GeoLocation(_rand.nextDouble() + 35, _rand.nextDouble() + 35, 0.0));

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
            gr.addNode((NodeData) node);
        }
        while (gr.edgeSize() < numOfEdges) {
            int l = _rand.nextInt(numOfNodes);
            int r = _rand.nextInt(numOfNodes);
            double w = _rand.nextDouble() + 1;
            if (l != r && w > 0)
                gr.connect(l, r, w);
        }
        return gr;
    }

    private void graphCreator1(int numOfNodes, int numOfEdges) {
        nodeCreator(numOfNodes);
        for (NodeData node : this.nodes) {
            g.addNode(node);
        }
        while (g.edgeSize() < numOfEdges) {
            int l = _rand.nextInt(numOfNodes);
            int r = _rand.nextInt(numOfNodes);
            double w = _rand.nextDouble() + 1;
            if (l != r && w > 0)
                g.connect(l, r, w);
        }
    }

    private static void printPath(List<NodeData> lst) {
        for (NodeData nodeData : lst) {
            System.out.print("->" + nodeData.getKey());
        }
    }
    private static String stringPathBuilder(List<NodeData> lst) {
        if(lst!= null) {
            String st = "";
            for (NodeData nodeData : lst) {
                st += "->" + nodeData.getKey();
            }
            return st;
        }
        return null;
    }
}
