package com.example.copingai.controller;

import com.example.copingai.service.FirebaseService;
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

    @PostMapping("/decodeToken")
    @Operation(summary = "Decode Firebase token", description = "Decodes a Firebase token and returns the UID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token decoded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid token")
    })
    public ResponseEntity<?> decodeToken(
            @RequestHeader(value = "Authorization", required = true)
            @Parameter(description = "Firebase ID token with Bearer prefix",
                    required = true,
                    example = "Bearer eyJhbGciOiJSUzI1NiIs...")
            String token) throws FirebaseAuthException {
        String uid = firebaseService.decodeToken(token);
        return ResponseEntity.ok().body(uid);
    }
}

