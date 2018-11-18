package main;

public class Stemmer {
    public static String stemToken(String token) {
        //TODO: use PorterStemmer to stem document
        // you can see exemplary use of stemmer in method main() of PorterStemmer class

        PorterStemmer stemmer = new PorterStemmer();
        stemmer.add(token.toCharArray(), token.length());
        stemmer.stem();

        return token;
    }
}
