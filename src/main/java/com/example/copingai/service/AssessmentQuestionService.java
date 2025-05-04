package com.example.copingai.service;

import com.example.copingai.data.AssessmentQuestionRepository;
import com.example.copingai.data.MHAssessmentRepository;
import com.example.copingai.entities.AssessmentQuestion;
import com.example.copingai.entities.MHAssessment;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AssessmentQuestionService {

    private final AssessmentQuestionRepository assessmentQuestionRepository;

    public MHAssessmentRepository mhAssessmentRepository;

    @Autowired
    public AssessmentQuestionService (AssessmentQuestionRepository assessmentQuestionRepository, MHAssessmentRepository mhAssessmentRepository) {
        this.assessmentQuestionRepository = assessmentQuestionRepository;
        this.mhAssessmentRepository = mhAssessmentRepository;
    }

    public AssessmentQuestion addAnAssessmentQuestion ( AssessmentQuestion assessmentQuestion) {
        if (assessmentQuestion == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question to add cannot be null");
        } else if (assessmentQuestion.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question to add cannot contain an id");
        } else if (assessmentQuestion.getAssessmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question to add must have an assessment id");
        }
        MHAssessment assessment = mhAssessmentRepository.findById(assessmentQuestion.getAssessmentId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));

        AssessmentQuestion question = assessmentQuestionRepository.save(assessmentQuestion);
        assessment.addAnAssessmentQuestion(question.getId());
        mhAssessmentRepository.save(assessment);
        return question;
    }

    public AssessmentQuestion findAssessmentQuestionById (Long id) {
        return assessmentQuestionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MHAssessment Question with ID " + id + " not found."));
    }

}
