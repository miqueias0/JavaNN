package br.com.mike.util;

abstract class MathArray {

    public static double[] sum(double[] x, double[] y) {
        double[] sum = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            sum[i] = x[i] + y[i];
        }
        return sum;
    }

    public static double sum(double[] x) {
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i];
        }
        return sum;
    }

    public static double dot(double[] x, double[] y) {
        double dot = 0;
        for (int i = 0; i < x.length; i++) {
            dot += x[i] * y[i];
        }
        return dot;
    }

    public static double[] multiplicar(double[] x, double[] y) {
        double[] dot = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            dot[i] = x[i] * y[i];
        }
        return dot;
    }

    public static double[] escalar(double[] x, double escalar) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = x[i] * escalar;
        }
        return res;
    }

    public static double[] exp(double[] x) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = Math.exp(x[i]);
        }
        return res;
    }

    public static double[] dividir(double[] x, double y) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = x[i] / y;
        }
        return res;
    }

    public static double[] sub(double[] x, double y) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = x[i] - y;
        }
        return res;
    }

    public static double[] sub(double[] x, double[] y) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = x[i] - y[i];
        }
        return res;
    }

    public static double max(double[] x) {
        double max = x[0];
        for (int i = 1; i < x.length; i++) {
            if (max < x[i]) {
                max = x[i];
            }
        }
        return max;
    }

    public static double[] log(double[] x) {
        double[] res = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            res[i] = Math.log(x[i]);
        }
        return res;
    }

}
