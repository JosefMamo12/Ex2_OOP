package src.classes;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
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
    public void init(DirectedWeightedGraph g) {
        this.g = g;

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
            copy.nodes.put(node.getKey(), new NodeData(node));
        }
        Iterator<EdgeData> edgeIt = g.edgeIter();
        while (edgeIt.hasNext()) {
            EdgeData edge = edgeIt.next();
            copy.edges.add(new EdgeData(edge));
        }

        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.graph.entrySet()) {
            Integer entryKey = entry.getKey();
            copy.edgeIn.put(entryKey,new HashSet<>());
            copy.graph.put(entryKey, new HashMap<>());
            for (Map.Entry<Integer, EdgeData> innerEntry : entry.getValue().entrySet()) {
                Integer innerKey = innerEntry.getKey();
                EdgeData innerEdge = innerEntry.getValue();
                copy.graph.get(entryKey).put(innerKey, new EdgeData(innerEdge));
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

    private void DFS(@NotNull DirectedWeightedGraph gr, Integer entryKey, boolean[] visited) {
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

    private void clean() {
        Arrays.fill(parent, -1);
        Iterator<NodeData> ndItr = g.nodeIter();
        while (ndItr.hasNext()) {
            NodeData curr = ndItr.next();
            curr.setWeight(inf);
            curr.setTag(0);
        }
    }

    public void DIJKSTRA(int src) {
        parent = new int[g.nodeSize()];
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
                lst.add(g.getNode(dest));
                dest = parent[dest];
            }
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
            double[] distance = new double[g.nodeSize()];
            double sum = 0;
            for (int i = 0; i < g.nodeSize(); i++) {
                sum = 0;
                DIJKSTRA(i);
                Iterator<NodeData> nodeDataIterator = g.nodeIter();
                while (nodeDataIterator.hasNext()) {
                    NodeData curr = nodeDataIterator.next();
                    sum += curr.getWeight();
                }
                distance[i] = sum;
            }
            sum = inf;
            for (int i = 0; i < distance.length; i++) {
                if (distance[i] < sum) {
                    sum = distance[i];
                    centerNode = g.getNode(i);
                }
            }
            return centerNode;
        }
        return null;
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
        System.out.println("In load");
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
            EdgeData ed = new EdgeData(src, dest, weight);
            this.g.connect(src, dest, weight);
        }
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }
}
