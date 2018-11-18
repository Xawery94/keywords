package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        return new HashMap<>();
        //TODO: calculate bag-of-words representation - count how many times each term from dictionary.getTerms
        // exists in document
    }

    private Map<String, Double> calculateTfs(Map<String, Double> bagOfWords) {
        return new HashMap<>();
        //TODO: calculate TF representation - divide elements from bag-of-words by maximal value from this vector
    }

    private List<Double> calculateTfIds(Map<String, Double> tfs, Dictionary dictionary) {
        //TODO: calculate TF-IDF representation - multiply elements from tf representation my matching IDFs (dictionary.getIfs())
        //return results as list of tf-IDF values for terms in the same order as dictionary.getTerms()
        return new ArrayList<>();
    }

    public double calculateSimilarity(Document query) {
        return 0; //TODO: calculate cosine similarity between current document and query document (use calculated TF_IDFs)
    }
}
