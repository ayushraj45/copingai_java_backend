package com.example.copingai.service;

import com.example.copingai.data.AssessmentQuestionRepository;
import com.example.copingai.data.MHAssessmentRepository;
import com.example.copingai.data.UserRepository;
import com.example.copingai.entities.AssessmentQuestion;
import com.example.copingai.entities.MHAssessment;
import com.example.copingai.entities.User;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MHAssessmentService {

    private final MHAssessmentRepository mhAssessmentRepository;
    public UserRepository userRepository;

    public AssessmentQuestionRepository assessmentQuestionRepository;

    @Autowired
    AssessmentQuestionService assessmentQuestionService;

    @Autowired
    EmailService emailService;

    @Autowired
    public MHAssessmentService (MHAssessmentRepository mhAssessmentRepository, UserRepository userRepository, AssessmentQuestionRepository assessmentQuestionRepository){
        this.mhAssessmentRepository = mhAssessmentRepository;
        this.userRepository = userRepository;
        this.assessmentQuestionRepository = assessmentQuestionRepository;
    }

    public MHAssessment createAnAssessment (MHAssessment assessment) {
        if (assessment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to add cannot be null");
        } else if (assessment.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to add cannot contain an id");
        }else if (assessment.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to must have a user ID");
        }
            MHAssessment createdAssessment = mhAssessmentRepository.save(assessment);
            Long createdAssessmentId = createdAssessment.getId();
            User user = userRepository.findById(createdAssessment.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
            user.addAnAssessment(createdAssessmentId);
            userRepository.save(user);
            List<Map<String, Object>> questions = Arrays.asList(
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel that the work I do is meaningful.", "domain", "Purpose & Fulfillment"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel genuinely connected to people around me.", "domain", "Relationships / Social"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I can recognize my emotions without being overwhelmed by them.", "domain", "Emotional Regulation"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel mentally exhausted at the end of most days.", "domain", "Stress & Burnout"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I wake up feeling rested most days.", "domain", "Sleep / Energy"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel good about who I am.", "domain", "Self-esteem / Self-talk"),

                    Map.of("assessmentId", createdAssessmentId, "question", "I have clear goals that give me a sense of direction.", "domain", "Purpose & Fulfillment"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I have at least one person I can confide in without judgment.", "domain", "Relationships / Social"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I have strategies to calm myself down when I’m upset.", "domain", "Emotional Regulation"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I find it difficult to relax, even during downtime.", "domain", "Stress & Burnout"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I fall asleep easily and stay asleep through the night.", "domain", "Sleep / Energy"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I talk to myself kindly, even when I make mistakes.", "domain", "Self-esteem / Self-talk"),

                    Map.of("assessmentId", createdAssessmentId, "question", "I often reflect on the bigger picture of my life.", "domain", "Purpose & Fulfillment"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I regularly engage in meaningful social interactions.", "domain", "Relationships / Social"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I bounce back quickly after emotionally difficult experiences.", "domain", "Emotional Regulation"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel like I’m constantly under pressure.", "domain", "Stress & Burnout"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I have consistent energy throughout the day.", "domain", "Sleep / Energy"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I believe in my ability to handle challenges.", "domain", "Self-esteem / Self-talk"),

                    Map.of("assessmentId", createdAssessmentId, "question", "I feel fulfilled with how I spend my time each day.", "domain", "Purpose & Fulfillment"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel supported by my friends or community.", "domain", "Relationships / Social"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I can express my feelings in a healthy way.", "domain", "Emotional Regulation"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I often feel overwhelmed by responsibilities.", "domain", "Stress & Burnout"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel refreshed after sleep.", "domain", "Sleep / Energy"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I accept myself, even when I don’t meet my own expectations.", "domain", "Self-esteem / Self-talk"),

                    Map.of("assessmentId", createdAssessmentId, "question", "I feel excited about what the future holds for me.", "domain", "Purpose & Fulfillment"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel like I belong somewhere.", "domain", "Relationships / Social"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel in control of how I react to emotional situations.", "domain", "Emotional Regulation"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I find it hard to stay motivated due to stress.", "domain", "Stress & Burnout"),
                    Map.of("assessmentId", createdAssessmentId, "question", "My sleep habits support my mental and physical health.", "domain", "Sleep / Energy"),
                    Map.of("assessmentId", createdAssessmentId, "question", "I feel confident in expressing myself to others.", "domain", "Self-esteem / Self-talk")
            );

            for (Map<String, Object> q : questions) {
                AssessmentQuestion question = new AssessmentQuestion();
                question.setAssessmentId((Long) q.get("assessmentId"));
                question.setQuestionText((String) q.get("question"));
                question.setQDomain((String) q.get("domain"));
                assessmentQuestionService.addAnAssessmentQuestion(question);
            }
        return mhAssessmentRepository.findById(createdAssessmentId).orElseThrow(() -> new EntityNotFoundException("Assessment not found"));
    }

    public List<MHAssessment> findAllAssessments() { return mhAssessmentRepository.findAll();}

    public String uploadAnswersAndCalculateResults(Long assessmentId, Map<Long, Integer> answers) {
        // Fetch the assessment using orElseThrow

        if (assessmentId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assessment to update must have an id");
        }

        MHAssessment assessment = mhAssessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("MHAssessment with ID " + assessmentId + " not found."));

        // 1. Upload Answers
        for (Map.Entry<Long, Integer> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            Integer answer = entry.getValue();

            // Fetch the question using orElseThrow
            AssessmentQuestion question = assessmentQuestionService.findAssessmentQuestionById(questionId);

            question.setQAnswer(answer); // Update the answer
            assessmentQuestionRepository.save(question); // Save the updated question
        }

        // 2. Calculate Results
        return calculateResults(assessment); // Call the calculation function
    }

    private String calculateResults(MHAssessment assessment) {
        List<AssessmentQuestion> questions = assessmentQuestionRepository.findByAssessmentId(assessment.getId());

        if (questions.isEmpty()) {
            // Handle case where no questions are associated with the assessment
            assessment.setAverageScore(0);
            assessment.setPurposeScore(0);
            assessment.setRelationshipsScore(0);
            assessment.setStressScore(0);
            assessment.setEmotionalRegulationScore(0);
            assessment.setEnergyScore(0);
            assessment.setSelfEsteemScore(0);
            mhAssessmentRepository.save(assessment);
            return "No questions found for assessment."; // Or handle as appropriate
        }

        int totalRawScore = questions.stream()
                .mapToInt(AssessmentQuestion::getQAnswer)
                .sum();

        // Scale total score out of 100 (max raw score is 30 questions * 5 = 150)
        int totalScaledScore = (int) Math.round((double) totalRawScore / 150 * 100);
        assessment.setAverageScore(totalScaledScore);
        User user = userRepository.findById(assessment.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        user.setCurrentDomainScore(totalScaledScore);
        userRepository.save(user);

        // Calculate Domain Scores
        Map<String, Integer> domainRawScores = new HashMap<>();
        // Initialize domain scores to 0
        domainRawScores.put("Purpose & Fulfillment", 0);
        domainRawScores.put("Relationships / Social", 0);
        domainRawScores.put("Emotional Regulation", 0);
        domainRawScores.put("Stress & Burnout", 0);
        domainRawScores.put("Sleep / Energy", 0);
        domainRawScores.put("Self-esteem / Self-talk", 0);

        for (AssessmentQuestion question : questions) {
            String domain = question.getQDomain();
            // Ensure domain is not null and is one of the expected domains before adding score
            if (domain != null && domainRawScores.containsKey(domain)) {
                domainRawScores.put(domain, domainRawScores.get(domain) + question.getQAnswer());
            } else System.out.println("Domain is Null or not found");
        }

        // Scale domain scores out of 100 (max raw score per domain is 5 questions * 5 = 25)
        Map<String, Integer> domainScaledScores = new HashMap<>(); // Switched to HashMap
        String lowestDomain = null;
        int lowestScore = 101; // Initialize with a value higher than max possible score

        for (Map.Entry<String, Integer> entry : domainRawScores.entrySet()) {
            String domain = entry.getKey();
            Integer rawScore = entry.getValue();
            int scaledScore = (int) Math.round((double) rawScore / 25 * 100); // Scaling based on 25 max raw score per domain
            domainScaledScores.put(domain, scaledScore);

            // Update the corresponding field in the MHAssessment entity
            switch (domain) {
                case "Purpose & Fulfillment":
                    assessment.setPurposeScore(scaledScore);
                    break;
                case "Relationships / Social":
                    assessment.setRelationshipsScore(scaledScore);
                    break;
                case "Stress & Burnout":
                    assessment.setStressScore(scaledScore);
                    break;
                case "Emotional Regulation":
                    assessment.setEmotionalRegulationScore(scaledScore);
                    break;
                case "Sleep / Energy":
                    assessment.setEnergyScore(scaledScore);
                    break;
                case "Self-esteem / Self-talk":
                    assessment.setSelfEsteemScore(scaledScore);
                    break;
                // Add default or error handling if domain doesn't match
            }

            // Check for the lowest score
            if (scaledScore < lowestScore) {
                lowestScore = scaledScore;
                lowestDomain = domain;
            } else if (scaledScore == lowestScore) {
                // Optional: handle ties if needed, e.g., keep the first one encountered
            }
        }

        // Save the updated MHAssessment entity with all scores
        MHAssessment scoredAssessment = mhAssessmentRepository.save(assessment);

        Map<String, Integer> templateData = new HashMap<>();
        templateData.put("purposeScore", domainScaledScores.getOrDefault("Purpose & Fulfillment", 0));
        templateData.put("relationshipsScore", domainScaledScores.getOrDefault("Relationships / Social", 0));
        templateData.put("emotionalRegulationScore", domainScaledScores.getOrDefault("Emotional Regulation", 0));
        templateData.put("stressScore", domainScaledScores.getOrDefault("Stress & Burnout", 0));
        templateData.put("energyScore", domainScaledScores.getOrDefault("Sleep / Energy", 0));
        templateData.put("selfEsteemScore", domainScaledScores.getOrDefault("Self-esteem / Self-talk", 0));
        templateData.put("averageScore", scoredAssessment.getAverageScore());

        System.out.println("Average Score at template" + (templateData.get("averageScore")));

        String email = scoredAssessment.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            String resultHtml;

        try {
            resultHtml = generateAssessmentResultHtml(templateData, lowestDomain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("we are at TRY to send the email");
            emailService.sendAssessmentResults(
                    email,
                    "Your Coping.AI Mental Health Assessment Results",
                    resultHtml
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }}

        return lowestDomain;
    }

    private String loadTemplate() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("result_template.html")) {
            if (inputStream == null) {
                throw new IOException("Template file not found!");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String generateAssessmentResultHtml(Map<String, Integer> data, String lowest) throws IOException {
        String template = loadTemplate();

        // Retrieve scores from your data object (e.g., a DTO from your service)
        int averageScore = data.get("averageScore");
        System.out.println("Average score here in generate: " + averageScore);
        // --- Calculations for the Circular Progress Bar ---
        final double radius = 74; // (160px size - 12px stroke * 2) / 2
        final double circumference = 2 * Math.PI * radius;
        double offset = circumference - (averageScore / 100.0) * circumference;

        // --- String replacements ---
        String html = template
                .replace("{{averageScore}}", String.valueOf(averageScore))
                .replace("{{circumference}}", String.valueOf(circumference))
                .replace("{{circularProgressOffset}}", String.valueOf(offset))
                .replace("{{purposeScore}}", String.valueOf(data.getOrDefault("purposeScore", 0)))
                .replace("{{relationshipsScore}}", String.valueOf(data.getOrDefault("relationshipsScore", 0)))
                .replace("{{stressScore}}", String.valueOf(data.getOrDefault("stressScore", 0)))
                .replace("{{emotionalRegulationScore}}", String.valueOf(data.getOrDefault("emotionalRegulationScore", 0)))
                .replace("{{energyScore}}", String.valueOf(data.getOrDefault("energyScore", 0)))
                .replace("{{selfEsteemScore}}", String.valueOf(data.getOrDefault("selfEsteemScore", 0)))
                .replace("{{suggestedPath}}", lowest);

        return html;
    }

    public List<AssessmentQuestion> getQuestionsById (Long assessmentId) {
        MHAssessment assessment = mhAssessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("MHAssessment with ID " + assessmentId + " not found."));
        List<AssessmentQuestion> questionsToReturn = new ArrayList<>();
        questionsToReturn = assessmentQuestionRepository.findByAssessmentId(assessmentId);
        return questionsToReturn;
    }

    public MHAssessment getAssessmentById (Long assessmentId){
       return mhAssessmentRepository.findById(assessmentId)
               .orElseThrow(() -> new EntityNotFoundException("MHAssessment with ID " + assessmentId + " not found."));
    }

    public void addEmailToAssessment(Long assessmentId, String email) {
        MHAssessment assessment = mhAssessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new EntityNotFoundException("MHAssessment with ID " + assessmentId + " not found."));
        assessment.setEmail(email);
        mhAssessmentRepository.save(assessment);


    }
}
