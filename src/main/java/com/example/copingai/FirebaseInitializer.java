package com.example.copingai;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class FirebaseInitializer {
    @Value("${FIREBASE_CREDENTIALS_ENCRYPTED:}")
    private String encryptedCredentials;
    @PostConstruct
    public void  initializeFirebase() throws IOException {

        GoogleCredentials credentials;

        if (!StringUtils.isEmpty(encryptedCredentials)) {
            KmsClient kmsClient = KmsClient.builder().region(Region.EU_NORTH_1).build();

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedCredentials);

            DecryptResponse decryptResponse = kmsClient.decrypt(DecryptRequest.builder()
                    .ciphertextBlob(SdkBytes.fromByteArray(encryptedBytes))
                    .build());

            credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(decryptResponse.plaintext().asByteArray())
            );
        } else {
            // Local development - read from file
            credentials = GoogleCredentials.fromStream(
                    new FileInputStream("coping-ai-firebase-adminsdk-4cyj8-6f85feaa3a.json")
            );
        }

         FirebaseApp.initializeApp(FirebaseOptions.builder()
                .setCredentials(credentials)
                .build());
    }
}
