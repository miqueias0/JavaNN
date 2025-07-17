package br.com.mike;

import br.com.mike.model.nn.Linear;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        float[][] x = new float[1000000][2];
        float[] y = new float[x.length];
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("text (1).txt")),
                StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            int cont = 0;
            for (int i = 0; i < x.length && line != null; i++, cont++, line = reader.readLine()) {
                String[] split = line.split(" ");
                for (int j = 0; j < x[0].length; j++) {
                    x[i][j] = Float.parseFloat(split[j]);
                }
                y[i] = Float.parseFloat(split[x[0].length]);
            }
            System.out.println(cont);
        } catch (Exception e) {
        }
        long startTime = System.currentTimeMillis();
        Linear linear = new Linear(x, y,1024,2);
//        System.out.println("loss: " + linear.loss(linear.forward(x)));
        System.out.println("backpropagation: " + linear.backpropagation(linear.forward(x)));
        System.out.println("time: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}