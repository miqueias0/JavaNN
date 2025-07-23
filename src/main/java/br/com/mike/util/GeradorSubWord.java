package br.com.mike.util;

import java.util.ArrayList;
import java.util.List;

public class GeradorSubWord {

    private final int nGramSize;

    public GeradorSubWord(int nGramSize) {
        this.nGramSize = nGramSize;
    }

    public List<List<String>> process(List<String> words) {
        List<List<String>> result = new ArrayList<>();
        for (String word1 : words) {
            result.add(process(word1));
        }
        return result;
    }

    public List<List<String>> process(String word, String patten) {
        String[] words = word.split(patten);
        List<List<String>> result = new ArrayList<>();
        for (String word1 : words) {
            result.add(process(word1));
        }
        return result;
    }

    public List<String> process(String word) {
        List<String> result = new ArrayList<>();
        String w = "<" + word + ">";
        for (int j = nGramSize; j <= 6; j++) {
            for (int i = 0; i <= w.length() - j; i++) {
                result.add(w.substring(i, i + j));
            }
        }
        return result;
    }
}
