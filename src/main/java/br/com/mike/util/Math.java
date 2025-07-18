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
        return (double) java.lang.Math.log(x);
    }

    public static double max(double x, double y) {
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
}
