package kr.jbnu.se.std;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import kr.jbnu.se.std.firebase.FirebaseUtil;

public class User {
    private  String id;
    private  static Long money, level, doubleItemNum, score, slowItemNum;
    private  Firestore db;

    public User(String id) {
        db = FirebaseUtil.getFirestore();
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
    }

    public static Long getDoubleItemNum() {
        return doubleItemNum;
    }

    public static void setDoubleItemNum(Long doubleItemNum) {
        User.doubleItemNum = doubleItemNum;
    }

    public static Long getScore() {
        return score;
    }

    public static void setScore(Long score) {
        User.score = score;
    }

    public static Long getSlowItemNum() {
        return slowItemNum;
    }

    public static void setSlowItemNum(Long slowItemNum) {
        User.slowItemNum = slowItemNum;
    }


    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        User.money = money;
    }

    public String getId() {
        return id;
    }
}
