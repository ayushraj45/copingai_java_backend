package com.example.copingai.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public String decodeToken(String token) throws FirebaseAuthException {
        try {
            // Remove "Bearer " prefix if it exists
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // Verify the token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();
            return uid;
        } catch (FirebaseAuthException e) {
            throw e;
        }
    }
}
