package com.example.copingai.service;

import com.example.copingai.data.EntryRepository;
import com.example.copingai.entities.Entry;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EntryService {

    private final EntryRepository entryRepository;

    @Autowired
    public EntryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public List<Entry> findAllEntries() {
        return entryRepository.findAll();
    }

    public int getQuestionCountForAnEntry(Long entryId) {
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        return entry.getQuestionCount();
    }

    public List<String> getQuestionListForAnEntry(Long entryId) {
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        return entry.getQuestions();
    }

    public List<String> getAnswerListForAnEntry(Long entryId) {
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        return entry.getQuestions();
    }
}
