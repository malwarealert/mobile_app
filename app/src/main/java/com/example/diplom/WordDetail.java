package com.example.diplom;

import java.util.List;

public class WordDetail {
    private String word;
    private String translation;
    private List<String> examples;
    private List<String> exampleTranslations;

    // Геттеры и сеттеры
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public List<String> getExamples() { return examples; }
    public void setExamples(List<String> examples) { this.examples = examples; }

    public List<String> getExampleTranslations() { return exampleTranslations; }
    public void setExampleTranslations(List<String> exampleTranslations) { this.exampleTranslations = exampleTranslations; }
}
