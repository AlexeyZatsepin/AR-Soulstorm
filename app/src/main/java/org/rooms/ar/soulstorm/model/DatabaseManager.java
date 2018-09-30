package org.rooms.ar.soulstorm.model;

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

    private DatabaseManager(){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DatabaseReference userLastOnlineRef = mDatabase.child("users").child("last_online");
        userLastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);

        String id = SignInState.getInstance().getUser().getUid();
        mDatabase.child("res").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadResource:onCancelled", databaseError.toException());
            }
        });
    }

    public static DatabaseManager getInstance() {
        if (manager == null) {
            manager = new DatabaseManager();

        }
        return manager;
    }

    public void saveResources (Resourses resourses) {
        String id = SignInState.getInstance().getUser().getUid();
        mDatabase.child("res").child(id).setValue(resourses).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> {

        });
    }
}
