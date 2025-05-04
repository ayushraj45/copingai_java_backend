package com.example.copingai.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EmotionActionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    @CollectionTable(name = "emotionAction_steps", joinColumns = @JoinColumn(name = "emotionactionplan_id"))
    @Column(name = "emotionactionstep_id")
    private List<Long> emotionActionStepIds = new ArrayList<>();
    @Column(name = "user_id")
    private Long userId;
    private String domain;
    private int currentStep;
    private int currentActionDay;

    private LocalDateTime timeTaken;


    public EmotionActionPlan() {
        this.timeTaken = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void addActionStep (Long emotionActionStepId){
        emotionActionStepIds.add(emotionActionStepId);
        setEmotionActionStepIds(emotionActionStepIds);
    }

    public List<Long> getEmotionActionStepIds() {
        return emotionActionStepIds;
    }

    public void setEmotionActionStepIds(List<Long> emotionActionStepIds) {
        this.emotionActionStepIds = emotionActionStepIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public LocalDateTime getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(LocalDateTime timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getCurrentActionDay() {
        return currentActionDay;
    }

    public void setCurrentActionDay(int currentActionDay) {
        this.currentActionDay = currentActionDay;
    }
}
