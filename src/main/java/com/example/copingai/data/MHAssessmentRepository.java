package com.example.copingai.data;

import com.example.copingai.entities.EmotionActionStep;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.MHAssessment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MHAssessmentRepository extends ListCrudRepository<MHAssessment, Long> {

    @Query("SELECT u FROM MHAssessment u WHERE u.email = :email")
    List<MHAssessment> findAllAssessmentByEmail(@Param("email") String email);

}
