package com.example.knowledgewarsapi.model;

import java.util.List;

public class Question {

    private String question;
    private List<String> options;
    private String correctAnswer;
    private String questionCategory;

    public Question(String question, List<String> options, String correctAnswer, String questionCategory) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.questionCategory = questionCategory;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public Question() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}
