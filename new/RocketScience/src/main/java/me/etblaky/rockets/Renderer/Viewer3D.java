package me.etblaky.rockets.Renderer;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;
import de.lessvoid.nifty.Nifty;
import me.etblaky.rockets.Main;
import me.etblaky.rockets.Orbit;
import me.etblaky.rockets.OrbitTracker;
import me.etblaky.rockets.Utils.Date;
import me.etblaky.rockets.Utils.Vector3;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Viewer3D extends SimpleApplication {

    private Orbit orbit;
    private Geometry sat;
    private Geometry earth;
    private Geometry obs;

    public Date date;
    public boolean shouldUpdateDate;
    public double velocity = 1;
    public int offsetTime = 0;

    public Nifty nifty;
    public GuiController guiController;

    public Viewer3D(Orbit o) {
        this.orbit = o;

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

        addOrbit();
        addSat();

        flyCam.setMoveSpeed(10f);
        flyCam.setDragToRotate(true);
        cam.setLocation(new Vector3f(5, 0, 0));
        cam.lookAt(earth.getLocalTranslation(), new Vector3f(0, 1f, 0));

        //date = new Date(2019, 1, 23, 0, 0, 0);
        date = new Date();
        shouldUpdateDate = false;
        addGUI();
    }

    private void addLightning() {
        DirectionalLight sun1 = new DirectionalLight();
        DirectionalLight sun2 = new DirectionalLight();

        sun1.setDirection(new Vector3f(5, 0, 0).normalizeLocal());
        sun1.setColor(ColorRGBA.White);

        sun2.setDirection(new Vector3f(-5, 0, 0).normalizeLocal());
        sun2.setColor(ColorRGBA.White);

        rootNode.addLight(sun1);
        rootNode.addLight(sun2);
    }

    private void addAxis() {
        Cylinder x_axis = new Cylinder(10, 10, 0.01f, 100f);
        Cylinder y_axis = new Cylinder(10, 10, 0.01f, 100f);
        Cylinder z_axis = new Cylinder(10, 10, 0.01f, 100f);

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
        x_axisGeo.setLocalTranslation(0, 0, 0);

        y_axisMat.setColor("Color", ColorRGBA.Blue);
        y_axisGeo.setMaterial(y_axisMat);
        y_axisGeo.setLocalRotation(new Quaternion().fromAngles((float) Math.toRadians(90), 0, 0));
        y_axisGeo.setLocalTranslation(0, 0, 0);

        z_axisMat.setColor("Color", ColorRGBA.Green);
        z_axisGeo.setMaterial(z_axisMat);
        z_axisGeo.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
        z_axisGeo.setLocalTranslation(0, 0, 0);

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
        xgeo.setLocalTranslation(2, 0, 0);

        ymat.setColor("Color", ColorRGBA.Green);
        ygeo.setMaterial(ymat);
        ygeo.setLocalRotation(new Quaternion().fromAngles((float) -Math.toRadians(90), 0, 0));
        ygeo.setLocalTranslation(0, 0, -2);

        zmat.setColor("Color", ColorRGBA.Blue);
        zgeo.setMaterial(zmat);
        zgeo.setLocalRotation(new Quaternion().fromAngles(0, 0, 0));
        zgeo.setLocalTranslation(0, 2, 0);

        rootNode.attachChild(xgeo);
        rootNode.attachChild(ygeo);
        rootNode.attachChild(zgeo);
    }

    private void addEarth() {
        Sphere earthMesh = new Sphere(32, 32, 1f);
        earth = new Geometry("Shiny rock", earthMesh);
        earthMesh.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(earthMesh);
        Material earthMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        earthMat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/earth.jpg"));
        earthMat.setTexture("NormalMap", assetManager.loadTexture("Textures/earth_normal.jpg"));
        earthMat.setBoolean("UseMaterialColors", true);
        earthMat.setColor("Diffuse", ColorRGBA.White);
        earthMat.setColor("Specular", ColorRGBA.Black);
        earthMat.setFloat("Shininess", 16f);
        earth.setMaterial(earthMat);
        earth.setLocalRotation(new Quaternion().fromAngles((float) Math.toRadians(-90), 0, 0));
        earth.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(earth);
    }

    private void addObserver() {
        Sphere obsMesh = new Sphere(32, 32, 0.05f);
        obs = new Geometry("Observer", obsMesh);
        Material obsMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        obsMat.setColor("Color", ColorRGBA.Yellow);
        obs.setMaterial(obsMat);
        rootNode.attachChild(obs);
    }

    private void addOrbit() {
        for (int i = 0; i < 720; i++) {
            Vector3 pos = orbit.r_geocentric(i / 2.0);
            Sphere dot = new Sphere(32, 32, 0.01f);
            Geometry dotGeo = new Geometry("OrbitCell", dot);
            Material dotMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            dotMat.setColor("Color", ColorRGBA.Gray);
            dotGeo.setMaterial(dotMat);
            dotGeo.setLocalTranslation((float) pos.x / 6300f, (float) pos.z / 6300f, (float) -pos.y / 6300f);
            rootNode.attachChild(dotGeo);
        }
    }

    private void addSat() {
        Sphere satMesh = new Sphere(32, 32, 0.05f);
        sat = new Geometry("Satellite", satMesh);
        Material satMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        satMat.setColor("Color", ColorRGBA.White);
        sat.setMaterial(satMat);
        sat.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(sat);
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
        Vector3 pos = orbit.r_geocentric(date);
        sat.setLocalTranslation((float) pos.x / 6300f, (float) pos.z / 6300f, (float) -pos.y / 6300f);

        Vector3 observer = new OrbitTracker(orbit, -22.856310, -47.057423, 0.660).observer_geocentric(date);
        obs.setLocalTranslation((float) observer.x / 6300f, (float) observer.z / 6300f, (float) -observer.y / 6300f);

        earth.setLocalRotation(new Quaternion().fromAngles((float) Math.toRadians(-90), (float) Math.toRadians(OrbitTracker.date2gmst(date)), 0));

        if (shouldUpdateDate) {
            offsetTime++;
            if (offsetTime * velocity % 60 == 0 && offsetTime != 0) {
                date.addSec(1);
            }
        }
        guiController.updateGuiDate();
        guiController.updateBtnTxt();
        Main.viewer.velocity = guiController.vel.getValue();

    }

}
