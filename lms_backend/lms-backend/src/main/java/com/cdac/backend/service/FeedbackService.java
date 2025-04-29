package com.cdac.backend.service;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.backend.model.Feedback;
import com.cdac.backend.repository.FeedbackRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Save feedback
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Get all feedback
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    // Get feedback by ID
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    // Delete feedback by ID
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}
