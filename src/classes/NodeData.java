package src.classes;

import api.GeoLocation;

import java.util.Objects;

public class NodeData implements src.api.NodeData {
    int key;
    GeoLocation location;
    double weight;
    String info;
    int tag;

    public NodeData(int key, GeoLocation location) {
        this.key = key;
        this.location = location;
    }
    public NodeData(NodeData copy){
        this.key = copy.key;
        this.location = copy.location;
        this.weight = copy.weight;
        this.info = copy.info;
        this.tag = copy.tag;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        return key == nodeData.key && Double.compare(nodeData.weight, weight) == 0 && tag == nodeData.tag && Objects.equals(location, nodeData.location) && Objects.equals(info, nodeData.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, location, weight, info, tag);
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
}
