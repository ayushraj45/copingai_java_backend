package com.example.copingai.controller;

import com.example.copingai.entities.EmotionActionPlan;
import com.example.copingai.entities.EmotionActionStep;
import com.example.copingai.service.EmotionActionPlanService;
import com.example.copingai.service.EmotionActionStepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Action Step Api")
@RequestMapping("/actionStep")
public class EmotionActionStepController {

    EmotionActionStepService emotionActionStepService;
    EmotionActionPlanService emotionActionPlanService;

    @Autowired

    public EmotionActionStepController(EmotionActionStepService emotionActionStepService, EmotionActionPlanService emotionActionPlanService) {
        this.emotionActionStepService = emotionActionStepService;
        this.emotionActionPlanService = emotionActionPlanService;
    }

    @Operation(summary = "Get an action step by id", description = "Returns an action step by ID")
    @GetMapping("/get/{stepId}")
    @ResponseStatus(HttpStatus.OK)
    public EmotionActionStep getActionStepById(@PathVariable Long stepId) {
        return emotionActionStepService.findStepById(stepId);
    }

    @Operation(summary = "Update an Action Step", description = "Update an Action Step")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmotionActionStep updateEmotionActionStep(@RequestBody EmotionActionStep emotionActionStep) {
        return emotionActionStepService.updateAnEmotionActionStep(emotionActionStep);
    }
}
