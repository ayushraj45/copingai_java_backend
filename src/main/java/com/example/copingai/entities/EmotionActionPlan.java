package com.example.copingai.entities;

import jakarta.persistence.*;

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



}
