package com.example.copingai.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String title;
    private String initFeeling;

    @ElementCollection
    @Schema(example = "[]")
    @CollectionTable(name = "entry_questions", joinColumns = @JoinColumn(name = "entry_id"))
    @Column(name = "questions")
    private List<String> questions;
    @ElementCollection
    @Schema(example = "[]")
    @CollectionTable(name = "entry_answers", joinColumns = @JoinColumn(name = "entry_id"))
    @Column(name = "answers")
    private List<String> answers;
    private String content;
    private int questionCount=0;
    private String notifStatus = "new";
    private LocalDateTime createdAt;

    //Constructor
    public Entry() {
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
    }

//    public Entry(Long userId) {
//        this.userId = userId;
//    }

    //Setters and Getters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitFeeling() {
        return initFeeling;
    }

    public void setInitFeeling(String initFeeling) {
        this.initFeeling = initFeeling;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public void addQuestion (String question) {
        questions.add(question);
        setQuestions(questions);
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void addAnswer (String answer){
        answers.add(answer);
        setAnswers(answers);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNotifStatus() {
        return notifStatus;
    }

    public void setNotifStatus(String notifStatus) {
        this.notifStatus = notifStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}


