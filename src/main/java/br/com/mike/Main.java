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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        List<List<String>> words = read("train (0).txt", "train (1).txt", "train (2).txt", "train (3).txt", "train (4).txt", "train (5).txt");
        Vocabulario vocab = new Vocabulario();
//        vocab.buildVocabulario(words, 5);
        vocab.buildVocabularioParquet(5, read("C:\\Users\\Mike\\Desktop\\projetos\\PLN/", "parquet"));
        words = read("train (0).txt");
        GeradorSubWord geradorSubWord = new GeradorSubWord(3);
        FastText fastText = new FastText(vocab.size(), 300, (int) (2 * Math.pow(10, 6)), 5, geradorSubWord, vocab);
        fastText.fit(words, 100, 0.025);
        String[] palavrasTeste = {"rei", "homem", "mulher", "trabalho", "brasil", "grande"};
        fastText.testeSimilaridade(palavrasTeste);
        fastText.testeSimilaridade("rei", "rainha", "princesa", "homem", "mulher");
        fastText.testeAnalogia("rei", "homem", "mulher");
        System.out.println("time: " + (System.currentTimeMillis() - startTime) + "ms");
        similar(fastText);
        analogia(fastText);
    }


    private static String[] read(String path, String suffix) throws IOException {
        Path pasta = Paths.get(path);
        List<String> arquivosParquet = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pasta, "*." + suffix)) {
            for (Path arquivo : stream) {
                arquivosParquet.add(arquivo.toAbsolutePath().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arquivosParquet.toArray(new String[0]);
    }

    private static void similar(FastText fastText) {
        // --- Testes de Similaridade ---

        System.out.println("\n--- INICIANDO TESTES DE SIMILARIDADE ---");

// 1. Conceitos Científicos (Astronomia)
// Esperado: Alta similaridade entre 'universo', 'estrela', 'planeta' e 'galáxia'.
        fastText.testeSimilaridade("universo", "estrela", "planeta", "galáxia", "átomo");

// 2. Relações Geográficas (Países e Continentes)
// Esperado: Alta similaridade entre países da América do Sul e o continente.
        fastText.testeSimilaridade("brasil", "argentina", "chile", "américa", "europa");

// 3. Sinônimos e Conceitos Abstratos
// Esperado: Alta similaridade entre 'guerra' e 'conflito'. Baixa similaridade com 'paz'.
        fastText.testeSimilaridade("guerra", "batalha", "conflito", "luta", "paz");

// 4. Ações e Resultados
// Esperado: Alta similaridade entre 'evolução' e 'desenvolvimento'.
        fastText.testeSimilaridade("evolução", "desenvolvimento", "mudança", "progresso", "estagnação");

// 5. Política e Sociedade
// Esperado: Alta similaridade entre 'governo', 'estado' e 'política'.
        fastText.testeSimilaridade("governo", "estado", "política", "lei", "sociedade");

// 6. Economia e Indústria
// Esperado: Alta similaridade entre 'economia', 'industrial' e 'comércio'.
        fastText.testeSimilaridade("economia", "industrial", "comércio", "mercado", "agricultura");

// 7. Animais e Características
// Esperado: 'ave' deve ter mais similaridade com 'voar' e 'pena' do que com 'nadar'.
        fastText.testeSimilaridade("ave", "voar", "pena", "bico", "nadar");

// 8. Pessoas e Títulos
// Esperado: Alta similaridade entre títulos de nobreza.
        fastText.testeSimilaridade("rei", "rainha", "imperador", "príncipe", "presidente");

        System.out.println("--- FIM DOS TESTES DE SIMILARIDADE ---");
    }

    private static void analogia(FastText fastText) {
        // --- Testes de Analogia ---

        System.out.println("\n--- INICIANDO TESTES DE ANALOGIA ---");

// 1. Relação Capital-País
// "Berlim está para Alemanha, assim como Paris está para ?" -> França
        fastText.testeAnalogia("alemanha", "berlim", "paris");

// "Lisboa está para Portugal, assim como Buenos Aires está para ?" -> Argentina
        fastText.testeAnalogia("portugal", "lisboa", "argentina");

// 2. Relação Masculino-Feminino
// "Homem está para Rei, assim como Mulher está para ?" -> Rainha
        fastText.testeAnalogia("homem", "rei", "mulher");

// "Príncipe está para Imperador, assim como Princesa está para ?" -> Imperatriz
        fastText.testeAnalogia("príncipe", "imperador", "princesa");

// 3. Relação Singular-Plural
// "País está para Países, assim como Cidade está para ?" -> Cidades
        fastText.testeAnalogia("país", "países", "cidade");

// "Exército está para Exércitos, assim como Soldado está para ?" -> Soldados
        fastText.testeAnalogia("exército", "exércitos", "soldado");

// 4. Relação Verbo no Infinitivo -> Substantivo
// "Governar está para Governo, assim como Produzir está para ?" -> Produção
        fastText.testeAnalogia("governar", "governo", "produzir");

// 5. Relação Objeto-Material
// "Casa está para Tijolo, assim como Livro está para ?" -> Papel
        fastText.testeAnalogia("casa", "tijolo", "livro");

// 6. Relação Causa-Efeito
// "Chuva está para Inundação, assim como Fogo está para ?" -> Incêndio
        fastText.testeAnalogia("chuva", "inundação", "fogo");

// 7. Relações Científicas
// "Física está para Einstein, assim como Biologia está para ?" -> Darwin
        fastText.testeAnalogia("física", "einstein", "biologia");

// "Astronomia está para Telescópio, assim como Biologia está para ?" -> Microscópio
        fastText.testeAnalogia("astronomia", "telescópio", "biologia");

        System.out.println("--- FIM DOS TESTES DE ANALOGIA ---");
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