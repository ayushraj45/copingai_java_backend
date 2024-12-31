package com.example.copingai.controller;

import com.example.copingai.entities.User;
import com.example.copingai.service.ExpoPushNotificationService;
import com.example.copingai.service.FirebaseService;
import com.example.copingai.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityScheme(name = "JWT Access Token", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Firebase Api")
@OpenAPIDefinition(security = @SecurityRequirement(name = "JWT Access Token"))
@RequestMapping("/firebase")
public class FirebaseController {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpoPushNotificationService expoPushNotificationService;

    @PostMapping("/login")
    public ResponseEntity<User> firebaseLogin (
            @RequestHeader(name = "Authorization") String token)
            throws FirebaseAuthException {
        System.out.println("Received token: " + token);
        String uid = firebaseService.decodeToken(token);
        User loginUser = userService.findByProviderId(uid);
        return ResponseEntity.ok(loginUser);
    }

    @PostMapping("/register")
    public ResponseEntity<User> fireBaseRegister(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody User appUser)
            throws FirebaseAuthException {
        System.out.println("Received token: " + token);
        String uid = firebaseService.decodeToken(token);
        appUser.setProviderId(uid);
        User newUser = userService.addAnAppUser(appUser);
        String expoToken = newUser.getFirebaseToken();

        expoPushNotificationService.sendPushNotification(expoToken, "Welcome to your Journey!", "Express and learn, make your first entry!");
        return ResponseEntity.ok(newUser);
    }
}

