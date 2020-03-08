package me.etblaky.rockets;

import me.etblaky.rockets.Renderer.Viewer3D;
import me.etblaky.rockets.Utils.*;

public class Main {

    public static Viewer3D viewer;

    public static void main(String[] args) {
        Debug.debug = false;
        Constants.setUnits(Constants.Unit.SEC, Constants.Unit.KM);

        //Orbit[] iridium = Orbit.fromTLEs(TLE.fromUrl("http://www.celestrak.com/NORAD/elements/iridium-NEXT.txt"));

        Orbit iss = Orbit.fromTLE(new TLE(
                "1 25544U 98067A   20066.71819539  .00001314  00000-0  31803-4 0  9995",
                "2 25544  51.6430 135.4762 0005521 354.0023  45.0469 15.49224026216135"
        ));

        Orbit moon = new Orbit(
                1.304823042154573E+01 / 360.0,
                4.464147828371250E-02,
                Math.toRadians(1.127705850675393E+02),
                Math.toRadians(3.285241826688299E+01),
                Math.toRadians(5.003801665438951E+00),
                Math.toRadians(2.285824524617438E+02),
                new Date(2019, 4, 5, 0, 0, 0)
        );
        //moon.size = 1730000 / Constants.re;
        moon.size = 1;

        //OrbitTracker observer = new OrbitTracker(new Vector3(90, 0, 0));

        OrbitTracker observer = new OrbitTracker(new Vector3(45, 90, 0.660));

        viewer = new Viewer3D(observer, iss, moon);
        viewer.start();
    }

}
