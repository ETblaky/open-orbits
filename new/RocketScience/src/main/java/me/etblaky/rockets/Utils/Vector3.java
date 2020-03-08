package me.etblaky.rockets.Utils;

public class Vector3 {

    public double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(double scalar, double angle) {
        this.x = scalar * Math.cos(Math.toRadians(angle));
        this.y = scalar * Math.sin(Math.toRadians(angle));
        this.z = 0;
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3(Matrix m) {
        this.x = m.matrix[0][0];
        this.y = m.matrix[1][0];
        this.z = m.matrix[2][0];
    }

    public Vector3 multiply(double d) {
        return new Vector3(x * d, y * d, z * d);
    }

    public Vector3 cross(Vector3 v2) {
        Vector3 newV = new Vector3();

        newV.x = y * v2.z - z * v2.y;
        newV.y = v2.x * z - v2.z * x;
        newV.z = x * v2.y - y * v2.x;

        return newV;
    }

    public double scalar() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public static double distance(Vector3 v1, Vector3 v2) {
        return Math.sqrt(Math.pow(v1.x - v2.x, 2) + Math.pow(v1.y - v2.y, 2) + Math.pow(v1.z - v2.z, 2));
    }

    @Override
    public String toString() {
        return "x:" + String.format("%f", x) + " y:" + String.format("%f", y) + " z:" + String.format("%f", z);
    }
}
