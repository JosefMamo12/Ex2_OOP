package src.classes;

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
}
