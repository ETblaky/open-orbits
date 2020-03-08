package me.etblaky.Utils;

public class Constants {

    public enum Unit {
        SEC,
        MIN,
        HOUR,
        TU,

        M,
        KM,
        FEET,
        DU
    }

    public static double G = 6.67408 * Math.pow(10, -11); // Gravitational Constant
    public static double earthMi; // Gravitational Parameter
    public static double f = 1 / 298.26; // Earth Flattering Factor
    public static double re; // Equatorial Radius

    public static Unit[] units;

    public static void setUnits(Unit time, Unit space) {
        units = new Unit[]{time, space};

        if (space == Unit.KM) {
            re = 6378.135;
        } else if (space == Unit.M) {
            re = 6378135.0;
        }

        if (time == Unit.SEC) {
            if (space == Unit.KM) {
                earthMi = 3.986004418 * Math.pow(10, 5);
            } else if (space == Unit.M) {
                earthMi = 3.986004418 * Math.pow(10, 14);
            } else if (space == Unit.FEET) {
                earthMi = 1.407654 * Math.pow(10, 16);
            }
        } else if (time == Unit.MIN) {
            if (space == Unit.KM) {
                earthMi = 3.986004418 * Math.pow(10, 5) * 3600;
            } else if (space == Unit.M) {
                earthMi = 3.986004418 * Math.pow(10, 14) * 3600;
            } else if (space == Unit.FEET) {
                earthMi = 1.407654 * Math.pow(10, 16) * 3600;
            }
        } else if (time == Unit.HOUR) {
            if (space == Unit.KM) {
                earthMi = 3.986004418 * Math.pow(10, 5) * 12960000;
            } else if (space == Unit.M) {
                earthMi = 3.986004418 * Math.pow(10, 14) * 12960000;
            } else if (space == Unit.FEET) {
                earthMi = 1.407654 * Math.pow(10, 16) * 12960000;
            }
        } else if (time == Unit.TU) {
            if (space == Unit.DU) {
                earthMi = 1;
            }
        }


    }

}
