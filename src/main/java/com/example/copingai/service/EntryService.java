package com.example.copingai.service;

import com.example.copingai.data.EntryRepository;
import com.example.copingai.data.UserRepository;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
public class EntryService {

    private final EntryRepository entryRepository;
    public UserRepository userRepository;

    @Autowired
    private ExpoPushNotificationService expoPushNotificationService;

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


    public void deleteEntryAllQuestionsAndAnswers(Long entryId) {
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        List<String> emptyList = entry.getQuestions();
        List<String> emptyAnsList = entry.getQuestions();
        emptyList.remove(0);
        emptyAnsList.remove(0);
        entry.setAnswers(emptyAnsList);
        entry.setQuestions(emptyList);
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
        int freeEntries = user.getRemainingFreeEntries();
        String subscriptionStatus = user.getSubscriptionStatus();
        if(subscriptionStatus != "free" || subscriptionStatus != ""){
            entry.setUserId(user.getId());
            entry.setCreatedAt(LocalDateTime.now());
            Entry returnEntry = entryRepository.save(entry);
            user.addAnEntry(returnEntry.getId());
            userRepository.save(user);
            setEntryStreak(user.getId());
            entryNotification(entry.getUserId());
            return returnEntry;
        }
        else if(subscriptionStatus == "free" || subscriptionStatus == "" && freeEntries > 0 ){
            entry.setUserId(user.getId());
            entry.setCreatedAt(LocalDateTime.now());
            Entry returnEntry = entryRepository.save(entry);
            user.addAnEntry(returnEntry.getId());
            user.setRemainingFreeEntries(freeEntries-1);
            userRepository.save(user);
            setEntryStreak(user.getId());
            entryNotification(entry.getUserId());
            return returnEntry;
        }
        else
        { throw new RuntimeException ("You've used all your free entries for this week.");}
    }

    private void setEntryStreak (Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastEntryTime = user.getLastEntryTime();

        if (lastEntryTime == null) {
            user.setStreak(1);
        } else {
            Duration duration = Duration.between(lastEntryTime, now);
            long hours = duration.toHours();
            if (hours >= 24 && hours < 48) {
                user.setStreak(user.getStreak() + 1);
            } else if (hours >= 48) {
                user.setStreak(1);
            }
        }
        user.setLastEntryTime(now);
        userRepository.save(user);
    }

    private void setStreakToZero (Long entryId) {
        User user = userRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setStreak(0);
        userRepository.save(user);
    }

    private void entryNotification (Long userId) {
        User entryMaker = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        int entries = (entryMaker.getEntryIds()).size();
        if(entries == 1) {
            expoPushNotificationService.sendPushNotification(entryMaker.getFirebaseToken(), "You did it!", "Congratulations on making your first entry!");
        }
        else if(entries == 10) {
            expoPushNotificationService.sendPushNotification(entryMaker.getFirebaseToken(), "It's a milestone!", "You've made 10 entries, keep the streak going!");
        }
        else if(entries == 25) {
            expoPushNotificationService.sendPushNotification(entryMaker.getFirebaseToken(), "That's significant!", "Thank you for making 25 entries!");
        }
        else if(entries == 50) {
            expoPushNotificationService.sendPushNotification(entryMaker.getFirebaseToken(), "You are unstoppable!", "99% of journal writers don't reach their 50th entry! You've done it! Keep it up!");
        }
        else return;
    }

    @Scheduled (cron = "0 0 */5 * * *")
    @Transactional
    protected  void sendStreakNotification () {
        System.out.println("Scheduled task triggered at: " + LocalDateTime.now()); // Log to verify it's triggered
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Long> entryIds = user.getEntryIds();
            if (entryIds == null || entryIds.isEmpty() || user.getFirebaseToken() == null || user.getFirebaseToken().trim().isEmpty()) {
                continue;
            }
            Long lastEntryId = Collections.max(entryIds);
            Entry lastEntry = findAnEntryById(lastEntryId);
            if(lastEntry == null) {
                continue;
            }
            LocalDateTime createdAt = lastEntry.getCreatedAt();
            if (createdAt == null) {
                System.out.println("Skipping user " + user.getId() + " because last entry's createdAt is null");
                continue;
            }
            long hoursSinceLastEntry = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());

            if (hoursSinceLastEntry >= 40) {
                continue;
            } else if (hoursSinceLastEntry >= 30 && !"red".equals(lastEntry.getNotifStatus())) {
                expoPushNotificationService.sendPushNotification(user.getFirebaseToken(), "Is everything okay? You lost your streak but...", "It's not too late! Build a meaningful self-reflection habit with Coping today!");
                lastEntry.setNotifStatus("red");

                entryRepository.save(lastEntry);
                setStreakToZero(lastEntryId);
            } else if (hoursSinceLastEntry >= 19 && !"orange".equals(lastEntry.getNotifStatus())) {
                expoPushNotificationService.sendPushNotification(user.getFirebaseToken(), "Don't lose your streak!", "There is still time, take 2 minutes to write an entry now and maintain your daily streak!");
                lastEntry.setNotifStatus("orange");
                entryRepository.save(lastEntry);
            }

        }
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
        calculateAndSetWordCount(entry.getId());
        return entryRepository.save(entry);
    }

    public void deleteAnEntry(Long entryId, Long userID) {
        if (entryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to delete must have an id");
        } else if (!entryRepository.existsById(entryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry to delete cannot be found");
        } else {
            User user = userRepository.findById(userID)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if(user.deleteAnEntry(entryId)) {entryRepository.deleteById(entryId);}
            else new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry could not be deleted");
        }

    }

    @Transactional // Keep Transactional as it involves fetching and saving the entity
    public void calculateAndSetWordCount(Long entryId) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        int totalWordCount = 0;

        // Helper lambda for calculating word count by splitting by space
        // Handles null, empty, or whitespace-only strings
        java.util.function.Function<String, Integer> simpleWordCount = (text) -> {
            if (text == null || text.trim().isEmpty()) {
                return 0;
            }
            // Split by one or more whitespace characters
            String[] words = text.trim().split("\\s+");
            // The split method might return an array with one empty string if the input was just whitespace,
            // so we check the length of the first element if the array is not empty.
            if (words.length == 1 && words[0].isEmpty()) {
                return 0;
            }
            return words.length;
        };


        // 1. Add word count from the 'content' field
        totalWordCount += simpleWordCount.apply(entry.getContent());

        // 2. Add word count from 'User:' parts of the 'questions' list
        List<String> questions = entry.getQuestions();
        if (questions != null) {
            for (String turn : questions) {
                // Check if the string starts with "User:" after trimming leading/trailing whitespace
                if (turn != null && turn.trim().startsWith("User:")) {
                    // Find the index after "User:"
                    int startIndex = turn.indexOf("User:") + "User:".length();
                    // Ensure there's text after "User:" and handle potential space
                    if (startIndex < turn.length()) {
                        // Get substring, trim leading/trailing space, and calculate word count
                        String userText = turn.substring(startIndex).trim();
                        totalWordCount += simpleWordCount.apply(userText);
                    }
                    // If it's just "User:", the substring will be empty after trim, word count will be 0, which is correct.
                }
            }
        }

        // Set the calculated total word count to the entry, overwriting the previous value
        entry.setWordCount(totalWordCount);

        // Save the entry with the updated word count
        entryRepository.save(entry);
        User user = userRepository.findById(entry.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setWordsWritten(entryRepository.getTotalWordCount(user.getId()));
    }


    public String getJournalingPromptForAnEntry(Long entryId) {
        if (entryId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to return cannot have an empty ID");
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        List<String> allquestions = entry.getQuestions();
        return allquestions.get(allquestions.size()-1);
    }
}
