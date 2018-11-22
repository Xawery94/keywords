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
        for (String term : dictionary.getTerms()) {
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

        for (Map.Entry<String, Double> ent : bagOfWords.entrySet())
            if (entry == null || ent.getValue().compareTo(entry.getValue()) > 0) {
                entry = ent;
            }

        for (Map.Entry<String, Double> ent : bagOfWords.entrySet())
            map.put(ent.getKey(), ent.getValue() / entry.getValue());

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
            list.add(result);
        }

        //TODO: calculate TF-IDF representation - multiply elements from tf representation my matching IDFs (dictionary.getIfs())
        //return results as list of tf-IDF values for terms in the same order as dictionary.getTerms()
        return list;
    }

    double calculateSimilarity(Document query) {
        double wektor = 0.0;
        double doc = 0.0;
        double document = 0.0;
        double q;
        double d;

        for (int i = 0; i < query._tfIdfs.size(); i++) {
            q = query._tfIdfs.get(i);
            d = this._tfIdfs.get(i);
            wektor += Math.sqrt(q * q);
            document += Math.sqrt(d * d);
            doc += d * q;
        }

        return doc / (wektor * document);
        //TODO: calculate cosine similarity between current document and query document (use calculated TF_IDFs)
    }
}
