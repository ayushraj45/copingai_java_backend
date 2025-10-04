package com.example.copingai.controller;

public class NewChatResponse {
    private int questionCount;
    private String newQuestion;

    public NewChatResponse(int questionCount, String newQuestion) {
        this.questionCount = questionCount;
        this.newQuestion = newQuestion;
    }

    // Getters and setters
    public int getQuestionCount() { return questionCount; }
    public String getNewQuestion() { return newQuestion; }
}
