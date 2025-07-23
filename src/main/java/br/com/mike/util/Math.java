package br.com.mike.util;

import br.com.mike.enuns.Axis;

public abstract class Math {

    public static double sqrt(double x) {
        return java.lang.Math.sqrt(x);
    }

    public static double tanh(double x) {
        return (double) java.lang.Math.tanh(x);
    }

    public static double log(double x) {
        if (x < 0) {
            return 0;
        }
        return (double) java.lang.Math.log(x);
    }

    public static double max(double x, double y) {
        return java.lang.Math.max(x, y);
    }

    public static int abs(int x) {
        return java.lang.Math.abs(x);
    }

    public static int max(int x, int y) {
        return java.lang.Math.max(x, y);
    }

    public static double min(double x, double y) {
        return java.lang.Math.min(x, y);
    }

    public static double exp(double x) {
        return (double) java.lang.Math.exp(x);
    }

    public static double pow(double x, double y) {
        return (double) java.lang.Math.pow(x, y);
    }

    public static int[][] dot(int[][] x, int[][] y) {
        return MathMatriz.dot(x, y);
    }

    public static float[][] dot(float[][] x, float[][] y) {
        return MathMatriz.dot(x, y);
    }

    public static double[][] dot(double[][] x, double[][] y) {
        return MathMatriz.dot(x, y);
    }

    public static int[][] sum(int[][] x, int[][] y) {
        return MathMatriz.sum(x, y);
    }

    public static float[][] sum(float[][] x, float[][] y) {
        return MathMatriz.sum(x, y);
    }

    public static double[][] sum(double[][] x, double[][] y) {
        return MathMatriz.sum(x, y);
    }

    public static double[][] sum(double[][] x, Axis axis) {
        return MathMatriz.sum(x, axis);
    }

    public static int[][] exp(int[][] x) {
        return MathMatriz.exp(x);
    }

    public static float[][] exp(float[][] x) {
        return MathMatriz.exp(x);
    }

    public static double[][] exp(double[][] x) {
        return MathMatriz.exp(x);
    }

    public static int[][] dividir(int[][] x, int[][] divisor) {
        return MathMatriz.dividir(x, divisor);
    }

    public static float[][] dividir(float[][] x, float[][] divisor) {
        return MathMatriz.dividir(x, divisor);
    }

    public static double[][] dividir(double[][] x, double[][] divisor) {
        return MathMatriz.dividir(x, divisor);
    }

    public static double[][] transposta(double[][] x) {
        return MathMatriz.transposta(x);
    }

    public static double[][] multiplicar(double[][] x, double[][] y) {
        return MathMatriz.multiplicar(x, y);
    }

    public static double dot(double[] x, double[] y) {
        return MathArray.dot(x, y);
    }

    public static double[] sum(double[] x, double[] y) {
        return MathArray.sum(x, y);
    }

    public static double[] escalar(double[] x, double y) {
        return MathArray.escalar(x, y);
    }

    public static double[] exp(double[] x) {
        return MathArray.exp(x);
    }

    public static double[] dividir(double[] x, double divisor) {
        return MathArray.dividir(x, divisor);
    }

    public static double[] sub(double[] x, double y) {
        return MathArray.sub(x, y);
    }

    public static double[] sub(double[] x, double[] y) {
        return MathArray.sub(x, y);
    }

    public static double max(double[] x) {
        return MathArray.max(x);
    }

    public static double sum(double[] x) {
        return MathArray.sum(x);
    }

    public static double[] softmax(double[] x) {
        double[] res = exp(sub(x, max(x)));
        return dividir(res, sum(res));
    }

    public static double[] log(double[] x) {
        return MathArray.log(x);
    }

    public static double[] multiplicar(double[] x, double[] y) {
        return MathArray.multiplicar(x, y);
    }
}
