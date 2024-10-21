package kr.jbnu.se.std;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.*;
import kr.jbnu.se.std.firebase.FirebaseUtil;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
public class User {
    private  static String id;
    private  static Long money, level, doubleItemNum, score, slowItemNum;
    private static Firestore db = FirebaseUtil.getFirestore();

    public User(String id) {

        this.id = id;
        try{
            DocumentReference docRef = db.collection("users").document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            money = document.getLong("Money");
            level = document.getLong("Level");
            doubleItemNum = document.getLong("DoubleItemNum");
            score = document.getLong("Score");
            slowItemNum = document.getLong("SlowItemNum");

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
        }

        public static Long getDoubleItemNum() {
            return doubleItemNum;
        }

        public static void setDoubleItemNum(Long doubleItemNum) {
            User.doubleItemNum = doubleItemNum;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("SetDoubleItemNum", doubleItemNum);
            docRef.set(user, SetOptions.merge());
        }

        public static Long getScore() {
            return score;
        }

        public static void setScore(Long score) {
            User.score = score;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("Score", score);
            docRef.set(user, SetOptions.merge());
        }

        public static Long getSlowItemNum() {
            return slowItemNum;
        }

        public static void setSlowItemNum(Long slowItemNum) {
            User.slowItemNum = slowItemNum;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("SlowItemNum", slowItemNum);
            docRef.set(user, SetOptions.merge());
        }


        public Long getMoney() {
            return money;
        }

        public void setMoney(Long money) {
            User.money = money;
            DocumentReference docRef = db.collection("users").document(id);
            Map<String, Object> user = new HashMap<>();
            user.put("Money", money);
            docRef.set(user, SetOptions.merge());
        }

        public String getId() {
            return id;
        }
    }