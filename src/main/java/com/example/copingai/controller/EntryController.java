package com.example.copingai.controller;


import com.example.copingai.entities.Entry;
import com.example.copingai.service.EntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Entry Api")
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;

    @Autowired
    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @Operation(summary = "Get a list of all entries", description = "Returns a list of all entries")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> getAllEntries() {
        return entryService.findAllEntries();
    }

    @Operation(summary = "Get an entrys Question Count", description = "Returns the question count of an entry")
    @GetMapping("/{entryId}/qCount")
    @ResponseStatus(HttpStatus.OK)
    public int getEntryQuestionCount(@PathVariable Long entryId) {
        return entryService.getQuestionCountForAnEntry(entryId);
    }
    @Operation(summary = "Get an entrys Question ", description = "Returns the questions of an entry")
    @GetMapping("/{entryId}/questions")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getEntryQuestionsList(@PathVariable Long entryId) {
        return entryService.getQuestionListForAnEntry(entryId);
    }
    @Operation(summary = "Get an entrys Answer", description = "Returns the list of answers of an entry")
    @GetMapping("/{entryId}/answers")
    @ResponseStatus(HttpStatus.OK)
    public List<String>  getEntryAnswerList(@PathVariable Long entryId) {
        return entryService.getAnswerListForAnEntry(entryId);
    }


}



