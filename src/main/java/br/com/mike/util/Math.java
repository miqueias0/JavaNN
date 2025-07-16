package br.com.mike.util;

public abstract class Math {

    public static double sqrt(double x) {
        return java.lang.Math.sqrt(x);
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

    public static float tanh(float x) {
        return (float) java.lang.Math.tanh(x);
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

    public static int[][] dividir(int[][] x, int divisor) {
        return MathMatriz.dividir(x, divisor);
    }

    public static float[][] dividir(float[][] x, float divisor) {
        return MathMatriz.dividir(x, divisor);
    }

    public static double[][] dividir(double[][] x, double divisor) {
        return MathMatriz.dividir(x, divisor);
    }
}
