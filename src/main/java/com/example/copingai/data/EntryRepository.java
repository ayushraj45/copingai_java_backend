package com.example.copingai.data;

import com.example.copingai.entities.Entry;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends ListCrudRepository<Entry, Long> {
}

