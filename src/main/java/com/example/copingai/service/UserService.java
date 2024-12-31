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

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository appUserRepository;
    private final EntryRepository entryRepository;

    @Autowired
    public UserService(UserRepository appUserRepository, EntryRepository entryRepository) {
        this.appUserRepository = appUserRepository;
        this.entryRepository = entryRepository;
    }

    // Methods
    public List<User> findAllAppUsers() {
        return appUserRepository.findAll();
    }
    public User findById(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User findByProviderId(String providerId) {
        if(providerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to find must have an id");
        }
        User user = appUserRepository.findByProviderId(providerId);
        if(user == null ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser with that Provider ID not found");
        }
        else return user;
    }
    public List<Entry> getAllUserEntries(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to delete must have an id");
        }
        User user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Long> userEntriesId = user.getEntryIds();
        List<Entry> userEntries = new ArrayList<>();
        for (Long entryID: userEntriesId) {
            Entry userEntry = entryRepository.findById(entryID)
                    .orElseThrow(() -> new EntityNotFoundException("Entry not found"));
            userEntries.add(userEntry);
        }
        return userEntries;
    }

    public String getUserSubscriptionStatus(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to delete must have an id");
        }
        User user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String subscriptionStatus = user.getSubscriptionStatus();
        return subscriptionStatus;
    }

    public User addAnAppUser(User appUser) {
        if (appUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to add cannot be null");
        } else if (appUser.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to add cannot contain an id");
        }

        if(appUser.getFirebaseToken() != null){

        }
        return appUserRepository.save(appUser);
    }

    public User updateAnAppUser(User appUser) {
        if (appUser == null || appUser.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to update must have an id");
        } else if (!appUserRepository.existsById(appUser.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AppUser to update cannot be found");
        }
        return appUserRepository.save(appUser);
    }

    public User updateAnAppUserSubscriptionStatus(Long appUserId, String subStatus) {
        if (appUserId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to delete must have an id");
        }
        User appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        appUser.setSubscriptionStatus(subStatus);
        return appUserRepository.save(appUser);
    }

    public void deleteAnAppUser(Long appUserId) {
        if (appUserId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AppUser to delete must have an id");
        } else if (!appUserRepository.existsById(appUserId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AppUser to delete cannot be found");
        } else {
            appUserRepository.deleteById(appUserId);
        }
    }

    public List<String> sendNotifToAllUsers(String title, String body) {
        List<User> users = findAllAppUsers();
        List<String> expoTokens = new ArrayList<>();
        for (User user: users
             ) {
            String userToken = user.getFirebaseToken();
            expoTokens.add(userToken);
        }

        return expoTokens;
    }

    public User getAppUserById(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to find must have an ID");
        } else if (!appUserRepository.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AppUser cannot be found");
        } else {
            User user =  appUserRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            return user;
        }
    }
}