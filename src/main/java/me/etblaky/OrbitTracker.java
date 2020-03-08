package me.etblaky;

import me.etblaky.Utils.Util;
import me.etblaky.Utils.Vector3;
import me.etblaky.Utils.Date;

public class OrbitTracker {

    private Vector3 lla;

    public OrbitTracker(Vector3 lla) {
        this.lla = lla;
    }

    public Vector3 observer_geocentric(Date d) {
        return Util.lla2eci(lla, d);
    }

}
