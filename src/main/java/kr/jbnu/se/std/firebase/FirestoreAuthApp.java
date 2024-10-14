package kr.jbnu.se.std.firebase;

import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FirestoreAuthApp extends JFrame {
    // UI 컴포넌트 선언
    private JTextField idField;
    private JPasswordField passwordField;
    private JTextArea messageArea;
    private Firestore db;

    // 생성자: UI 초기화 및 Firebase 초기화
    public FirestoreAuthApp() {
        // Firebase 초기화
        initializeFirebase();

        // UI 구성
        setTitle("Firebase Firestore Authentication");  // 창 제목 설정
        setSize(400, 300);  // 창 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 창 닫을 때 프로그램 종료 설정
        setLayout(new BorderLayout());  // 레이아웃 설정

        // 텍스트 필드 및 버튼 초기화
        idField = new JTextField(15);  // ID 입력 필드
        passwordField = new JPasswordField(15);  // 비밀번호 입력 필드
        JButton registerButton = new JButton("Register");  // 회원가입 버튼
        JButton loginButton = new JButton("Login");  // 로그인 버튼
        messageArea = new JTextArea();  // 메시지 출력 영역
        messageArea.setEditable(false);  // 메시지 영역은 수정 불가로 설정

        // 입력 패널 구성
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());  // GridBagLayout 사용하여 컴포넌트 배치
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // 컴포넌트 사이의 여백 설정

        // ID 라벨 및 필드 추가
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;  // ID 라벨 위치 설정
        inputPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;  // ID 입력 필드 위치 설정
        inputPanel.add(idField, gbc);

        // 비밀번호 라벨 및 필드 추가
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;  // 비밀번호 라벨 위치 설정
        inputPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;  // 비밀번호 입력 필드 위치 설정
        inputPanel.add(passwordField, gbc);

        // 회원가입 및 로그인 버튼 추가
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;  // 회원가입 버튼 위치 설정
        inputPanel.add(registerButton, gbc);

        gbc.gridx = 1;
        inputPanel.add(loginButton, gbc);  // 로그인 버튼 위치 설정

        // 메시지 영역 및 입력 패널 추가
        add(new JScrollPane(messageArea), BorderLayout.CENTER);  // 메시지 영역을 중앙에 추가
        add(inputPanel, BorderLayout.SOUTH);  // 입력 패널을 아래쪽에 추가

        // 회원가입 버튼 액션 리스너 추가
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();  // 입력된 ID 가져오기
                String password = new String(passwordField.getPassword());  // 입력된 비밀번호 가져오기
                if (!id.isEmpty() && !password.isEmpty()) {  // ID와 비밀번호가 비어있지 않은지 확인
                    checkUserExistsAndRegister(id, password);  // 사용자 존재 여부 확인 후 회원가입 메서드 호출
                } else {
                    messageArea.append("Please enter ID and password\n");  // 입력 요청 메시지 출력
                }
            }
        });

        // 로그인 버튼 액션 리스너 추가
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();  // 입력된 ID 가져오기
                String password = new String(passwordField.getPassword());  // 입력된 비밀번호 가져오기
                if (!id.isEmpty() && !password.isEmpty()) {  // ID와 비밀번호가 비어있지 않은지 확인
                    loginUser(id, password);  // 로그인 메서드 호출
                } else {
                    messageArea.append("Please enter ID and password\n");  // 입력 요청 메시지 출력
                }
            }
        });
    }

    // Firebase 초기화 메서드
    private void initializeFirebase() {
        try {
            // Firebase 서비스 계정 키 파일 로드
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");

            // Firebase 옵션 설정
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))  // 인증 정보 설정
                    .setDatabaseUrl("https://shoottheduck-29c34.firebaseio.com")  // Firebase 데이터베이스 URL 설정
                    .build();
            FirebaseApp.initializeApp(options);  // Firebase 앱 초기화
            db = FirestoreClient.getFirestore();  // Firestore 데이터베이스 객체 가져오기
        } catch (IOException e) {
            e.printStackTrace();  // 오류 발생 시 스택 트레이스 출력
        }
    }

    // 사용자 존재 여부 확인 후 회원가입 메서드
    private void checkUserExistsAndRegister(String id, String password) {
        DocumentReference docRef = db.collection("users").document(id);  // 사용자 문서 참조 생성
        ApiFuture<DocumentSnapshot> future = docRef.get();  // 사용자 데이터 가져오기

        future.addListener(() -> {
            try {
                DocumentSnapshot document = future.get();  // 문서 스냅샷 가져오기
                if (document.exists()) {  // 문서가 이미 존재하는지 확인
                    messageArea.append("User already exists. Please use a different ID.\n");  // 이미 존재하는 사용자 메시지 출력
                } else {
                    registerUser(id, password);  // 사용자 등록 메서드 호출
                }
            } catch (Exception e) {
                messageArea.append("Registration failed: " + e.getMessage() + "\n");  // 오류 메시지 출력
            }
        }, Runnable::run);
    }

    // 회원가입 메서드
    private void registerUser(String id, String password) {
        DocumentReference docRef = db.collection("users").document(id);  // 사용자 문서 참조 생성
        Map<String, Object> user = new HashMap<>();
        user.put("password", password);  // 비밀번호를 사용자 데이터로 저장

        ApiFuture<WriteResult> future = docRef.set(user);  // Firestore에 데이터 저장
        future.addListener(() -> {
            messageArea.append("User registered successfully\n");  // 회원가입 성공 메시지 출력
        }, Runnable::run);
    }

    // 로그인 메서드
    private void loginUser(String id, String password) {
        DocumentReference docRef = db.collection("users").document(id);  // 사용자 문서 참조 생성
        ApiFuture<DocumentSnapshot> future = docRef.get();  // 사용자 데이터 가져오기

        future.addListener(() -> {
            try {
                DocumentSnapshot document = future.get();  // 문서 스냅샷 가져오기
                if (document.exists() && document.getString("password").equals(password)) {  // 문서가 존재하고 비밀번호가 일치하는지 확인
                    messageArea.append("Login successful\n");  // 로그인 성공 메시지 출력
                } else {
                    messageArea.append("Invalid ID or password\n");  // 로그인 실패 메시지 출력
                }
            } catch (Exception e) {
                messageArea.append("Login failed: " + e.getMessage() + "\n");  // 로그인 오류 메시지 출력
            }
        }, Runnable::run);
    }

    // 메인 메서드: 프로그램 실행
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FirestoreAuthApp authApp = new FirestoreAuthApp();  // FirestoreAuthApp 인스턴스 생성
                authApp.setVisible(true);  // 창을 화면에 표시
            }
        });
    }
}