package br.com.mike;

import br.com.mike.model.nn.Linear;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        double[][] x = new double[1000][2];
        int[] y = new int[x.length];
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("text (2).txt")),
                StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            int cont = 0;
            for (int i = 0; i < x.length && line != null; i++, cont++, line = reader.readLine()) {
                String[] split = line.split(" ");
                for (int j = 0; j < x[0].length; j++) {
                    x[i][j] = Double.parseDouble(split[j]);
                }
                y[i] = Integer.parseInt(split[x[0].length].split("\\.")[0]);
            }
            System.out.println(cont);
        } catch (Exception e) {
        }
        long startTime = System.currentTimeMillis();
        Linear linear = new Linear(512, 3);
        linear.fit(x, y, 10000, 0.000001);
        double[][] X = new double[1000000 - x.length][2];
        y = new int[X.length];
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("text (2).txt")),
                StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            for (int i = 0; i < x.length && line != null; i++, line = reader.readLine()) {

            }
            int cont = 0;
            for (int i = 0; i < X.length && line != null; i++, cont++, line = reader.readLine()) {
                String[] split = line.split(" ");
                for (int j = 0; j < X[0].length; j++) {
                    X[i][j] = Double.parseDouble(split[j]);
                }
                y[i] = Integer.parseInt(split[X[0].length].split("\\.")[0]);
            }
            System.out.println(cont);
        } catch (Exception e) {
        }
        int correct = 0;
        for (int i = 0; i < X.length; i++) {
            double[][] values = new double[1][X[0].length];
            values[0] = X[i];
            correct += linear.predict(values) == y[i] ? 1 : 0;
        }
        System.out.println("correct: " + correct + " / " + X.length + ", percent: " + ((double) correct * 100 / X.length));
        System.out.println("time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}