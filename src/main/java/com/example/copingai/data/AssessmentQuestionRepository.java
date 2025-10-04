package com.example.copingai.data;

import com.example.copingai.entities.AssessmentQuestion;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionRepository extends ListCrudRepository<AssessmentQuestion, Long> {

    @Query("SELECT q FROM AssessmentQuestion q WHERE q.assessmentId = :assessmentId")
    List<AssessmentQuestion> findByAssessmentId(@Param("assessmentId") Long assessmentId);

}
