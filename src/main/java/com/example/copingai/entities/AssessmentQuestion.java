package com.example.copingai.entities;

import jakarta.persistence.*;

@Entity
public class AssessmentQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "mhassessment_id")
    private Long assessmentId;
    private String questionText;
    private String qDomain;
    private int qAnswer = 0;

    public AssessmentQuestion() {
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getqDomain() {
        return qDomain;
    }

    public void setqDomain(String qDomain) {
        this.qDomain = qDomain;
    }

    public int getqAnswer() {
        return qAnswer;
    }

    public void setqAnswer(int qAnswer) {
        this.qAnswer = qAnswer;
    }
}
