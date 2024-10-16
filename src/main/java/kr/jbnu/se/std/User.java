package kr.jbnu.se.std;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import kr.jbnu.se.std.firebase.FirebaseUtil;

public class User {
    private  String id;
    private Long money, level, doubleItemNum, score, slowItemNum;
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


    public int getMoney() {
        return money.intValue();
    }


    public int getLevel() {
        return level.intValue();
    }

    public int getDoubleItemNum() {
        return doubleItemNum.intValue();
    }

    public int getScore() {
        return score.intValue();
    }

    public int getSlowItemNum() {
        return slowItemNum.intValue();
    }
}
