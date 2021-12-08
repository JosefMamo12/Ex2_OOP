package classes;

import com.google.gson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DirectedWeightedGraphAlgorithms implements api.DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph g;
    private int[] parent;
    final double inf = Double.MAX_VALUE;



    @Override
    public void init(api.DirectedWeightedGraph g) {
        this.g = (DirectedWeightedGraph) g;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.g;
    }

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

        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.graph.entrySet()) {
            Integer entryKey = entry.getKey();
            for (Map.Entry<Integer, EdgeData> innerEntry : entry.getValue().entrySet()) {
                EdgeData e = innerEntry.getValue();
                copy.connect(e.getSrc(), e.getDest(), e.getWeight());
            }
        }
        return copy;
    }


    @Override
    public boolean isConnected() {
        if (g == null)
            return true;

        boolean[] visited = new boolean[g.nodeSize()];
        int entryKey = g.getNode(0).getKey();
        DirectedWeightedGraph gTrans = graphTranspose();
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                DFS(this.g, entryKey, visited);
            }
            if (i == 1) {
                DFS(gTrans, entryKey, visited);
            }
            for (boolean b : visited) {
                if (!b)
                    return false;
            }
            Arrays.fill(visited, false);
        }
        return true;
    }

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
     * DFS Algorithm for returning if the graph is fully Connected
     *
     * @param gr
     * @param entryKey
     * @param visited
     */
    private void DFS(DirectedWeightedGraph gr, Integer entryKey, boolean[] visited) {
        Stack<Integer> st = new Stack<>();
        visited[entryKey] = true;
        st.add(entryKey);
        while (!st.isEmpty()) {
            int node = st.pop();
            Iterator<EdgeData> itr = gr.edgeIter(node);
            while (itr.hasNext()) {
                EdgeData e = itr.next();
                if (!visited[e.getDest()]) {
                    visited[e.getDest()] = true;
                    st.add(e.getDest());
                }
            }
        }
    }

    /**
     * Clean method like cleaning buffer of the graph before we do a new algorithm.
     */
    public void clean() {
        parent = new int[g.nodeSize()];
        Arrays.fill(parent, -1);
        Iterator<NodeData> ndItr = g.nodeIter();
        while (ndItr.hasNext()) {
            NodeData curr = ndItr.next();
            curr.setWeight(inf);
            curr.setTag(0);
            curr.setInfo("");
        }
        Iterator<EdgeData> edgeDataIterator = g.edgeIter();
        while(edgeDataIterator.hasNext()){
            edgeDataIterator.next().setInfo("");
        }
    }

    /**
     * Known algorithm which given source node and it returning the
     * shortest path from the particular source node to all other nodes in the graph.
     * Time Complexity O(V + E*log*V)
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
                        parent[neighbour.getKey()] = currNode.getKey();
                        pq.add(neighbour);
                    }
                }
            }
            currNode.setTag(1);
        }
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        clean();
        if (src != dest && g != null && g.nodes.containsKey(src) && g.nodes.containsKey(dest)) {
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
                int tempSrc = parent[dest];
                this.g.getEdge(tempSrc,tempDest).setInfo("ToPaint");
                dest = parent[dest];
            }
            g.getNode(src).setInfo("Path");
            lst.add(g.getNode(src));
            Collections.reverse(lst);
            return lst;
        }
        return null;
    }

    @Override
    public NodeData center() {
        NodeData centerNode = null;
        if (isConnected()) {
            double tempSum = inf;
            double sum;
            for (int i = 0; i < g.nodeSize(); i++) {
                sum = Double.MIN_VALUE;
                DIJKSTRA(i);
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
                    centerNode = g.getNode(i);
                }
            }
            assert centerNode != null;
            centerNode.setInfo("Center");
            return centerNode;
        }
        return null;
    }
    public void whenLoadClicked(){
            g = null;
            g = new DirectedWeightedGraph();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean save(String file) {
        JSONObject jo = new JSONObject();
        JSONArray jaE = new JSONArray();
        JSONArray jaN = new JSONArray();
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.graph.entrySet()) {
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

    //1 > 4 >5 >3 >9 >1
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        ArrayList<NodeData> path = new ArrayList<>();
        for (int i = 0; i < cities.size() - 1; i++) {
            path.addAll(shortestPath(cities.get(i).getKey(), cities.get(i + 1).getKey()));
        }
        return path;
    }

}
