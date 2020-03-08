package me.etblaky.rockets;

import me.etblaky.rockets.Renderer.Viewer3D;
import me.etblaky.rockets.Utils.*;

public class Main {

    public static Viewer3D viewer;

    public static void main(String[] args) {
        Debug.debug = true;
        Constants.setUnits(Constants.Unit.SEC, Constants.Unit.KM);

        TLE info = new TLE(
                "1 25544U 98067A   19024.69130787  .00000289  00000-0  11946-4 0  9991",
                "2 25544  51.6429 354.9058 0004630 308.2748  50.7575 15.53168972152956"
        );
        Orbit iss = Orbit.fromTLE(info);

/*        TLE info2 = new TLE(
                "1 41328U 16007A   19024.56274192  .00000061  00000-0  00000+0 0  9990",
                "2 41328  54.8256 196.6842 0026129 213.7140 146.0997  2.00554590 21722"
        );
        Orbit gps = Orbit.fromTLE(info2);*/

        viewer = new Viewer3D(iss);
        viewer.start();
    }

}
