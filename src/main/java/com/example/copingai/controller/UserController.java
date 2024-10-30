package com.example.copingai.controller;

import com.example.copingai.entities.Entry;
import com.example.copingai.entities.User;
import com.example.copingai.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "User Api")
@RequestMapping("/users")
public class UserController {
    private UserService appUserService;

    @Autowired
    public UserController(UserService appUserService) {
        this.appUserService = appUserService;
    }

    @Operation(summary = "Get a list of all app users", description = "Returns a list of all app users")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllAppUsers() {
        return appUserService.findAllAppUsers();
    }

    @Operation(summary = "Get an app user by ID ", description = "Returns an app user by ID")
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getAppUserById(@PathVariable Long userId){
        return appUserService.getAppUserById(userId);
    }

    @Operation(summary = "Get an app user by Provider ID ", description = "Returns an app user by provider ID")
    @GetMapping("/provider/{providerId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByProviderId(@PathVariable String providerId){
        return appUserService.findByProviderId(providerId);
    }


    @Operation(summary = "Get a list of all app user's entries", description = "Returns a list of all entries by a users")
    @GetMapping("/entries/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Entry> getAllUserEntries(@PathVariable Long userId){
        return appUserService.getAllUserEntries(userId);
    }

    @Operation(summary = "Get an app user's subscription status", description = "Get an app user's subscription status")
    @GetMapping("/{userId}/subscription")
    @ResponseStatus(HttpStatus.OK)
    public String getUserSubscriptionStatus(@PathVariable Long userId){
        return appUserService.getUserSubscriptionStatus(userId);
    }



    @Operation(summary = "Add an AppUser", description = "Add an app user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addAppUser(@RequestBody User appUser) {
        return appUserService.addAnAppUser(appUser);
    }

    @Operation(summary = "Update an AppUser", description = "Update an app user")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User updateAppUser(@RequestBody User appUser) {
        return appUserService.updateAnAppUser(appUser);
    }
    @Operation(summary = "Update an AppUser subscription status", description = "Update an app user subscription status")
    @PutMapping("/{appUserId}/subscription/update")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateAppUserSubscriptionStatus(@RequestBody Long appUserId, String status) {
        return appUserService.updateAnAppUserSubscriptionStatus(appUserId, status);
    }

    @Operation(summary = "Delete an AppUser", description = "Delete an app user")
    @DeleteMapping("/{appUserId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAppUser(@PathVariable Long appUserId) {
        appUserService.deleteAnAppUser(appUserId);
    }

}
