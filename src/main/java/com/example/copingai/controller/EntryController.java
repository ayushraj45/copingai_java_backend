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
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Entry Api")
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;

    @Autowired
    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @Operation(summary = "Get an entry by ID", description = "Returns an entry from ID")
    @GetMapping("/{entryID}")
    @ResponseStatus(HttpStatus.OK)
    public Entry getEntryById(@PathVariable Long entryID) {
        return entryService.findAnEntryById(entryID);
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

    @Operation(summary = "Get an entrys Journaling prompt ", description = "Returns the Journaling Prompt of an entry")
    @GetMapping("/{entryId}/journalingprompt")
    @ResponseStatus(HttpStatus.OK)
    public String getEntryJournalingPrompt(@PathVariable Long entryId) {
        return entryService.getJournalingPromptForAnEntry(entryId);
    }
    @Operation(summary = "Get an entrys Answer", description = "Returns the list of answers of an entry")
    @GetMapping("/{entryId}/answers")
    @ResponseStatus(HttpStatus.OK)
    public List<String>  getEntryAnswerList(@PathVariable Long entryId) {
        return entryService.getAnswerListForAnEntry(entryId);
    }

    @Operation(summary = "Add an Entry", description = "Add an entry")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entry addEntry(@RequestBody Entry entry) {
        return entryService.addAnEntry(entry);
    }

    @Operation(summary = "Update an Entry", description = "Update an entry")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entry updateEntry(@RequestBody Entry entry) {
        return entryService.updateAnEntry(entry);
    }

    @Operation(summary = "Delete an Entry", description = "Delete an entry")
    @DeleteMapping("/user/{userId}/{entryId}/")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteEntry(@PathVariable Long entryId,@PathVariable Long userId) {
         entryService.deleteAnEntry(entryId, userId);
    }
}



