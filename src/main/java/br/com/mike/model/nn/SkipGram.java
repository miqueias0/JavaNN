package br.com.mike.model.nn;

import br.com.mike.Main;
import br.com.mike.tokenizer.ITokenizer;
import br.com.mike.tokenizer.tokenizers.TokenizerStandard;
import br.com.mike.tokenizer.tokenizers.Tuple;
import br.com.mike.util.Math;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SkipGram {

    private final ITokenizer tokenizer;
    private Map<String, Integer> vocab = new HashMap<>() {
        {
            put("<pad>", 0);
        }
    };
    private Map<Integer, String> words = new HashMap<>() {
        {
            put(0, "<pad>");
        }
    };
    private int embeddingDim;
    private double[][] embeddings;
    private double[] W;

    public SkipGram(int embeddingDim) {
        this.tokenizer = new TokenizerStandard();
        tokenizer.load("merge1");
        for (String arq : List.of("train (0).txt")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(arq)),
                    StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    createVocab(line);
                }
            } catch (Exception e) {
            }
        }
        this.embeddingDim = embeddingDim;
        W = iniciarPesos(embeddingDim);
        embeddings = createEmbeddings();
    }

    public void createVocab(String word) {
        String[] tokens = word.toLowerCase(Locale.ROOT).split(((TokenizerStandard) tokenizer).getPATTERN());
        for (int i = 0, j = words.size(); i < tokens.length; i++) {
            String token = tokens[i];
            if (vocab.containsKey(token)) {
                continue;
            }
            vocab.put(token, j);
            words.put(j++, token);
        }
    }

    private double[][] createEmbeddings() {
        double[][] embeddings = new double[vocab.size()][embeddingDim];
        int[][] tokens = tokenizer.encode(words.values().parallelStream().collect(Collectors.joining(" ")), true, embeddingDim);
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < embeddingDim; j++) {
                embeddings[i][j] = tokens[i][j] == Integer.MAX_VALUE ? 0 : tokens[i][j];
            }
        }
        return embeddings;
    }

    private double[] iniciarPesos(int x) {
        double[] pesos = new double[x];
        Random random = new Random();
        for (int i = 0; i < x; i++) {
            pesos[i] = (random.nextDouble() / Math.sqrt(x));
        }
        return pesos;
    }

    public void embadding(String input, int embaddingSize) {
        double[] vector = oneHot(input.toLowerCase(Locale.ROOT), embaddingSize);
        double[] matrix = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            matrix[i] = vector[i] * W[i];
        }
    }

    private double[] oneHot(String word, int vocabSize) {
        double[] oneHotVector = new double[vocabSize];
        int categoryIdx = vocab.get(word);
//        oneHotVector[categoryIdx] = 1.0;
        return embeddings[categoryIdx];
    }

    private List<Tuple<Integer, Integer>> nGrams(int[] wordVector, int contextSize) {
        List<Tuple<Integer, Integer>> nGrams = new ArrayList<>();
        for (int i = 0; i < wordVector.length; i++) {
            for (int j = Math.max(0, i - contextSize); j <= i + contextSize && j < wordVector.length; j++) {
                if (j == i) {
                    continue;
                }
                nGrams.add(new Tuple<>(wordVector[i], wordVector[j]));
            }
        }
        return nGrams;
    }
}
