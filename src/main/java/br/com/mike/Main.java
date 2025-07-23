package br.com.mike;

import br.com.mike.model.nn.FastText;
import br.com.mike.model.nn.Linear;
import br.com.mike.util.GeradorSubWord;
import br.com.mike.util.Math;
import br.com.mike.util.Vocabulario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<List<String>> words = read("train (0).txt", "train (1).txt", "train (2).txt", "train (3).txt", "train (4).txt", "train (5).txt");
        Vocabulario vocab = new Vocabulario();
        vocab.buildVocabulario(words, 5);
        words = read("train (0).txt");
        GeradorSubWord geradorSubWord = new GeradorSubWord(3);
        FastText fastText = new FastText(vocab.size(), 64, (int) (2 * Math.pow(10, 6)), 5, geradorSubWord, vocab);
        fastText.fit(words, 20, 0.025);
        String[] palavrasTeste = {"rei", "homem", "mulher", "trabalho", "brasil", "grande"};
        fastText.testeSimilaridade(palavrasTeste);
        fastText.testeSimilaridade("rei", "rainha", "princesa", "homem", "mulher");
        fastText.testeAnalogia("rei", "homem", "mulher");
        System.out.println("time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static String[] embaralhar(List<String> words) {
        String[] res = new String[words.size()];
        Random r = new Random();
        for (int i = 0; i < words.size(); i++) {
            Integer index;
            while (res[(index = r.nextInt(words.size()))] != null) {
            }
            res[index] = words.get(i);
        }
        return res;
    }

    private static void predicao() {
        double[][] x = new double[1000][2];
        int[] y = new int[x.length];
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("text (2).txt")),
                StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            int cont = 0;
            for (int i = 0; i < x.length && line != null; i++, cont++, line = reader.readLine()) {
                String[] split = line.split(" ");
                for (int j = 0; j < x[0].length; j++) {
                    x[i][j] = Double.parseDouble(split[j]);
                }
                y[i] = Integer.parseInt(split[x[0].length].split("\\.")[0]);
            }
            System.out.println(cont);
        } catch (Exception e) {
        }
        Linear linear = new Linear(512, 3);
        linear.fit(x, y, 10000, 0.000001);
        double[][] X = new double[1000000 - x.length][2];
        y = new int[X.length];
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("text (2).txt")),
                StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            for (int i = 0; i < x.length && line != null; i++, line = reader.readLine()) {

            }
            int cont = 0;
            for (int i = 0; i < X.length && line != null; i++, cont++, line = reader.readLine()) {
                String[] split = line.split(" ");
                for (int j = 0; j < X[0].length; j++) {
                    X[i][j] = Double.parseDouble(split[j]);
                }
                y[i] = Integer.parseInt(split[X[0].length].split("\\.")[0]);
            }
            System.out.println(cont);
        } catch (Exception e) {
        }
        int correct = 0;
        for (int i = 0; i < X.length; i++) {
            double[][] values = new double[1][X[0].length];
            values[0] = X[i];
            correct += linear.predict(values) == y[i] ? 1 : 0;
        }
        System.out.println("correct: " + correct + " / " + X.length + ", percent: " + ((double) correct * 100 / X.length));
    }

    public static List<List<String>> read(String... paths) throws IOException {
        List<List<String>> sentences = new ArrayList<>();
        for (String path : paths) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream(path)),
                    StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.toLowerCase().replaceAll("[^a-záéíóúçãõâêô ]", "");
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] tokens = line.trim().split("\\s+");
                    if (tokens.length > 0) {
                        sentences.add(Arrays.asList(tokens));
                    }
                }
            }
        }
        return sentences;
    }
}