package kr.jbnu.se.std;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.*;
import kr.jbnu.se.std.firebase.FirebaseUtil;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class User {
    private  static String id;
    private  static Long money, level, RedItem, score, BlueItem;
    private static Firestore db = FirebaseUtil.getFirestore();

    public User(String id) {

        this.id = id;
        try{
            DocumentReference docRef = db.collection("users").document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            money = document.getLong("Money");
            level = document.getLong("Level");
            RedItem = document.getLong("RedItem");
            score = document.getLong("Score");
            BlueItem = document.getLong("BlueItem");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Long getLevel() {
        return level;
    }

    public static void setLevel(Long level) {
            User.level = level;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("Level", level);
            docRef.set(user, SetOptions.merge());
        };

    public static Long getRedItemNum() {
            return RedItem;
        };

    public static void setRedItemNum(Long RedItem) {
            User.RedItem = RedItem;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("RedItem", RedItem);
            docRef.set(user, SetOptions.merge());
    };

    public static Long getScore() {
            return score;
        };

    public static void setScore(Long score) {
        User.score = score;
        DocumentReference docRef = db.collection("users").document(id);
        Map<String, Object> user = new HashMap<>();
        user.put("Score", score);
        docRef.set(user, SetOptions.merge());
    };
    public static Long getBlueItemNum() {
        return BlueItem;
    };

    public static void setBlueItemNum(Long slowItemNum) {
            User.BlueItem = slowItemNum;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("BlueItem", slowItemNum);
            docRef.set(user, SetOptions.merge());
        };


    public static Long getMoney() {
            return money;
        };

    public static void setMoney(Long money) {
            User.money = money;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("Money", money);
            docRef.set(user, SetOptions.merge());
        };

    public String getId() {
            return id;
        };


    public static void getTopScores() {
        // "users" 컬렉션에서 score가 가장 높은 3개의 문서를 가져오는 쿼리
        CollectionReference usersRef = db.collection("users");
        Query query = usersRef.orderBy("Score", Query.Direction.DESCENDING).limit(5);

        // Firestore에서 데이터를 비동기적으로 가져옴
        ApiFuture<QuerySnapshot> querySnapshotFuture = query.get();

        try {
            // 결과가 완료되면 가져오기
            QuerySnapshot querySnapshot = querySnapshotFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            // 문서 처리
            for (QueryDocumentSnapshot document : documents) {
                String userId = document.getId();
                Long score = document.getLong("Score");

                // 사용자 ID와 점수 출력
                System.out.println("UserID: " + userId + " | Score: " + score);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    };
}
