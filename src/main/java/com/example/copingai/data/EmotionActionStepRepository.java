package com.example.copingai.data;

import com.example.copingai.entities.EmotionActionStep;
import com.example.copingai.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmotionActionStepRepository extends ListCrudRepository<EmotionActionStep, Long> {
    @Query("SELECT u FROM EmotionActionStep u WHERE u.emotionActionPlanId = :emotionActionPlanId")
    List<EmotionActionStep> findAllSteps(@Param("emotionActionPlanId") Long emotionActionPlanId);

    @Query("Select emotionActionPlanId FROM EmotionActionStep s WHERE s.emotionActionPlanId = :emotionActionPlanId ")
    Long findPlanIdByStepId(@Param("emotionActionPlanId") Long emotionActionPlanId);
}
