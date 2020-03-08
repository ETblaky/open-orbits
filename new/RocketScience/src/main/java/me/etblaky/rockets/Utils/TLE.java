package me.etblaky.rockets.Utils;

import java.io.Serializable;

public class TLE implements Serializable {

    public final int satelliteNumber;
    public final char classification;
    public final int launchYear;
    public final int launchNumber;
    public final String launchPiece;
    public final int ephemerisType;
    public final int elementNumber;
    public final int revolutionNumberAtEpoch;

    public final double eccentricity;
    public final double long_asc_node;
    public final double arg_peri;
    public final double inclination;
    public final double meanMotion;
    public final double meanAnomaly;
    public final Date epoch;

    public String line1;
    public String line2;

    public TLE(final String line1, final String line2) {

        satelliteNumber = parseInteger(line1, 2, 5);
        classification = line1.charAt(7);
        launchYear = parseYear(line1, 9);
        launchNumber = parseInteger(line1, 11, 3);
        launchPiece = line1.substring(14, 17).trim();
        ephemerisType = parseInteger(line1, 62, 1);
        elementNumber = parseInteger(line1, 64, 4);
        revolutionNumberAtEpoch = parseInteger(line2, 63, 5);


        eccentricity = Double.parseDouble("." + line2.substring(26, 33).replace(' ', '0'));
        long_asc_node = Math.toRadians(Double.parseDouble(line2.substring(17, 25).replace(' ', '0')));
        arg_peri = Math.toRadians(parseDouble(line2, 34, 8));
        inclination = Math.toRadians(parseDouble(line2, 8, 8));
        meanMotion = parseDouble(line2, 52, 11);
        meanAnomaly = Math.toRadians(parseDouble(line2, 43, 8));

        epoch = epochToDate(line1);

        this.line1 = line1;
        this.line2 = line2;

    }


    private double parseDouble(final String line, final int start, final int length) {
        final String field = line.substring(start, start + length).trim();
        return field.length() > 0 ? Double.parseDouble(field.replace(' ', '0')) : 0;
    }

    private int parseInteger(final String line, final int start, final int length) {
        final String field = line.substring(start, start + length).trim();
        return field.length() > 0 ? Integer.parseInt(field.replace(' ', '0')) : 0;
    }

    private int parseYear(final String line, final int start) {
        final int year = 2000 + parseInteger(line, start, 2);
        return (year > 2056) ? (year - 100) : year;
    }

    private Date epochToDate(String line) {
        final int year_temp = 2000 + parseInteger(line, 18, 2);
        int year = (year_temp > 2056) ? (year_temp - 100) : year_temp;
        double day_decimal = parseDouble(line, 20, 12);
        return new Date(year, day_decimal);
    }

    public String toString() {
        return line1 + "\n" + line2;
    }

}