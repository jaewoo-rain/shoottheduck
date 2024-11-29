package kr.jbnu.se.std.firebase;

import com.google.auth.oauth2 .GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseUtil {
    private static Firestore db;

    public static Firestore getFirestore() {
        if (db == null) {
            initializeFirebase();
        }
        return db;
    }

    private static void initializeFirebase() {
        try {
//            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey1.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://shoottheduck-29c34.firebaseio.com") // 재우 서버
                    .setDatabaseUrl("https://shooooooot-409ac.firebaseio.com") // 제훈 서버
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
