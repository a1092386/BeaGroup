package com.example.sharon.beagroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PushNotification {
    Context context;
    PushNotification(Context ctx){
        context = ctx;
    }
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    private String currentID;

    public void onPush(String findID){
        mFirestore = FirebaseFirestore.getInstance();
        String uid= FirebaseAuth.getInstance().getUid();
        currentID = SaveSharedPreference.getID(context);
        String message = "send from "+uid;
        if(!TextUtils.isEmpty(message)){
            Map<String, Object> notificationMessage = new HashMap<>();
            notificationMessage.put("message", message);
            notificationMessage.put("from", currentID);

            mFirestore.collection("Users/" +findID + "/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(context, "通知送出!!!!!!!!", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "通知未送出", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}