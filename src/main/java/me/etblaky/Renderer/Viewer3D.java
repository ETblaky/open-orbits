package me.etblaky.Renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Curve;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;
import de.lessvoid.nifty.Nifty;
import me.etblaky.Orbit;
import me.etblaky.OrbitTracker;
import me.etblaky.Utils.Constants;
import me.etblaky.Utils.Date;
import me.etblaky.Utils.Util;
import me.etblaky.Utils.Vector3;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Viewer3D extends SimpleApplication {

    public Orbit[] orbits;
    private OrbitTracker observer;
    private Geometry[] sats;
    private Geometry earth;
    private Geometry obsGeo;

    public Date date;
    public boolean shouldUpdateDate;
    public double velocity = 1;
    public int offsetTime = 0;

    public Nifty nifty;
    public GuiController guiController;

    public Viewer3D(OrbitTracker observer, Orbit... o) {
        this.orbits = o;
        this.observer = observer;
        this.sats = new Geometry[orbits.length];

        setShowSettings(false);
        AppSettings newSetting = new AppSettings(true);
        newSetting.setFrameRate(60);
        newSetting.setResolution(1280, 720);
        setSettings(newSetting);
        setDisplayFps(false);
        setDisplayStatView(false);
        Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
    }

    @Override
    public void simpleInitApp() {
        addLightning();
        addAxis();
        addPositiveDir();

        addEarth();
        addObserver();

        addOrbits();
        addSats();

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
        cam.setLocation(new Vector3f(5, 0, 0));
        cam.lookAt(earth.getLocalTranslation(), new Vector3f(0, 1f, 0));

        date = new Date();
        shouldUpdateDate = false;
        addGUI();

    }

    private void addLightning() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.95f));
        rootNode.addLight(al);
    }

    private void addAxis() {
        Cylinder x_axis = new Cylinder(10, 10, 0.01f, 3f);
        Cylinder y_axis = new Cylinder(10, 10, 0.01f, 3f);
        Cylinder z_axis = new Cylinder(10, 10, 0.01f, 3f);

        Geometry x_axisGeo = new Geometry("X Axis", x_axis);
        Geometry y_axisGeo = new Geometry("Y Axis", y_axis);
        Geometry z_axisGeo = new Geometry("Z Axis", z_axis);

        Material x_axisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material y_axisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material z_axisMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        TangentBinormalGenerator.generate(x_axisGeo);
        TangentBinormalGenerator.generate(y_axisGeo);
        TangentBinormalGenerator.generate(z_axisGeo);

        x_axisMat.setColor("Color", ColorRGBA.Red);
        x_axisGeo.setMaterial(x_axisMat);
        x_axisGeo.setLocalRotation(new Quaternion().fromAngles(0, (float) Math.toRadians(90), 0));
        x_axisGeo.setLocalTranslation(1.5f, 0, 0);

        y_axisMat.setColor("Color", ColorRGBA.Blue);
        y_axisGeo.setMaterial(y_axisMat);
        y_axisGeo.setLocalRotation(new Quaternion().fromAngles((float) Math.toRadians(90), 0, 0));
        y_axisGeo.setLocalTranslation(0, 1.5f, 0);

        z_axisMat.setColor("Color", ColorRGBA.Green);
        z_axisGeo.setMaterial(z_axisMat);
        z_axisGeo.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
        z_axisGeo.setLocalTranslation(0, 0, -1.5f);

        rootNode.attachChild(x_axisGeo);
        rootNode.attachChild(y_axisGeo);
        rootNode.attachChild(z_axisGeo);
    }

    private void addPositiveDir() {
        Dome x = new Dome(Vector3f.ZERO, 2, 32, 0.1f, false);
        Dome y = new Dome(Vector3f.ZERO, 2, 32, 0.1f, false);
        Dome z = new Dome(Vector3f.ZERO, 2, 32, 0.1f, false);

        Geometry xgeo = new Geometry("X Positive Direction", x);
        Geometry ygeo = new Geometry("Y Positive Direction", y);
        Geometry zgeo = new Geometry("Z Positive Direction", z);

        Material xmat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material ymat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material zmat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        xmat.setColor("Color", ColorRGBA.Red);
        xgeo.setMaterial(xmat);
        xgeo.setLocalRotation(new Quaternion().fromAngles(0, 0, (float) -Math.toRadians(90)));
        xgeo.setLocalTranslation(3, 0, 0);

        ymat.setColor("Color", ColorRGBA.Green);
        ygeo.setMaterial(ymat);
        ygeo.setLocalRotation(new Quaternion().fromAngles((float) -Math.toRadians(90), 0, 0));
        ygeo.setLocalTranslation(0, 0, -3);

        zmat.setColor("Color", ColorRGBA.Blue);
        zgeo.setMaterial(zmat);
        zgeo.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
        zgeo.setLocalTranslation(0, 3, 0);

        rootNode.attachChild(xgeo);
        rootNode.attachChild(ygeo);
        rootNode.attachChild(zgeo);
    }

    private void addEarth() {
        Sphere earthMesh = new Sphere(32, 32, 1f);
        earth = new Geometry("Earth", earthMesh);
        earthMesh.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(earthMesh);

        Material earthMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        earthMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/earth.jpg"));
        earthMat.setColor("Diffuse", ColorRGBA.White);
        earth.setMaterial(earthMat);

        earth.setLocalRotation(new Quaternion().fromAngles((float) Math.toRadians(-90), 0, 0));
        earth.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(earth);
    }

    private void addObserver() {
        Sphere obsMesh = new Sphere(32, 32, 0.05f);
        obsGeo = new Geometry("Observer", obsMesh);
        Material obsMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        obsMat.setColor("Color", ColorRGBA.Yellow);
        obsGeo.setMaterial(obsMat);
        rootNode.attachChild(obsGeo);
    }

    private void addOrbits() {
        for (Orbit o : orbits) {
            Vector3f[] points = new Vector3f[45];
            for (int i = 0; i < 45; i++) {
                Vector3 v = o.r_geocentric(Math.toRadians(i * 360.0 / 44));
                points[i] = new Vector3f((float) (v.x / Constants.re), (float) (v.z / Constants.re), (float) (-v.y / Constants.re));
            }
            Curve curve = new Curve(points, 1);
            Geometry curveGeo = new Geometry("Orbit " + o.e, curve);
            Material curveMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            curveMat.setColor("Color", ColorRGBA.White);
            curveGeo.setMaterial(curveMat);
            curveGeo.setLocalTranslation(0, 0, 0);
            rootNode.attachChild(curveGeo);
        }
    }

    private void addSats() {
        for (int i = 0; i < orbits.length; i++) {
            Sphere satMesh = new Sphere(32, 32, (float) orbits[i].size);
            sats[i] = new Geometry("Satellite", satMesh);
            Material satMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            satMat.setColor("Color", ColorRGBA.White);
            sats[i].setMaterial(satMat);
            sats[i].setLocalTranslation(0, 0, 0);
            rootNode.attachChild(sats[i]);
        }
    }

    private void addGUI() {
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiController = new GuiController();
        nifty.fromXml("GUI/Screen.xml", "start", guiController);
        guiViewPort.addProcessor(niftyDisplay);
    }

    @Override
    public void simpleUpdate(float tpf) {
        Vector3 obsV3 = observer.observer_geocentric(date);
        obsGeo.setLocalTranslation((float) (obsV3.x / Constants.re), (float) (obsV3.z / Constants.re), (float) (-obsV3.y / Constants.re));

        for (int i = 0; i < orbits.length; i++) {
            Vector3 pos = orbits[i].r_geocentric(date);
            //System.out.println(pos);
            sats[i].setLocalTranslation((float) (pos.x / Constants.re), (float) (pos.z / Constants.re), (float) (-pos.y / Constants.re));

            //Debug.log("el", Util.calcEl(obsV3, pos));
        }

        earth.setLocalRotation(new Quaternion().fromAngles((float) Math.toRadians(-90), (float) Math.toRadians(Util.gms_time(date)), 0));

        if (offsetTime * velocity % 60 == 0 && offsetTime != 0) {
            date.addSec(1);
            guiController.updateGuiDate();
        }
        if (shouldUpdateDate) {
            offsetTime++;
        }

    }

}
