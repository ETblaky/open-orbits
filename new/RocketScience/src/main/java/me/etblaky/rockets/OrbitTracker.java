package me.etblaky.rockets;

import me.etblaky.rockets.Utils.Constants;
import me.etblaky.rockets.Utils.Date;
import me.etblaky.rockets.Utils.Vector3;

public class OrbitTracker {

    private Orbit o;
    private double lat, lon, alt;

    public OrbitTracker(Orbit o, double lat, double lon, double alt) {
        this.o = o;
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }


    public Vector3 observer_geocentric(Date d){
        return lla2eci(lat, lon, alt, d);
    }

    private static Vector3 lla2eci(double lat, double lon, double alt, Date date) {

        double julian_date = greg2Julian(date);
        double mjd = julian_date - 2400000.5;
        double gmst = mjd2gmst(mjd);

        double right_ascension = lon + gmst;
        double d = Math.sqrt(1 - (2 * Constants.f - Math.pow(Constants.f, 2)) * Math.pow(Math.sin(Math.toRadians(lat)), 2));

        Vector3 eci = new Vector3();
        eci.x = (Constants.re / d + alt) * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(right_ascension));
        eci.y = (Constants.re / d + alt) * Math.cos(Math.toRadians(lat)) * Math.sin(Math.toRadians(right_ascension));
        eci.z = (Constants.re * Math.pow(1 - Constants.f, 2) / d + alt) * Math.sin(Math.toRadians(lat));

        return eci;
    }

    private static double mjd2gmst(double mjd) {
        double T = (mjd - 51544.5) / 36525.0;
        double gmst = ((280.46061837 + 360.98564736629 * (mjd - 51544.5)) + 0.000387933 * T * T - T * T * T / 38710000.0) % 360.0;
        if (gmst < 0) {
            gmst += 360.0;
        }
        return gmst;
    }

    private static double greg2Julian(Date gregorianCalendar) {
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

    public static double date2gmst(Date d){
        return mjd2gmst(greg2Julian(d));
    }

}
