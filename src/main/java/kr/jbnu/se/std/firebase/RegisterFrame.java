package kr.jbnu.se.std.firebase;

import javax.swing.*;
import java.awt.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class RegisterFrame extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private Firestore db;

    public RegisterFrame() {
        db = FirebaseUtil.getFirestore();

        setTitle("Register");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 입력 패널 생성
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // ID 라벨과 필드
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(idLabel, gbc);

        idField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(idField, gbc);

        // 비밀번호 라벨과 필드
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        inputPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        inputPanel.add(passwordField, gbc);

        // 회원가입 버튼
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(registerButton, gbc);

        // 입력 패널을 메인 패널에 추가
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // 메인 패널을 프레임에 추가
        add(mainPanel);

        // 회원가입 버튼 액션 리스너
        registerButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (!id.isEmpty() && !password.isEmpty()) {
                checkUserExistsAndRegister(id, password);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter ID and password", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // 사용자 존재 여부 확인 후 회원가입 메서드
    private void checkUserExistsAndRegister(String id, String password) {
        DocumentReference docRef = db.collection("users").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        future.addListener(() -> {
            try {
                DocumentSnapshot document = future.get();
                if (document.exists()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "User already exists. Please use a different ID.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    });
                } else {
                    registerUser(id, password);
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }, Runnable::run);
    }

    // 회원가입 메서드
    private void registerUser(String id, String password) {
        DocumentReference docRef = db.collection("users").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("password", password);
        user.put("DoubleItemNum", 0);
        user.put("Level", 1);
        user.put("Money", 0);
        user.put("Score", 0);
        user.put("SlowItemNum", 0);

        ApiFuture<WriteResult> future = docRef.set(user);
        future.addListener(() -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });
        }, Runnable::run);
    }
}
