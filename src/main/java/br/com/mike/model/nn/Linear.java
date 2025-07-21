package br.com.mike.model.nn;

import br.com.mike.enuns.Axis;
import br.com.mike.util.Math;

import java.util.Random;

public class Linear {

    private double[][] pesosW1;
    private double[][] pesosW2;
    private double[][] f1;
    private double[][] z1;
    private double[] b1;
    private double[] b2;
    private int neuroniosEscondidos;
    private int neuroniosSaida;

    public Linear() {
        neuroniosSaida = 2;
        neuroniosEscondidos = 10;
        b1 = new double[neuroniosEscondidos];
        b2 = new double[neuroniosSaida];
    }

    public Linear(int neuroniosEscondidos, int neuroniosSaida) {
        this.neuroniosEscondidos = neuroniosEscondidos;
        this.neuroniosSaida = neuroniosSaida;
        b1 = new double[neuroniosEscondidos];
        b2 = new double[neuroniosSaida];
    }

    public int predict(double[][] X) {
        double[][] outputs = forward(X);
        int index = 0;
        for (int j = 0; j < outputs.length; j++) {
            double maxValue = Double.MIN_VALUE;
            for (int k = 0; k < outputs[0].length; k++) {
                if (outputs[j][k] > maxValue) {
                    maxValue = outputs[j][k];
                    index = k;
                }
            }
        }
        return index;
    }

    public int predict(double[][] X, int y) {
        int index = 0;
        for (int j = 0; j < X.length; j++) {
            double maxValue = Double.MIN_VALUE;
            for (int k = 0; k < X[0].length; k++) {
                if (X[j][k] > maxValue) {
                    maxValue = X[j][k];
                    index = k;
                }
            }
        }
        return index == y ? 1 : 0;
    }

    public void fit(final double[][] X, final int[] Y, int epochs, double learningRate) {
        int corretos = 0;
        int tamanho = 0;
        double losss = 0;
        int cont = 0;
        int div = 1;
        iniciarPesos(X[0].length, neuroniosEscondidos, neuroniosSaida);
        for (int i = 1; i <= epochs; i++, cont++, div = 2) {
            double[][] outputs = forward(X);
            int correct = 0;
            for (int j = 0; j < outputs.length; j++) {
                double[][] values = new double[1][outputs[0].length];
                values[0] = outputs[j];
                correct += predict(values, Y[j]);
            }
            double loss = loss(outputs, Y);
            backpropagation(outputs, learningRate, X, Y);
            corretos = (correct + corretos) / div;
            tamanho = (tamanho + Y.length) / div;
            losss = (losss + loss) / div;
            if (i % (epochs / 10) == 0) {
                double accuracy = (double) (corretos) / (tamanho);
                System.out.println("Epoch: " + i + "/ " + epochs + " accuracy: " + accuracy + " loss: " + losss);
                corretos = 0;
                tamanho = 0;
                losss = 0;
                cont = 0;
            }
        }
    }

    public double[][] forward(double[][] x) {
        z1 = sumWithBias(Math.dot(x, pesosW1), b1);
        f1 = new double[z1.length][z1[0].length];
        for (int i = 0; i < z1.length; i++) {
            for (int j = 0; j < z1[0].length; j++) {
                f1[i][j] = Math.tanh(z1[i][j]);
            }
        }
        double[][] f2 = Math.exp(sumWithBias(Math.dot(f1, pesosW2), b2));
        return Math.dividir(f2, Math.sum(f2, Axis.COL));
    }

    public double loss(double[][] softMax, final int[] Y) {
        double predictions = 0;
        for (int i = 0; i < softMax.length; i++) {
            double prediction = softMax[i][Y[i]];
            predictions -= Math.log(prediction);
        }
        return predictions / Y.length;
    }

    public void backpropagation(double[][] softMax, double learningRate, final double[][] X, final int[] Y) {
        for (int i = 0; i < softMax.length; i++) {
            int j = Y[i];
            softMax[i][j] = -Math.max(1, j) * ((double) 1 - softMax[i][j]);
        }
        for (int i = 0; i < z1.length; i++) {
            for (int j = 0; j < z1[0].length; j++) {
                z1[i][j] = 1 - Math.pow(Math.tanh(z1[i][j]), 2);
            }
        }
        double[][] delta1 = Math.multiplicar(Math.dot(softMax, Math.transposta(pesosW2)), z1);
        pesosW2 = Math.sum(pesosW2, setPesos(Math.dot(Math.transposta(f1), softMax), learningRate));
        pesosW1 = Math.sum(pesosW1, setPesos(Math.dot(Math.transposta(X), delta1), learningRate));
        setBias(Math.sum(softMax, Axis.LIN), this.b2, learningRate);
        setBias(Math.sum(delta1, Axis.LIN), this.b1, learningRate);
    }

    private double[][] setPesos(double[][] pesos, double learningRate) {
        for (int i = 0; i < pesos.length; i++) {
            for (int j = 0; j < pesos[0].length; j++) {
                pesos[i][j] = -(pesos[i][j] * learningRate);
            }
        }
        return pesos;
    }

    private void setBias(double[][] bias, double[] b, double learningRate) {
        for (int i = 0; i < bias.length; i++) {
            for (int j = 0; j < bias[0].length; j++) {
                b[j] += (-(bias[i][j] * learningRate));
            }
        }
    }

    private double[][] sumWithBias(double[][] z, double[] bias) {
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                z[i][j] += bias[j];
            }
        }
        return z;
    }


    private void iniciarPesos(int inputsLength, int neuroniosEscondidos, int neuroniosSaida) {
        pesosW1 = iniciarPesos(inputsLength, neuroniosEscondidos);
        pesosW2 = iniciarPesos(neuroniosEscondidos, neuroniosSaida);
    }

    private double[][] iniciarPesos(int x, int y) {
        double[][] pesos = new double[x][y];
        Random random = new Random();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                pesos[i][j] = (random.nextDouble() / Math.sqrt(x));
            }
        }
        return pesos;
    }
}
