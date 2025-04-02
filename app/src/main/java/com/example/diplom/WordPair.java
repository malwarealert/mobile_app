package com.example.diplom;

public class WordPair {
    private String foreignWord;
    private String translation;

    public WordPair(String foreignWord, String translation) {
        this.foreignWord = foreignWord;
        this.translation = translation;
    }

    public String getForeignWord() {
        return foreignWord;
    }


    public String getTranslation() {
        return translation;
    }
}