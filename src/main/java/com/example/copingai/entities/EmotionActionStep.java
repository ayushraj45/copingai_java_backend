package com.example.copingai.entities;

import jakarta.persistence.*;

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
}
