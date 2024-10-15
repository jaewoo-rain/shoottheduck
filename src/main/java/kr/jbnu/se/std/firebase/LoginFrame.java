package kr.jbnu.se.std.firebase;

import javax.swing.*;
import java.awt.*;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import kr.jbnu.se.std.Window;

public class LoginFrame extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private Firestore db;

    public LoginFrame() {
        db = FirebaseUtil.getFirestore();

        setTitle("Login");
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

        // 로그인 버튼
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(loginButton, gbc);

        // 입력 패널을 메인 패널에 추가
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // 메인 패널을 프레임에 추가
        add(mainPanel);

        // 로그인 버튼 액션 리스너
        loginButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (!id.isEmpty() && !password.isEmpty()) {
                loginUser(id, password);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter ID and password", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // 로그인 메서드
    private void loginUser(String id, String password) {
        DocumentReference docRef = db.collection("users").document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();

        future.addListener(() -> {
            try {
                DocumentSnapshot document = future.get();
                if (document.exists() && document.getString("password").equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // 로그인 성공 후 다음 창 열기
                    SwingUtilities.invokeLater(() -> {
                        Window window = new Window();
                        window.setVisible(true);
                    });
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid ID or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }, Runnable::run);
    }
}
