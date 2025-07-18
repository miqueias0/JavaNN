package br.com.mike.model.nn;

import br.com.mike.enuns.Axis;
import br.com.mike.enuns.TipoFuncoes;
import br.com.mike.neuronio.Perceptron;
import br.com.mike.util.Math;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Linear {

    private final int[] Y;
    private final double[][] X;
    private double[][] pesosW1;
    private double[][] pesosW2;
    private double[][] f1;
    private double[][] z1;
    private final Perceptron[] neuroniosEscondidos;
    private final Perceptron[] neuroniosSaida;

    public Linear(double[][] x, int[] y) {
        X = x;
        Y = y;
        neuroniosEscondidos = new Perceptron[10];
        neuroniosSaida = new Perceptron[2];
        iniciarPesos(x[0].length, neuroniosEscondidos.length, neuroniosSaida.length);
        iniciarPerceptrons(new TipoFuncoes[]{TipoFuncoes.TANH, TipoFuncoes.EXP});
    }

    public Linear(double[][] x, int[] y, int neuroniosEscondidos, int neuroniosSaida) {
        X = x;
        Y = y;
        this.neuroniosEscondidos = new Perceptron[neuroniosEscondidos];
        this.neuroniosSaida = new Perceptron[neuroniosSaida];
        iniciarPesos(x[0].length, neuroniosEscondidos, neuroniosSaida);
        iniciarPerceptrons(new TipoFuncoes[]{TipoFuncoes.TANH, TipoFuncoes.EXP});
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
        return index == y ? 1 : index;
    }

    public void fit(int epochs, double learningRate) {
        for (int i = 1; i <= epochs; i++) {
            double[][] outputs = forward(X);
            double loss = loss(outputs);
            backpropagation(outputs, learningRate);
            int correct = 0;
            for (int j = 0; j < outputs.length; j++) {
                double[][] values = new double[1][outputs[0].length];
                values[0] = outputs[j];
                correct += predict(values, Y[j]);
            }
            double accuracy = (correct) / (Y.length);

            if (i % (10) == 0) {
                System.out.println("Epoch: " + i + "/ " + epochs + " accuracy: " + accuracy + " loss: " + loss);
            }
        }
    }

    public double[][] forward(double[][] x) {
        z1 = sumWithBias(Math.dot(x, pesosW1), neuroniosEscondidos);
        f1 = setF(z1, neuroniosEscondidos);
        double[][] f2 = sumWithBias(Math.dot(f1, pesosW2), neuroniosSaida);
        f2 = setF(f2, neuroniosSaida);
        return Math.dividir(f2, Math.sum(f2, Axis.COL));
    }

    public double loss(double[][] softMax) {
        double predictions = 0;
        for (int i = 0; i < softMax.length; i++) {
            double prediction = softMax[i][(int) Y[i]];
            predictions -= Math.log(prediction);
        }
        return predictions / Y.length;
    }

    public void backpropagation(double[][] softMax, double learningRate) {
        double[][] delta2 = Arrays.copyOf(softMax, softMax.length);
        for (int i = 0; i < softMax.length; i++) {
            int j = (int) Y[i];
            delta2[i][j] = -Math.max(1, j) * (1 - softMax[i][(int) j]);
        }
        for (int i = 0; i < z1.length; i++) {
            for (int j = 0; j < z1[0].length; j++) {
                z1[i][j] = 1 - Math.pow(Math.tanh(z1[i][j]), 2);
            }
        }
        double[][] b2 = Math.sum(delta2, Axis.LIN);
        double[][] delta1 = Math.multiplicar(Math.dot(delta2, Math.transposta(pesosW2)), z1);
        double[][] b1 = Math.sum(delta1, Axis.LIN);
        pesosW2 = Math.sum(pesosW2, setPesos(Math.dot(Math.transposta(f1), delta2), learningRate));
        pesosW1 = Math.sum(pesosW1, setPesos(Math.dot(Math.transposta(X), delta1), learningRate));
        setBias(b2, neuroniosSaida, learningRate);
        setBias(b1, neuroniosEscondidos, learningRate);
    }

    private double[][] setPesos(double[][] pesos, double learningRate) {
        for (int i = 0; i < pesos.length; i++) {
            for (int j = 0; j < pesos[0].length; j++) {
                pesos[i][j] = -(pesos[i][j] * learningRate);
            }
        }
        return pesos;
    }

    private void setBias(double[][] bias, Perceptron[] neuronios, double learningRate) {
        for (int i = 0; i < bias.length; i++) {
            for (int j = 0; j < bias[0].length; j++) {
                Perceptron perceptron = neuronios[j];
                perceptron.setBias(perceptron.getBias() + (-(bias[i][j] * learningRate)));
            }
        }
    }

    private double[][] sumWithBias(double[][] z, Perceptron[] neuronios) {
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                z[i][j] += neuronios[j].getBias();
            }
        }
        return z;
    }

    private double[][] setF(double[][] f, Perceptron[] neuronios) {
        double[][] res = new double[f.length][f[0].length];
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[0].length; j++) {
                res[i][j] = neuronios[j].funcaoAtivacao(f[i][j]);
            }
        }
        return res;
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
                pesos[i][j] = (double) (random.nextDouble() / Math.sqrt(x));
            }
        }
        return pesos;
    }

    private void iniciarPerceptrons(TipoFuncoes[] tipoFuncoes) {
        List<Perceptron[]> perceptrons = List.of(neuroniosEscondidos, neuroniosSaida);
        for (int i = 0; i < tipoFuncoes.length; i++) {
            iniciarPerceptrons(perceptrons.get(i).length, tipoFuncoes[i], perceptrons.get(i));
        }
    }

    private void iniciarPerceptrons(int length, TipoFuncoes tipoFuncoes, Perceptron[] perceptrons) {
        for (int i = 0; i < length; i++) {
            perceptrons[i] = new Perceptron(tipoFuncoes);
        }
    }
}
