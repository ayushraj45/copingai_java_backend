package com.example.copingai.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class EmotionActionStep {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "emotionactionplan_id")
    private Long emotionActionPlanId;
    private String domain;
    private boolean active;
    private boolean isCompleted;
    private int actionDay;
    private int stepNumber;
    private String actionText;
    private String textContent;
    private String followUpPrompt;
    private String followUpContent;
    private String title;
    private boolean toBeActivated = false; // Default to false
    private Instant scheduledActivationTime;

    public EmotionActionStep() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getEmotionActionPlanId() {
        return emotionActionPlanId;
    }

    public void setEmotionActionPlanId(Long emotionActionPlanId) {
        this.emotionActionPlanId = emotionActionPlanId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getActionDay() {
        return actionDay;
    }

    public void setActionDay(int actionDay) {
        this.actionDay = actionDay;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getFollowUpPrompt() {
        return followUpPrompt;
    }

    public void setFollowUpPrompt(String followUpPrompt) {
        this.followUpPrompt = followUpPrompt;
    }

    public String getFollowUpContent() {
        return followUpContent;
    }

    public void setFollowUpContent(String followUpContent) {
        this.followUpContent = followUpContent;
    }

    public boolean isToBeActivated() {
        return toBeActivated;
    }

    public void setToBeActivated(boolean toBeActivated) {
        this.toBeActivated = toBeActivated;
    }

    public Instant getScheduledActivationTime() {
        return scheduledActivationTime;
    }

    public void setScheduledActivationTime(Instant scheduledActivationTime) {
        this.scheduledActivationTime = scheduledActivationTime;
    }
}
