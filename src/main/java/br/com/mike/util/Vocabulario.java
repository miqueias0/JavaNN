package br.com.mike.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vocabulario {

    private Map<String, Integer> vocab = new HashMap<>();
    private Map<Integer, String> words = new HashMap<>();

    public void buildVocabulario(List<List<String>> sentences, int qtdMinima) {
        Map<String, Integer> frequencia = new HashMap<>();
        for (List<String> sentence : sentences) {
            for (String word : sentence) {
                frequencia.merge(word, 1, Integer::sum);
            }
        }
        for (Map.Entry<String, Integer> entry : frequencia.entrySet()) {
            if (entry.getValue() >= qtdMinima) {
                int index = size();
                vocab.put(entry.getKey(), index);
                words.put(index, entry.getKey());
            }
        }
    }

    public int size() {
        return vocab.size();
    }

    public Integer getIndex(String word) {
        return vocab.get(word);
    }

    public String getWord(int index) {
        return words.get(index);
    }

    public List<String> getWords() {
        return new ArrayList<>(words.values());
    }
}
