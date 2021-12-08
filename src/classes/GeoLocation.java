package classes;

import java.util.Objects;

public class GeoLocation implements api.GeoLocation {
    private double x;
    private double y;
    private double z;

    public GeoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeoLocation that = (GeoLocation) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(api.GeoLocation g) {
        return Math.sqrt(Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2));
    }
}
