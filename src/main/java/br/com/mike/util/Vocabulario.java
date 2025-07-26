package br.com.mike.util;

import br.com.mike.tokenizer.pattern.PatternGPT4;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroReadSupport;
import org.apache.parquet.hadoop.ParquetReader;

import java.io.IOException;
import java.util.*;

public class Vocabulario {

    private Map<String, Integer> vocab = new HashMap<>();
    private Map<Integer, String> words = new HashMap<>();

    public void buildVocabulario(List<List<String>> sentences, int qtdMinima) {
        Map<String, Integer> frequencia = new HashMap<>();
        buildVocabulario(sentences, qtdMinima, frequencia);
    }

    private void buildVocabulario(List<List<String>> sentences, int qtdMinima, Map<String, Integer> frequencia) {
        for (List<String> sentence : sentences) {
            for (String word : sentence) {
                frequencia.merge(word, 1, Integer::sum);
            }
        }
    }

    public void buildVocabularioParquet(int qtdMinima, String... paths) {
        Map<String, Integer> frequencia = new HashMap<>();
        int length = 500000;
        PatternGPT4 patternGPT4 = new PatternGPT4();
        for (String path : paths) {
            Configuration conf = new Configuration();
            conf.setBoolean("parquet.avro.schema.leniency", true);
            Schema projectionSchema = SchemaBuilder.record("MeuSchema")
                    .namespace("br.com.mike")
                    .fields()
                    .name("content").type().nullable().stringType().noDefault()
                    .endRecord();
            AvroReadSupport.setRequestedProjection(conf, projectionSchema);
            try (ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(new Path(path))
                    .withConf(conf)
                    .build()) {
                GenericRecord record;
                int i = 0;
                while ((record = reader.read()) != null) {
                    buildVocabulario(Arrays.asList(record.get("content").toString().split("\n")).parallelStream().map(word -> Arrays.asList(patternGPT4.split(word))).toList(), qtdMinima, frequencia);
                    i++;
                    if (i % 1000 == 0) {
                        System.out.println("Processed: " + i + "/" + length + " percent: " + ((double) i / length * 100) + "%");
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo Parquet: " + e.getMessage());
                e.printStackTrace();
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
