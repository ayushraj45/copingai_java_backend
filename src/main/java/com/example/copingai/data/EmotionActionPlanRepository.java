package com.example.copingai.data;

import com.example.copingai.entities.EmotionActionPlan;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionActionPlanRepository extends ListCrudRepository<EmotionActionPlan, Long> {
}
