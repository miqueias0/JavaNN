package br.com.mike;

import br.com.mike.model.nn.Linear;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        float[][] x = new float[10][2];
        float[] y = new float[10];
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                x[i][j] = r.nextFloat();
            }
            y[i] = r.nextFloat();
        }
        Linear linear = new Linear(x, y);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                x[i][j] = r.nextFloat();
            }
        }
        linear.forward(x);
    }
}