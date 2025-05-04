package com.example.copingai.service;

import com.example.copingai.data.EmotionActionPlanRepository;
import com.example.copingai.data.UserRepository;
import com.example.copingai.entities.EmotionActionPlan;
import com.example.copingai.entities.EmotionActionStep;
import com.example.copingai.entities.MHAssessment;
import com.example.copingai.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class EmotionActionPlanService {

    private final EmotionActionPlanRepository emotionActionPlanRepository;
    public UserRepository userRepository;

    @Autowired
    public EmotionActionStepService emotionActionStepService;
    @Autowired
    public EmotionActionPlanService(EmotionActionPlanRepository emotionActionPlanRepository, UserRepository userRepository) {
        this.emotionActionPlanRepository = emotionActionPlanRepository;
        this.userRepository = userRepository;
    }

    public EmotionActionPlan createAnActionPlan (EmotionActionPlan emotionActionPlan){
        if (emotionActionPlan == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to add cannot be null");
        } else if (emotionActionPlan.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to add cannot contain an id");
        }else if (emotionActionPlan.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to must have a user ID");
        }
        switch (emotionActionPlan.getDomain()) {
            case "Purpose & Fulfillment":
                return createPurposeActionPlan(emotionActionPlan);
            case "Relationships / Social Life":
                return createRelationshipActionPlan(emotionActionPlan);
            case "Emotional Regulation":
                return createEmoRegulationActionPlan(emotionActionPlan);
            case "Stress & Burnout":
                return createStressActionPlan(emotionActionPlan);
            case "Sleep / Energy":
                return createSleepActionPlan(emotionActionPlan);
            case "Self-esteem / Self-talk":
                return createSelfEsteemActionPlan(emotionActionPlan);
            default:
                return createSelfEsteemActionPlan(emotionActionPlan);
        }
    }

    public EmotionActionPlan createPurposeActionPlan ( EmotionActionPlan emotionActionPlan) {
        EmotionActionPlan createdActionPlan = emotionActionPlanRepository.save(emotionActionPlan);
        Long createdPlanId = createdActionPlan.getId();
        User user = userRepository.findById(createdActionPlan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        user.addEmotionPlan(createdPlanId);
        userRepository.save(user);

        List<Map<String, Object>> steps = Arrays.asList(
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","When was the last time you felt truly fulfilled, and what were you doing at that moment?", "domain", "Purpose & Fulfillment", "stepNumber",1, "active",true, "actionDay",1),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Focus","actionText","Write down 3 activities that make you feel like your time is well spent.", "domain", "Purpose & Fulfillment", "stepNumber",2, "active",false, "actionDay",2),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Writing","actionText","Make a Mood Entry", "domain", "Purpose & Fulfillment", "stepNumber",3, "active",false, "actionDay",3),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Think","actionText","In what areas of your life do you feel like you're just going through the motions?", "domain", "Purpose & Fulfillment", "stepNumber",4, "active",false, "actionDay",4),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","List 2 things you could do this month to align more with your values.", "domain", "Purpose & Fulfillment", "stepNumber",5, "active",false, "actionDay",5),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make an Explore Entry - Choose a positive feeling to write about", "domain", "Purpose & Fulfillment", "stepNumber",6, "active",false, "actionDay",6),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","What beliefs or fears might be holding you back from pursuing what matters most to you?", "domain", "Purpose & Fulfillment", "stepNumber",7, "active",false, "actionDay",7),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","How do you define purpose for yourself, and where do you feel disconnected from it?", "domain", "Purpose & Fulfillment", "stepNumber",8, "active",false, "actionDay",8),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Rest Day: Make an Event Entry - Write about a recent win from your previous action items", "domain", "Purpose & Fulfillment", "stepNumber",9, "active",false, "actionDay",9),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Visualise","actionText","Create a short vision statement for how you'd like your days to feel.", "domain", "Purpose & Fulfillment", "stepNumber",10, "active",false, "actionDay",10),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","What past decisions have led you away from a sense of meaning or direction?", "domain", "Purpose & Fulfillment", "stepNumber",11, "active",false, "actionDay",11),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Perspective","actionText","What would your younger self think of how you're spending your time now?", "domain", "Purpose & Fulfillment", "stepNumber",12, "active",false, "actionDay",12),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Today Entry - Write about how you are going to incorporate ", "domain", "Purpose & Fulfillment", "stepNumber",13, "active",false, "actionDay",13),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","List 1 habit you can try this week that brings more intention into your day.", "domain", "Purpose & Fulfillment", "stepNumber",14, "active",false, "actionDay",14)
                );
        for (Map<String, Object> s : steps
             ) {
            EmotionActionStep newEmotionActionStep = new EmotionActionStep();
            newEmotionActionStep.setEmotionActionPlanId((Long) s.get("emotionActionPlanId"));
            newEmotionActionStep.setActionText((String) s.get("actionText"));
            newEmotionActionStep.setDomain((String) s.get("domain"));
            newEmotionActionStep.setStepNumber((int) s.get("stepNumber"));
            newEmotionActionStep.setActive((boolean) s.get("active"));
            newEmotionActionStep.setActionDay((int) s.get("actionDay"));
            newEmotionActionStep.setTitle((String) s.get("title"));
            emotionActionStepService.addAnEmotionActionStep(newEmotionActionStep);
        }
        return emotionActionPlanRepository.findById(createdPlanId).orElseThrow(() -> new EntityNotFoundException("Action Plan not found"));
    }


    public List<EmotionActionPlan> findAllActionPlans() { return emotionActionPlanRepository.findAll();}

    public EmotionActionPlan createRelationshipActionPlan ( EmotionActionPlan emotionActionPlan) {

        EmotionActionPlan createdActionPlan = emotionActionPlanRepository.save(emotionActionPlan);
        Long createdPlanId = createdActionPlan.getId();
        User user = userRepository.findById(createdActionPlan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        user.addEmotionPlan(createdPlanId);
        userRepository.save(user);

        List<Map<String, Object>> steps = Arrays.asList(
                Map.of("emotionActionPlanId", createdPlanId, "title", "Think","actionText","What moments of connection have felt most nourishing to you in the past year?", "domain", "Relationships / Social Life", "stepNumber",1, "active",true, "actionDay",1),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Identify 2 people you want to reconnect with and write how you might reach out.", "domain", "Relationships / Social Life", "stepNumber",2, "active",false, "actionDay",2),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Mood Entry: Choose 'Love' or 'Gratitude' emotions", "domain", "Relationships / Social Life", "stepNumber",3, "active",false, "actionDay",3),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","What stops you from being fully open or authentic with others?", "domain", "Relationships / Social Life", "stepNumber",4, "active",false, "actionDay",4),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Decision","actionText","Make a short list of qualities you value in meaningful relationships.", "domain", "Relationships / Social Life", "stepNumber",5, "active",false, "actionDay",5),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Reflection Day: Make an Explore Entry and Explore Relationships", "domain", "Relationships / Social Life", "stepNumber",6, "active",false, "actionDay",6),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","How do you usually respond when you feel hurt or disconnected from someone?", "domain", "Relationships / Social Life", "stepNumber",7, "active",false, "actionDay",7),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","Are there relationships in your life that drain you more than they support you? Why?", "domain", "Relationships / Social Life", "stepNumber",8, "active",false, "actionDay",8),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","Write a plan for one way you could cultivate community or connection this month.", "domain", "Relationships / Social Life", "stepNumber",9, "active",false, "actionDay",9),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Appreciate","actionText","Make an Event Entry and journal about a recent relationship win", "domain", "Relationships / Social Life", "stepNumber",10, "active",false, "actionDay",10),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Perspective","actionText","What does “feeling seen” by others mean to you, and when was the last time you felt it?", "domain", "Relationships / Social Life", "stepNumber",11, "active",false, "actionDay",11),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Make a Today Entry - Plan how you're gonna make an effort for a relationship you value.", "domain", "Relationships / Social Life", "stepNumber",12, "active",false, "actionDay",12),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","What role does loneliness play in your life right now?", "domain", "Relationships / Social Life", "stepNumber",13, "active",false, "actionDay",13),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Focus","actionText","Set one intention for how you’d like to show up in your relationships this week.", "domain", "Relationships / Social Life", "stepNumber",14, "active",false, "actionDay",14)
                );
        for (Map<String, Object> s : steps
        ) {
            EmotionActionStep newEmotionActionStep = new EmotionActionStep();
            newEmotionActionStep.setEmotionActionPlanId((Long) s.get("emotionActionPlanId"));
            newEmotionActionStep.setActionText((String) s.get("actionText"));
            newEmotionActionStep.setDomain((String) s.get("domain"));
            newEmotionActionStep.setStepNumber((int) s.get("stepNumber"));
            newEmotionActionStep.setActive((boolean) s.get("active"));
            newEmotionActionStep.setActionDay((int) s.get("actionDay"));
            newEmotionActionStep.setTitle((String) s.get("title"));
            emotionActionStepService.addAnEmotionActionStep(newEmotionActionStep);
        }
        return emotionActionPlanRepository.findById(createdPlanId).orElseThrow(() -> new EntityNotFoundException("Action Plan not found"));
    }

    public EmotionActionPlan createEmoRegulationActionPlan ( EmotionActionPlan emotionActionPlan) {
        EmotionActionPlan createdActionPlan = emotionActionPlanRepository.save(emotionActionPlan);
        Long createdPlanId = createdActionPlan.getId();
        User user = userRepository.findById(createdActionPlan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        user.addEmotionPlan(createdPlanId);
        userRepository.save(user);

        List<Map<String, Object>> steps = Arrays.asList(
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","How do you typically react when strong emotions arise — and why?", "domain", "Emotional Regulation", "stepNumber",1, "active",true, "actionDay",1),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","Write down 3 healthy ways you could respond to stress or emotional pain.", "domain", "Emotional Regulation", "stepNumber",2, "active",false, "actionDay",2),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Today Entry - based on the previous step incorporate a plan to regulate your emotions better", "domain", "Emotional Regulation", "stepNumber",3, "active",false, "actionDay",3),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Question","actionText","Are there emotions you tend to avoid or suppress? Where do you think that pattern began?", "domain", "Emotional Regulation", "stepNumber",4, "active",false, "actionDay",4),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Visualise","actionText","Create a personal “calm down” toolkit — list what helps you reset emotionally.", "domain", "Emotional Regulation", "stepNumber",5, "active",false, "actionDay",5),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","What does your inner critic sound like during moments of emotional struggle?", "domain", "Emotional Regulation", "stepNumber",6, "active",false, "actionDay",6),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Mood entry: Use the emotion you think you will feel majorly today ", "domain", "Emotional Regulation", "stepNumber",7, "active",false, "actionDay",7),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Question","actionText","When was the last time you sat with an uncomfortable feeling instead of avoiding it?", "domain", "Emotional Regulation", "stepNumber",8, "active",false, "actionDay",8),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Write a short script you could say to yourself the next time you feel overwhelmed.", "domain", "Emotional Regulation", "stepNumber",9, "active",false, "actionDay",9),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write and Feel","actionText","Make an Explore Entry - Explore Stress in your Life", "domain", "Emotional Regulation", "stepNumber",10, "active",false, "actionDay",10),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Perspective","actionText","How do your emotions influence your decisions, and are you okay with that?", "domain", "Emotional Regulation", "stepNumber",11, "active",false, "actionDay",11),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Visualise","actionText","What would emotional resilience look like for you in day-to-day life?", "domain", "Emotional Regulation", "stepNumber",12, "active",false, "actionDay",12),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make an Event Entry: Write about a win against emotional pain or stress in the last 2 weeks ", "domain", "Emotional Regulation", "stepNumber",13, "active",false, "actionDay",13),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","Pick 1 small situation from today and describe how you could respond to it differently.", "domain", "Emotional Regulation", "stepNumber",14, "active",false, "actionDay",14)
                );
        for (Map<String, Object> s : steps
        ) {
            EmotionActionStep newEmotionActionStep = new EmotionActionStep();
            newEmotionActionStep.setEmotionActionPlanId((Long) s.get("emotionActionPlanId"));
            newEmotionActionStep.setActionText((String) s.get("actionText"));
            newEmotionActionStep.setDomain((String) s.get("domain"));
            newEmotionActionStep.setStepNumber((int) s.get("stepNumber"));
            newEmotionActionStep.setActive((boolean) s.get("active"));
            newEmotionActionStep.setActionDay((int) s.get("actionDay"));
            newEmotionActionStep.setTitle((String) s.get("title"));
            emotionActionStepService.addAnEmotionActionStep(newEmotionActionStep);
        }
        return emotionActionPlanRepository.findById(createdPlanId).orElseThrow(() -> new EntityNotFoundException("Action Plan not found"));
    }

    public EmotionActionPlan createStressActionPlan ( EmotionActionPlan emotionActionPlan) {
        EmotionActionPlan createdActionPlan = emotionActionPlanRepository.save(emotionActionPlan);
        Long createdPlanId = createdActionPlan.getId();
        User user = userRepository.findById(createdActionPlan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        user.addEmotionPlan(createdPlanId);
        userRepository.save(user);

        List<Map<String, Object>> steps = Arrays.asList(
                Map.of("emotionActionPlanId", createdPlanId, "title", "Think back","actionText","What recent situations have made you feel like you’re at a breaking point?", "domain", "Stress & Burnout", "stepNumber",1, "active",true, "actionDay",1),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Decide","actionText","List 3 boundaries you could set to protect your energy.", "domain", "Stress & Burnout", "stepNumber",2, "active",false, "actionDay",2),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Today Entry - based on the previous step incorporate a plan to protect your energy", "domain", "Stress & Burnout", "stepNumber",3, "active",false, "actionDay",3),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","What is your relationship to rest — do you allow yourself to truly recharge?", "domain", "Stress & Burnout", "stepNumber",4, "active",false, "actionDay",4),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Plan one specific time this week to do something restorative.", "domain", "Stress & Burnout", "stepNumber",5, "active",false, "actionDay",5),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write ","actionText","Make a Mood entry: Use the emotion you think you will feel majorly today ", "domain", "Stress & Burnout", "stepNumber",6, "active",false, "actionDay",6),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","How does your self-worth relate to how busy or useful you are?", "domain", "Stress & Burnout", "stepNumber",7, "active",false, "actionDay",7),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","What does burnout feel like in your body, and when did you first start noticing it?", "domain", "Stress & Burnout", "stepNumber",8, "active",false, "actionDay",8),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","How much of your stress comes from things within your control vs. outside of it?", "domain", "Stress & Burnout", "stepNumber",9, "active",false, "actionDay",9),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Write a 5-minute action plan for managing your stress in the coming week.", "domain", "Stress & Burnout", "stepNumber",10, "active",false, "actionDay",10),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","Make an Event Entry: Talk about how your plans are coming along, it's okay if you haven't followed them: Write about why not", "domain", "Stress & Burnout", "stepNumber",11, "active",false, "actionDay",11),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Perspective","actionText","What messages (from culture, family, etc.) have shaped how you think about productivity?", "domain", "Stress & Burnout", "stepNumber",12, "active",false, "actionDay",12),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make an Explore Entry: Explore the physical effects of feeling stress and how you can manage them ", "domain", "Stress & Burnout", "stepNumber",13, "active",false, "actionDay",13),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","Choose 1 task to delegate, delay, or drop this week. Write it down and why.", "domain", "Stress & Burnout", "stepNumber",14, "active",false, "actionDay",14)
                );
        for (Map<String, Object> s : steps
        ) {
            EmotionActionStep newEmotionActionStep = new EmotionActionStep();
            newEmotionActionStep.setEmotionActionPlanId((Long) s.get("emotionActionPlanId"));
            newEmotionActionStep.setActionText((String) s.get("actionText"));
            newEmotionActionStep.setDomain((String) s.get("domain"));
            newEmotionActionStep.setStepNumber((int) s.get("stepNumber"));
            newEmotionActionStep.setActive((boolean) s.get("active"));
            newEmotionActionStep.setActionDay((int) s.get("actionDay"));
            newEmotionActionStep.setTitle((String) s.get("title"));
            emotionActionStepService.addAnEmotionActionStep(newEmotionActionStep);
        }
        return emotionActionPlanRepository.findById(createdPlanId).orElseThrow(() -> new EntityNotFoundException("Action Plan not found"));
    }

    public EmotionActionPlan createSleepActionPlan ( EmotionActionPlan emotionActionPlan) {

        EmotionActionPlan createdActionPlan = emotionActionPlanRepository.save(emotionActionPlan);
        Long createdPlanId = createdActionPlan.getId();
        User user = userRepository.findById(createdActionPlan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        user.addEmotionPlan(createdPlanId);
        userRepository.save(user);

        List<Map<String, Object>> steps = Arrays.asList(
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","What thoughts or feelings keep you up at night most often?", "domain", "Sleep / Energy", "stepNumber",1, "active",true, "actionDay",1),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","List 3 things you can do tonight to help your body wind down.", "domain", "Sleep / Energy", "stepNumber",2, "active",false, "actionDay",2),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Today Entry - based on the previous step incorporate the plan to wind down properly", "domain", "Sleep / Energy", "stepNumber",3, "active",false, "actionDay",3),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Question","actionText","How do your energy levels change throughout the day, and what do you think affects that?", "domain", "Sleep / Energy", "stepNumber",4, "active",false, "actionDay",4),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","Describe your ideal morning routine — and identify one step you could try tomorrow.", "domain", "Sleep / Energy", "stepNumber",5, "active",false, "actionDay",5),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Visualise","actionText","Make a Mood entry: Use the emotion you think you will feel majorly today ", "domain", "Sleep / Energy", "stepNumber",6, "active",false, "actionDay",6),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","What patterns have you noticed between your sleep and your emotional well-being?", "domain", "Sleep / Energy", "stepNumber",7, "active",false, "actionDay",7),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Question","actionText","How do you talk to yourself on days when your energy is low?", "domain", "Sleep / Energy", "stepNumber",8, "active",false, "actionDay",8),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Write a short list of “energy boosters” — things that leave you feeling refreshed.", "domain", "Sleep / Energy", "stepNumber",9, "active",false, "actionDay",9),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write and Feel","actionText","Make an Event Entry: Talk about how your plans are coming along, it's okay if you haven't followed them: Write about why not", "domain", "Sleep / Energy", "stepNumber",10, "active",false, "actionDay",10),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Perspective","actionText","In what ways are you using sleep or tiredness to avoid something else?", "domain", "Sleep / Energy", "stepNumber",11, "active",false, "actionDay",11),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","When was the last time you felt well-rested, and what made that possible?", "domain", "Sleep / Energy", "stepNumber",12, "active",false, "actionDay",12),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan and Action","actionText","Create a wind-down ritual — list 3 steps you’ll do before bed this week.", "domain", "Sleep / Energy", "stepNumber",13, "active",false, "actionDay",13),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make an Event Entry: Write about following your wind down ritual ", "domain", "Sleep / Energy", "stepNumber",14, "active",false, "actionDay",14)
                );
        for (Map<String, Object> s : steps
        ) {
            EmotionActionStep newEmotionActionStep = new EmotionActionStep();
            newEmotionActionStep.setEmotionActionPlanId((Long) s.get("emotionActionPlanId"));
            newEmotionActionStep.setActionText((String) s.get("actionText"));
            newEmotionActionStep.setDomain((String) s.get("domain"));
            newEmotionActionStep.setStepNumber((int) s.get("stepNumber"));
            newEmotionActionStep.setActive((boolean) s.get("active"));
            newEmotionActionStep.setActionDay((int) s.get("actionDay"));
            newEmotionActionStep.setTitle((String) s.get("title"));
            emotionActionStepService.addAnEmotionActionStep(newEmotionActionStep);
        }
        return emotionActionPlanRepository.findById(createdPlanId).orElseThrow(() -> new EntityNotFoundException("Action Plan not found"));
    }

    public EmotionActionPlan createSelfEsteemActionPlan ( EmotionActionPlan emotionActionPlan) {
        EmotionActionPlan createdActionPlan = emotionActionPlanRepository.save(emotionActionPlan);
        Long createdPlanId = createdActionPlan.getId();
        User user = userRepository.findById(createdActionPlan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        user.addEmotionPlan(createdPlanId);
        userRepository.save(user);

        List<Map<String, Object>> steps = Arrays.asList(
                Map.of("emotionActionPlanId", createdPlanId, "title", "Think back","actionText","When do you feel most confident, and what do you believe about yourself in those moments?", "domain", "Self-esteem / Self-talk", "stepNumber",1, "active",true, "actionDay",1),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Decide","actionText","Write 3 affirmations or reminders you could repeat on difficult days.", "domain", "Self-esteem / Self-talk", "stepNumber",2, "active",false, "actionDay",2),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make a Mood Entry: Think about a negative reinforcement and explore how it makes you feel", "domain", "Self-esteem / Self-talk", "stepNumber",3, "active",false, "actionDay",3),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","What kinds of things do you say to yourself after a mistake?", "domain", "Self-esteem / Self-talk", "stepNumber",4, "active",false, "actionDay",4),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Plan","actionText","Make a short list of your recent wins, no matter how small.", "domain", "Self-esteem / Self-talk", "stepNumber",5, "active",false, "actionDay",5),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write ","actionText","Make a Today Entry - based on the previous step incorporate a plan to talk to yourself more positively", "domain", "Self-esteem / Self-talk", "stepNumber",6, "active",false, "actionDay",6),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Think","actionText","What early experiences shaped how you see your worth or value?", "domain", "Self-esteem / Self-talk", "stepNumber",7, "active",false, "actionDay",7),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Contemplate","actionText","How would you describe your inner voice — critical, encouraging, doubtful, etc.?", "domain", "Self-esteem / Self-talk", "stepNumber",8, "active",false, "actionDay",8),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Perspective","actionText","Write a letter to yourself from the perspective of someone who truly believes in you.", "domain", "Self-esteem / Self-talk", "stepNumber",9, "active",false, "actionDay",9),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make an explore entry: Explore the benefits of positive thinking", "domain", "Self-esteem / Self-talk", "stepNumber",10, "active",false, "actionDay",10),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Reflect","actionText","How do you compare yourself to others, and what impact does that have on you?", "domain", "Self-esteem / Self-talk", "stepNumber",11, "active",false, "actionDay",11),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Question","actionText","What do you believe you need to achieve in order to feel “enough”?", "domain", "Self-esteem / Self-talk", "stepNumber",12, "active",false, "actionDay",12),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Write","actionText","Make an Event Entry: Think about a recent win and explore what caused it, how could you repeat it? ", "domain", "Self-esteem / Self-talk", "stepNumber",13, "active",false, "actionDay",13),
                Map.of("emotionActionPlanId", createdPlanId, "title", "Action","actionText","List 2 small, kind things you can do for yourself this week.", "domain", "Self-esteem / Self-talk", "stepNumber",14, "active",false, "actionDay",14)
);
        for (Map<String, Object> s : steps
        ) {
            EmotionActionStep newEmotionActionStep = new EmotionActionStep();
            newEmotionActionStep.setEmotionActionPlanId((Long) s.get("emotionActionPlanId"));
            newEmotionActionStep.setActionText((String) s.get("actionText"));
            newEmotionActionStep.setDomain((String) s.get("domain"));
            newEmotionActionStep.setStepNumber((int) s.get("stepNumber"));
            newEmotionActionStep.setActive((boolean) s.get("active"));
            newEmotionActionStep.setActionDay((int) s.get("actionDay"));
            newEmotionActionStep.setTitle((String) s.get("title"));
            emotionActionStepService.addAnEmotionActionStep(newEmotionActionStep);
        }
        return emotionActionPlanRepository.findById(createdPlanId).orElseThrow(() -> new EntityNotFoundException("Action Plan not found"));
    }

    public List<EmotionActionStep> getAllStepsForPlanById(Long planId){
        List<EmotionActionStep> stepsToReturn = emotionActionStepService.getAllStepsForPlan(planId);
        return stepsToReturn;
    }

    public EmotionActionPlan findPlanById(Long planId) {
        EmotionActionPlan planToReturn = emotionActionPlanRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        return planToReturn;
    }


}
