package kr.jbnu.se.std.firebase;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.api.core.ApiFuture;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirestoreChat extends JFrame {
    private JTextField inputField;
    private JTextArea chatArea;
    private Firestore db;

    public FirestoreChat() {
        // Firebase 초기화
        initializeFirebase();

        // UI 구성
        setTitle("Firebase Firestore Chat");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(20);
        JButton sendButton = new JButton("Send");
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);

        add(new JScrollPane(chatArea), "Center");
        add(panel, "South");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                if (!text.isEmpty()) {
                    sendMessageToFirestore(text);
                    inputField.setText("");
                }
            }
        });
    }

    private void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://shoottheduck-29c34.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToFirestore(String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);

        DocumentReference docRef = db.collection("chats").document("chat1");
        ApiFuture<WriteResult> result = docRef.set(data);

        // Firestore에서 최신 메시지 가져오기
        getMessageFromFirestore();
    }

    private void getMessageFromFirestore() {
        DocumentReference docRef = db.collection("chats").document("chat1");
        ApiFuture<DocumentSnapshot> future = docRef.get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                String message = document.getString("message");
                chatArea.append("Received: " + message + "\n");
            } else {
                chatArea.append("No message received.\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FirestoreChat chatApp = new FirestoreChat();
                chatApp.setVisible(true);
            }
        });
    }
}
