package org.rooms.ar.soulstorm;


import com.google.firebase.auth.FirebaseUser;

public class SignInState {
    private static SignInState INSTANCE = new SignInState();
    private FirebaseUser user;

    private SignInState(){}

    public static SignInState getInstance() {
        return INSTANCE;
    }

    public FirebaseUser getUser() {
        return user;
    }
}
