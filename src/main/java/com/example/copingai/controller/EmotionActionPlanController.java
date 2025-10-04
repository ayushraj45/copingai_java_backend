package com.example.copingai.controller;

import com.example.copingai.entities.EmotionActionPlan;
import com.example.copingai.entities.EmotionActionStep;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.MHAssessment;
import com.example.copingai.service.EmotionActionPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Action Plan Api")
@RequestMapping("/actionplan")
public class EmotionActionPlanController {

    EmotionActionPlanService emotionActionPlanService;

    @Autowired
    public EmotionActionPlanController(EmotionActionPlanService emotionActionPlanService) {
        this.emotionActionPlanService = emotionActionPlanService;
    }

    @Operation(summary = "Get a list of all action plans", description = "Returns a list of all action plans")
    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<EmotionActionPlan> getAllActionPlans() {
        return emotionActionPlanService.findAllActionPlans();
    }

    @Operation(summary = "Get a list of all action plans", description = "Returns a list of all action plans")
    @GetMapping("/get/{planId}")
    @ResponseStatus(HttpStatus.OK)
    public EmotionActionPlan getActionPlanById(@PathVariable Long planId) {
        return emotionActionPlanService.findPlanById(planId);
    }

    @Operation(summary = "Get all steps by assessment ID", description = "Returns all steps from assessment ID")
    @GetMapping("/{planId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmotionActionStep> getAllStepsById(@PathVariable Long planId) {
        return emotionActionPlanService.getAllStepsForPlanById(planId);
    }

    @Operation(summary = "Add an action plan", description = "Add an action plan")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmotionActionPlan addAnActionActionPlan(@RequestBody EmotionActionPlan emotionActionPlan) {
        return emotionActionPlanService.createAnActionPlan(emotionActionPlan);
    }
}
