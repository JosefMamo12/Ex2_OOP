package classes;

import api.GeoLocation;
import org.junit.jupiter.api.function.Executable;

import java.util.Objects;

public class NodeData implements src.api.NodeData, Comparable<NodeData> {
    private final int key;
    private GeoLocation location;
    private double weight;
    private String info;
    private int tag;
    private static int counter = 0;


    public NodeData(GeoLocation p){
        this.key = counter;
        this.setLocation(p);
        this.weight = Double.MAX_VALUE;
        this.info = "";
        counter++;
    }

    public NodeData(int key, GeoLocation location) {
        this.key = key;
        this.setLocation(location);
        this.weight = Double.MAX_VALUE;
        this.info = "";
        counter++;
    }

    public NodeData(NodeData copy) {
        this.key = copy.key;
        this.setLocation(copy.getLocation());
        this.weight = copy.weight;
        this.info = copy.info;
        this.tag = copy.tag;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key && Double.compare(nodeData.weight, weight) == 0 && tag == nodeData.tag && Objects.equals(getLocation(), nodeData.getLocation()) && Objects.equals(info, nodeData.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, getLocation(), weight, info, tag);
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public int compareTo(NodeData o) {
        if (this.weight > o.weight) return 1;
        else if (this.weight < o.weight) return -1;
        return 0;
    }

}
