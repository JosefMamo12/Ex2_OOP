package Util;
/**
 * This class represents a simple world 2 frame conversion (both ways).
 * @author boaz.benmoshe
 *
 */

import api.GeoLocation;

public class Range2Range {

    private Range2D _world, _frame;

    public Range2Range(Range2D w, Range2D f) {
        _world = new Range2D(w);
        _frame = new Range2D(f);
    }

    public GeoLocation world2frame(GeoLocation p) {
        Point3D d = _world.getPortion(p);
        return _frame.fromPortion(d);
    }

    public Point3D frame2world(GeoLocation p) {
        Point3D d = _frame.getPortion(p);
        return _world.fromPortion(d);
    }

    public Range2D getWorld() {
        return _world;
    }

    public Range2D getFrame() {
        return _frame;
    }
}

