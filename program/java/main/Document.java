package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Document {
    private String _content;
    private String _title;
    private List<String> _terms;
    private List<Double> _tfIdfs;

    public Document(String content, String title) {
        this._content = content;
        this._title = title;
        preprocessDocument();
    }

    public String getContent() {
        return _content;
    }

    public String getTitle() {
        return _title;
    }

    public List<String> getTerms() {
        return _terms;
    }

    public List<Double> getTfIdfs() {
        return _tfIdfs;
    }

    public void preprocessDocument() {
        String normalized = normalizeText(_content);
        List<String> tokens = tokenizeDocument(normalized);
        _terms = stemTokens(tokens);
    }

    public void calculateRepresentations(Dictionary dictionary) {
        Map<String, Double> bagOfWords = calculateBagOfWords(_terms, dictionary);
        Map<String, Double> tfs = calculateTfs(bagOfWords);
        _tfIdfs = calculateTfIds(tfs, dictionary);
    }

    private List<String> stemTokens(List<String> tokens) {
        return tokens.stream()
                .map(Stemmer::stemToken)
                .collect(Collectors.toList());
    }

    private String normalizeText(String content) {
        return content.replaceAll("[^A-Za-z0-9 ]", "");
        //TODO remove non-alphanumeric signs, keep only letters, digits and spaces.
    }

    private List<String> tokenizeDocument(String normalized) {
        return Arrays.asList(normalized.split("\\s+"));
        //TODO: tokenize document - use simple division on white spaces.
    }

    private Map<String, Double> calculateBagOfWords(List<String> terms, Dictionary dictionary) {
        Map<String, Double> bagOfWords = new HashMap<>();
        for (Iterator<String> iterator = dictionary.getTerms().iterator(); iterator.hasNext(); ) {
            String term = iterator.next();
            int occurrences = Collections.frequency(terms, term);
            bagOfWords.put(term, (double) occurrences);
        }

        return bagOfWords;
        //TODO: calculate bag-of-words representation - count how many times each term from dictionary.getTerms
        // exists in document
    }

    private Map<String, Double> calculateTfs(Map<String, Double> bagOfWords) {
        Map<String, Double> map = new HashMap<>();
        Map.Entry<String, Double> entry = null;

        for (Map.Entry<String, Double> ent : bagOfWords.entrySet()) {
            if (entry == null || ent.getValue().compareTo(entry.getValue()) > 0) {
                entry = ent;
            }
        }

        for (Map.Entry<String, Double> ent : bagOfWords.entrySet()) {
            if (Double.isNaN(ent.getValue() / entry.getValue())) {
                double value = 0.0;
                map.put(ent.getKey(), value);
            } else {
                map.put(ent.getKey(), ent.getValue() / entry.getValue());
            }
        }

        return map;
        //TODO: calculate TF representation - divide elements from bag-of-words by maximal value from this vector
    }

    private List<Double> calculateTfIds(Map<String, Double> tfs, Dictionary dictionary) {
        List<Double> list = new ArrayList<>();

        for (Iterator<Map.Entry<String, Double>> iterator = dictionary.getIdfs().entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Double> entry = iterator.next();
            String k = entry.getKey();
            Double v = entry.getValue();
            double result = v * tfs.get(k);

            if (Double.isNaN(result)) {
                list.add(0.0);
            } else {
                list.add(result);
            }
        }

        //TODO: calculate TF-IDF representation - multiply elements from tf representation my matching IDFs (dictionary.getIfs())
        //return results as list of tf-IDF values for terms in the same order as dictionary.getTerms()
        return list;
    }

    double calculateSimilarity(Document query) {
        double vector = 0.0;
        double doc = 0.0;
        double document = 0.0;
        double q;
        double d;

        int i = 0;
        while (i < query._tfIdfs.size()) {
            q = query._tfIdfs.get(i);
            d = this._tfIdfs.get(i);
            vector += Math.pow(q, 2);
            document += Math.pow(d, 2);
            doc += d * q;
            i++;
        }

        double similarity = doc / (Math.sqrt(vector) * Math.sqrt(document));

        if (Double.isNaN(similarity)) {
            return 0.0;
        } else {
            return similarity;
        }
        //TODO: calculate cosine similarity between current document and query document (use calculated TF_IDFs)
    }
}
