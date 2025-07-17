package br.com.mike.model.nn;

import br.com.mike.enuns.Axis;
import br.com.mike.enuns.TipoFuncoes;
import br.com.mike.neuronio.Perceptron;
import br.com.mike.util.Math;

import java.util.List;
import java.util.Random;

public class Linear {

    private final float[] Y;
    private final float[][] X;
    private float[][] pesosW1;
    private float[][] pesosW2;
    private float[][] f1;
    private float[][] f2;
    private final Perceptron[] neuroniosEscondidos;
    private final Perceptron[] neuroniosSaida;

    public Linear(float[][] x, float[] y) {
        X = x;
        Y = y;
        neuroniosEscondidos = new Perceptron[10];
        neuroniosSaida = new Perceptron[2];
        iniciarPesos(x[0].length, neuroniosEscondidos.length, neuroniosSaida.length);
        iniciarPerceptrons(new TipoFuncoes[]{TipoFuncoes.TANH, TipoFuncoes.SOFTMAX});
    }

    public Linear(float[][] x, float[] y, int neuroniosEscondidos, int neuroniosSaida) {
        X = x;
        Y = y;
        this.neuroniosEscondidos = new Perceptron[neuroniosEscondidos];
        this.neuroniosSaida = new Perceptron[neuroniosSaida];
        iniciarPesos(x[0].length, neuroniosEscondidos, neuroniosSaida);
        iniciarPerceptrons(new TipoFuncoes[]{TipoFuncoes.TANH, TipoFuncoes.SOFTMAX});
    }

    public float[][] forward(float[][] x) {
        f1 = Math.dot(x, pesosW1);
        setZ(f1, neuroniosEscondidos);
        f2 = Math.dot(f2, pesosW2);
        return Math.dividir(f2, Math.sum(f2, Axis.COL));
    }

    public float loss(float[][] softMax) {
        float predictions = 0;
        for (int i = 0; i < softMax.length; i++) {
            float prediction = softMax[i][(int) Y[i]];
            predictions -= Math.log(prediction);
        }
        return predictions / Y.length;
    }

    public float backpropagation(float[][] softMax) {
        float[] delta2 = new float[softMax.length];
        for (int i = 0; i < softMax.length; i++) {
            delta2[i] = -Math.max(1, Y[i]) * (1 - softMax[i][(int) Y[i]]);
        }
        float[][] dw2 = Math.dot(Math.transposta(f1), delta2);
        return pesosW1[(int) Y[0]][(int) Y[1]];
    }


    private void setZ(float[][] z, Perceptron[] neuronios) {
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[0].length; j++) {
                z[i][j] = neuronios[j].funcaoAtivacao(z[i][j]);
            }
        }
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
