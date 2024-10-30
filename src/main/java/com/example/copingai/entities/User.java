package com.example.copingai.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "user_entries", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "entry_id")
    private List<Long> entryIds = new ArrayList<>();

    private String username;

    private int maxQuestions = 3;

    private String email;

    private String firebaseUID;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private int remainingFreeEntries;
    private LocalDateTime updatedAt;

    private String providerId;

    private String firebaseToken;

    private String subscriptionStatus;

    private LocalDateTime lastLogin;
    //constructors

    public User() {
        this.remainingFreeEntries = 10;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canMakeFreeEntry() {
        resetEntriesIfNeeded();
        if (remainingFreeEntries > 0) {
            setRemainingFreeEntries(remainingFreeEntries-1);
            return true;
        }
        return false;
    }

    private void resetEntriesIfNeeded() {
        LocalDateTime now = LocalDateTime.now();
        if (updatedAt == null) {
            updatedAt = now;
            remainingFreeEntries = 10;
            return;
        }
        long daysSinceLastReset = ChronoUnit.DAYS.between(updatedAt, now);
        if (daysSinceLastReset >= 7) {
            remainingFreeEntries = 10;
            updatedAt = now;
        }
    }

    // Getters and setters for each field

    public Long getId() {
        return id;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public void setMaxQuestions(int maxQuestions) {
        this.maxQuestions = maxQuestions;
    }

    public int getRemainingFreeEntries() { resetEntriesIfNeeded(); return remainingFreeEntries;}

    public void addAnEntry (Long entryId){
        entryIds.add(entryId);
        setRemainingFreeEntries(remainingFreeEntries-1);
        setEntryIds(this.entryIds);
    }

    public boolean deleteAnEntry(Long entryId){
        boolean isDeleted = entryIds.remove(entryId);
        setEntryIds(this.entryIds);
        return isDeleted;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }



    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setRemainingFreeEntries(int remainingFreeEntries) {
        this.remainingFreeEntries = remainingFreeEntries;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<Long> getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(List<Long> entryIds) {
        this.entryIds = entryIds;
    }
}
