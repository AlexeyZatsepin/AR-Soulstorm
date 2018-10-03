package org.rooms.ar.soulstorm.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class DatabaseManager {
    private DatabaseReference mDatabase;
    private static DatabaseManager manager;
    private static String TAG = DatabaseManager.class.getSimpleName();

    private DatabaseManager() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        DatabaseReference userLastOnlineRef = mDatabase.child("users").child("last_online");
        userLastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    public static DatabaseManager getInstance() {
        if (manager == null) {
            manager = new DatabaseManager();
        }
        return manager;
    }

    public void saveResources(MyResources resources) {
        String id = SignInState.getInstance().getUser().getUid();
        mDatabase.child("res").child(id).setValue(resources).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> Log.e(TAG, e.getMessage()));
    }

    public void requestResources() {
        String id = SignInState.getInstance().getUser().getUid();
        mDatabase.child("res").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MyResources resources = dataSnapshot.getValue(MyResources.class);
                SignInState.getInstance().setResources(resources);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        });
    }
}
