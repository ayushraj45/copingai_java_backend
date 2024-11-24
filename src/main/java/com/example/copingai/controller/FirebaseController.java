package com.example.copingai.controller;

import com.example.copingai.service.FirebaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "Firebase Api")
@RequestMapping("/firebase")
public class FirebaseController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/decodeToken")
    public String tokenDecoder(@RequestParam String token){
        String uid = firebaseService.decodeToken(token);
        return uid;
    }
}
