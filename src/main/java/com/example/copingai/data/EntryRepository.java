package com.example.copingai.data;

import com.example.copingai.entities.Entry;
import com.example.copingai.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends ListCrudRepository<Entry, Long> {

    @Query("SELECT sum(wordCount) FROM Entry e WHERE e.userId = :userId")
    long getTotalWordCount(@Param("userId") Long userId);

}

