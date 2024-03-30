package com.example.copingai.service;

import com.example.copingai.data.EntryRepository;
import com.example.copingai.data.UserRepository;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    public UserRepository userRepository;

    @Autowired
    public EntryService(EntryRepository entryRepository, UserRepository userRepository) {
        this.entryRepository = entryRepository;
        this.userRepository=userRepository;
    }

    public List<Entry> findAllEntries() {
        return entryRepository.findAll();
    }

    public Entry findAnEntryById(Long entryId) {
        return entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));}
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
        return entry.getAnswers();
    }

    public void saveEntryQuestion(String question, Long entryId){
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        entry.addQuestion(question);
        entryRepository.save(entry);
    }

    public void saveEntryAnswer(String answer, Long entryId){
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        entry.addAnswer(answer);
        entryRepository.save(entry);
    }

    public void increaseQuestionCount(Long entryId){
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        int newQCount = entry.getQuestionCount()+1;
        entry.setQuestionCount(newQCount);
        entryRepository.save(entry);
    }

    public Entry addAnEntry(Entry entry) {
        if (entry == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to add cannot be null");
        } else if (entry.getId() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to add cannot contain an id");
        }   else if(entry.getUserId() == null){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to add must have an user ID");
        }
        User user = userRepository.findById(entry.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        entry.setUserId(user.getId());
        Entry returnEntry = entryRepository.save(entry);
        user.addAnEntry(returnEntry.getId());
        userRepository.save(user);
        return returnEntry;
    }

    public Entry updateAnEntry(Entry entry) {
        if (entry == null || entry.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to update must have an id");
        } else if (!entryRepository.existsById(entry.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry to update cannot be found");
        }else if(entry.getUserId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to add must have an user ID");
        }
        User user = userRepository.findById(entry.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return entryRepository.save(entry);
    }

    public void deleteAnEntry(Long entryId) {
        if (entryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to delete must have an id");
        } else if (!entryRepository.existsById(entryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry to delete cannot be found");
        } else {
            User user = userRepository.findById(entryId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if(user.deleteAnEntry(entryId)) {entryRepository.deleteById(entryId);}
            else new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry could not be deleted");
        }

    }
}
