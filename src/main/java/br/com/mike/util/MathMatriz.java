package br.com.mike.util;

import br.com.mike.enuns.Axis;

import java.lang.Math;

abstract class MathMatriz {

    public static int[][] dot(int[][] x, int[][] y) {
        if (x == null || y == null || x.length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        verificarDimensoes(x[0].length, y.length);
        int[][] res = new int[x.length][y[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                int sum = 0;
                for (int k = 0; k < y.length; k++) {
                    sum += x[i][k] * y[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    public static float[][] dot(float[][] x, float[][] y) {
        if (x == null || y == null || x.length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        verificarDimensoes(x[0].length, y.length);
        float[][] res = new float[x.length][y[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                float sum = 0;
                for (int k = 0; k < y.length; k++) {
                    sum += x[i][k] * y[k][j];
                }
                res[i][j] = sum;
            }
        }

        return res;
    }

    public static double[][] dot(double[][] x, double[][] y) {
        if (x == null || y == null || x.length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        verificarDimensoes(x[0].length, y.length);
        double[][] res = new double[x.length][y[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                double sum = 0;
                for (int k = 0; k < y.length; k++) {
                    sum += x[i][k] * y[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    public static int[][] sum(int[][] x, int[][] y) {
        if (x == null || y == null || x.length == 0 || y.length == 0
                || x[0].length == 0 || y[0].length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        verificarDimensoes(x.length, y.length);
        verificarDimensoes(x[0].length, y[0].length);
        int[][] res = new int[x.length][y[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                res[i][j] = x[i][j] + y[i][j];
            }
        }
        return res;
    }

    public static float[][] sum(float[][] x, float[][] y) {
        if (x == null || y == null || x.length == 0 || y.length == 0
                || x[0].length == 0 || y[0].length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        verificarDimensoes(x.length, y.length);
        verificarDimensoes(x[0].length, y[0].length);
        float[][] res = new float[x.length][y[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = x[i][j] + y[i][j];
            }
        }
        return res;
    }

    public static float[][] sum(float[][] x, Axis axis) {
        if (x == null || x.length == 0 || x[0].length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        float[][] res = switch (axis) {
            case COL -> new float[x.length][1];
            case LIN -> new float[1][x[0].length];
        };
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                switch (axis) {
                    case COL -> res[i][0] += x[i][j];
                    case LIN -> res[0][j] += x[i][j];
                }
            }
        }
        return res;
    }

    public static double[][] sum(double[][] x, double[][] y) {
        if (x == null || y == null || x.length == 0 || y.length == 0
                || x[0].length == 0 || y[0].length == 0) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        verificarDimensoes(x.length, y.length);
        verificarDimensoes(x[0].length, y[0].length);
        double[][] res = new double[x.length][y[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = x[i][j] + y[i][j];
            }
        }
        return res;
    }

    public static int[][] exp(int[][] x) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        int[][] res = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = (int) Math.exp(x[i][j]);
            }
        }
        return res;
    }

    public static float[][] exp(float[][] x) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }

        float[][] res = new float[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = (float) Math.exp(x[i][j]);
            }
        }
        return res;
    }

    public static double[][] exp(double[][] x) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        double[][] res = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = Math.exp(x[i][j]);
            }
        }
        return res;
    }

    public static int[][] dividir(int[][] x, int[][] divisor) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        int[][] res = new int[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = x[i][j] / divisor[i][0];
            }
        }
        return res;
    }

    public static float[][] dividir(float[][] x, float[][] divisor) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }

        float[][] res = new float[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = x[i][j] / divisor[i][0];
            }
        }
        return res;
    }

    public static double[][] dividir(double[][] x, double[][] divisor) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        double[][] res = new double[x.length][x[0].length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[i][j] = x[i][j] / divisor[i][0];
            }
        }
        return res;
    }

    public static float[][] transposta(float[][] x) {
        if (x == null) {
            throw new java.lang.IllegalArgumentException("Dados de matrizes Inválidos");
        }
        float[][] res = new float[x[0].length][x.length];
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                res[j][i] = x[i][j];
            }
        }
        return res;
    }

    private static void verificarDimensoes(int x, int y) {
        if (x != y) {
            throw new ArithmeticException("Dados de matrizes Inválidos");
        }
    }
}
