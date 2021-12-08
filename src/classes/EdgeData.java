package classes;

import java.util.Objects;

public class EdgeData implements api.EdgeData {
    private int src;
    private int dest;
    private double weight;
    private int tag;
    private String info;

    public EdgeData(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.tag = 0;
        this.info = "";
    }

    public EdgeData(EdgeData other) {
        this.src = other.src;
        this.dest = other.dest;
        this.info = other.info;
        this.weight = other.weight;
        this.tag = other.tag;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.weight;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src && dest == edgeData.dest && tag == edgeData.tag && Objects.equals(info, edgeData.info);
    }

    @Override
    public String toString() {
        return "EdgeData{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight, tag, info);
    }
}
