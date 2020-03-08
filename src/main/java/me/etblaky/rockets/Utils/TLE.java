package me.etblaky.rockets.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public TLE(String line1, String line2) {

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

    public static TLE[] fromUrl(String link) {
        TLE[] data;
        List<String> rawData = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader((new URL(link)).openStream()));
            String line;
            while ((line = in.readLine()) != null) { rawData.add(line); }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        data = new TLE[rawData.size() / 3];

        for (int i = 0; i < rawData.size() / 3; i++) {
            data[i] = new TLE(rawData.get(3 * i + 1), rawData.get(3 * i + 2));
        }

        return data;
    }


    private double parseDouble(String line, int start, int length) {
        final String field = line.substring(start, start + length).trim();
        return field.length() > 0 ? Double.parseDouble(field.replace(' ', '0')) : 0;
    }

    private int parseInteger(String line, int start, int length) {
        final String field = line.substring(start, start + length).trim();
        return field.length() > 0 ? Integer.parseInt(field.replace(' ', '0')) : 0;
    }

    private int parseYear(String line, int start) {
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