package br.com.mike.model.nn;

import br.com.mike.enuns.TipoFuncoes;
import br.com.mike.neuronio.Perceptron;
import br.com.mike.util.Math;

import java.util.Arrays;
import java.util.Random;

public class Linear {

    private final float[] Y;
    private final float[][] X;
    private float[][] pesosW1;
    private float[][] pesosW2;
    private final Perceptron[] neuroniosEscondidos;
    private final Perceptron[] neuroniosSaida;

    public Linear(float[][] x, float[] y) {
        X = x;
        Y = y;
        neuroniosEscondidos = new Perceptron[10];
        neuroniosSaida = new Perceptron[2];
        iniciarPesos(x[0].length, neuroniosEscondidos.length, neuroniosSaida.length);
        iniciarPerceptrons(TipoFuncoes.TANH);
    }

    public Linear(float[][] x, float[] y, int neuroniosEscondidos, int neuroniosSaida) {
        X = x;
        Y = y;
        this.neuroniosEscondidos = new Perceptron[neuroniosEscondidos];
        this.neuroniosSaida = new Perceptron[neuroniosSaida];
        iniciarPesos(x[0].length, neuroniosEscondidos, neuroniosSaida);
        iniciarPerceptrons(TipoFuncoes.TANH);
    }

    public float[][] forward(float[][] x) {
        float[][] z1 = Math.dot(x, pesosW1);
        float[][] f1 = new float[z1.length][z1[0].length];
        for (int i = 0; i < neuroniosEscondidos.length; i++) {
            f1[i][0] = neuroniosEscondidos[i].funcaoAtivacao(z1[i][0]);
        }
        z1 = Math.dot(f1, pesosW2);
        f1 = new float[z1.length][z1[0].length];
        for (int i = 0; i < neuroniosSaida.length; i++) {
            f1[i][0] = neuroniosSaida[i].funcaoAtivacao(z1[i][0]);
        }
        z1 = Math.exp(f1);
        return z1;
    }


    private void iniciarPesos(int inputsLength, int neuroniosEscondidos, int neuroniosSaida) {
        pesosW1 = iniciarPesos(inputsLength, neuroniosEscondidos);
        pesosW2 = iniciarPesos(neuroniosEscondidos, neuroniosSaida);
    }

    private float[][] iniciarPesos(int x, int y) {
        float[][] pesos = new float[x][y];
        Random random = new Random();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                pesos[i][j] = (float) (random.nextFloat() / Math.sqrt(x));
            }
        }
        return pesos;
    }

    private void iniciarPerceptrons(TipoFuncoes tipoFuncoes) {
        for (Perceptron[] perceptrons : Arrays.asList(neuroniosEscondidos, neuroniosSaida)) {
            iniciarPerceptrons(perceptrons.length, tipoFuncoes, perceptrons);
        }
    }

    private void iniciarPerceptrons(int length, TipoFuncoes tipoFuncoes, Perceptron[] perceptrons) {
        for (int i = 0; i < length; i++) {
            perceptrons[i] = new Perceptron(tipoFuncoes);
        }
    }
}
