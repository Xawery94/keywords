package main;

import java.util.HashMap;
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
        _idfs = new HashMap<>();
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

        int N = documents.size();
        for (String term : _terms) {
            List<Document> documents1 = documents.stream().filter(t -> t.getTerms().contains(term)).collect(Collectors.toList());
            double m = documents1.size();
            if (m == 0) {
                _idfs.put(term, 0.0);
            }

            _idfs.put(term, Math.log(N / m));
        }
    }
}
