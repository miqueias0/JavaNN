package br.com.mike.model.nn;

import br.com.mike.util.GeradorSubWord;
import br.com.mike.util.Math;
import br.com.mike.util.Vocabulario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FastText {

    private int embeddingDim;
    private int subWordSize;
    private int bucketSize;
    private GeradorSubWord gen;
    private double[][] inputEmbeddings;
    private double[][] outputEmbeddings;
    private Vocabulario vocab;
    private int contextSize;
    private int totalComparacoes = 0;
    private double lastPredition = 0;
    private int correct = 0;

    public FastText(int vocabSize, int embeddingDim, int bucketSize, int contextSize, GeradorSubWord gen, Vocabulario vocab) {
        this.embeddingDim = embeddingDim;
        this.bucketSize = bucketSize;
        this.gen = gen;
        inputEmbeddings = new double[bucketSize][embeddingDim];
        outputEmbeddings = new double[vocabSize][embeddingDim];
        this.vocab = vocab;
        this.contextSize = contextSize;
        inicializar();
    }

    private void inicializar() {
        Random rand = new Random();
        double range = 1.0 / embeddingDim;
        for (int i = 0; i < bucketSize; i++) {
            for (int j = 0; j < embeddingDim; j++) {
                inputEmbeddings[i][j] = (rand.nextDouble() * 2 * range) - range;
            }
        }
        for (int i = 0; i < outputEmbeddings.length; i++) {
            for (int j = 0; j < outputEmbeddings[0].length; j++) {
                outputEmbeddings[i][j] = (rand.nextDouble() * 2 * range) - range;
            }

        }
    }

    private double[] forward(String word) {
        double[] res = new double[embeddingDim];
        List<String> words = gen.process(word);
        for (String w : words) {
            int hash = hash(w);
            res = Math.sum(res, inputEmbeddings[hash]);
        }
        res = Math.escalar(res, 1D / words.size());
        return res;
    }

    public void fit(List<List<String>> words, int epochs, double learningRate) {
        for (int i = 1; i <= epochs; i++) {
            double lr = Math.max(0.0001, learningRate * (1.0 - ((double) (i - 1) / epochs)));
            double loss = 0;
            int processed = 0;
            correct = 0;
            totalComparacoes = 0;
            lastPredition = 0;
            long time = System.currentTimeMillis();
            for (List<String> w : words) {
                for (int j = 0; j < w.size(); j++) {
                    String word = w.get(j);
                    Integer wordIndex = vocab.getIndex(word);
                    if (wordIndex == null) continue;
                    for (int k = Math.max(0, j - contextSize); k < Math.min(w.size(), j + contextSize + 1); k++) {
                        if (k == j) continue;
                        String contextWord = w.get(k);
                        Integer contextIndex = vocab.getIndex(contextWord);
                        if (contextIndex == null) continue;
                        loss += backpropagation(word, wordIndex, contextIndex, lr);
                        processed += contextSize + 1;
                    }
                }
            }
//            if (i % 10 == 0) {
            System.out.println("Epoch: " + i + "/" + epochs + " Accuracy: " + ((double) correct / totalComparacoes) + " Loss: " + loss / processed + " time: " + (System.currentTimeMillis() - time) + "ms");
//            }
        }
    }

    private double loss(double prediction, int label) {
        double epsilon = 1e-10D; // A small value to prevent log(0)
        prediction = Math.max(epsilon, Math.min(1 - epsilon, prediction));

        if (label == 1) {
            return -Math.log(prediction);
        } else {
            return -Math.log(1 - prediction);
        }
    }

    private double backpropagation(String word, int wordIndex, int contextIndex, double learningRate) {
        double[] res = forward(word);
        double loss = grad(res, word, contextIndex, 1, learningRate);
        Random rand = new Random();
        for (int i = 0; i < contextSize; i++) {
            int negativaIndex = rand.nextInt(vocab.size());
            if (negativaIndex == contextIndex || Math.abs(negativaIndex - wordIndex) <= 1) {
                i--;
                continue;
            }
            loss += grad(res, word, negativaIndex, 0, learningRate);
        }
        return loss;
    }

    private double grad(double[] hidden, String word, int index, int label, double learningRate) {
        double score = Math.dot(hidden, outputEmbeddings[index]);
        double prediction = (1D / (1D + Math.exp(-score)));
        if (label == 1) {
            lastPredition = prediction;
        } else {
            correct += lastPredition > prediction ? 1 : 0;
            totalComparacoes++;
        }
        double g = learningRate * (label - prediction);
        double clipValue = 5.0;
        g = Math.max(-clipValue, Math.min(clipValue, g));
        double[] gradHidden = new double[embeddingDim];
        for (int i = 0; i < hidden.length; i++) {
            gradHidden[i] = g * outputEmbeddings[index][i];
        }
        for (int i = 0; i < outputEmbeddings[index].length; i++) {
            outputEmbeddings[index][i] += g * hidden[i];
        }
        List<String> words = gen.process(word);
        for (String w : words) {
            int hash = hash(w);
            for (int j = 0; j < hidden.length; j++) {
                inputEmbeddings[hash][j] += gradHidden[j];
            }
        }
        return loss(prediction, label);
    }

    public void testeSimilaridade(String... words) {
        String word = words[0];
        Map<String, Double> maisCompativeis = new HashMap<>();
        for (int i = 1; i < words.length; i++) {
            double similariedade = similaridade(word, words[i]);
            if (maisCompativeis.size() < 5) {
                maisCompativeis.put(words[i], similariedade);
            } else if (maisCompativeis.values().stream().anyMatch(v -> v < similariedade)) {
                for (Map.Entry<String, Double> entry : maisCompativeis.entrySet()) {
                    if (entry.getValue() < similariedade) {
                        maisCompativeis.remove(entry.getKey());
                        maisCompativeis.put(words[i], similariedade);
                        break;
                    }
                }
            }
        }
        for (Map.Entry<String, Double> entry : maisCompativeis.entrySet()) {
            System.out.println("Similaridade " + word + " e " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private double similaridade(String word1, String word2) {
        double[] fwdW1 = forward(word1);
        double[] fwdW2 = forward(word2);
        double similarity = 0;
        double norm1 = 0;
        double norm2 = 0;
        for (int i = 0; i < fwdW1.length; i++) {
            similarity += fwdW1[i] * fwdW2[i];
            norm1 += fwdW1[i] * fwdW1[i];
            norm2 += fwdW2[i] * fwdW2[i];
        }
        if (norm1 * norm2 == 0) return 0D;
        return similarity / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private double similaridade(double[] fwdW1, double[] fwdW2) {
        double similarity = 0;
        double norm1 = 0;
        double norm2 = 0;
        for (int i = 0; i < fwdW1.length; i++) {
            similarity += fwdW1[i] * fwdW2[i];
            norm1 += fwdW1[i] * fwdW1[i];
            norm2 += fwdW2[i] * fwdW2[i];
        }
        if (norm1 * norm2 == 0) return 0D;
        return similarity / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public void testeAnalogia(String palavraA, String palavraB, String palavraC) {
        double[] vecA = forward(palavraA);
        double[] vecB = forward(palavraB);
        double[] vecC = forward(palavraC);
        double[] resultadoAnalogia = Math.sum(Math.sub(vecB, vecA), vecC);
        String palavraMaisProxima = "";
        double maiorSimilaridade = -1.0;
        for (String palavraVocab : vocab.getWords()) {
            double similaridade = similaridade(resultadoAnalogia, forward(palavraVocab));
            if (similaridade > maiorSimilaridade) {
                maiorSimilaridade = similaridade;
                palavraMaisProxima = palavraVocab;
            }
        }

        System.out.println("Analogia: " + palavraA + " -> " + palavraB + " :: " + palavraC + " -> " + palavraMaisProxima);
    }

    public int hash(String word) {
        return (fnv1a_32(word) & 0x7fffffff) % bucketSize;
    }

    private int fnv1a_32(String text) {
        int hash = 0x811c9dc5;
        for (byte b : text.getBytes()) {
            hash ^= b;
            hash *= 16777619;
        }
        return hash;
    }
}
