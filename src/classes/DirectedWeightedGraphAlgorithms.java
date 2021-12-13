package classes;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Node;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class represnating a bunch of algorithms which need to work on
 * the DirectedWeightedGraph object.
 */
public class DirectedWeightedGraphAlgorithms implements api.DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph g;
    private HashMap<Integer, Integer> parent;
    final double inf = Double.MAX_VALUE;

    /**
     * Init a directedWeightedGraph object which this algortihm class  will work on.
     * @param g
     */
    @Override
    public void init(api.DirectedWeightedGraph g) {
        this.g = (DirectedWeightedGraph) g;
    }

    /**
     * Return the specific graph.
     * @return
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

    /**
     * Create a deep copy of the graph.
     * without the mc changes.
     * @return DirectedWeightedGraph
     */
    @Override
    public DirectedWeightedGraph copy() {
        if (g == null)
            return null;
        DirectedWeightedGraph copy = new DirectedWeightedGraph();
        Iterator<NodeData> nodeIt = g.nodeIter();
        while (nodeIt.hasNext()) {
            NodeData node = nodeIt.next();
            copy.addNode(node);
        }

        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.getGraph().entrySet()) {
            Integer entryKey = entry.getKey();
            for (Map.Entry<Integer, EdgeData> innerEntry : entry.getValue().entrySet()) {
                EdgeData e = innerEntry.getValue();
                copy.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return copy;
    }

    /**
     * Graph is called connected if and only if there is path between u and v which u and v are vertex in the graph.
     * Did it with the help of dfs algorithm which go over all the graph and see it there is a path between a source code
     * to all other nodes in the graph.
     * Also transposed the graph to save the amount of time by using the dfs algorithm to each node.
     * @return
     */
    @Override
    public boolean isConnected() {
        if (g == null)
            return true;

        HashMap<Integer, Boolean> visited = new HashMap<>();
        fillHashFalse(visited);
        int entryKey = g.getGraph().entrySet().iterator().next().getKey();
        DirectedWeightedGraph gTrans = graphTranspose();
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                DFS(this.g, entryKey, visited);
            }
            if (i == 1) {
                DFS(gTrans, entryKey, visited);
            }
            for (Boolean value : visited.values()) {
                if (value == false)
                    return false;
            }
            fillHashFalse(visited);
        }
        return true;
    }

    /**
     * Helper to dfs.
     * @param visited
     */
    private void fillHashFalse(HashMap<Integer, Boolean> visited) {
        Iterator<NodeData> nItr = g.nodeIter();
        while (nItr.hasNext()) {
            NodeData n = nItr.next();
            visited.put(n.getKey(), false);
        }
    }

    /**
     * Helper to is connected graph,
     * return new transposed graph,
     * by opposited all the edges direction.
     * @return
     */
    private DirectedWeightedGraph graphTranspose() {
        DirectedWeightedGraph ans = new DirectedWeightedGraph();
        Iterator<NodeData> nItr = g.nodeIter();
        while (nItr.hasNext()) {
            NodeData curr = nItr.next();
            ans.addNode(new NodeData(curr));
        }
        Iterator<EdgeData> eItr = g.edgeIter();
        while (eItr.hasNext()) {
            EdgeData e = eItr.next();
            ans.connect(e.getDest(), e.getSrc(), e.getWeight());
        }
        return ans;
    }

    /**
     * Helper to isConnected function.
     * getting a soruce node id and and iterate over all his neighbors nood add it to stack and after that
     * it iterating over the neighbors of the neighbor and reach to the all nodes that related to the source Tying
     * element.
     * @param gr
     * @param entryKey
     * @param visited
     */
    private void DFS(DirectedWeightedGraph gr, int entryKey, HashMap<Integer, Boolean> visited) {
        Stack<Integer> st = new Stack<>();
        visited.put(entryKey, true);
        st.add(entryKey);
        while (!st.isEmpty()) {
            int node = st.pop();
            Iterator<EdgeData> itr = gr.edgeIter(node);
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                if (!visited.get(e.getDest())) {
                    visited.put(e.getDest(), true);
                    st.add(e.getDest());
                }
            }
        }
    }

    /**
     * Clean method like cleaning buffer of the graph before we do a new algorithm.
     */
    public void clean() {
        parent = new HashMap<>();
        Iterator<NodeData> ndItr = g.nodeIter();
        while (ndItr.hasNext()) {
            NodeData curr = ndItr.next();
            parent.put(curr.getKey(), -1);
            curr.setWeight(inf);
            curr.setTag(0);
            curr.setInfo("");
        }
        Iterator<EdgeData> edgeDataIterator = g.edgeIter();
        while (edgeDataIterator.hasNext()) {
            EdgeData ed = edgeDataIterator.next();
            ed.setInfo("");
        }
    }

    /**
     * Known algorithm which given source node and it returning the
     * shortest path from the particular source node to all other nodes in the graph.
     * Time Complexity O(V + E*log*V)
     *
     * @param src
     */

    public void DIJKSTRA(int src) {
        clean();
        g.getNode(src).setWeight(0);
        PriorityQueue<NodeData> pq = new PriorityQueue<>();
        pq.add(g.getNode(src));
        while (!pq.isEmpty()) {
            NodeData currNode = pq.poll();
            Iterator<EdgeData> itr = g.edgeIter(currNode.getKey());
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                if (g.getNode(e.getDest()).getTag() == 0) {
                    NodeData neighbour = g.getNode(e.getDest());
                    double weight = currNode.getWeight() + e.getWeight();
                    if (weight < neighbour.getWeight()) {
                        neighbour.setWeight(weight);
                        parent.put(neighbour.getKey(), currNode.getKey());
                        pq.add(neighbour);
                    }
                }
            }
            currNode.setTag(1);
        }
    }

    /**
     * Return the distance between the source node to the dest node, by computing the weights thats exists on each edge
     * thats have been contained in the path.
     * Return -1 if there is no path.
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        clean();
        if (src != dest && g != null && g.getNodes().containsKey(src) && g.getNodes().containsKey(dest)) {
            DIJKSTRA(src);
            if (g.getNode(dest).getWeight() != inf)
                return g.getNode(dest).getWeight();
        }
        return -1;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        if (shortestPathDist(src, dest) != -1) {
            List<NodeData> lst = new LinkedList<>();
            while (src != dest) {
                g.getNode(dest).setInfo("Path");
                lst.add(g.getNode(dest));
                int tempDest = dest;
                int tempSrc = parent.get(dest);
                this.g.getEdge(tempSrc, tempDest).setInfo("ToPaint");
                dest = tempSrc;
            }
            g.getNode(src).setInfo("Path");
            lst.add(g.getNode(src));
            Collections.reverse(lst);
            return lst;
        }
        return null;
    }

    /**
     * Center of a graph is the set of all vertices of minimum eccentricity,
     * that is, the set of all vertices u where the greatest distance d(u,v) to other vertices v is minimal.
     * used the wikipedia - https://en.wikipedia.org/wiki/Graph_center
     * @return
     */
    @Override
    public NodeData center() {
        clean();
        NodeData centerNode = null;
        if (isConnected()) {
            double tempSum = inf;
            double sum;
            Iterator<NodeData> nItr = g.nodeIter();
            while (nItr.hasNext()) {
                NodeData bestNode = nItr.next();
                sum = Double.MIN_VALUE;
                DIJKSTRA(bestNode.getKey());
                Iterator<NodeData> nodeDataIterator = g.nodeIter();
                while (nodeDataIterator.hasNext()) {
                    NodeData curr = nodeDataIterator.next();
                    double sumCalc = curr.getWeight();
                    if (sumCalc > sum) {
                        sum = sumCalc;
                    }
                }
                if (sum < tempSum) {
                    tempSum = sum;
                    centerNode = bestNode;
                }
            }
            assert centerNode != null;
            centerNode.setInfo("Center");
            return centerNode;
        }
        return null;
    }

    /**
     * Only for reloading new G1,G2,G3.JSONS!!
     */
    public void whenLoadClicked() {
        g = null;
        g = new DirectedWeightedGraph();
    }

    /**
     * Save the DirectedWeightedGraph as json.
     * @param file - the file name (may include a relative path).
     * @return
     */

    @SuppressWarnings("unchecked")
    @Override
    public boolean save(String file) {
        JSONObject jo = new JSONObject();
        JSONArray jaE = new JSONArray();
        JSONArray jaN = new JSONArray();
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.getGraph().entrySet()) {
            Integer entryKey = entry.getKey();
            NodeData nd = g.getNode(entryKey);
            JSONObject node = new JSONObject();
            node.put("id", nd.getKey());
            node.put("pos", nd.getLocation().toString());
            jaN.add(node);
            for (Map.Entry<Integer, EdgeData> innerEntry : entry.getValue().entrySet()) {
                EdgeData innerEdge = innerEntry.getValue();
                JSONObject edge = new JSONObject();
                edge.put("src", innerEdge.getSrc());
                edge.put("w", innerEdge.getWeight());
                edge.put("dest", innerEdge.getDest());
                jaE.add(edge);

            }
        }
        jo.put("Edges", jaE);
        jo.put("Nodes", jaN);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jo.toJSONString());
        String prettyPrinting = gson.toJson(je);
        try (FileWriter f = new FileWriter(file)) {
            f.write(prettyPrinting);
            f.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Load a json file to DirectedWeightedGraph object.
     * @param file - file name of JSON file
     * @return
     */
    @Override
    public boolean load(String file) {
        JsonParser jsonParser = new JsonParser();
        try (FileReader reader = new FileReader(file)) {
            Object obj = jsonParser.parse(reader);
            JsonObject dwg = (JsonObject) obj;
            JsonArray jaE = (JsonArray) dwg.get("Edges");
            JsonArray jaN = (JsonArray) dwg.get("Nodes");
            createGraphFromJson(jaN, jaE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void createGraphFromJson(JsonArray jaN, JsonArray jaE) {
        for (int i = 0; i < jaN.size(); i++) {
            JsonObject node = (JsonObject) jaN.get(i);
            String pos = node.get("pos").getAsString();
            String[] st = pos.split(",");
            GeoLocation nodeLoc = new GeoLocation(Double.parseDouble(st[0]), Double.parseDouble(st[1]), Double.parseDouble(st[2]));
            int id = node.get("id").getAsInt();
            NodeData nd = new NodeData(id, nodeLoc);
            this.g.addNode(nd);
        }
        for (int i = 0; i < jaE.size(); i++) {
            JsonObject edge = (JsonObject) jaE.get(i);
            int src = edge.get("src").getAsInt();
            int dest = edge.get("dest").getAsInt();
            double weight = edge.get("w").getAsDouble();
            this.g.connect(src, dest, weight);
        }
    }

    /**
     * This particular tsp is kind of greedy algorithm, which chose the better node to start from.
     * In the algorithm I issolate the relative nodes and edges that related to cities.
     * I going over leftest city (x Location) and then looking for the promsing closest node
     * by using shortestPathDist function.
     * The algoritm works for both sides to see if it better start from rightest side.
     * @param cities
     * @return
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        clean();
        if (cities == null || cities.size() == 1)
            return null;
        if(cities.size() == 2) {
            return shortestPathDist(cities.get(0).getKey(),cities.get(1).getKey()) > shortestPathDist(cities.get(0).getKey(),cities.get(1).getKey()) ?
                    shortestPath(cities.get(0).getKey(),cities.get(1).getKey()) :  shortestPath(cities.get(1).getKey(),cities.get(0).getKey());
        }
        DirectedWeightedGraph graphForTsp = buildGraphOnlyForCities(cities);
        DirectedWeightedGraphAlgorithms tspAlgo = new DirectedWeightedGraphAlgorithms();
        tspAlgo.init(graphForTsp);
        if (!tspAlgo.isConnected()) return null;


        ArrayList<NodeData> ans1 = new ArrayList<>();
        ArrayList<NodeData> ans2 = new ArrayList<>();

        List<NodeData> tempList = new ArrayList<>();
        double shortestPathDist1 = 0;
        double shortestPathDist2 = 0;

        NodeData right = findTheRightestNode(cities);
        NodeData left = findTheLeftestNode(cities);
        NodeData n = null;
        for (int i = 0; i < 2; i++) {
            if (i == 0) n = right;
            else n = left;
            tempList.addAll(cities);
            while (tempList.size() > 1) {
                tempList.remove(n);
                NodeData next = searchForClosestNode(n.getKey(), tempList);
                if (next != null)
                    if (i == 0)
                        shortestPathDist1 += shortestPathDist(n.getKey(), next.getKey());
                    else
                        shortestPathDist2 += shortestPathDist(n.getKey(), next.getKey());
                List<NodeData> tempAns1 = shortestPath(n.getKey(), next.getKey());
                for (NodeData d : tempAns1) {
                    if (i == 0)
                        ans1.add(d);
                    else
                        ans2.add(d);
                }
                if (tempList.size() > 1)
                    if (i == 0)
                        ans1.remove(ans1.size() - 1);
                    else
                        ans2.remove(ans2.size() - 1);
                n = next;

            }
            tempList.clear();
        }
        return shortestPathDist1 > shortestPathDist2 ? ans2 : ans1;

    }

    /**
     * Private function which find the rightest node in given list by node geolocation(x value),
     * This function is tsp helper function.
     * @param cities
     * @return
     */
    private NodeData findTheRightestNode(List<NodeData> cities) {
        double rightestIndex = 0;
        NodeData leftestNode = null;
        for (NodeData n : cities) {
            double x = n.getLocation().x();
            if (x > rightestIndex) {
                leftestNode = n;
                rightestIndex = x;
            }
        }
        return leftestNode;
    }

    /**
     * Search the closest node to the relative src node by given templist of destination nodes candidates.
     * TSP helper function.
     * @param src
     * @param tempList
     * @return
     */
    private NodeData searchForClosestNode(int src, List<NodeData> tempList) {
        double minDist = Double.MAX_VALUE;
        NodeData closestNode = null;
        for (NodeData n : tempList) {
            double tempDist = shortestPathDist(src, n.getKey());
            if (minDist > tempDist) {
                closestNode = n;
                minDist = tempDist;
            }
        }
        return closestNode;
    }

    /**
     * private function which find the leftest node in a given list by node geolocation (x value).
     *  This function is tsp helper function.
     * @param cities
     * @return
     */

    private NodeData findTheLeftestNode(List<NodeData> cities) {
        double leftestIndex = Double.MAX_VALUE;
        NodeData leftestNode = null;
        for (NodeData n : cities) {
            double x = n.getLocation().x();
            if (x < leftestIndex) {
                leftestNode = n;
                leftestIndex = x;
            }
        }
        return leftestNode;
    }

    /**
     * Building sub graph that contains all the edges and all the nodes that
     * relative to specific cities list.
     *
     * @param cities
     * @return
     */
    private DirectedWeightedGraph buildGraphOnlyForCities(List<NodeData> cities) {
        DirectedWeightedGraph dwg = new DirectedWeightedGraph();
        ArrayList<NodeData> tempCities = new ArrayList<>();
        tempCities.addAll(cities);
        ListIterator<NodeData> iter = tempCities.listIterator();
        while (iter.hasNext()) {
            NodeData node = iter.next();
            if (!dwg.contains(node.getKey()))
                dwg.addNode(new NodeData(node));
            Iterator<EdgeData> eItr = this.g.edgeIter(this.getGraph().getNode(node.getKey()).getKey());
            while (eItr.hasNext()) {
                EdgeData ed = eItr.next();
                int neig = ed.getDest();
                if (!dwg.contains(neig)) {
                    NodeData neigNode = new NodeData(this.getGraph().getNode(neig));
                    dwg.addNode(neigNode);
                    if (!tempCities.contains(neigNode)) {
                        iter.add(neigNode);
                        iter.previous();
                    }

                }
                dwg.connect(ed.getSrc(), ed.getDest(), ed.getWeight());
            }
        }
        return dwg;
    }

}
