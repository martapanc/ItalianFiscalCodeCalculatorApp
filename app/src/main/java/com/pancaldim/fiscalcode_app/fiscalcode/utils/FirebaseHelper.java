package com.pancaldim.fiscalcode_app.fiscalcode.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FirebaseHelper {

    private static final String MESSAGES = "messages";
    private static final String MESSAGE = "message";
    private static final String INSTANCE_ID = "instanceId";
    private static final String TIMESTAMP = "timestamp";

    private final FirebaseFirestore db;

    public FirebaseHelper(Context context) {
        AndroidThreeTen.init(context);
        this.db = FirebaseFirestore.getInstance();
    }

    public void addMessage(String message, String instanceId) {
        DocumentReference newMessage = db.collection(MESSAGES).document();
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put(MESSAGE, message);
        messageMap.put(INSTANCE_ID, instanceId);
        messageMap.put(TIMESTAMP, LocalDateTime.now().toString());

        try {
            newMessage.set(messageMap)
                    .addOnCompleteListener(res -> Log.d(TAG, "Message added: " + res.getResult()))
                    .addOnFailureListener(err -> Log.w(TAG, "Error adding message", err));
        } catch (Exception e) {
            Log.w(TAG, "Error writing to database", e);
        }
    }
}
