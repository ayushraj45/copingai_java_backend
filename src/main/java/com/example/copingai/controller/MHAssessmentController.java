package com.example.copingai.controller;

import com.example.copingai.entities.AssessmentQuestion;
import com.example.copingai.entities.MHAssessment;
import com.example.copingai.entities.User;
import com.example.copingai.service.MHAssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Assessment Api")
@RequestMapping("/assessment")
public class MHAssessmentController {

    MHAssessmentService mhAssessmentService;
    @Autowired
    public MHAssessmentController(MHAssessmentService mhAssessmentService) {
        this.mhAssessmentService = mhAssessmentService;
    }

    @Operation(summary = "Get a list of all assessments", description = "Returns a list of all assessments")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MHAssessment> getAllAssessments() {
        return mhAssessmentService.findAllAssessments();
    }

    @Operation(summary = "Get an assessment by id", description = "Returns an assessment by id")
    @GetMapping("/{assessmentId}")
    @ResponseStatus(HttpStatus.OK)
    public MHAssessment getAssessmentDataById(@PathVariable Long assessmentId) {
        return mhAssessmentService.getAssessmentById(assessmentId);
    }

    @Operation(summary = "Get a list of all assessment questions with assessment ID ", description = "Returns a list of all assessment questions")
    @GetMapping("/getQuestions")
    @ResponseStatus(HttpStatus.OK)
    public List<AssessmentQuestion> getAllAssessmentQuestions(@RequestParam Long assessmentId) {
        return mhAssessmentService.getQuestionsById(assessmentId);
    }

    @Operation(summary = "Add an Assessment", description = "Add an Assessment")
    @PostMapping("/createAssessment")
    @ResponseStatus(HttpStatus.CREATED)
    public MHAssessment addAnAssessment(@RequestBody MHAssessment mhAssessment) {
        return mhAssessmentService.createAnAssessment(mhAssessment);
    }

    @Operation(summary = "Calculate Assessment Results", description = "Calculate Assessment Results")
    @PostMapping("/{assessmentId}/calculate-results") // Example path
    @ResponseStatus(HttpStatus.OK) // Use OK for successful processing, CREATED is typically for creating new resources
    public String calculateResults(
            @PathVariable Long assessmentId, // Get ID from path
            @RequestBody Map<Long, Integer> answers // Get answers from the request body
    ) {
        return mhAssessmentService.uploadAnswersAndCalculateResults(assessmentId, answers);
    }

    @Operation(summary = "Add email to an Assessment", description = "Adding user email from web assessments.")
    @PostMapping("/{assessmentId}/addEmail/{email}") // Example path
    @ResponseStatus(HttpStatus.OK) // Use OK for successful processing, CREATED is typically for creating new resources
    public void addEmail(
            @PathVariable Long assessmentId, // Get ID from path
            @PathVariable String email // Get answers from the request body
    ) {
        mhAssessmentService.addEmailToAssessment(assessmentId, email);
    }
}
