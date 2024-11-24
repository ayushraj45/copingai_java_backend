package com.example.copingai.controller;

import com.example.copingai.entities.Entry;
import com.example.copingai.entities.Feedback;
import com.example.copingai.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Feedback Api")
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Get a list of all feedbacks", description = "Returns a list of all feedbacks")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.findAllFeedbacks();
    }

    @Operation(summary = "Add an Feedback", description = "Add an Feedback")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.addAFeedback(feedback);
    }

    @Operation(summary = "Delete all Feedbacks", description = "Delete all Feedback")
    @DeleteMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteEntry(@PathVariable Long entryId,@PathVariable Long userId) {
        feedbackService.deleteAllFeedback();
    }
}
