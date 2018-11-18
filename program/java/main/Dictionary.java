package main;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dictionary {
    private Map<String, Double> _idfs;
    private List<String> _terms;

    public Dictionary(List<String> keywords) {
        _terms = keywords
                .stream()
                .map(Stemmer::stemToken)
                .distinct()
                .collect(Collectors.toList());
    }

    public Map<String, Double> getIdfs() {
        return _idfs;
    }

    public List<String> getTerms() {
        return _terms;
    }

    public void calculateIdfs(List<Document> documents) {
        //TODO: calculate idfs for each term - log(N/m) - N - documents count, m - number of documents containing given term
        //assign computed values to _idfs map (key: term, value: IDF)
    }

}
