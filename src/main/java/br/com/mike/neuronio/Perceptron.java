package br.com.mike.neuronio;

import br.com.mike.enuns.Axis;
import br.com.mike.enuns.TipoFuncoes;
import br.com.mike.util.Math;

public class Perceptron {

    private final TipoFuncoes tipo;
    private float bias = 0;

    public Perceptron(TipoFuncoes tipo) {
        this.tipo = tipo;
    }

    public Perceptron(float bias) {
        this.bias = bias;
        tipo = TipoFuncoes.TANH;
    }

    public Perceptron(TipoFuncoes tipo, float bias) {
        this.tipo = tipo;
        this.bias = bias;
    }

    public float funcaoAtivacao(float x) {
        return switch (tipo) {
            case RELU -> 0.0F;
            case TANH -> Math.tanh(x + bias);
            case SOFTMAX -> 0.0F;
        };
    }

    public float[][] funcaoAtivacao(float[][] x) {
        switch (tipo) {
            case RELU:
                return new float[0][0];
            case TANH:
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < x[i].length; j++) {
                        x[i][j] = Math.tanh(x[i][j]);
                    }
                }
                return x;
            case SOFTMAX:
                x = Math.exp(x);
                return Math.dividir(x, Math.sum(x, Axis.COL));
            default:
                return null;
        }
    }

    public TipoFuncoes getTipo() {
        return tipo;
    }
}
