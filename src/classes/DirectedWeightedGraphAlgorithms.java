package src.classes;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;

import javax.swing.text.html.HTMLDocument;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DirectedWeightedGraphAlgorithms implements api.DirectedWeightedGraphAlgorithms {
    DirectedWeightedGraph g;
    int[] parent;
//    final int WHITE = 0, GREY = 1, BLACK = 2;

    @Override
    public void init(DirectedWeightedGraph g) {
        this.g = g;
        parent = new int[g.nodeSize()];

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
            copy.nodes.put(node.key, new NodeData(node));
        }
        Iterator<EdgeData> edgeIt = g.edgeIter();
        while (edgeIt.hasNext()) {
            EdgeData edge = edgeIt.next();
            copy.edges.add(new EdgeData(edge));
        }
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.graph.entrySet()) {
            Integer entryKey = entry.getKey();
            copy.graph.put(entryKey, new HashMap<>());
            for (Map.Entry<Integer, EdgeData> innerEntry : entry.getValue().entrySet()) {
                Integer innerKey = innerEntry.getKey();
                EdgeData innerEdge = innerEntry.getValue();
                copy.graph.get(entryKey).put(innerKey, new EdgeData(innerEdge));
            }
        }
        copy.mc = this.g.mc;
        return copy;
    }


    @Override
    public boolean isConnected() {
        if (g == null)
            return true;
        clean();
        boolean[] visited = new boolean[g.nodeSize()];
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : g.graph.entrySet()) {
            Integer entryKey = entry.getKey();
            DFS(entryKey, visited);
            for (boolean b : visited) {
                if (!b)
                    return false;

            }
            Arrays.fill(visited, false);
        }
        return true;
    }

    private void DFS(@NotNull Integer entryKey, boolean[] visited) {
        Stack<Integer> st = new Stack<>();
        visited[entryKey] = true;
        st.add(entryKey);
        while (!st.isEmpty()) {
            int node = st.pop();
            Iterator<EdgeData> itr = g.edgeIter(node);
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
        Arrays.fill(parent, 0);
//        Arrays.fill(colors, 0);
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public NodeData center() {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
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
            EdgeData ed = new EdgeData(src,dest,weight);
            this.g.connect(src,dest,weight);
        }
    }


    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }
}
