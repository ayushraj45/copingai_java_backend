package com.example.copingai.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MHAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    @CollectionTable(name = "assessment_questions", joinColumns = @JoinColumn(name = "mhassessment_id"))
    @Column(name = "assessmentquestion_id")
    private List<Long> assessmentsQuestionIds = new ArrayList<>();
    @Column(name = "user_id")
    private Long userId;
    private boolean initial;
    private LocalDateTime timeTaken;
    private int averageScore;
    private int purposeScore;
    private int relationshipsScore;
    private int stressScore;
    private int emotionalRegulationScore;
    private int energyScore;
    private int selfEsteemScore;
    private String email;

    public MHAssessment() {
        this.timeTaken = LocalDateTime.now();
    }

    //Functions

    public void addAnAssessmentQuestion (Long assessmentQId){
        assessmentsQuestionIds.add(assessmentQId);
        setAssessmentsQuestionIds(this.assessmentsQuestionIds);
    }

    //Getters and Setters
    public List<Long> getAssessmentsQuestionIds() {
        return assessmentsQuestionIds;
    }

    public void setAssessmentsQuestionIds(List<Long> assessmentsQuestionIds) {
        this.assessmentsQuestionIds = assessmentsQuestionIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial(boolean initial) {
        this.initial = initial;
    }

    public LocalDateTime getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(LocalDateTime timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getPurposeScore() {
        return purposeScore;
    }

    public void setPurposeScore(int purposeScore) {
        this.purposeScore = purposeScore;
    }

    public int getRelationshipsScore() {
        return relationshipsScore;
    }

    public void setRelationshipsScore(int relationshipsScore) {
        this.relationshipsScore = relationshipsScore;
    }

    public int getStressScore() {
        return stressScore;
    }

    public void setStressScore(int stressScore) {
        this.stressScore = stressScore;
    }

    public int getEmotionalRegulationScore() {
        return emotionalRegulationScore;
    }

    public void setEmotionalRegulationScore(int emotionalRegulationScore) {
        this.emotionalRegulationScore = emotionalRegulationScore;
    }

    public int getEnergyScore() {
        return energyScore;
    }

    public void setEnergyScore(int energyScore) {
        this.energyScore = energyScore;
    }

    public int getSelfEsteemScore() {
        return selfEsteemScore;
    }

    public void setSelfEsteemScore(int selfEsteemScore) {
        this.selfEsteemScore = selfEsteemScore;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
