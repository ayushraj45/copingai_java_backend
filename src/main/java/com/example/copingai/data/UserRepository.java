package com.example.copingai.data;

import com.example.copingai.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ListCrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.providerId = :providerId")
    User findByProviderId(@Param("providerId") String providerId);
}
