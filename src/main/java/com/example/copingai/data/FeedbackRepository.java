package com.example.copingai.data;
import com.example.copingai.entities.Feedback;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends ListCrudRepository<Feedback, Long> {
}
