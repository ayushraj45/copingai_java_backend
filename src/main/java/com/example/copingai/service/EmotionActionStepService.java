package com.example.copingai.service;

import com.example.copingai.data.EmotionActionPlanRepository;
import com.example.copingai.data.EmotionActionStepRepository;
import com.example.copingai.data.UserRepository;
import com.example.copingai.entities.EmotionActionPlan;
import com.example.copingai.entities.EmotionActionStep;
import com.example.copingai.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmotionActionStepService {
    private final EmotionActionStepRepository emotionActionStepRepository;
    public EmotionActionPlanRepository emotionActionPlanRepository;

    public UserRepository userRepository;
    @Autowired
    public EmotionActionStepService(EmotionActionStepRepository emotionActionStepRepository, EmotionActionPlanRepository emotionActionPlanRepository, UserRepository userRepository) {
        this.emotionActionStepRepository = emotionActionStepRepository;
        this.emotionActionPlanRepository = emotionActionPlanRepository;
        this.userRepository = userRepository;
    }

    public EmotionActionStep addAnEmotionActionStep (EmotionActionStep emotionActionStep) {
        if (emotionActionStep == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question to add cannot be null");
        } else if (emotionActionStep.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question to add cannot contain an id");
        } else if (emotionActionStep.getEmotionActionPlanId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question to add must have an assessment id");
        }
        EmotionActionPlan emotionActionPlan = emotionActionPlanRepository.findById(emotionActionStep.getEmotionActionPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        EmotionActionStep actionStep = emotionActionStepRepository.save(emotionActionStep);
        emotionActionPlan.addActionStep(actionStep.getId());
        emotionActionPlanRepository.save(emotionActionPlan);
        return actionStep;
    }

    public List<EmotionActionStep> getAllStepsForPlan(Long planId){
        EmotionActionPlan plan= emotionActionPlanRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
        List<EmotionActionStep> stepsToReturn = new ArrayList<>();
        stepsToReturn = emotionActionStepRepository.findAllSteps(planId);
        return stepsToReturn;
    }

    public EmotionActionStep findStepById(Long stepId) {
        return emotionActionStepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Step not found"));
    }


    public EmotionActionStep updateAnEmotionActionStep(EmotionActionStep emotionActionStep) {
        if (emotionActionStep == null || emotionActionStep.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EmotionActionStep to update must have an id");
        } else if (!emotionActionStepRepository.existsById(emotionActionStep.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "EmotionActionStep to update cannot be found");
        }else if(emotionActionStep.getEmotionActionPlanId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EmotionActionStep to add must have an Plan ID");
        }

        EmotionActionPlan plan= emotionActionPlanRepository.findById(emotionActionStep.getEmotionActionPlanId())
                .orElseThrow(() -> new EntityNotFoundException("Plan not found"));

        emotionActionStep.setActive(false);
        emotionActionStep.setCompleted(true);
        updateUserWordCount(emotionActionStep.getId());
        emotionActionStepRepository.save(emotionActionStep); // Make sure to save the updated step

        // --- Logic to set the next step ---

        List<Long> stepIds = plan.getEmotionActionStepIds(); // Get the ordered list of step IDs
        if (stepIds == null || stepIds.isEmpty()) {
            System.err.println("Warning: Plan with ID " + plan.getId() + " has no emotionActionStepIds.");
            plan.setCurrentStep(-1); // Example: Use -1 to indicate no active step
        } else {
            int currentIndex = stepIds.indexOf(emotionActionStep.getId()); // Find the index of the current step's ID

            if (currentIndex == -1) {
                System.err.println("Warning: Current step ID " + emotionActionStep.getId() + " not found in plan's step list for plan ID " + plan.getId());
                plan.setCurrentStep(-1); // Example: Indicate an issue or no active step
            } else if (currentIndex < stepIds.size() - 1) {
                Long nextStepId = stepIds.get(currentIndex + 1);
                EmotionActionStep nextStep = emotionActionStepRepository.findById(nextStepId)
                        .orElseThrow(() -> new EntityNotFoundException("Step not found"));
                nextStep.setActive(true);
                emotionActionStepRepository.save(nextStep);
                int stepNumber = nextStep.getStepNumber();
                plan.setCurrentStep(stepNumber); // Assuming currentStep is an int field storing the ID
            } else {
                plan.setCurrentStep(-1); // Example: Use -1 to indicate completion
            }
        }
        emotionActionPlanRepository.save(plan);

        return emotionActionStep;
    }

    public void updateUserWordCount(Long stepId){

        java.util.function.Function<String, Integer> simpleWordCount = (text) -> {
            if (text == null || text.trim().isEmpty()) {
                return 0;
            }
            // Split by one or more whitespace characters
            String[] words = text.trim().split("\\s+");
            // The split method might return an array with one empty string if the input was just whitespace,
            // so we check the length of the first element if the array is not empty.
            if (words.length == 1 && words[0].isEmpty()) {
                return 0;
            }
            return words.length;
        };

        EmotionActionStep step = emotionActionStepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Step not found"));
        String wordCountText = step.getTextContent();

        Long wordLength = Long.valueOf(simpleWordCount.apply(wordCountText));
        Long planId = step.getEmotionActionPlanId();
        EmotionActionPlan plan = emotionActionPlanRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found"));
        User user = userRepository.findById(plan.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Long words = user.getWordsWritten();
        words += wordLength;
        user.setWordsWritten(words);
        userRepository.save(user);
    }
    public Long getPlanIdByStepId (Long stepId) {
        return emotionActionStepRepository.findPlanIdByStepId(stepId);
    }
}
