package me.etblaky.rockets;

import me.etblaky.rockets.Utils.Date;
import me.etblaky.rockets.Utils.Util;
import me.etblaky.rockets.Utils.Vector3;

public class OrbitTracker {

    private Vector3 lla;

    public OrbitTracker(Vector3 lla) {
        this.lla = lla;
    }

    public Vector3 observer_geocentric(Date d) {
        return Util.lla2eci(lla, d);
    }

}
