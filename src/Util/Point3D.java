package Util;

import api.GeoLocation;

import java.io.Serializable;

public class Point3D implements api.GeoLocation, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Simple set of constants - should be defined in a different class (say class Constants).
     */
    public static final double EPS1 = 0.001, EPS2 = Math.pow(EPS1, 2), EPS = EPS2;
    /**
     * This field represents the origin point:[0,0,0]
     */
    public static final Point3D ORIGIN = new Point3D(0, 0, 0);
    private double _x, _y, _z;

    public Point3D(double x, double y, double z) {
        _x = x;
        _y = y;
        _z = z;
    }


    @Override
    public double x() {
        return _x;
    }

    @Override
    public double y() {
        return _y;
    }

    @Override
    public double z() {
        return _z;
    }


    public String toString() {
        return _x + "," + _y + "," + _z;
    }

    @Override
    public double distance(GeoLocation p2) {
        double dx = this.x() - p2.x();
        double dy = this.y() - p2.y();
        double dz = this.z() - p2.z();
        double t = (dx * dx + dy * dy + dz * dz);
        return Math.sqrt(t);
    }

    public String toString(boolean all) {
        if (all) return "[" + _x + "," + _y + "," + _z + "]";
        else return "[" + (int) _x + "," + (int) _y + "," + (int) _z + "]";
    }
}
