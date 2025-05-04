package com.example.copingai.data;

import com.example.copingai.entities.Entry;
import com.example.copingai.entities.MHAssessment;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MHAssessmentRepository extends ListCrudRepository<MHAssessment, Long> {
}
