package me.etblaky.rockets;

import me.etblaky.rockets.Utils.*;

import static java.lang.Math.*;

public class Orbit {

    //Classical Orbital Elements
    public double a, e, i, long_asc_node, arg_peri, mean_motion;

    //Orbital Elements Based on Time
    //r, v, true_anomaly, eccentric_anomaly, mean_anomaly
    public Date tle_epoch;
    public double tle_mean_anomaly;

    //Other Orbital Elements
    private double periapsis, apoapsis;
    private double b, c, p;
    private double h, E;
    private double period;

    public double size = 0.05;

    static Orbit fromTLE(TLE info) {
        return new Orbit(info.meanMotion, info.eccentricity, info.long_asc_node, info.arg_peri, info.inclination, info.meanAnomaly, info.epoch);
    }

    static Orbit[] fromTLEs(TLE... info) {
        Orbit[] os = new Orbit[info.length];
        for (int i = 0; i < info.length; i++) {
            os[i] = fromTLE(info[i]);
        }
        return os;
    }

    public Orbit(double mean_motion, double eccentricity, double long_asc_node, double arg_peri, double inclination, double tle_mean_anomaly, Date tle_epoch) {

        this.mean_motion = mean_motion;
        this.long_asc_node = long_asc_node;
        this.arg_peri = arg_peri;
        this.i = inclination;

        this.tle_mean_anomaly = tle_mean_anomaly;
        this.tle_epoch = tle_epoch;

        this.a = Math.pow(Constants.earthMi, 1.0 / 3.0) / Math.pow((2 * mean_motion * Math.PI) / 86400, 2.0 / 3.0);

        this.periapsis = a * (1 - eccentricity);
        this.apoapsis = a * (1 + eccentricity);

        this.b = sqrt(periapsis * apoapsis);
        this.e = (apoapsis - periapsis) / (apoapsis + periapsis);
        this.c = e * a;
        this.p = a * (1 - pow(e, 2));


        this.h = sqrt(p * Constants.earthMi);
        this.E = -Constants.earthMi / (2 * a);

        if (Constants.units[0] == Constants.Unit.SEC) {
            this.period = (2 * PI * sqrt(pow(a, 3) / Constants.earthMi));
        } else if (Constants.units[0] == Constants.Unit.MIN) {
            this.period = (2 * PI * sqrt(pow(a, 3) / Constants.earthMi)) / 60;
        } else if (Constants.units[0] == Constants.Unit.HOUR) {
            this.period = (2 * PI * sqrt(pow(a, 3) / Constants.earthMi)) / 3600;
        } else {
            this.period = (2 * PI * sqrt(pow(a, 3) / Constants.earthMi));
        }

        if (Debug.debug) debug();
    }

    private void debug() {
        Debug.log("Semi-Major Axis (a)", a);
        Debug.log("Eccentricity (e)", e);
        Debug.log("Inclination (i)", i);
        Debug.log("Longitude Of Ascending Node (Ω)", long_asc_node);
        Debug.log("Argument of Periapsis (ω)", arg_peri);
        Debug.log("Mean Motion (n)", mean_motion);
        Debug.space();
        Debug.log("Periapsis", periapsis);
        Debug.log("Apoapsis", apoapsis);
        Debug.space();
        Debug.log("Semi-Minor Axis (b)", b);
        Debug.log("Distance Between Centers (c)", c);
        Debug.log("Latus Rectum (p)", p);
        Debug.space();
        Debug.log("Specific Angular Momentum (h)", h);
        Debug.log("Specific Mechanical Energy (ε)", E);
        Debug.space();
        Debug.log("Period (t)", period);
        Debug.space();
    }


    private Vector3 r_perifocal(double r, double true_anomaly) {
        return new Vector3(r * cos(true_anomaly), r * sin(true_anomaly), 0);
    }

    private Vector3 r_geocentric(Vector3 r_perifocal) {
        return Util.pq2eci(this, r_perifocal);
    }

    public double r_scalar(double true_anomaly) {
        return p / (1 + e * cos(true_anomaly));
    }


    public Vector3 r_perifocal(Date d) {
        double mean = mean_anomaly(Date.offset_days(d, tle_epoch));
        double truea = true_anomaly(eccentric_anomaly(mean));
        return r_perifocal(r_scalar(truea), truea);
    }

    public Vector3 r_perifocal(double true_anomaly) {
        return r_perifocal(r_scalar(true_anomaly), true_anomaly);
    }


    public Vector3 r_geocentric(Date d) {
        double mean = mean_anomaly(Date.offset_days(d, tle_epoch));
        double truea = true_anomaly(eccentric_anomaly(mean));

        return r_geocentric(r_perifocal(r_scalar(truea), truea));
    }

    public Vector3 r_geocentric(double true_anomaly) {
        return r_geocentric(r_perifocal(r_scalar(true_anomaly), true_anomaly));
    }


    public double v_scalar(double true_anomaly) {
        return sqrt(Constants.earthMi * ((2 / r_scalar(true_anomaly)) - (1 / a)));
    }

    public Vector3 v_perifocal(double true_anomaly) {
        double part1 = sqrt(Constants.earthMi / p);
        return new Vector3(part1 * (-sin(true_anomaly)), part1 * (e + cos(true_anomaly)), 0);
    }

    private Vector3 v_geocentric(Vector3 v_perifocal) {
        return new Vector3(Matrix.perifocal2Geocentric(long_asc_node, arg_peri, i).multiply(new Matrix(v_perifocal)));
    }

    public Vector3 v_geocentric(double true_anomaly) {
        return new Vector3(Matrix.perifocal2Geocentric(long_asc_node, arg_peri, i).multiply(new Matrix(v_perifocal(true_anomaly))));
    }


    public double mean_anomaly(double time_days) {

        double angle_raw = (360 * mean_motion * time_days);

        if (angle_raw > 360) {
            angle_raw = angle_raw % 360;
        } else if (angle_raw < -360) {
            angle_raw = 360 + (angle_raw % 360);
        }
        double angle = angle_raw + Math.toDegrees(tle_mean_anomaly);
        if (angle > 360) {
            angle = angle % 360;
        }

        return toRadians(angle);
    }

    public double eccentric_anomaly(double meanAnomaly) {
        double tempResult = meanAnomaly;
        double numerator;

        for (int i = 0; i < 10; i++) {
            numerator = meanAnomaly + e * sin(tempResult) - e * tempResult * cos(tempResult);
            tempResult = numerator / (1 - e * cos(tempResult));
        }

        return tempResult;
    }

    public double true_anomaly(double eccentric_anomaly) {
        return 2 * atan(sqrt((1 + e) / (1 - e)) * tan(eccentric_anomaly / 2));
    }

}
