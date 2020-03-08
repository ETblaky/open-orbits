package me.etblaky.rockets.Utils;

import me.etblaky.rockets.Orbit;

public class Util {

    public static Vector3 lla2eci(Vector3 lla, Date date) {

        double julian_date = gregToJulian(date);
        double mjd = julian_date - 2400000.5;
        double gmst = gmst(mjd);
        double right_ascension = lla.y + gmst;

        double flattering = Math.sqrt(1 - (2 * Constants.f - Math.pow(Constants.f, 2)) * Math.pow(Math.sin(Math.toRadians(lla.x)), 2));
        double radius = Constants.re / flattering;

        Vector3 eci = new Vector3();
        eci.x = (radius + lla.z) * Math.cos(Math.toRadians(lla.x)) * Math.cos(Math.toRadians(right_ascension));
        eci.y = (radius + lla.z) * Math.cos(Math.toRadians(lla.x)) * Math.sin(Math.toRadians(right_ascension));
        eci.z = (radius * Math.pow(1 - Constants.f, 2) + lla.z) * Math.sin(Math.toRadians(lla.x));

        return eci;
    }

    public static Vector3 pq2eci(Orbit o, Vector3 perifocal) {
        return new Vector3(Matrix.perifocal2Geocentric(o.long_asc_node, o.arg_peri, o.i).multiply(new Matrix(perifocal)));
    }

    public static double calcEl(Vector3 obs, Vector3 sat) {
        //Debug.log("obs pos", obs.scalar());
        //Debug.log("sat pos", sat.scalar());
        //Debug.log("diff", sat.minus(obs).toString());

/*        Debug.space();
        Debug.space();
        double angle = Math.acos(sat.dot(obs) / (sat.scalar() * obs.scalar()));
        Debug.log("angle", Math.toDegrees(angle));
        Debug.space();
        Debug.log("theta sat", Math.toDegrees(Math.acos(sat.z / sat.scalar())));
        Debug.log("phi sat", Math.toDegrees(Math.acos(sat.x / Math.sqrt(Math.pow(sat.x, 2) + Math.pow(sat.y, 2)))));
        Debug.space();
        Debug.log("theta obs", Math.toDegrees(Math.acos(obs.z / obs.scalar())));
        Debug.log("phi obs", Math.toDegrees(Math.acos(obs.x / Math.sqrt(Math.pow(obs.x, 2) + Math.pow(obs.y, 2)))));*/

        double satEl = Math.toDegrees(Math.acos(sat.z / sat.scalar()));
        return satEl /*+ 22.856310*/;
    }


    public static double gmst(double mjd) {
        double T = (mjd - 51544.5) / 36525.0;
        double gmst = ((280.46061837 + 360.98564736629 * (mjd - 51544.5)) + 0.000387933 * T * T - T * T * T / 38710000.0) % 360.0;
        if (gmst < 0) {
            gmst += 360.0;
        }

        return gmst;
    }

    public static double gregToJulian(Date gregorianCalendar) {
        int month = gregorianCalendar.month;
        int year = gregorianCalendar.year;
        int day = gregorianCalendar.day;
        int hour = gregorianCalendar.hour;
        int minute = gregorianCalendar.minute;
        int seconds = gregorianCalendar.second;


        double leapYear = Double.NaN;
        Date gregStartDate = new Date(1583, 9, 16);

        if (gregorianCalendar.after(gregStartDate)) {
            leapYear = 2 - (Math.round(year / 100.0)) + Math.round((Math.round(year / 100.0)) / 4.0);
        }

        Date gregBeginDate = new Date(1583, 9, 4);

        if ((gregorianCalendar.before(gregBeginDate)) || (gregorianCalendar.equals(gregBeginDate))) {
            leapYear = 0;
        }

        double fracSecs = (double) ((hour * 3600) + (minute * 60) + seconds) / 86400;

        long c = (long) (365.25 * (year + 4716));
        long d = (long) (30.6001 * (month + 1));

        return day + c + d + fracSecs + leapYear - 1524.5;
    }

    public static double gms_time(Date d) {
        return gmst(gregToJulian(d));
    }

}
