package com.example.copingai.service;

import com.example.copingai.data.FeedbackRepository;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;


    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> findAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback addAFeedback (Feedback feedback){
        feedbackRepository.save(feedback);
        return feedback;
    }

    public void deleteAllFeedback () {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        feedbackRepository.deleteAll(feedbacks);
    }
}
