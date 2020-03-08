package me.etblaky.rockets.Utils;

import static java.lang.Math.*;

public class Matrix {

    public double[][] matrix;

    public Matrix(double[][] m) {
        this.matrix = m;
    }

    public Matrix(Vector3 v) {
        matrix = new double[][]{
                {v.x},
                {v.y},
                {v.z}
        };
    }

/*    public Matrix multiply(Matrix m) {
        double[][] m1 = matrix;
        double[][] m2 = m.matrix;

        int a_rows = m1.length;
        int a_columns = m1.length;
        int b_rows = m2.length;
        int b_columns = m2.length;

        if (a_columns != b_rows) {
            System.out.println("Columns of m1 are different from Rows of m2.");
            return null;
        }

        double[][] m3 = new double[a_rows][b_columns];

        for (int i = 0; i < a_rows; i++) {
            for (int j = 0; j < b_columns; j++) {
                m3[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < a_rows; i++) { // aRow
            for (int j = 0; j < b_columns; j++) { // bColumn
                for (int k = 0; k < a_columns; k++) { // aColumn
                    m3[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return new Matrix(m3);
    }*/

    public Matrix multiply(Matrix m) {
        double[][] m1 = matrix;
        double[][] m2 = m.matrix;

        int m1_col = m1[0].length;
        int m2_row = m2.length;

        if (m1_col != m2_row) return null; // matrix multiplication is not possible

        int m3_row = m1.length;
        int m3_col = m2[0].length;

        double[][] m3 = new double[m3_row][m3_col];
        for (int i = 0; i < m3_row; i++) {
            for (int j = 0; j < m3_col; j++) {
                for (int k = 0; k < m1_col; k++) {
                    m3[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return new Matrix(m3);
    }

    public static Matrix perifocal2Geocentric(double long_asc_node, double arg_peri, double i) {

        double[][] m = new double[3][3];

        m[0][0] = cos(long_asc_node) * cos(arg_peri) - sin(long_asc_node) * sin(arg_peri) * cos(i);
        m[0][1] = -cos(long_asc_node) * sin(arg_peri) - sin(long_asc_node) * cos(arg_peri) * cos(i);
        m[0][2] = sin(long_asc_node) * sin(i);

        m[1][0] = sin(long_asc_node) * cos(arg_peri) + cos(long_asc_node) * sin(arg_peri) * cos(i);
        m[1][1] = -sin(long_asc_node) * sin(arg_peri) + cos(long_asc_node) * cos(arg_peri) * cos(i);
        m[1][2] = -cos(long_asc_node) * sin(i);

        m[2][0] = sin(arg_peri) * sin(i);
        m[2][1] = cos(arg_peri) * sin(i);
        m[2][2] = cos(i);

        return new Matrix(m);
    }

    @Override
    public String toString() {
        StringBuilder end = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                end.append(matrix[i][j]).append(" ");
            }
            end.append("\n");
        }
        return end.toString();
    }
}
