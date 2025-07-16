package br.com.mike.neuronio;

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
        };
    }
}
